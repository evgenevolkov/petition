'use strict';

angular.module('petitionApp')
    .controller('PetitionDetailController', function ($scope, $rootScope, $stateParams, entity, Petition, User, Subscription) {
        $scope.petition = entity;
        $scope.load = function (id) {
            Petition.get({id: id}, function(result) {
                $scope.petition = result;
            });
        };
        var unsubscribe = $rootScope.$on('petitionApp:petitionUpdate', function(event, result) {
            $scope.petition = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
