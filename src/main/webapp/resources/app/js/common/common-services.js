(function() {
    'use strict';
    angular.module('common-services', [ 'resources' ]);
})();

(function() {
    'use strict';

    var SUNDAY = 0;
    var SATURDAY = 6;

    var DateResource;

    function DateTimeService(InjectedDateResource, $filter) {
        DateResource = InjectedDateResource;
        this.$filter = $filter;
    }

    DateTimeService.prototype.getDayOfWeek = function(targetDate) {
        return new Date(targetDate).getDay();
    };

    DateTimeService.prototype.isHoliday = function(targetDate) {
        if (targetDate == undefined) {
            return false;
        }
        var targetDayOfWeek = new Date(targetDate).getDay();
        return targetDayOfWeek === SUNDAY || targetDayOfWeek === SATURDAY;
    };

    DateTimeService.prototype.formatDate = function(date, format) {
        if (!angular.isDate(date)) {
            return date;
        }
        return this.$filter('date')(date, format);
    };

    DateTimeService.prototype.getCurrentDateTime = function() {
        return DateResource.get({
            dateId : 'currentDateTime'
        }).$promise;
    };

    DateTimeService.prototype.truncateWithTimeUnit = function(minute, timeUnitMinute) {
        if (0 <= minute) {
            return minute - (minute % timeUnitMinute);
        } else {
            return minute + (minute % timeUnitMinute);
        }
    };

    DateTimeService.prototype.formatTime = function(minute, ignoreZero) {
        if (!angular.isNumber(minute)) {
            return minute;
        }
        if (ignoreZero === true && minute === 0) {
            return null;
        }
        var hourOfTime;
        if (0 <= minute) {
            hourOfTime = Math.floor(minute / 60);
        } else {
            hourOfTime = Math.ceil(minute / 60);
            if (hourOfTime == 0) {
                hourOfTime = "-" + hourOfTime;
            }
        }
        return hourOfTime + ':' + ("0" + (minute % 60)).slice(-2);
    };

    angular.module('common-services').service('dateTimeService',
            [ 'DateResource', '$filter', DateTimeService ]);

})();

(function() {
    'use strict';

    function NumberService() {
    }

    NumberService.prototype.floorNumber = function(number) {
        if (!angular.isNumber(number)) {
            return number;
        }
        return Math.floor(number);
    };

    angular.module('common-services').service('numberService', [ NumberService ]);

})();