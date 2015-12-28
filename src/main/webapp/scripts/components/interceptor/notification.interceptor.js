 'use strict';

angular.module('petitionApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-petitionApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-petitionApp-params')});
                }
                return response;
            }
        };
    });
