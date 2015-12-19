'use strict';

angular.module('myhealthApp')
    .controller('DateEntityDetailController', function ($scope, $rootScope, $stateParams, entity, DateEntity) {
        $scope.dateEntity = entity;
        $scope.load = function (id) {
            DateEntity.get({id: id}, function(result) {
                $scope.dateEntity = result;
            });
        };
        var unsubscribe = $rootScope.$on('myhealthApp:dateEntityUpdate', function(event, result) {
            $scope.dateEntity = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
