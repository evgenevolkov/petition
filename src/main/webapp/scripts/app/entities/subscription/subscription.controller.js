'use strict';

angular.module('petitionApp')
    .controller('SubscriptionController', function ($scope, $state, Subscription) {

        $scope.subscriptions = [];
        $scope.loadAll = function() {
            Subscription.query(function(result) {
               $scope.subscriptions = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.subscription = {
                subscriptionDateTime: null,
                id: null
            };
        };
    });
