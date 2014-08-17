<div class="well">
    <div>
        <spring:message code="menu" />
    </div>
    <ul class="nav nav-pills nav-stacked">
        <sec:authorize url="/admin/h2Console">
            <li class="${fn:startsWith(matchingPathPattern,'/admin/h2Console') ? 'active' : ''}"><a
                href="${contextPath}/admin/h2Console"><span class="glyphicon glyphicon-cog"></span>
                    <spring:message code="menu.h2AdminConsole" /></a></li>
        </sec:authorize>
        <sec:authorize url="/accounts">
            <li class="${fn:startsWith(matchingPathPattern,'/accounts') ? 'active' : ''}"><a
                href="${contextPath}/accounts"><span class="glyphicon glyphicon-user"></span> <spring:message
                        code="menu.accountsManagement" /></a></li>
        </sec:authorize>
        <sec:authorize access="!isAuthenticated()">
            <li class="${fn:startsWith(matchingPathPattern,'/password') ? 'active' : ''}"><a
                href="${contextPath}/password"><span class="glyphicon glyphicon-lock"></span> <spring:message
                        code="menu.changePassword" /></a></li>
        </sec:authorize>
        <sec:authorize url="/timecards">
            <li class="${fn:startsWith(matchingPathPattern,'/timecards') ? 'active' : ''}"><a
                href="${contextPath}/timecards"><span class="glyphicon glyphicon-time"></span> <spring:message
                        code="menu.timeCard" /></a></li>
            <li class="divider"></li>
        </sec:authorize>
    </ul>
</div>
