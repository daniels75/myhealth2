 'use strict';

angular.module('myhealthApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-myhealthApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-myhealthApp-params')});
                }
                return response;
            }
        };
    });
