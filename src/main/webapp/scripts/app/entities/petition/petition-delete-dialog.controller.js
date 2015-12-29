'use strict';

angular.module('petitionApp')
	.controller('PetitionDeleteController', function($scope, $uibModalInstance, entity, Petition) {

        $scope.petition = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Petition.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
