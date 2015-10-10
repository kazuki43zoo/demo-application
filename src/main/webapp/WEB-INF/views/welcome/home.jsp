<h1 id="welcomeMessage"><spring:message code="welcome.message" /></h1>

<p>
    The time on the server is
    <joda:format value="${serverTime}"
            pattern="${applicationConfigs.dateTimeFormat}"
            dateTimeZone="${applicationConfigs.timeZoneFormat}"/>.
</p>

<t:messagesPanel/>