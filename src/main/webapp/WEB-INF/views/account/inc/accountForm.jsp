
<div class="form-group">
    <form:label path="accountId" cssClass="col-sm-2 control-label">Account ID</form:label>
    <div class="col-sm-8">
        <form:input cssClass="form-control" path="accountId" />
        <form:errors path="accountId" />
    </div>
</div>
<div class="form-group">
    <form:label path="firstName" cssClass="col-sm-2 control-label">Name</form:label>
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
    <label class="col-sm-2 control-label">Enabled</label>
    <div class="col-sm-4">
        <c:forEach var="enabledCodeListElement" items="${CL_ENABLED}">
            <div class="radio-inline">
                <form:radiobutton path="enabled" value="${enabledCodeListElement.key}"
                    label="${enabledCodeListElement.value}" />
            </div>
        </c:forEach>
        <div>
            <form:errors path="enabled" />
        </div>
    </div>

</div>
<div class="form-group">
    <form:label path="password" class="col-sm-2 control-label">Password</form:label>
    <div class="col-sm-4">
        <form:input type="password" cssClass="form-control" path="password" />
        <form:errors path="password" />
    </div>
    <div class="col-sm-4">
        <form:input type="password" cssClass="form-control" path="confirmPassword" placeholder="Confirm Password" />
    </div>
</div>

<div class="form-group">
    <form:label path="authorities" class="col-sm-2 control-label">Authorities</form:label>
    <div class="col-sm-8">
        <c:forEach var="authorityCodeListElement" items="${CL_AUTHORITY}" varStatus="rowStatus">
            <c:set var="authority">${fn:substringAfter(authorityCodeListElement.key,'ROLE_')}</c:set>
            <div class="checkbox-inline">
                <form:checkbox path="authorities" value="${authority}" label="${authorityCodeListElement.value}" />
            </div>
        </c:forEach>
        <div>
            <form:errors path="authorities" />
        </div>
    </div>
</div>

