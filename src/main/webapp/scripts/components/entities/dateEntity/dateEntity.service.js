'use strict';

angular.module('myhealthApp')
    .factory('DateEntity', function ($resource, DateUtils) {
        return $resource('api/dateEntitys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.firstDate = DateUtils.convertLocaleDateFromServer(data.firstDate);
                    data.secondDate = DateUtils.convertDateTimeFromServer(data.secondDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.firstDate = DateUtils.convertLocaleDateToServer(data.firstDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.firstDate = DateUtils.convertLocaleDateToServer(data.firstDate);
                    return angular.toJson(data);
                }
            }
        });
    });
