'use strict';

angular.module('myhealthApp')
    .factory('Preferences', function ($resource) {
        return $resource('api/preferences/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'user': { method: 'GET', isArray: false, url: '/api/my-preferences'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
