<t:messagesPanel />

<form action="${contextPath}/app/profile" class="form-horizontal" method="get">

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-8">
            <button class="btn btn-default pull-right">
                <span class="glyphicon glyphicon-refresh"></span> Refresh
            </button>
        </div>
    </div>


    <jsp:include page="/WEB-INF/views/account/inc/accountDisplay.jsp" />

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-8">
            <button class="btn btn-default" name="edit">
                <span class="glyphicon glyphicon-edit"></span> Edit
            </button>
        </div>

    </div>
</form>
