(function ($localStorage) {
    'use strict';

    //создается angular приложение
    angular
        //с именем app и зависимостями ngRoute и ngStorage для работы с роутингом и локальным хранилищем
        .module('app', ['ngRoute', 'ngStorage'])
        //отдельная функция config для конфигурирования
        .config(config)
        //отдельная функция run для запуска
        .run(run);


    /*
        Функция конфига настраивает наше приложение
        $routeProvider - предоставляет маршрутизацию
        $httpProvider - позволяет работать с запросами
     */
    function config($routeProvider, $httpProvider) {
        //указываем переход по какой ссылке приводит на какую страницу
        $routeProvider
            .when('/', {
                //путь к html
                templateUrl: 'home/home.html',
                //имя контроллера который управляет этой страницей
                controller: 'homeController'
            })
            .when('/products', {
                templateUrl: 'products/products.html',
                controller: 'productsController'
            })
            .when('/cart', {
                templateUrl: 'cart/cart.html',
                controller: 'cartController'
            })
            .when('/order_confirmation', {
                templateUrl: 'order_confirmation/order_confirmation.html',
                controller: 'orderConfirmationController'
            })
            .when('/order_result/:orderId', {
                templateUrl: 'order_result/order_result.html',
                controller: 'orderResultController'
            })
            .when('/orders', {
                templateUrl: 'orders/orders.html',
                controller: 'ordersController'
            })
            //если переходим по любой другой ссылке то переходим на home
            .otherwise({
                redirectTo: '/'
            });

        //TODO сделать $httpProvider
    }

    /*
        При старте, если в локальном хранилище есть пользователь, то у него запрашиваем токен и подшиваем к
        хедеру авторизации.
     */
    function run($rootScope, $http, $localStorage) {
        if ($localStorage.currentUser) {
            $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
        }
    }
})();

/*
    Объявляем indexController
 */
angular.module('app').controller('indexController', function ($scope, $http, $localStorage) {
    const contextPath = 'http://localhost:8189/market';

    $scope.tryToAuth = function () {
        $http.post(contextPath + '/auth', $scope.user)
            .then(function successCallback(response) {
                if (response.data.token) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.currentUser = {username: $scope.user.username, token: response.data.token};

                    $scope.currentUserName = $scope.user.username;

                    $scope.user.username = null;
                    $scope.user.password = null;
                }
            }, function errorCallback(response) {
            });
    };

    $scope.tryToLogout = function () {
        $scope.clearUser();
        if ($scope.user.username) {
            $scope.user.username = null;
        }
        if ($scope.user.password) {
            $scope.user.password = null;
        }
    };

    $scope.clearUser = function () {
        delete $localStorage.currentUser;
        $http.defaults.headers.common.Authorization = '';
    };

    $scope.isUserLoggedIn = function () {
        if ($localStorage.currentUser) {
            return true;
        } else {
            return false;
        }
    };
});