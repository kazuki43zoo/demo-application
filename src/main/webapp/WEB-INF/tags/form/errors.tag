<%@ tag body-content="scriptless" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="f" uri="http://terasoluna.org/functions" %>

<%@ attribute name="path" type="java.lang.String" required="true" %>

<form:errors path="${path}">
    <ul style="-webkit-padding-start: 20px; padding-left: 20px; margin-bottom: 0px;" class="error">
        <c:forEach var="message" items="${messages}">
            <li>${f:h(message)}</li>
        </c:forEach>
    </ul>
</form:errors>
