(function() {
	'use strict';

	// --------
	// Models
	// --------
	var resources = angular.module('resources', [ 'ngResource' ]);
	var contextPath = angular.element("meta[name='contextPath']").attr(
			'content');
	resources.factory("DateResource", [ '$resource', function($resource) {
		return $resource(contextPath + '/api/v1/dates/:dateId', {
			todoId : "@dateId"
		});
	} ]);

})();