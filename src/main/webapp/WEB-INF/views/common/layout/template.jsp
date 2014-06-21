<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="viewport" content="width=device-width" />
<c:set var="titleKey">
    <tiles:insertAttribute name="title" ignore="true" />
</c:set>
<title><spring:message code="${titleKey}" text="Demo Application" /></title>
<link rel="stylesheet" href="${contextPath}/app/resources/vendor/bootstrap/dist/css/bootstrap.css">
<link rel="stylesheet" href="${contextPath}/app/resources/app/css/styles.css">
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
    <div class="navbar navbar-default navbar-fixed-top">
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
    <script src="${contextPath}/app/resources/vendor/jquery/dist/jquery.min.js"></script>
    <script src="${contextPath}/app/resources/vendor/bootstrap/dist/js/bootstrap.min.js"></script>
</body>
</html>
