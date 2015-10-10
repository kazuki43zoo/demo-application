<div class="container">
    <div class="navbar-header">
        <jsp:include page="inc/topNavbarHeader.jsp"/>
    </div>
    <div class="navbar-header">
        <jsp:include page="inc/languageDropdown.jsp"/>
    </div>
    <div class="navbar-header">
        <jsp:include page="inc/themeDropdown.jsp"/>
    </div>
    <div class="collapse navbar-collapse pull-right">
        <sec:authorize access="isAuthenticated()">
            <jsp:include page="inc/userMenuDropdown.jsp"/>
        </sec:authorize>
    </div>
</div>
