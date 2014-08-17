(function() {
    'use strict';
    angular.module('app.usecase', []);
    angular.module('app', [ 'filters', 'common-controllers', 'app.usecase' ])
    //
    .constant("apiBasePath",
            angular.element("meta[name='contextPath']").attr('content') + '/api/v1');
})();
