<t:messagesPanel/>

<a href="${contextPath}/profile/authenticationHistories"
   class="btn btn-default pull-right"> <span
        class="glyphicon glyphicon-refresh"></span> Refresh
</a>

<table class="table table-hover">
    <thead>
    <tr>
        <th>#</th>
        <th>Date Time</th>
        <th>Type</th>
        <th>Result</th>
        <th>Note</th>
        <th>Address</th>
        <th>Agent</th>
    </tr>
    </thead>
    <c:forEach var="authenticationHistory"
               items="${account.authenticationHistories}"
               varStatus="rowStatus">
        <c:set var="strAuthenticationResult">${authenticationHistory.authenticationResult}</c:set>
        <tr>
            <td>${f:h(rowStatus.count)}</td>
            <td><joda:format value="${authenticationHistory.createdAt}"
                             pattern="${dateTimeFormat}"
                             dateTimeZone="${timeZoneFormat}"/></td>
            <td>${f:h(authenticationHistory.authenticationType)}</td>
            <td>${f:h(CL_AUTHENTICATION_RESULT[strAuthenticationResult])}</td>
            <td>${f:h(authenticationHistory.failureReason)}</td>
            <td>${f:h(authenticationHistory.remoteAddress)}</td>
            <td>${f:h(authenticationHistory.agent)}</td>
        </tr>
    </c:forEach>
</table>
