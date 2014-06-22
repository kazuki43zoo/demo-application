
<t:messagesPanel />

<form:form action="${contextPath}/profile" class="form-horizontal" method="post" modelAttribute="profileForm">

    <div class="form-group">
        <form:label path="accountId" class="col-sm-2 control-label">Account ID</form:label>
        <div class="col-sm-8">
            <form:input cssClass="form-control" path="accountId" />
            <form:errors path="accountId" />
        </div>
    </div>
    <div class="form-group">
        <form:label path="firstName" class="col-sm-2 control-label">Name</form:label>
        <div class="col-sm-4">
            <form:input cssClass="form-control" path="firstName" placeholder="First Name" />
            <form:errors path="firstName" />
        </div>
        <div class="col-sm-4">
            <form:input cssClass="form-control" path="lastName" placeholder="Last name" />
            <form:errors path="lastName" />
        </div>
    </div>
    <div class="form-group">
        <form:label path="password" class="col-sm-2 control-label">Password</form:label>
        <div class="col-sm-4">
            <form:input type="password" cssClass="form-control" path="password" placeholder="New password" />
            <form:errors path="password" />
        </div>
        <div class="col-sm-4">
            <form:input type="password" cssClass="form-control" path="confirmPassword" placeholder="Confirm password" />
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-8">
            <button class="btn btn-default" name="_method" value="patch">
                <span class="glyphicon glyphicon-floppy-save"></span> Save
            </button>
            <a href="${contextPath}/profile" class="btn btn-default"> <span
                class="glyphicon glyphicon-step-backward"></span> Back
            </a>
        </div>
    </div>
</form:form>
