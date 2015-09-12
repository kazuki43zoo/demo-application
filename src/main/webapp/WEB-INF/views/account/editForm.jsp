<t:messagesPanel/>

<form:form method="put" action="${contextPath}/app/accounts/${f:h(account.accountUuid)}" modelAttribute="accountForm" class="form-horizontal" >

    <jsp:include page="inc/accountForm.jsp"/>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-8">
            <a href="${contextPath}/app/accounts/${f:h(account.accountUuid)}" class="btn btn-default">
                <span class="glyphicon glyphicon-step-backward"></span>
                Back
            </a>
            <form:button class="btn btn-default">
                <span class="glyphicon glyphicon-floppy-save"></span>
                Save
            </form:button>
        </div>
    </div>
</form:form>
