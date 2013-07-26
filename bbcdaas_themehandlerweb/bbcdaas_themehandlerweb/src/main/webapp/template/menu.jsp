<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@page import="de.bbcdaas.themehandlerweb.constants.Constants" %>

<c:set var="forward_login" value="<%=Constants.FORWARD_LOGIN %>" />
<c:set var="forward_logout" value="<%=Constants.FORWARD_LOGOUT %>" />
<c:set var="forward_themeCloudCreator" value="<%=Constants.FORWARD_THEMECLOUD_CREATOR %>" />
<c:set var="forward_userManagement" value="<%=Constants.FORWARD_USERMANAGEMENT %>" />

<sec:authorize access="isAnonymous()">
	<br/>
	<a href="${forward_login}">
		<spring:message code="menu.login"/>
	</a>   
</sec:authorize>
		
<sec:authorize ifAnyGranted="ROLE_USER, ROLE_ADMIN">
	<br/>
	<a href="<c:url value='j_spring_security_logout' />">
		<spring:message code="menu.logout"/>
	</a>   
</sec:authorize>
	
<sec:authorize ifAnyGranted="ROLE_USER, ROLE_ADMIN">
	<br/>
	<a href="${forward_themeCloudCreator}">
		<spring:message code="menu.themeCloudCreator"/>
	</a>   
</sec:authorize>

<sec:authorize ifAnyGranted="ROLE_ADMIN">
	<br/>
	<a href="${forward_userManagement}">
		<spring:message code="menu.userManagement"/>
	</a>
</sec:authorize>