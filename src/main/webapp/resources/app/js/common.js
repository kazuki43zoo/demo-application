(function() {
	'use strict';
	$(function() {
		var $logoutForm = $('#logoutForm');
		$('#logout').on('click', function() {
			$logoutForm.submit();
		});

	});
})();