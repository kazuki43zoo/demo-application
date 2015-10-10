<c:if test="${!param.including}">
    <t:messagesPanel messagesAttributeName="SPRING_SECURITY_LAST_EXCEPTION" messagesType="danger"/>
    <t:messagesPanel/>
    <spring:hasBindErrors name="loginForm">
        <spring:nestedPath path="loginForm">
            <div class="alert alert-danger">
                <form:errors path="*"/>
            </div>
        </spring:nestedPath>
    </spring:hasBindErrors>
</c:if>
<form:form method="post" servletRelativeAction="/app/auth/login" modelAttribute="loginForm" class="navbar-form">
    <div class="form-group">
        <spring:message var="labelAccountId" code="accountId" />
        <form:input path="username" class="form-control" placeholder="${f:h(labelAccountId)}"/>
    </div>
    <div class="form-group">
        <spring:message var="labelPassword" code="password" />
        <form:password path="password" class="form-control" placeholder="${f:h(labelPassword)}"/>
    </div>
    <form:button class="btn">
        <span class="glyphicon glyphicon-log-in"></span>
    </form:button>
</form:form>
