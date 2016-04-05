(function() {
    'use strict';

    angular
        .module('tweeterApp')
        .controller('TweetDialogController', TweetDialogController);

    TweetDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tweet', 'User'];

    function TweetDialogController ($scope, $stateParams, $uibModalInstance, entity, Tweet, User) {
        var vm = this;
        vm.tweet = entity;
        vm.users = User.query();
        vm.load = function(id) {
            Tweet.get({id : id}, function(result) {
                vm.tweet = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tweeterApp:tweetUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.tweet.id !== null) {
                Tweet.update(vm.tweet, onSaveSuccess, onSaveError);
            } else {
                Tweet.save(vm.tweet, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.publication_date = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
