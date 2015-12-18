'use strict';

angular.module('myhealthApp')
    .controller('WeightController', function ($scope, $state, Weight) {

        $scope.weights = [];
        $scope.loadAll = function() {
            Weight.query(function(result) {
               $scope.weights = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.weight = {
                timestamp: null,
                weight: null,
                id: null
            };
        };
    });
