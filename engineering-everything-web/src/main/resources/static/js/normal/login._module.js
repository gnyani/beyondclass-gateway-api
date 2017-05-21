(function(){
'use strict';
    angular
        .module('LifeStyle.login', []);
    angular
        .module('LifeStyle.login')
        .config(['$httpProvider',function($httpProvider) {
        $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
            }]);
 })();