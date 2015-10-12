<c:if test="${!param.including}">
    <t:messagesPanel messagesAttributeName="SPRING_SECURITY_LAST_EXCEPTION" messagesType="danger"/>
    <t:messagesPanel/>
    <spring:hasBindErrors name="loginForm">
        <spring:nestedPath path="loginForm">
            <div class="alert alert-danger">
                <formEx:errors path="*"/>
            </div>
        </spring:nestedPath>
    </spring:hasBindErrors>
</c:if>

<spring:hasBindErrors name="loginForm"><c:set var="bindingResult" scope="request" value="${errors}"/></spring:hasBindErrors>

<form:form method="post" servletRelativeAction="/app/auth/login" modelAttribute="loginForm" class="navbar-form">
    <div class="form-group">
        <div class="${bindingResult.hasFieldErrors('username') ? 'has-error' : ''}">
            <spring:message var="labelAccountId" code="accountId"/>
            <form:input path="username" class="form-control" cssErrorClass="form-control"
                        placeholder="${f:h(labelAccountId)}"/>
        </div>
    </div>
    <div class="form-group">
        <div class="${bindingResult.hasFieldErrors('password') ? 'has-error' : ''}">
            <spring:message var="labelPassword" code="password"/>
            <form:password path="password" class="form-control" placeholder="${f:h(labelPassword)}"/>
        </div>
    </div>
    <form:button id="loginBtn" class="btn">
        <span class="glyphicon glyphicon-log-in"></span>
    </form:button>
</form:form>
