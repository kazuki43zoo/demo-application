
<ul class="nav navbar-nav">
    <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"> <span
            class="glyphicon glyphicon-user"></span> <sec:authentication property="principal.account.firstName" /><b
            class="caret"></b>
    </a>
        <ul class="dropdown-menu pull-right">
            <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_USER')">
                <li><a href="${contextPath}/profile"><span class="glyphicon glyphicon-edit"></span> Edit
                        profile</a></li>
                <li class="divider"></li>
            </sec:authorize>
            <li><a id="logout" href="javascript:logout();"><span class="glyphicon glyphicon-log-out"></span>
                    Logout</a></li>
        </ul></li>
</ul>
<form:form id="logoutForm" action="${contextPath}/logout" class="sr-only" />
<script>
    function logout() {
        $('#logoutForm').submit();
    }
</script>
