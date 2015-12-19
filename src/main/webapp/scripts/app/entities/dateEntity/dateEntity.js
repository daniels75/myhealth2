'use strict';

angular.module('myhealthApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('dateEntity', {
                parent: 'entity',
                url: '/dateEntitys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'myhealthApp.dateEntity.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/dateEntity/dateEntitys.html',
                        controller: 'DateEntityController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dateEntity');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('dateEntity.detail', {
                parent: 'entity',
                url: '/dateEntity/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'myhealthApp.dateEntity.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/dateEntity/dateEntity-detail.html',
                        controller: 'DateEntityDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dateEntity');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'DateEntity', function($stateParams, DateEntity) {
                        return DateEntity.get({id : $stateParams.id});
                    }]
                }
            })
            .state('dateEntity.new', {
                parent: 'dateEntity',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/dateEntity/dateEntity-dialog.html',
                        controller: 'DateEntityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    firstDate: null,
                                    secondDate: null,
                                    test: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('dateEntity', null, { reload: true });
                    }, function() {
                        $state.go('dateEntity');
                    })
                }]
            })
            .state('dateEntity.edit', {
                parent: 'dateEntity',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/dateEntity/dateEntity-dialog.html',
                        controller: 'DateEntityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['DateEntity', function(DateEntity) {
                                return DateEntity.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('dateEntity', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('dateEntity.delete', {
                parent: 'dateEntity',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/dateEntity/dateEntity-delete-dialog.html',
                        controller: 'DateEntityDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['DateEntity', function(DateEntity) {
                                return DateEntity.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('dateEntity', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
