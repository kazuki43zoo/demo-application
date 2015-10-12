(function() {
    'use strict';
    $(function() {
        $('#logoutUserMenu').on('click', function(event) {
            event.preventDefault();
            $('#logoutForm').submit();
        });
    });
})();