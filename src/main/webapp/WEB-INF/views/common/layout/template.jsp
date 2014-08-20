<!DOCTYPE html>
<html ng-app="app">
<head>

<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="viewport" content="width=device-width" />
<meta name="contextPath" content="${contextPath}">
<meta name="csrfHeaderName" content="${f:h(_csrf.headerName)}" />
<meta name="csrfToken" content="${f:h(_csrf.token)}" />

<c:set var="titleKey">
    <tiles:insertAttribute name="title" ignore="true" />
</c:set>
<c:set var="usecaseName">
    <tiles:insertAttribute name="usecaseName" ignore="true" />
</c:set>

<title><spring:message code="${titleKey}" text="Demo Application" /></title>

<link rel="stylesheet" href="${contextPath}/resources/vendor/bootstrap/dist/css/bootstrap.min.css">
<link rel="stylesheet"
    href="${contextPath}/resources/vendor/bootstrap/dist/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="${contextPath}/resources/app/styles.css">
<link rel="stylesheet" href="${contextPath}/resources/app/${usecaseName}/${usecaseName}.css">
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
    <div class="navbar navbar-default navbar-fixed-top"
        ng-controller="NavBarController as navBarCtrl" ng-cloak>
        <tiles:insertAttribute name="topNavbar" />
    </div>
    <div class="container">
        <div class="row">
            <div id="leftNav" class="col col-xs-3">
                <tiles:insertAttribute name="leftNavbar" />
            </div>
            <div id="content" class="col col-xs-9">
                <tiles:insertAttribute name="content" />
            </div>
        </div>
    </div>
    <script src="${contextPath}/resources/vendor/jquery/dist/jquery.min.js"></script>
    <script src="${contextPath}/resources/vendor/bootstrap/dist/js/bootstrap.min.js"></script>
    <script src="${contextPath}/resources/vendor/angular/angular.min.js"></script>
    <script src="${contextPath}/resources/vendor/angular-i18n/angular-locale_${locale}.js"></script>
    <script src="${contextPath}/resources/vendor/angular-resource/angular-resource.min.js"></script>
    <script src="${contextPath}/resources/app/common/common-handlers.js"></script>
    <script src="${contextPath}/resources/app/app.js"></script>
    <script src="${contextPath}/resources/app/app-resources.js"></script>
    <script src="${contextPath}/resources/app/app-filters.js"></script>
    <script src="${contextPath}/resources/app/common/common-services.js"></script>
    <script src="${contextPath}/resources/app/common/common-controllers.js"></script>
    <script src="${contextPath}/resources/app/${usecaseName}/${usecaseName}.js"></script>
    <script src="${contextPath}/resources/app/${usecaseName}/${usecaseName}-controllers.js"></script>
    <script src="${contextPath}/resources/app/${usecaseName}/${usecaseName}-services.js"></script>
</body>
</html>
