'use strict';

angular.module('test')
    .controller('DetailCtrl', function ($window, $scope, $routeParams, test) {
        var id = $routeParams.id;
        $scope.loadDetail = function() {
            test.detail(id, function (detail) {
                $scope.formDetail = detail.data;
            });
        }

        $scope.save = function() {
            test.save($scope.formDetail, function() {
            	alert("¡Book saved!");
            });
        }

        $scope.delete = function() {
            test.delete($scope.formDetail.id, function() {
            	alert("¡Book deleted!");
            	$window.location.href = '#/';
            });
        }

        $scope.loadDetail();
    });