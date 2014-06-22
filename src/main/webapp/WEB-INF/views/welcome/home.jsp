
<h1>Welcome Demo Application!!</h1>
<p>
    The time on the server is
    <joda:format value="${serverTime}" pattern="${dateTimeFormat}" dateTimeZone="${timeZoneFormat}" />
    .
</p>

<t:messagesPanel />