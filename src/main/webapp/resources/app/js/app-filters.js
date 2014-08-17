(function() {
    'use strict';

    angular.module('filters', [ 'common-services' ])
    //
    .filter("formatTime", [ 'dateTimeService', function(dateTimeService) {
        return dateTimeService.formatTime;
    } ])
    //
    .filter("dayOfWeek", [ 'dateTimeService', function(dateTimeService) {
        return dateTimeService.getDayOfWeek;
    } ])
    //
    .filter("isHoliday", [ 'dateTimeService', function(dateTimeService) {
        return dateTimeService.isHoliday;
    } ])
    //
    .filter("floorNumber", [ 'numberService', function(numberService) {
        return numberService.floorNumber;
    } ]);

})();
