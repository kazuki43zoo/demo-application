<t:messagesPanel/>

<form:form action="${contextPath}/accounts"
           class="form-horizontal"
           method="post"
           modelAttribute="accountForm">

    <jsp:include page="inc/accountForm.jsp"/>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-8">
            <button class="btn btn-default">
                <span class="glyphicon glyphicon-floppy-save"></span>
                Save
            </button>
            <a href="${contextPath}/accounts" class="btn btn-default">
                <span class="glyphicon glyphicon-step-backward"></span>
                Back
            </a>
        </div>
    </div>

</form:form>
