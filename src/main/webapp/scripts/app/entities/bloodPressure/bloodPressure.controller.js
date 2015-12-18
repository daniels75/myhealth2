'use strict';

angular.module('myhealthApp')
    .controller('BloodPressureController', function ($scope, $state, BloodPressure) {

        $scope.bloodPressures = [];
        $scope.loadAll = function() {
            BloodPressure.query(function(result) {
               $scope.bloodPressures = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.bloodPressure = {
                timestamp: null,
                systolic: null,
                diastolic: null,
                id: null
            };
        };
    });
