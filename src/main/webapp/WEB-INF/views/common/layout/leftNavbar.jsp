<c:set var="matchingPathPattern">
    ${requestScope['org.springframework.web.servlet.HandlerMapping.bestMatchingPattern']}
</c:set>
<div class="well">
    <div>
        <spring:message code="menu"/>
    </div>
    <ul class="nav nav-pills nav-stacked">
        <sec:authorize url="/app/admin/h2Console">
            <li class="${fn:startsWith(matchingPathPattern,'/admin/h2Console') ? 'active' : ''}">
                <a id="h2ConsoleMenuLink"
                   href="<c:url value="/app/admin/h2Console/"/>">
                    <span class="glyphicon glyphicon-cog"></span>
                    <spring:message code="menu.h2AdminConsole"/>
                </a>
            </li>
        </sec:authorize>
        <sec:authorize url="/app/accounts">
            <li class="${fn:startsWith(matchingPathPattern,'/accounts') ? 'active' : ''}">
                <a id="accountsMenuLink"
                   href="<c:url value="/app/accounts/"/>">
                    <span class="glyphicon glyphicon-user"></span>
                    <spring:message code="menu.accountsManagement"/>
                </a>
            </li>
        </sec:authorize>
        <sec:authorize access="!isAuthenticated()">
            <li class="${fn:startsWith(matchingPathPattern,'/password') ? 'active' : ''}">
                <a id="passwordMenuLink"
                   href="<c:url value="/app/password/"/>">
                    <span class="glyphicon glyphicon-lock"></span>
                    <spring:message code="menu.changePassword"/>
                </a>
            </li>
        </sec:authorize>
        <sec:authorize url="/app/timecards">
            <li class="${fn:startsWith(matchingPathPattern,'/timecards') ? 'active' : ''}">
                <a id="timecardsMenuLink"
                   href="<c:url value="/app/timecards/"/>">
                    <span class="glyphicon glyphicon-time"></span>
                    <spring:message code="menu.timeCard"/>
                </a>
            </li>
            <li class="divider"></li>
        </sec:authorize>
    </ul>
</div>
