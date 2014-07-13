(function(){
	'use strict';
	var timeOfOneSeconds = 1000;
	var timeOfOneMinutes = timeOfOneSeconds * 60;
	var timeOfReloadInterval =  timeOfOneMinutes * 60;
	var timeOfTimezoneOffset =  new Date().getTimezoneOffset() * timeOfOneMinutes;
	angular.module('app').controller(
			'NavBarController',
			[ '$scope', '$timeout','DateResource',
					function($scope, $timeout, DateResource) {
						var currentDateResource = new DateResource();
						var refreshPromise = null;
						var loadCurrentDateTime = function() {
							currentDateResource.$get({
								dateId : 'currentDateTime'
							}).then(function() {
								var currentDateTime = new Date(currentDateResource.dateTime);
								var utcTime = currentDateTime.getTime() + timeOfTimezoneOffset;
								$scope.currentDateTime.setTime(utcTime);
								$timeout(loadCurrentDateTime, timeOfReloadInterval);
								if(refreshPromise != null){
									$timeout.cancel(refreshPromise);
								}
								refreshPromise = $timeout(refreshDateTime, timeOfOneSeconds);
							});
						}
						var refreshDateTime = function() {
							var nextTime = $scope.currentDateTime.getTime() + timeOfOneSeconds;
							$scope.currentDateTime.setTime(nextTime);
							refreshPromise = $timeout(refreshDateTime, timeOfOneSeconds);
						}

						$scope.currentDateTime = new Date();

						loadCurrentDateTime();

			} ]);
})();