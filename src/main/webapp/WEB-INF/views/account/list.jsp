

<t:messagesPanel />

<form action="${contextPath}/accounts" class="form-horizontal" method="get">
    <spring:nestedPath path="accountsSearchQuery">
        <div class="form-group">
            <div class="col-sm-8">
                <form:input cssClass="form-control" path="accountId" placeholder="(Please input Account ID)" />
                <form:errors path="accountId" />
            </div>
            <button class="btn btn-default" name="filter">Filter</button>
            <a href="${contextPath}/accounts?form=create" class="btn btn-default pull-right">New</a>
        </div>
    </spring:nestedPath>
</form>

<table class="table table-hover">
    <thead>
        <tr>
            <th>#</th>
            <th>Account ID</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Enabled</th>
        </tr>
    <thead>
        <c:forEach var="account" items="${page.content}" varStatus="rowStatus">
            <c:set var="enabled">${account.enabled}</c:set>
            <tr>
                <td>${f:h(rowStatus.count)}</td>
                <td><a href="${contextPath}/accounts/${f:h(account.accountUuid)}">${f:h(account.accountId)}</a></td>
                <td>${f:h(account.firstName)}</td>
                <td>${f:h(account.lastName)}</td>
                <td>${f:h(CL_ENABLED[enabled])}</td>
            </tr>
        </c:forEach>
</table>
<div class="paginationContainer">
    <t:pagination page="${page}" outerElementClass="pagination" />
</div>
