<t:messagesPanel/>
<form:form method="post" action="${contextPath}/app/password" modelAttribute="passwordForm" class="form-horizontal">

    <sec:authorize access="isAuthenticated()" var="isAuthenticated"/>
    <c:choose>
        <c:when test="${isAuthenticated}">
            <form:hidden path="username"/>
        </c:when>
        <c:otherwise>
            <div class="form-group">
                <spring:message var="labelAccountId" code="accountId" />
                <form:label path="username" cssClass="col-sm-2 control-label">${f:h(labelAccountId)}</form:label>
                <div class="col-sm-8">
                    <form:input path="username" cssClass="form-control" placeholder="${f:h(labelAccountId)}"/>
                    <form:errors path="username"/>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
    <div class="form-group">
        <spring:message var="labelCurrentPassword" code="currentPassword" />
        <form:label path="currentPassword" class="col-sm-2 control-label">${f:h(labelCurrentPassword)}</form:label>
        <div class="col-sm-4">
            <form:password path="currentPassword" cssClass="form-control" placeholder="${f:h(labelCurrentPassword)}"/>
            <form:errors path="currentPassword"/>
        </div>
    </div>
    <div class="form-group">
        <spring:message var="labelNewPassword" code="newPassword" />
        <spring:message var="labelConfirmPassword" code="confirmPassword" />
        <form:label path="password" class="col-sm-2 control-label">${f:h(labelNewPassword)}</form:label>
        <div class="col-sm-4">
            <form:password path="password" cssClass="form-control" placeholder="${f:h(labelNewPassword)}"/>
            <form:errors path="password"/>
        </div>
        <div class="col-sm-4">
            <form:password path="confirmPassword" cssClass="form-control" placeholder="${f:h(labelConfirmPassword)}"/>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-8">
            <form:button class="btn btn-default" name="change">
                <span class="glyphicon glyphicon-edit"></span>
                <spring:message code="change" />
            </form:button>
            <c:if test="${!isAuthenticated}">
                <form:button class="btn btn-default" name="changeAndLogin">
                    <span class="glyphicon glyphicon-log-in"></span>
                    <spring:message code="changeAndLogin" />
                </form:button>
            </c:if>
        </div>
    </div>

</form:form>