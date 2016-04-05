(function() {
    'use strict';

    angular
        .module('tweeterApp')
        .controller('TweetController', TweetController);

    TweetController.$inject = ['$scope', '$state', 'Tweet'];

    function TweetController ($scope, $state, Tweet) {
        var vm = this;
        vm.tweets = [];
        vm.loadAll = function() {
            Tweet.query(function(result) {
                vm.tweets = result;
            });
        };

        vm.loadAll();
        
    }
})();
