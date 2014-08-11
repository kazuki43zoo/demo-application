(function() {
	'use strict';
	var timeOfOneSeconds = 1000;
	var timeOfOneMinutes = timeOfOneSeconds * 60;
	var timeOfReloadInterval = timeOfOneMinutes * 60;
	var timeOfTimezoneOffset = new Date().getTimezoneOffset()
			* timeOfOneMinutes;

	var NavBarController = function($timeout, DateResource) {
		var currentDateResource = new DateResource();
		var refreshPromise = null;
		var _this = this;
		var loadCurrentDateTime = function() {
			currentDateResource.$get({
				dateId : 'currentDateTime'
			}).then(
					function() {
						var utcTime = new Date(currentDateResource.dateTime)
								.getTime()
								+ timeOfTimezoneOffset;
						if (_this.currentDateTime == null) {
							_this.currentDateTime = new Date(utcTime);
						} else {
							_this.currentDateTime.setTime(utcTime);
						}
						$timeout(loadCurrentDateTime, timeOfReloadInterval);
						if (refreshPromise != null) {
							$timeout.cancel(refreshPromise);
						}
						refreshPromise = $timeout(refreshDateTime,
								timeOfOneSeconds);
					});
		}
		var refreshDateTime = function() {
			var nextTime = _this.currentDateTime.getTime() + timeOfOneSeconds;
			_this.currentDateTime.setTime(nextTime);
			refreshPromise = $timeout(refreshDateTime, timeOfOneSeconds);
		}

		this.currentDateTime = null;
		loadCurrentDateTime();
	};

	angular.module('app').controller('NavBarController',
			[ '$timeout', 'DateResource', NavBarController ]);

})();