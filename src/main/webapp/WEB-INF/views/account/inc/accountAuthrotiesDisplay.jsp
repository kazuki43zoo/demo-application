<c:forEach var="grantedAuthority"
           items="${account.authorities}"
           varStatus="rowStatus">
    <c:set var="roleCode">ROLE_${grantedAuthority.authority}</c:set>
    <div class="form-group">
        <c:choose>
            <c:when test="${rowStatus.count==1}">
                <label class="col-sm-2 control-label">Authorities</label>
                <div class="col-sm-8">
                    <span class="form-control">${f:h(CL_AUTHORITY[roleCode])}</span>
                </div>
            </c:when>
            <c:otherwise>
                <div class="col-sm-offset-2 col-sm-8">
                    <span class="form-control">${f:h(CL_AUTHORITY[roleCode])}</span>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</c:forEach>
