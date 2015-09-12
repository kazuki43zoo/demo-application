<t:messagesPanel/>
<form method="get" action="${contextPath}/app/accounts/${f:h(account.accountUuid)}" class="form-horizontal">
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-8">
            <a href="${contextPath}/app/accounts" class="btn btn-default pull-left">
                <span class="glyphicon glyphicon-step-backward"></span>
                Account List
            </a>
            <button class="btn btn-default pull-right">
                <span class="glyphicon glyphicon-refresh"></span>
                Refresh
            </button>
        </div>
    </div>

    <jsp:include page="inc/accountDisplay.jsp"/>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-8">
            <button class="btn btn-default" name="form" value="edit">
                <span class="glyphicon glyphicon-edit"></span>
                Edit
            </button>
            <div class="btn btn-default" onclick="$('#deleteForm').submit();">
                <span class="glyphicon glyphicon-trash"></span>
                Delete
            </div>
            <c:if test="${account.passwordLock != null and securityConfigs.authenticationFailureMaxCount < account.passwordLock.failureCount}">
                <div class="btn btn-default pull-right" onclick="$('#unlockForm').submit();">
                    <span class="glyphicon glyphicon-lock"></span>
                    <span class="glyphicon glyphicon-remove"></span>
                    Unlock
                </div>
            </c:if>
        </div>
    </div>
</form>

<form:form id="deleteForm" method="delete" action="${contextPath}/app/accounts/${f:h(account.accountUuid)}" class="sr-only" />
<form:form id="unlockForm" method="patch" action="${contextPath}/app/accounts/${f:h(account.accountUuid)}/unlock" class="sr-only"/>
