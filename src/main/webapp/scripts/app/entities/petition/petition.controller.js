'use strict';

angular.module('petitionApp')
    .controller('PetitionController', function ($scope, $state, Petition) {

        $scope.petitions = [];
        $scope.loadAll = function() {
            Petition.query(function(result) {
               $scope.petitions = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.petition = {
                title: null,
                body: null,
                creationDateTime: null,
                id: null
            };
        };
    });
