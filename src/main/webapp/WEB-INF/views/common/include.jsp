<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://terasoluna.org/functions" prefix="f" %>
<%@ taglib uri="http://terasoluna.org/tags" prefix="t" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles-extras" prefix="tilesx" %>
<%@ taglib uri="http://www.joda.org/joda/time/tags" prefix="joda" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<spring:eval expression="@securityConfigs" var="securityConfigs"/>
<spring:eval expression="@applicationConfigs" var="applicationConfigs"/>
