<c:if test="${!param.including}">
    <t:messagesPanel messagesAttributeName="SPRING_SECURITY_LAST_EXCEPTION"
                     messagesType="danger"/>
    <t:messagesPanel/>
    <spring:hasBindErrors name="loginForm">
        <spring:nestedPath path="loginForm">
            <div class="alert alert-danger">
                <form:errors path="*"/>
            </div>
        </spring:nestedPath>
    </spring:hasBindErrors>
</c:if>
<form:form action="${contextPath}/auth/login" class="navbar-form" method="post" modelAttribute="loginForm">
    <div class="form-group">
        <spring:message var="labelAccountId" code="accountId" />
        <form:input
                path="accountId"
                class="form-control"
                placeholder="${f:h(labelAccountId)}"/>
    </div>
    <div class="form-group">
        <spring:message var="labelPassword" code="password" />
        <form:password
                path="password"
                class="form-control"
                placeholder="${f:h(labelPassword)}"/>
    </div>
    <button class="btn">
        <span class="glyphicon glyphicon-log-in"></span>
    </button>
</form:form>
