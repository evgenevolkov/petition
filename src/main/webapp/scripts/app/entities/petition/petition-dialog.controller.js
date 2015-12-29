'use strict';

angular.module('petitionApp').controller('PetitionDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Petition', 'User', 'Subscription',
        function($scope, $stateParams, $uibModalInstance, entity, Petition, User, Subscription) {

        $scope.petition = entity;
        $scope.users = User.query();
        $scope.subscriptions = Subscription.query();
        $scope.load = function(id) {
            Petition.get({id : id}, function(result) {
                $scope.petition = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('petitionApp:petitionUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.petition.id != null) {
                Petition.update($scope.petition, onSaveSuccess, onSaveError);
            } else {
                Petition.save($scope.petition, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForCreationDateTime = {};

        $scope.datePickerForCreationDateTime.status = {
            opened: false
        };

        $scope.datePickerForCreationDateTimeOpen = function($event) {
            $scope.datePickerForCreationDateTime.status.opened = true;
        };
}]);
