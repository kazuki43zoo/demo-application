(function() {
    'use strict';
    angular.module('common-controllers', [ 'common-services' ]);
})();

(function() {
    'use strict';
    var timeOfOneSeconds = 1000;
    var timeOfOneMinutes = timeOfOneSeconds * 60;
    var timeOfReloadInterval = timeOfOneMinutes * 60;

    var NavBarController = function($timeout, dateTimeService) {
        var refreshPromise = null;
        var _this = this;
        var reflectLoadedCurrentDateTime = function(loadedCurrentDateTime) {
            var time = new Date(loadedCurrentDateTime.dateTime).getTime();
            if (_this.currentDateTime === null) {
                _this.currentDateTime = new Date(time);
            } else {
                _this.currentDateTime.setTime(time);
            }
            $timeout(loadCurrentDateTime, timeOfReloadInterval);
            if (refreshPromise !== null) {
                $timeout.cancel(refreshPromise);
            }
            refreshPromise = $timeout(refreshDateTime, timeOfOneSeconds);
        };
        var loadCurrentDateTime = function() {
            dateTimeService.getCurrentDateTime().then(reflectLoadedCurrentDateTime);
        };
        var refreshDateTime = function() {
            var nextTime = _this.currentDateTime.getTime() + timeOfOneSeconds;
            _this.currentDateTime.setTime(nextTime);
            refreshPromise = $timeout(refreshDateTime, timeOfOneSeconds);
        };

        this.currentDateTime = null;
        loadCurrentDateTime();
    };

    angular.module('common-controllers').controller('NavBarController',
            [ '$timeout', 'dateTimeService', NavBarController ]);

})();