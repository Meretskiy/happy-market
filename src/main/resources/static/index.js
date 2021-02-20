//зависимость ngStorage добавляет модуль локального хранилища и позволяет пользоваться $localStorage в браузере
angular.module('app',['ngStorage']).controller('indexController', function ($scope, $http, $localStorage) {
    const contextPath = 'http://localhost:8189/market';

    $scope.authorized = false;
    $scope.currentUsername = null;

    //если pageIndex не указан, то по дефолту берем 1
    $scope.showProductsPage = function (pageIndex = 1) {
        $http({
            url: contextPath + '/api/v1/products',
            method: 'GET',
            params: {
                title: $scope.filter ? $scope.filter.title : null,
                min_price: $scope.filter ? $scope.filter.min_price : null,
                max_price: $scope.filter ? $scope.filter.max_price : null,
                p: pageIndex
            }
        }).then(function (response) {
                //как только бэк пришлет данные, получаем список наших товаром на странице
                $scope.ProductsPage = response.data;

                let minPageIndex = pageIndex - 2;
                if (minPageIndex < 1) {
                    minPageIndex = 1;
                }

                let maxPageIndex = pageIndex + 2;
                if (maxPageIndex > $scope.ProductsPage.totalPages) {
                    maxPageIndex = $scope.ProductsPage.totalPages;
                }

                //генерируем набор чисел
                $scope.PaginationArray = $scope.generatePagesIndexes(minPageIndex, maxPageIndex);
            });
    };


    $scope.generatePagesIndexes = function (startPage, endPage) {
        let arr = [];
        for (let i = startPage; i < endPage + 1; i++) {
            arr.push(i);
        }
        return arr;
    }

    $scope.submitCreateNewProduct = function () {
        $http.post(contextPath + '/api/v1/products', $scope.newProduct)
            .then(function (response) {
                $scope.newProduct = null;
                $scope.showProductsPage();
            });
    };

    $scope.deleteProductById = function(productId) {
        $http.delete(contextPath + '/api/v1/products/' + productId)
            .then(function (response) {
                $scope.showProductsPage();
            });
    };

    $scope.showCart = function () {
        $http.get(contextPath + '/api/v1/cart')
            .then(function (response) {
                $scope.Cart = response.data;
            });

    };

    $scope.showMyOrders = function () {
        $http.get(contextPath + '/api/v1/orders')
            .then(function (response) {
                $scope.MyOrders = response.data;
            });

    };

    $scope.addToCart = function (productId) {
        $http.get(contextPath + '/api/v1/cart/add/' + productId)
            .then(function (response) {
                $scope.showCart();
            });
    };

    $scope.incrementQuantity = function (productTitle) {
        $http.get(contextPath + '/api/v1/cart/add/+/' + productTitle)
            .then(function (response) {
                $scope.showCart();
            });
    };

    $scope.decrementQuantity = function (productTitle) {
        $http.get(contextPath + '/api/v1/cart/add/-/' + productTitle)
            .then(function (response) {
                $scope.showCart();
            });
    };

    $scope.clearItem = function (productTitle) {
        $http.get(contextPath + '/api/v1/cart/clear/' + productTitle)
            .then(function (response) {
                $scope.showCart();
            });
    };

    $scope.clearCart = function () {
        $http.get(contextPath + '/api/v1/cart/clear/')
            .then(function (response) {
                $scope.showCart();
            });
    };

    $scope.createOrder = function () {
        $http.post(contextPath + '/api/v1/orders/', $scope.deliveryAddress)
            .then(function (response) {
                $scope.deliveryAddress = null;
                $scope.showMyOrders();
                $scope.showCart();
            });
    };

    $scope.tryToAuth = function () {
        //в тело запроса зашиваем json user который мы собираем из наших форм на фронте
        $http.post(contextPath + '/auth', $scope.user)
            //прилетает или положительный ответ
            .then(function successCallback(response) {
                //проверяем есть ли в ответе токен
                if (response.data.token) {
                    //ко всем запросам на бэк создаем стандартный хедер common с названием Authorization и нашим токеном
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    //в локальное хранилище запоминаем, что мы зашли под таким пользователем с таким токеном
                    $localStorage.happyUsername = $scope.user.username;
                    $scope.currentUsername = $scope.user.username;
                    $localStorage.happyTokenWithBearerPrefix = 'Bearer ' + response.data.token;


                    $scope.user.username = null;
                    $scope.user.password = null;
                    $scope.authorized = true;
                    $scope.showProductsPage();
                    $scope.showCart();
                    $scope.showMyOrders();
                }
                //или отрицательный
            }, function errorCallback(response) {
                window.alert("Error");
            });
    };

    $scope.logout = function () {
        $http.defaults.headers.common.Authorization = null;
        delete $localStorage.happyUsername;
        delete $localStorage.happyTokenWithBearerPrefix;
        $scope.authorized = false;
    }

    //если в локальном хранилище есть такое имя, то мы в качестве токена автоматом подшиваем токен от сохраненного пользователя
    if ($localStorage.happyUsername) {
        $http.defaults.headers.common.Authorization = $localStorage.happyTokenWithBearerPrefix;
        $scope.showProductsPage();
        $scope.showMyOrders();
        $scope.showCart();
        $scope.authorized = true;
    }
});