(function() {
    'use strict';

    angular
        .module('tweeterApp')
        .controller('TweetDeleteController',TweetDeleteController);

    TweetDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tweet'];

    function TweetDeleteController($uibModalInstance, entity, Tweet) {
        var vm = this;
        vm.tweet = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Tweet.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
