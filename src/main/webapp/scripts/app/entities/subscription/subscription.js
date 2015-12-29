'use strict';

angular.module('petitionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('subscription', {
                parent: 'entity',
                url: '/subscriptions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Subscriptions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/subscription/subscriptions.html',
                        controller: 'SubscriptionController'
                    }
                },
                resolve: {
                }
            })
            .state('subscription.detail', {
                parent: 'entity',
                url: '/subscription/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Subscription'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/subscription/subscription-detail.html',
                        controller: 'SubscriptionDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Subscription', function($stateParams, Subscription) {
                        return Subscription.get({id : $stateParams.id});
                    }]
                }
            })
            .state('subscription.new', {
                parent: 'subscription',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/subscription/subscription-dialog.html',
                        controller: 'SubscriptionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    subscriptionDateTime: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('subscription', null, { reload: true });
                    }, function() {
                        $state.go('subscription');
                    })
                }]
            })
            .state('subscription.edit', {
                parent: 'subscription',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/subscription/subscription-dialog.html',
                        controller: 'SubscriptionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Subscription', function(Subscription) {
                                return Subscription.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('subscription', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('subscription.delete', {
                parent: 'subscription',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/subscription/subscription-delete-dialog.html',
                        controller: 'SubscriptionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Subscription', function(Subscription) {
                                return Subscription.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('subscription', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
