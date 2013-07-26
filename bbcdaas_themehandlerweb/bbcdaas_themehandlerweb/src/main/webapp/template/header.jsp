<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@page import="de.bbcdaas.themehandlerweb.constants.Constants" %>

<c:set var="forward_startpage" value="<%=Constants.FORWARD_STARTPAGE %>" />

<span id="dnd_mouseText" class="dnd_mouseText"></span>
<span id="header_title">
    <a href="${forward_startpage}">
		<spring:message code="header.title"/>
	</a>
</span>
<br/>
<sec:authorize ifAnyGranted="ROLE_USER, ROLE_ADMIN">
    <spring:message code="header.loggedInAs"/>
	: "<sec:authentication property="principal.username" />"
</sec:authorize>
<sec:authorize ifAllGranted="ROLE_ADMIN">
	(<spring:message code="project.admin"/>)
</sec:authorize>

<span id="header_languageSelection">
	<spring:message code="header.displayLanguage"/>:
	<a href="?lang=en">
		en
	</a>
	|
	<a href="?lang=de">
		de
	</a>
	|
	<a href="?lang=ru">
		ru
	</a>
</span>