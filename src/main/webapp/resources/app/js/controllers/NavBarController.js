(function(){
	'use strict';
	angular.module('app').controller(
			'NavBarController',
			[ '$scope', '$timeout', 'DateResource',
					function($scope, $timeout, DateResource) {
						var oneSeconds = 1000;
						var oneMinutes = oneSeconds * 60;
						var oneHours =  oneSeconds * 60 * 60;
						var currentDateResource = new DateResource();
						var refreshPromise = null;
						var loadCurrentDateTime = function() {
							currentDateResource.$get({
								dateId : 'currentDateTime'
							}).then(function() {
								var currentDateTime = new Date(currentDateResource.dateTime);
								$scope.currentDateTime = new Date(currentDateTime.getTime() + (currentDateTime.getTimezoneOffset() * oneMinutes));
								$timeout(loadCurrentDateTime, oneHours);
								if(refreshPromise != null){
									$timeout.cancel(refleshPromise);
								}
								refreshPromise = $timeout($scope.refreshDateTime, oneSeconds);
							});
						}

						$scope.refreshDateTime = function() {
							$scope.currentDateTime.setTime($scope.currentDateTime.getTime() + oneSeconds);
							refreshPromise = $timeout($scope.refreshDateTime, oneSeconds);
						}

						loadCurrentDateTime();

			} ]);
})();