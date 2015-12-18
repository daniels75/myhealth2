'use strict';

angular.module('myhealthApp')
    .controller('PointsDetailController', function ($scope, $rootScope, $stateParams, entity, Points, User) {
        $scope.points = entity;
        $scope.load = function (id) {
            Points.get({id: id}, function(result) {
                $scope.points = result;
            });
        };
        var unsubscribe = $rootScope.$on('myhealthApp:pointsUpdate', function(event, result) {
            $scope.points = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
