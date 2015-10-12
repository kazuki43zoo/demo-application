<div class="form-group">
    <form:label path="accountId" cssClass="col-sm-2 control-label">Account ID</form:label>
    <div class="col-sm-8">
        <form:input path="accountId" cssClass="form-control" />
        <formEx:errors path="accountId"/>
    </div>
</div>
<div class="form-group">
    <form:label path="firstName" cssClass="col-sm-2 control-label">Name</form:label>
    <div class="col-sm-4">
        <form:input path="firstName" cssClass="form-control" placeholder="First Name"/>
        <formEx:errors path="firstName"/>
    </div>
    <div class="col-sm-4">
        <form:input path="lastName" cssClass="form-control" placeholder="Last name"/>
        <formEx:errors path="lastName"/>
    </div>
</div>
<div class="form-group">
    <form:label path="enabled" cssClass="col-sm-2 control-label">Enabled</form:label>
    <div class="col-sm-4">
        <c:forEach var="enabledCodeListElement" items="${CL_ENABLED}">
            <div class="radio-inline">
                <form:radiobutton path="enabled" value="${enabledCodeListElement.key}" label="${enabledCodeListElement.value}"/>
            </div>
        </c:forEach>
        <div><formEx:errors path="enabled"/></div>
    </div>
</div>
<div class="form-group">
    <form:label path="enabledAutoLogin" cssClass="col-sm-2 control-label">Auto Login</form:label>
    <div class="col-sm-4">
        <c:forEach var="enabledAutoLoginCodeListElement" items="${CL_ENABLED}">
            <div class="radio-inline">
                <form:radiobutton path="enabledAutoLogin" value="${enabledAutoLoginCodeListElement.key}" label="${enabledAutoLoginCodeListElement.value}"/>
            </div>
        </c:forEach>
        <div><formEx:errors path="enabledAutoLogin"/></div>
    </div>
</div>
<div class="form-group">
    <form:label path="password" class="col-sm-2 control-label">Password</form:label>
    <div class="col-sm-4">
        <form:password path="password" cssClass="form-control" placeholder="Password" />
        <formEx:errors path="password"/>
    </div>
    <div class="col-sm-4">
        <form:password path="confirmPassword" cssClass="form-control" placeholder="Confirm Password" />
        <formEx:errors path="confirmPassword"/>
    </div>
</div>

<div class="form-group">
    <form:label path="authorities" class="col-sm-2 control-label">Authorities</form:label>
    <div class="col-sm-8">
        <c:forEach var="authorityCodeListElement" items="${CL_AUTHORITY}">
            <div class="checkbox-inline">
                <form:checkbox path="authorities" value="${authorityCodeListElement.key}" label="${authorityCodeListElement.value}"/>
            </div>
        </c:forEach>
        <div><formEx:errors path="authorities"/></div>
    </div>
</div>

