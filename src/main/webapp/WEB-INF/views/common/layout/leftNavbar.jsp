
<div class="well">
	<div>Menu</div>
	<ul id="nav-tab" class="nav nav-pills nav-stacked">
		<sec:authorize url="/admin/h2Console">
			<li><a href="${contextPath}/admin/h2Console"><span
					class="glyphicon glyphicon-cog"></span> H2 Admin Console</a></li>
		</sec:authorize>
		<sec:authorize url="/accounts">
			<li><a href="${contextPath}/accounts"><span
					class="glyphicon glyphicon-user"></span> Accounts Management</a></li>
		</sec:authorize>
		<sec:authorize access="!isAuthenticated()">
			<li><a href="${contextPath}/password"><span
					class="glyphicon glyphicon-lock"></span> Change Password</a></li>
		</sec:authorize>
		<sec:authorize url="/timecards">
			<li><a href="${contextPath}/timecards"><span
					class="glyphicon glyphicon-time"></span> Time Card </a></li>
			<li class="divider"></li>
		</sec:authorize>

	</ul>
</div>
