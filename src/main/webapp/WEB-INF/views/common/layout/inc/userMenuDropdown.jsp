
<ul class="nav navbar-nav">
	<li class="dropdown"><a href="#" class="dropdown-toggle"
		data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span>
			<sec:authentication property="principal.account.firstName" /> <b
			class="caret"></b>
	</a>
		<ul class="dropdown-menu pull-right">
			<sec:authorize url="/profile">
				<li><a href="${contextPath}/profile"><span
						class="glyphicon glyphicon-edit"></span> Edit Profile</a></li>
				<li><a href="${contextPath}/profile/authenticationHistories"><span
						class="glyphicon glyphicon-list"></span> Authentication Histories</a></li>
				<li class="divider"></li>
			</sec:authorize>
			<li><a href="${contextPath}/password"><span
					class="glyphicon glyphicon-lock"></span> Change Password</a></li>
			<li class="divider"></li>
			<li><a id="logout"><span class="glyphicon glyphicon-log-out"></span>
					Logout</a></li>
		</ul></li>
</ul>
<form:form id="logoutForm" action="${contextPath}/auth/logout"
	class="sr-only" />
