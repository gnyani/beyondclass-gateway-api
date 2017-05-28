(function() {
    'use strict';

    angular
    	.module('LifeStyle.login')
    	.config(loginRouter)

    loginRouter.$inject = ['$stateProvider', '$urlRouterProvider'];

    function loginRouter($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise('/')
    	$stateProvider
    	.state('beforeLogin', {
    		url: "/",
    		templateUrl: "html/login.html",
    		controller : "LoginCtrl",
    		controllerAs : "LC",
    	})
    	.state('hello', {
    		url: "/hello",
    		templateUrl: "html/about.html",
    		controller : "LoginCtrl",
            controllerAs : "LC",
    	})
//    	.state('admin.sites', {
//    		url: "/Sites",
//    		templateUrl: "app/admin/site/sites.html",
//    		controller: "AdminSiteController",
//    		controllerAs : "vm"
//    	})
//    	.state('admin.models', {
//    		url: "/Models",
//    		templateUrl: "app/admin/model/models.html",
//    		controller: "AdminModelController",
//    		controllerAs: "vm"
//    	})
//    	.state('admin.services', {
//    		url: "/Services",
//    		templateUrl: "app/admin/service/services.html",
//    		controller: "AdminServicesController",
//    		controllerAs: "vm"
//    	})
//    	.state('admin.tags', {
//    		url: "/Tags",
//    		templateUrl: "app/admin/tag/tags.html",
//    		controller: "AdminTagController",
//    		controllerAs:"vm"
//    	})
//    	.state('admin.alarms', {
//    		url: "/Alarms",
//    		templateUrl: "app/admin/alarm/alarms.html",
//    		controller: "AdminAlarmController",
//    		controllerAs: "vm"
//    	})
//    	.state('admin.status', {
//    		url: "/Status",
//    		templateUrl: "app/admin/status/status.html",
//    		controller: "AdminStatusController",
//    		controllerAs: "vm"
//    	})
    }
})();
