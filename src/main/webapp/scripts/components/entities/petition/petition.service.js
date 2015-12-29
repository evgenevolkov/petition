'use strict';

angular.module('petitionApp')
    .factory('Petition', function ($resource, DateUtils) {
        return $resource('api/petitions/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creationDateTime = DateUtils.convertDateTimeFromServer(data.creationDateTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
