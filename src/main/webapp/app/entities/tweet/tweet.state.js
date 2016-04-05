(function() {
    'use strict';

    angular
        .module('tweeterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tweet', {
            parent: 'entity',
            url: '/tweet',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tweets'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tweet/tweets.html',
                    controller: 'TweetController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('tweet-detail', {
            parent: 'entity',
            url: '/tweet/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tweet'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tweet/tweet-detail.html',
                    controller: 'TweetDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Tweet', function($stateParams, Tweet) {
                    return Tweet.get({id : $stateParams.id});
                }]
            }
        })
        .state('tweet.new', {
            parent: 'tweet',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tweet/tweet-dialog.html',
                    controller: 'TweetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userid: null,
                                publication_date: null,
                                tweet: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tweet', null, { reload: true });
                }, function() {
                    $state.go('tweet');
                });
            }]
        })
        .state('tweet.edit', {
            parent: 'tweet',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tweet/tweet-dialog.html',
                    controller: 'TweetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tweet', function(Tweet) {
                            return Tweet.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('tweet', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tweet.delete', {
            parent: 'tweet',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tweet/tweet-delete-dialog.html',
                    controller: 'TweetDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tweet', function(Tweet) {
                            return Tweet.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('tweet', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
