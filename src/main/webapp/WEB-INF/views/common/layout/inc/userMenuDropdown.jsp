
<ul class="nav navbar-nav">
    <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"> <span
            class="glyphicon glyphicon-user"></span> <sec:authentication property="principal.account.firstName" /><b
            class="caret"></b>
    </a>
        <ul class="dropdown-menu pull-right">
            <sec:authorize url="/app/profile">
                <li><a href="${contextPath}/app/profile"><span class="glyphicon glyphicon-edit"></span> Edit
                        profile</a></li>
                <li><a href="${contextPath}/app/profile/authenticationHistories"><span
                        class="glyphicon glyphicon-list"></span> Authentication Histories</a></li>
                <li class="divider"></li>
            </sec:authorize>
            <li><a id="logout" href="javascript:logout();"><span class="glyphicon glyphicon-log-out"></span>
                    Logout</a></li>
        </ul></li>
</ul>
<form:form id="logoutForm" action="${contextPath}/auth/logout" class="sr-only" />
<script>
    function logout() {
        $('#logoutForm').submit();
    }
</script>
