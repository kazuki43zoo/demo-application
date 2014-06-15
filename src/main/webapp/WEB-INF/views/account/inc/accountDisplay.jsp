
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
    <div class="col-sm-8">
        <c:set var="enabled">${account.enabled}</c:set>
        <span class="form-control">${f:h(CL_ENABLED[enabled])}</span>
    </div>
</div>
<div class="form-group">
    <label class="col-sm-2 control-label">Password</label>
    <div class="col-sm-8">
        <span class="form-control">***************</span>
    </div>
</div>

<jsp:include page="accountAuthrotiesDisplay.jsp" />
