<t:messagesPanel/>
<form:form action="${contextPath}/app/password" class="form-horizontal"
           method="post"
           modelAttribute="passwordForm">

    <sec:authorize access="isAuthenticated()" var="isAuthenticated"/>
    <c:choose>
        <c:when test="${isAuthenticated}">
            <form:hidden path="accountId"/>
        </c:when>
        <c:otherwise>
            <div class="form-group">
                <spring:message var="labelAccountId" code="accountId" />
                <form:label path="accountId" cssClass="col-sm-2 control-label">
                    ${f:h(labelAccountId)}
                </form:label>
                <div class="col-sm-8">
                    <form:input
                            cssClass="form-control"
                            path="accountId"
                            placeholder="${f:h(labelAccountId)}"/>
                    <form:errors path="accountId"/>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
    <div class="form-group">
        <spring:message var="labelCurrentPassword" code="currentPassword" />
        <form:label path="currentPassword" class="col-sm-2 control-label">
            ${f:h(labelCurrentPassword)}
        </form:label>
        <div class="col-sm-4">
            <form:password
                    cssClass="form-control"
                    path="currentPassword"
                    placeholder="${f:h(labelCurrentPassword)}"/>
            <form:errors path="currentPassword"/>
        </div>
    </div>
    <div class="form-group">
        <spring:message var="labelNewPassword" code="newPassword" />
        <spring:message var="labelConfirmPassword" code="confirmPassword" />
        <form:label path="password" class="col-sm-2 control-label">
            ${f:h(labelNewPassword)}
        </form:label>
        <div class="col-sm-4">
            <form:password
                    cssClass="form-control"
                    path="password"
                    placeholder="${f:h(labelNewPassword)}"/>
            <form:errors path="password"/>
        </div>
        <div class="col-sm-4">
            <form:password
                    cssClass="form-control"
                    path="confirmPassword"
                    placeholder="${f:h(labelConfirmPassword)}"/>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-8">
            <button class="btn btn-default" name="change">
                <span class="glyphicon glyphicon-edit"></span>
                <spring:message code="change" />
            </button>
            <c:if test="${!isAuthenticated}">
                <button class="btn btn-default" name="changeAndLogin">
                    <span class="glyphicon glyphicon-log-in"></span>
                    <spring:message code="changeAndLogin" />
                </button>
            </c:if>
        </div>
    </div>

</form:form>