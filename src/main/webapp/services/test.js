'use strict';

angular.module('test')
    .service('test', function ($http) {
        return {
            list: function (success) {
                return $http.get("/rest/book").then(success);
            },
            save: function (book, success) {
                return $http.post("/rest/book", book).then(success);
            },
            detail: function (id, success) {
                return $http.get("/rest/book/" + id).then(success);
            }
            ,
            delete: function (id, success) {
                return $http.delete("/rest/book/" + id).then(success);
            }
        };
    });
