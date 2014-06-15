<form:form action="${contextPath}/accounts/${f:h(account.accountUuid)}" class="form-horizontal" method="post"
    modelAttribute="accountForm">

    <jsp:include page="inc/accountForm.jsp" />

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-8">
            <button class="btn btn-default" name="_method" value="put">
                <span class="glyphicon glyphicon-floppy-save"></span> Save
            </button>
        </div>
    </div>
</form:form>
