'use strict';

angular.module('myhealthApp')
	.controller('DateEntityDeleteController', function($scope, $uibModalInstance, entity, DateEntity) {

        $scope.dateEntity = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            DateEntity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
