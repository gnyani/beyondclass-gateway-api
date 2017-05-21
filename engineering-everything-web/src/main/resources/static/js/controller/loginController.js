(function(){
'use strict';
    angular
        .module('LifeStyle.login')
        .controller('LoginCtrl',router);
        router.$inject = ['$http'];
        function router($http) {
            var lc =this;

            var getUser = function() {
                $http.get('/user').then(function(user) {
                    console.log(user);
                    lc.user = user.data;
                },function(error) {
                    lc.resource = error;
                });
            };
            getUser();
            lc.point = function() {
                            $http.get('http://ip-api.com/json').then(function(coordinates) {
                                lc.coordinates = coordinates;
                            },function(error) {
                                lc.resource = error;
                            });
                        };
            lc.logout = function() {
                $http.post('/logout').then(function(res) {
                    lc.user = null;
                },function(error) {
                    console.log("Logout error : ", error);
                });
            };
        };
})();