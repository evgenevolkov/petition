'use strict';

angular.module('petitionApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('petition', {
                parent: 'entity',
                url: '/petitions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Petitions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/petition/petitions.html',
                        controller: 'PetitionController'
                    }
                },
                resolve: {
                }
            })
            .state('petition.detail', {
                parent: 'entity',
                url: '/petition/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Petition'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/petition/petition-detail.html',
                        controller: 'PetitionDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Petition', function($stateParams, Petition) {
                        return Petition.get({id : $stateParams.id});
                    }]
                }
            })
            .state('petition.new', {
                parent: 'petition',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/petition/petition-dialog.html',
                        controller: 'PetitionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    body: null,
                                    creationDateTime: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('petition', null, { reload: true });
                    }, function() {
                        $state.go('petition');
                    })
                }]
            })
            .state('petition.edit', {
                parent: 'petition',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/petition/petition-dialog.html',
                        controller: 'PetitionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Petition', function(Petition) {
                                return Petition.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('petition', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('petition.delete', {
                parent: 'petition',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/petition/petition-delete-dialog.html',
                        controller: 'PetitionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Petition', function(Petition) {
                                return Petition.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('petition', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
