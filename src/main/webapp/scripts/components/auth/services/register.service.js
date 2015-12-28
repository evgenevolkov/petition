'use strict';

angular.module('petitionApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


