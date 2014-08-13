(function() {
    'use strict';

    // --------
    // Models
    // --------
    var apiBasePath = angular.element("meta[name='contextPath']").attr('content') + '/api/v1';

    angular.module('resources', [ 'ngResource' ])
    //
    .factory("DateResource", [ '$resource', function($resource) {
        return $resource(apiBasePath + '/dates/:dateId', {
            dateId : "@dateId"
        });
    } ])
    //
    .factory("TimeCardResource", [ '$resource', function($resource) {
        return $resource(apiBasePath + '/timecards/:targetMonth', {
            targetMonth : "@targetMonth"
        }, {
            put : {
                method : 'PUT'
            }
        });
    } ])
    //
    .factory("DailyAttendanceResource", [ '$resource', function($resource) {
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
