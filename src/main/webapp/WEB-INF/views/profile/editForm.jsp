<t:messagesPanel/>

<form:form method="put" servletRelativeAction="/app/profile" modelAttribute="profileForm" class="form-horizontal">

    <div class="form-group">
        <form:label path="accountId" class="col-sm-2 control-label">Account ID</form:label>
        <div class="col-sm-8">
            <form:input path="accountId" cssClass="form-control" />
            <form:errors path="accountId"/>
        </div>
    </div>
    <div class="form-group">
        <form:label path="firstName" class="col-sm-2 control-label">Name</form:label>
        <div class="col-sm-4">
            <form:input path="firstName" cssClass="form-control" placeholder="First Name"/>
            <form:errors path="firstName"/>
        </div>
        <div class="col-sm-4">
            <form:input path="lastName" cssClass="form-control" placeholder="Last name"/>
            <form:errors path="lastName"/>
        </div>
    </div>
    <div class="form-group">
        <form:label path="enabledAutoLogin" cssClass="col-sm-2 control-label">Auto Login</form:label>
        <div class="col-sm-4">
            <c:forEach var="enabledAutoLoginCodeListElement" items="${CL_ENABLED}">
                <div class="radio-inline">
                    <form:radiobutton path="enabledAutoLogin"
                            value="${enabledAutoLoginCodeListElement.key}"
                            label="${enabledAutoLoginCodeListElement.value}"/>
                </div>
            </c:forEach>
            <div>
                <form:errors path="enabledAutoLogin"/>
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-8">
            <button class="btn btn-default">
                <span class="glyphicon glyphicon-floppy-save"></span>
                Save
            </button>
            <a href="${contextPath}/app/profile" class="btn btn-default">
                <span class="glyphicon glyphicon-step-backward"></span>
                Back
            </a>
        </div>
    </div>
</form:form>
