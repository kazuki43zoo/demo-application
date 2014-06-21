<c:if test="${!param.including}">
    <t:messagesPanel messagesAttributeName="SPRING_SECURITY_LAST_EXCEPTION" messagesType="danger" />
    <t:messagesPanel />
    <spring:hasBindErrors name="loginForm">
        <spring:nestedPath path="loginForm">
            <div class="alert alert-danger">
                <form:errors path="*" element="ul" />
            </div>
        </spring:nestedPath>
    </spring:hasBindErrors>
</c:if>
<form:form action="${contextPath}/auth/login" class="navbar-form" method="post" modelAttribute="loginForm">
    <div class="form-group">
        <form:input type="text" path="accountId" class="form-control" placeholder="Account ID" />
    </div>
    <div class="form-group">
        <form:password path="password" class="form-control" placeholder="Password" />
    </div>
    <button class="btn">
        <span class="glyphicon glyphicon-log-in"></span>
    </button>
</form:form>
