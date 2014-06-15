<%@page session="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="viewport" content="width=device-width" />
<title>${param.title}</title>
<link rel="stylesheet" href="${contextPath}/resources/vendor/bootstrap/dist/css/bootstrap.css">
<link rel="stylesheet" href="${contextPath}/resources/app/css/styles.css">
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
    <div class="navbar navbar-default navbar-fixed-top">
        <jsp:include page="/WEB-INF/views/common/layout/topNavbarNoLoginForm.jsp" />
    </div>
    <div id="container">
        <h1>${param.title}</h1>
        <div class="error">
            <c:choose>
                <c:when test="${empty exceptionCode}">
                    <spring:message code="${param.messageCode}" />
                </c:when>
                <c:otherwise>
                    [${f:h(exceptionCode)}] <spring:message code="${exceptionCode}" />
                </c:otherwise>
            </c:choose>
            <t:messagesPanel />
        </div>
    </div>
    <script src="${contextPath}/resources/vendor/jquery/dist/jquery.min.js"></script>
    <script src="${contextPath}/resources/vendor/bootstrap/dist/js/bootstrap.min.js"></script>
</body>
</html>