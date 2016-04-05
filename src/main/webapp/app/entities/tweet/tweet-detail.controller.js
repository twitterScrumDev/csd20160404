(function() {
    'use strict';

    angular
        .module('tweeterApp')
        .controller('TweetDetailController', TweetDetailController);

    TweetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Tweet', 'User'];

    function TweetDetailController($scope, $rootScope, $stateParams, entity, Tweet, User) {
        var vm = this;
        vm.tweet = entity;
        vm.load = function (id) {
            Tweet.get({id: id}, function(result) {
                vm.tweet = result;
            });
        };
        var unsubscribe = $rootScope.$on('tweeterApp:tweetUpdate', function(event, result) {
            vm.tweet = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
