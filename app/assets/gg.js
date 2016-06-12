var LoginModule = angular.module('GossipGirl', ['ui.router', 'toaster', 'ng']);

LoginModule.config(['$urlRouterProvider', '$stateProvider', '$locationProvider'
    , function ($urlRouterProvider, $stateProvider, $locationProvider) {
        $locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        });
        $stateProvider
            .state('home', {
                url: '/home',
                views: {
                    'editProfileList': {
                        controller: 'HomeController',
                        templateUrl: '/assets/partials/editProfile.html'
                    },
                    'subscribedPersonsList': {
                        controller: 'SubscribeController',
                        templateUrl: '/assets/partials/subscribedPersons.html'
                    },
                    'UnsubscribedUsersList': {
                        controller: 'UnsubscribedUsersController',
                        templateUrl: '/assets/partials/unsubscribedPersons.html'
                    }

                },

                resolve: {
                    subscribedPersons: function ($http) {
                        return $http.get('/getSubscribedPersons');

                    },
                    unsubscribedPersons: function ($http) {
                        return $http.get('/getUnsubscribedPersons');
                    }


                }
                //}

            });
        //.state('forgotPassword', {
        //    url: '/forgotPassword',
        //    controller: 'LoginCntrlr',
        //    templateUrl: '/assets/partials/forgetPassword.html'
        //})
        //.state('resetPassword', {
        //    url: '/resetPassword/:emailId/:vCode',
        //    controller: 'LoginCntrlr',
        //    templateUrl: '/assets/partials/resetPassword.html'
        //});

    }]);
LoginModule.controller('HomeController', ['$scope', 'toaster', '$window', '$http', '$state', '$stateParams', 'subscribedPersons',
    function ($scope, toaster, $window, $http, $state, $stateParams, subscribedPersons) {
        console.log("inside home");
        $scope.credentials = subscribedPersons.data[0];
        $scope.values = subscribedPersons.data;


        $scope.editProfile = function () {
            console.log("inside edit Profile");
            console.log($scope.credentials);
            $http({
                url: '/edit',
                method: 'POST',
                dataType: 'json',
                data: $scope.credentials
            }).success(function (response) {
                var obj = angular.fromJson(response);

                toaster.success(response.successMessage);
                //window.location.href = "/home";
                //alert('Success');
            }).error(function (response) {
                var obj = angular.fromJson(response);
                console.log("invalid inside toaster " + response.errorMessage);
                toaster.error(response.errorMessage);
            });
        };


        if ('WebSocket' in window) {
            console.log("WEB SOCKETS will work");
            var connection = new WebSocket('ws://localhost:9000/websocket');

            connection.onopen = function () {
                console.log('Connection open!');
                connection.send($scope.credentials.emailId);
                toaster.success('Conection open');

            };

            connection.onclose = function () {
                console.log('Connection closed');
            };
            connection.onerror = function (error) {
                console.log('Error detected: ' + error);
            };
            connection.onmessage = function (e) {
                var server_message = e.data;
                console.log(server_message);
                toaster.success(server_message);
            };


        } else {
            console.log("WEB SOCKETS will not  work");
            /*WebSockets are not supported. Try a fallback method like long-polling etc*/
        }
    }]);
LoginModule.controller('SubscribeController', ['$rootScope','$scope', 'toaster', '$window', '$http', '$state', '$stateParams', 'subscribedPersons',
    function ($rootScope, $scope, toaster, $window, $http, $state, $stateParams, subscribedPersons) {
        console.log("inside subscribe controller");
        $scope.values = subscribedPersons.data;
        $scope.subscribed = [];

        angular.forEach($scope.values, function (value, index) {

            if (index > 0)
                this.push(value.emailId);

        }, $scope.subscribed);
        console.log($scope.subscribed);

        $scope.doSomething = function (Person) {
            // console.log($scope.unSubscribed[i]);

            $http({
                url: '/unsubscribePerson',
                method: 'GET',
                params: {emailId: Person}
            }).success(function (response) {
                $rootScope.$broadcast('unsubscribingPerson', Person);
                angular.forEach($scope.subscribed, function (value, key) {
                    if (value == Person)
                        $scope.subscribed.splice(key, 1);
                });
                var obj = angular.fromJson(response);
                //window.location.href = "/home";
                //alert('Success');
            }).error(function (response) {
                var obj = angular.fromJson(response);
                toaster.error(response.errorMessage);
            });
        }
        ;


        $scope.$on('addingPerson', function (event, data) {
            console.log("event is " + event);

            $scope.subscribed.push(data);
        });
    }]);

LoginModule.controller('UnsubscribedUsersController', ['$rootScope', '$scope', 'toaster', '$window', '$http', '$state', '$stateParams', 'unsubscribedPersons',
    function ($rootScope, $scope, toaster, $window, $http, $state, $stateParams, unsubscribedPersons) {
        console.log("inside unSubscribe controller");
        $scope.values = unsubscribedPersons.data;
        $scope.unSubscribed = [];

        angular.forEach($scope.values, function (value, index) {
            this.push(value.emailId);
        }, $scope.unSubscribed);
        console.log($scope.unSubscribed);


        $scope.doSomething = function (Person) {
            // console.log($scope.unSubscribed[i]);

            $http({
                url: '/subscribePerson',
                method: 'GET',
                params: {emailId: Person}
            }).success(function (response) {
                $rootScope.$broadcast('addingPerson', Person);

                angular.forEach($scope.unSubscribed, function (value, key) {
                    if (value == Person)
                        $scope.unSubscribed.splice(key, 1);
                });


                var obj = angular.fromJson(response);
                //window.location.href = "/home";
                //alert('Success');
            }).error(function (response) {
                var obj = angular.fromJson(response);
                toaster.error(response.errorMessage);
            });
        };
        $scope.$on('unsubscribingPerson', function (event, data) {
            $scope.unSubscribed.push(data);
        });
    }]);

LoginModule.controller('GGController', ['$scope', 'toaster', '$window', '$http', '$state', '$stateParams',
    function ($scope, toaster, $window, $http, $state, $stateParams) {
        console.log("GG controller initialised");
        $scope.credentials = {};
        $scope.forgot = {};
        $scope.subscribedPersons = {};

        //$state.go('index');
        $scope.login = function () {
            console.log("inside login");
            console.log($scope.credentials);
            $http({
                url: '/login',
                method: 'POST',
                dataType: 'json',
                data: $scope.credentials
            }).success(function (response) {
                var obj = angular.fromJson(response);
                console.log("before toaster");

                toaster.success('hey man u are successfully logged in, Enjoy our gossip girl app');
                console.log("Going to home");
                window.location.href = "/home";
                //alert('Success');
            }).error(function (response) {
                var obj = angular.fromJson(response);
                console.log("invalid inside toaster " + response.errorMessage);
                toaster.error(response.errorMessage);
            });
        };
        $scope.signUp = function () {
            console.log("inside signUP");
            console.log($scope.credentials);
            $http({
                url: '/signUp',
                method: 'POST',
                dataType: 'json',
                data: $scope.credentials
            }).success(function (response) {
                var obj = angular.fromJson(response);
                toaster.success(response.successMessage);
                $window.location.href = "/";
                //alert('Success');
            }).error(function (response) {
                var obj = angular.fromJson(response);
                console.log("invalid inside toaster " + response.errorMessage);
                toaster.error(response.errorMessage);
            });
        };


        //$scope.mailSentMessage = false;
        //$scope.mailMessage = {};
        //$scope.sendAccessCode = function () {
        //    $http({
        //        url: '/sendAccessCode',
        //        method: 'POST',
        //        dataType: 'json',
        //        data: $scope.forgot
        //    }).success(function (response) {
        //        var obj = angular.fromJson(response);
        //        $scope.mailSentMessage = true;
        //        $scope.mailMessage = response.successMessage;
        //        console.log($scope.mailMessage);
        //        //toastr.success(response.successMessage);
        //        //$state.go('resetPassword');
        //        //alert('Success');
        //    }).error(function (response) {
        //        toastr.error('Invalid EmailId', response.errorMessage);
        //    });
        //};
        //$scope.setNewPassword = function () {
        //    $scope.forgot.vCode = $stateParams.vCode;
        //    $scope.forgot.emailId = $stateParams.emailId;
        //    $http({
        //        url: '/web/updatePassword',
        //        method: 'POST',
        //        dataType: 'json',
        //        data: $scope.forgot
        //    }).success(function (response) {
        //        var obj = angular.fromJson(response);
        //        toastr.success(response.successMessage);
        //        $state.go('loginPage');
        //        //alert('Success');
        //    }).error(function (response) {
        //        toastr.error('Error', response.errorMessage);
        //    });
        //};
        ////$scope.forgotPassword = function () {
        ////    console.log("hi inside forget password");
        ////    $state.go('forgotPassword');
        ////};

    }]);
