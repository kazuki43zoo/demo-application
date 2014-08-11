(function() {
	'use strict';

	angular.module('filters', [])
			//
			.filter(
					"formatTime",
					[ function() {
						return function(minute, ignoreZero) {
							if (minute == undefined || minute == null
									|| minute === '') {
								return null;
							}
							return (ignoreZero === true && minute === 0) ? null
									: Math.floor(minute / 60) + ':'
											+ ("0" + (minute % 60)).slice(-2);
						};
					} ]);

})();
