<div class="form-group">
    <label class="col-sm-2 control-label">Account ID</label>

    <div class="col-sm-8">
        <span class="form-control">${f:h(account.accountId)}</span>
    </div>
</div>
<div class="form-group">
    <label class="col-sm-2 control-label">Name</label>

    <div class="col-sm-4">
        <span class="form-control">${f:h(account.firstName)}</span>
    </div>
    <div class="col-sm-4">
        <span class="form-control">${f:h(account.lastName)}</span>
    </div>
</div>
<div class="form-group">
    <label class="col-sm-2 control-label">Enabled</label>

    <div class="col-sm-2">
        <c:set var="enabled">${account.enabled}</c:set>
        <span class="form-control">${f:h(CL_ENABLED[enabled])}</span>
    </div>
    <c:if test="${account.passwordLock != null}">
        <label class="col-sm-4 control-label">Password Failure Count</label>

        <div class="col-sm-2">
            <div class="form-control">
                <c:if
                        test="${account.passwordLock != null and securityConfigs.authenticationFailureMaxCount < account.passwordLock.failureCount}">
                    <span class="glyphicon glyphicon-lock"></span>
                </c:if>
                    ${f:h(account.passwordLock.failureCount)}
            </div>
        </div>
    </c:if>
</div>
<div class="form-group">
    <label class="col-sm-2 control-label">Password</label>

    <div class="col-sm-3">
        <span class="form-control">***************</span>
    </div>
    <div class="col-sm-5">
        <span class="form-control">Modified at : <joda:format
                value="${account.passwordModifiedAt}"
                pattern="${dateTimeFormat}"
                dateTimeZone="${timeZoneFormat}"/></span>
    </div>
</div>

<jsp:include page="accountAuthrotiesDisplay.jsp"/>
