<!DOCTYPE html>
<html ng-app="app">
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="viewport" content="width=device-width" />
<meta name="contextPath" content="${contextPath}" >
<c:set var="titleKey">
    <tiles:insertAttribute name="title" ignore="true" />
</c:set>
<title><spring:message code="${titleKey}" text="Demo Application" /></title>
<link rel="stylesheet" href="${contextPath}/resources/vendor/bootstrap/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/vendor/bootstrap/dist/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="${contextPath}/resources/app/css/styles.css">
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
    <div class="navbar navbar-default navbar-fixed-top" ng-controller="NavBarController as navBar" ng-cloak>
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
    <script src="${contextPath}/resources/vendor/angular-resource/angular-resource.min.js"></script>
    <script src="${contextPath}/resources/app/js/common.js"></script>
    <script src="${contextPath}/resources/app/js/app.js"></script>
    <script src="${contextPath}/resources/app/js/app-resources.js"></script>
    <script src="${contextPath}/resources/app/js/controllers/NavBarController.js"></script>
</body>
</html>
