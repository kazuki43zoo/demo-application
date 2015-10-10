<!DOCTYPE html>
<html data-ng-app="app">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="viewport" content="width=device-width"/>
    <meta name="contextPath" content="${contextPath}">
    <sec:csrfMetaTags />

    <c:set var="titleKey">
        <tiles:insertAttribute name="title" ignore="true"/>
    </c:set>
    <c:set var="usecaseName">
        <tiles:insertAttribute name="usecaseName" ignore="true"/>
    </c:set>

    <title><spring:message code="${titleKey}" text="Demo Application using TERASOLUNA Server Framework for Java (5.x)"/></title>

    <link rel="stylesheet" href="<c:url value="/resources/static/vendor/bootstrap/dist/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/static/vendor/bootstrap/dist/css/bootstrap-theme.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/static/app/styles.css"/>">
    <spring:theme var="themeStylesCssFile" code="stylesCss" text="default.css"/>
    <link rel="stylesheet" href="<c:url value="/resources/static/app/${themeStylesCssFile}"/>">
    <link rel="stylesheet" href="<c:url value="/resources/static/app/${usecaseName}/${usecaseName}.css"/>">
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="navbar navbar-default navbar-fixed-top"
     data-ng-controller="NavBarController as navBarCtrl"
     data-ng-cloak>
    <tiles:insertAttribute name="topNavbar"/>
</div>
<div class="container">
    <div class="row">
        <div id="leftNav" class="col col-xs-3">
            <tiles:insertAttribute name="leftNavbar"/>
        </div>
        <div id="content" class="col col-xs-9">
            <tiles:insertAttribute name="content"/>
        </div>
    </div>
</div>
<script src="<c:url value="/resources/static/vendor/jquery/dist/jquery.min.js"/>"></script>
<script src="<c:url value="/resources/static/vendor/bootstrap/dist/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/resources/static/vendor/angular/angular.min.js"/>"></script>
<script src="<c:url value="/resources/static/vendor/angular-i18n/angular-locale_${locale}.js"/>"></script>
<script src="<c:url value="/resources/static/vendor/angular-resource/angular-resource.min.js"/>"></script>
<script src="<c:url value="/resources/static/app/common/common-handlers.js"/>"></script>
<script src="<c:url value="/resources/static/app/app.js"/>"></script>
<script src="<c:url value="/resources/static/app/app-resources.js"/>"></script>
<script src="<c:url value="/resources/static/app/app-filters.js"/>"></script>
<script src="<c:url value="/resources/static/app/common/common-services.js"/>"></script>
<script src="<c:url value="/resources/static/app/common/common-controllers.js"/>"></script>
<script src="<c:url value="/resources/static/app/${usecaseName}/${usecaseName}.js"/>"></script>
<script src="<c:url value="/resources/static/app/${usecaseName}/${usecaseName}-controllers.js"/>"></script>
<script src="<c:url value="/resources/static/app/${usecaseName}/${usecaseName}-services.js"/>"></script>
</body>
</html>
