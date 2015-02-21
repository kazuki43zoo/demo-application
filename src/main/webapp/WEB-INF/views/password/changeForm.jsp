<t:messagesPanel/>
<form:form action="${contextPath}/password" class="form-horizontal"
           method="post"
           modelAttribute="passwordForm">

    <sec:authorize access="isAuthenticated()" var="isAuthenticated"/>
    <c:choose>
        <c:when test="${isAuthenticated}">
            <form:hidden path="accountId"/>
        </c:when>
        <c:otherwise>
            <div class="form-group">
                <form:label path="accountId" cssClass="col-sm-2 control-label">Account ID</form:label>
                <div class="col-sm-8">
                    <form:input cssClass="form-control" path="accountId" placeholder="Account ID"/>
                    <form:errors path="accountId"/>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
    <div class="form-group">
        <form:label path="oldPassword" class="col-sm-2 control-label">Old Password</form:label>
        <div class="col-sm-4">
            <form:password cssClass="form-control" path="oldPassword" placeholder="Old Password"/>
            <form:errors path="oldPassword"/>
        </div>
    </div>
    <div class="form-group">
        <form:label path="password" class="col-sm-2 control-label">New Password</form:label>
        <div class="col-sm-4">
            <form:password cssClass="form-control" path="password" placeholder="New Password"/>
            <form:errors path="password"/>
        </div>
        <div class="col-sm-4">
            <form:password cssClass="form-control" path="confirmPassword" placeholder="Confirm Password"/>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-8">
            <button class="btn btn-default" name="change">
                <span class="glyphicon glyphicon-edit"></span>
                Change
            </button>
            <c:if test="${!isAuthenticated}">
                <button class="btn btn-default" name="changeAndLogin">
                    <span class="glyphicon glyphicon-log-in"></span>
                    Change and Login
                </button>
            </c:if>
        </div>
    </div>

</form:form>