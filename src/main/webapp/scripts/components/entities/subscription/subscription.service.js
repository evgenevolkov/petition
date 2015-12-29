'use strict';

angular.module('petitionApp')
    .factory('Subscription', function ($resource, DateUtils) {
        return $resource('api/subscriptions/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.subscriptionDateTime = DateUtils.convertDateTimeFromServer(data.subscriptionDateTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
