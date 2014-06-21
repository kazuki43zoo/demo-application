
<div class="container">
    <div class="navbar-header">
        <jsp:include page="inc/topNavbarHeader.jsp" />
    </div>
    <div class="collapse navbar-collapse pull-right">
        <sec:authorize access="isAuthenticated()" var="isAuthenticated" />
        <c:choose>
            <c:when test="${isAuthenticated}">
                <jsp:include page="inc/userMenuDropdown.jsp" />
            </c:when>
            <c:otherwise>
                <jsp:include page="/WEB-INF/views/auth/loginForm.jsp">
                    <jsp:param name="including" value="true" />
                </jsp:include>
            </c:otherwise>
        </c:choose>
    </div>
</div>
