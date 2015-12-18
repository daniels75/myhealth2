'use strict';

angular.module('myhealthApp')
    .controller('PreferencesController', function ($scope, $state, Preferences) {

        $scope.preferencess = [];
        $scope.loadAll = function() {
            Preferences.query(function(result) {
               $scope.preferencess = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.preferences = {
                weekly_goal: null,
                weight_units: null,
                id: null
            };
        };
    });
