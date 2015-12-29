'use strict';

angular.module('petitionApp')
    .controller('SubscriptionDetailController', function ($scope, $rootScope, $stateParams, entity, Subscription, User, Petition) {
        $scope.subscription = entity;
        $scope.load = function (id) {
            Subscription.get({id: id}, function(result) {
                $scope.subscription = result;
            });
        };
        var unsubscribe = $rootScope.$on('petitionApp:subscriptionUpdate', function(event, result) {
            $scope.subscription = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
