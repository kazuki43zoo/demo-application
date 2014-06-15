
<div class="well">
    <div>Menu</div>
    <ul id="nav-tab" class="nav nav-pills nav-stacked">
        <sec:authorize access="hasRole('ROLE_ADMIN')">
            <li><a href="${contextPath}/admin/h2Console">H2 Admin Console</a></li>
        </sec:authorize>
        <sec:authorize access="hasRole('ROLE_ACCOUNTMNG')">
            <li><a href="${contextPath}/accounts">Accounts Management</a></li>
        </sec:authorize>
    </ul>
</div>
