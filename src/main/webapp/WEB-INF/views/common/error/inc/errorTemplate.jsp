<%@page session="false" %>
<!DOCTYPE html>
<html data-ng-app="app">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="viewport" content="width=device-width"/>
    <meta name="contextPath" content="${contextPath}">
    <sec:csrfMetaTags />

    <title>${param.title}</title>
    <link rel="stylesheet" href="<c:url value="/resources/vendor/bootstrap/dist/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/vendor/bootstrap/dist/css/bootstrap-theme.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/app/styles.css"/>">
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="navbar navbar-default navbar-fixed-top"
     data-ng-controller="NavBarController as navBarCtrl"
     data-ng-cloak>
    <jsp:include page="/WEB-INF/views/common/layout/topNavbarNoLoginForm.jsp"/>
</div>
<div id="container">
    <h1>${param.title}</h1>

    <div class="error">
        <c:choose>
            <c:when test="${empty exceptionCode}">
                <spring:message code="${param.messageCode}"/>
            </c:when>
            <c:otherwise>
                [${f:h(exceptionCode)}] <spring:message code="${exceptionCode}"/>
            </c:otherwise>
        </c:choose>
        <t:messagesPanel/>
    </div>
</div>
<script src="<c:url value="/resources/vendor/jquery/dist/jquery.min.js"/>"></script>
<script src="<c:url value="/resources/vendor/bootstrap/dist/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/resources/vendor/angular/angular.min.js"/>"></script>
<script src="<c:url value="/resources/vendor/angular-resource/angular-resource.min.js"/>"></script>
<script src="<c:url value="/resources/app/app.js"/>"></script>
<script src="<c:url value="/resources/app/app-resources.js"/>"></script>
<script src="<c:url value="/resources/app/app-filters.js"/>"></script>
<script src="<c:url value="/resources/app/common/common-controllers.js"/>"></script>
<script src="<c:url value="/resources/app/common/common-services.js"/>"></script>
<script src="<c:url value="/resources/app/error/error.js"/>"></script>
</body>
</html>