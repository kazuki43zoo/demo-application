(function() {
    'use strict';

    // --------
    // Models
    // --------

    angular.module('resources', [ 'ngResource' ])
    //
    .factory("DateResource", [ '$resource', 'apiBasePath', function($resource, apiBasePath) {
        return $resource(apiBasePath + '/dates/:dateId', {
            dateId : "@dateId"
        });
    } ])
    //
    .factory("TimeCardResource", [ '$resource', 'apiBasePath', function($resource, apiBasePath) {
        return $resource(apiBasePath + '/timecards/:targetMonth', {
            targetMonth : "@targetMonth"
        }, {
            put : {
                method : 'PUT'
            }
        });
    } ])
    //
    .factory("DailyAttendanceResource",
            [ '$resource', 'apiBasePath', function($resource, apiBasePath) {
                return $resource(apiBasePath + '/timecards/:targetMonth/:targetDay', {
                    targetMonth : "@targetMonth",
                    targetDay : "@targetDay"
                }, {
                    put : {
                        method : 'PUT'
                    }
                });
            } ]);

})();
