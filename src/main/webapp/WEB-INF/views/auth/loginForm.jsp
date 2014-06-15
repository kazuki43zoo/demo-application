<t:messagesPanel messagesAttributeName="SPRING_SECURITY_LAST_EXCEPTION" messagesType="danger" />
<c:remove var="SPRING_SECURITY_LAST_EXCEPTION"/>
<form:form action="${contextPath}/authentication" class="navbar-form" method="post">
    <div class="form-group">
        <input type="text" name="userId" class="form-control" placeholder="User ID">
    </div>
    <div class="form-group">
        <input type="password" name="password" class="form-control" placeholder="Password">
    </div>
    <button class="btn">
        <span class="glyphicon glyphicon-log-in"></span>
    </button>
</form:form>
