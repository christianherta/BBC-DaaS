<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.themehandlerweb.constants.Constants" %>

<c:set var="sessionContainer" value="<%=Constants.KEY_SESSION_CONTAINER %>" />
<c:set var="role_admin" value="<%=Constants.ROLE_ADMIN %>" />

<%-- user list --%>
<div class="userList">
	<b><spring:message code="project.user"/>:</b>
	<br/>
	<c:set var="i" value="0"/>
	<select name="userList" id="userList" size="5" style="min-width: 100px;">
		<c:forEach var="user" items="${sessionScope[sessionContainer].userList}">
			<option value="${user.id}"
					<c:if test="${user.role eq role_admin}">style="color: red;"</c:if>
					<c:if test="${i eq 0}">selected="selected"</c:if>>
				<c:out value="${user.name}"/>
			</option>
			<c:set var="i" value="1"/>
		</c:forEach>
	</select>
</div>
<%-- /user list --%>