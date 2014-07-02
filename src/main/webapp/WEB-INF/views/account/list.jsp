

<t:messagesPanel />

<form action="${contextPath}/accounts" class="form-horizontal" method="get">
    <spring:nestedPath path="accountsSearchQuery">
        <div class="form-group">
            <div class="col-sm-4">
                <form:input cssClass="form-control" path="word" placeholder="Account ID or Account Name" />
                <form:errors path="word" />
            </div>
            <div class="col-sm-4">

                <c:forEach var="accountSearchTargetCodeListElement" items="${CL_ACCOUNT_SEARCH_TARGET}"
                    varStatus="rowStatus">
                    <div class="checkbox-inline">
                        <form:checkbox path="targets" value="${accountSearchTargetCodeListElement.key}"
                            label="${accountSearchTargetCodeListElement.value}" />
                    </div>
                </c:forEach>

                <div>
                    <form:errors path="targets" />
                </div>
            </div>
            <button class="btn btn-default" name="filter">
                <span class="glyphicon glyphicon-search"></span> Filter
            </button>
            <a href="${contextPath}/accounts?form=create" class="btn btn-default pull-right"><span
                class="glyphicon glyphicon-plus"></span> Create</a>
        </div>

    </spring:nestedPath>
</form>

<table class="table table-hover">
    <thead>
        <tr>
            <th>#</th>
            <th>Account ID</th>
            <th>Account name</th>
            <th>Disabled</th>
            <th>Locked</th>
        </tr>
    <thead>
        <c:forEach var="account" items="${page.content}" varStatus="rowStatus">
            <c:set var="enabled">${account.enabled}</c:set>
            <tr>
                <td>${f:h(rowStatus.count)}</td>
                <td><a href="${contextPath}/accounts/${f:h(account.accountUuid)}">${f:h(account.accountId)}</a></td>
                <td>${f:h(account.firstName)}&nbsp;${f:h(account.lastName)}</td>
                <td><c:if test="${!account.enabled}">
                        <span class="glyphicon glyphicon-ok"></span>
                    </c:if></td>
                <td><c:if
                        test="${account.passwordLock != null and securityConfigs.authenticationFailureMaxCount < account.passwordLock.failureCount}">
                        <span class="glyphicon glyphicon-ok"></span>
                    </c:if></td>
            </tr>
        </c:forEach>
</table>
<div class="paginationContainer">
    <t:pagination page="${page}" criteriaQuery="${f:query(accountsSearchQuery)}" outerElementClass="pagination" />
</div>
