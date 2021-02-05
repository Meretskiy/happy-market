angular.module('app',[]).controller('indexController', function ($scope, $http) {
    const contextPath = 'http://localhost:8189/market';

    $scope.authorized = false;
    $scope.username = null;

    //если pageIndex не указан, то по дефолту берем 1
    $scope.fillTable = function (pageIndex = 1) {
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
                $scope.fillTable();
            });
    };

    $scope.deleteProductById = function(productId) {
        $http.delete(contextPath + '/api/v1/products/' + productId)
            .then(function (response) {
                $scope.fillTable();
            });
    };

    $scope.showCart = function () {
        $http.get(contextPath + '/api/v1/cart')
            .then(function (response) {
                $scope.Cart = response.data;
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

    $scope.saveOrder = function () {
        $http.get(contextPath + '/api/v1/cart/save/')
            .then(function (response) {
                $scope.clearCart();
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
                    $scope.username = $scope.user.username;
                    $scope.user.username = null;
                    $scope.user.password = null;
                    $scope.authorized = true;
                    $scope.fillTable();
                }
                //или отрицательный
            }, function errorCallback(response) {
                window.alert("Error");
            });
    };

    // $scope.showCart();
    // $scope.fillTable();
});