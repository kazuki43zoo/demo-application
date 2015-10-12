<t:messagesPanel/>

<spring:hasBindErrors name="accountForm">
    <c:set var="bindingResult" scope="request" value="${errors}"/>
</spring:hasBindErrors>

<form:form method="put" servletRelativeAction="/app/accounts/${f:h(account.accountUuid)}" modelAttribute="accountForm"
           class="form-horizontal">

    <jsp:include page="inc/accountForm.jsp"/>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-8">
            <a href="<c:url value='/app/accounts/${f:h(account.accountUuid)}'/>" class="btn btn-default">
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
