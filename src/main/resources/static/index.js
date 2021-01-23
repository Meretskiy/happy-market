angular.module('app',[]).controller('indexController', function ($scope, $http) {
    const contextPath = 'http://localhost:8189/market/api/v1';

    //если pageIndex не указан, то по дефолту берем 1
    $scope.fillTable = function (pageIndex = 1) {
        $http({
            url: contextPath + '/products',
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
                //генерируем набор чисел
                $scope.PaginationArray = $scope.generatePagesIndexes(1, $scope.ProductsPage.totalPages);
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
        $http.post(contextPath + '/products', $scope.newProduct)
            .then(function (response) {
                $scope.newProduct = null;
                $scope.fillTable();
            });
    };

    $scope.deleteProductById = function(productId) {
        $http.delete(contextPath + '/products/' + productId)
            .then(function (response) {
                $scope.fillTable();
            });
    };

    $scope.fillTable();
});