'use strict';

angular.module('test')
    .controller('ListCtrl', function ($scope, test) {

        $scope.load = function() {
            test.list(function (list) {
                $scope.list = list.data;
            });
        }

        $scope.save = function() {
            test.save($scope.form, function() {
                $scope.load();
            });
        }

        $scope.form = {};

        $scope.load();
    });
