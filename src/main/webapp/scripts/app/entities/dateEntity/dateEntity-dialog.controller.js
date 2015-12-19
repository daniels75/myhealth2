'use strict';

angular.module('myhealthApp').controller('DateEntityDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'DateEntity',
        function($scope, $stateParams, $uibModalInstance, entity, DateEntity) {

        $scope.dateEntity = entity;
        $scope.load = function(id) {
            DateEntity.get({id : id}, function(result) {
                $scope.dateEntity = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('myhealthApp:dateEntityUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.dateEntity.id != null) {
                DateEntity.update($scope.dateEntity, onSaveSuccess, onSaveError);
            } else {
                DateEntity.save($scope.dateEntity, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
