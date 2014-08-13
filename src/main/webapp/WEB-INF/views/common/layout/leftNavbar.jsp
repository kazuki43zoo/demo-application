
<div class="well">
    <div>
        <spring:message code="menu" />
    </div>
    <ul id="nav-tab" class="nav nav-pills nav-stacked">
        <sec:authorize url="/admin/h2Console">
            <li><a href="${contextPath}/admin/h2Console"><span
                    class="glyphicon glyphicon-cog"></span> <spring:message
                        code="menu.h2AdminConsole" /></a></li>
        </sec:authorize>
        <sec:authorize url="/accounts">
            <li><a href="${contextPath}/accounts"><span class="glyphicon glyphicon-user"></span>
                    <spring:message code="menu.accountsManagement" /></a></li>
        </sec:authorize>
        <sec:authorize access="!isAuthenticated()">
            <li><a href="${contextPath}/password"><span class="glyphicon glyphicon-lock"></span>
                    <spring:message code="menu.changePassword" /></a></li>
        </sec:authorize>
        <sec:authorize url="/timecards">
            <li><a href="${contextPath}/timecards"><span class="glyphicon glyphicon-time"></span>
                    <spring:message code="menu.timeCard" /></a></li>
            <li class="divider"></li>
        </sec:authorize>

    </ul>
</div>
