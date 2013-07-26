<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@page import="de.bbcdaas.themehandlerweb.constants.Constants" %>

<c:set var="sessionContainer" value="<%=Constants.KEY_SESSION_CONTAINER %>"/>

<div id="themeCloudViewer" style="background: lightgrey;height: 670px; width: 60%;overflow: auto;padding-top: 10px;"
	 <c:if test="${themeCloudViewer_active eq 0}">class='hidden'</c:if>>

	<input type="hidden" name="selectedThemeCloudName" id="selectedThemeCloudName"/>
	<c:set var="themeCloudCount" value="0"/>
	<c:forEach var="themeCloud" items="${sessionScope[sessionContainer].themeCloudSessionBean.themeClouds}">
		<c:set var="themeCloudCount" value="${themeCloudCount + 1}"/>
	</c:forEach>
	<c:if test="${themeCloudCount eq 0}" >
		<span style="color:red">
			<spring:message code="themeClouds.noThemeClouds"/>
		</span>
	</c:if>
	<c:forEach var="themeCloud" items="${sessionScope[sessionContainer].themeCloudSessionBean.themeClouds}">
		<sec:authorize access="hasRole('ROLE_ADMIN') or ('${themeCloud.user.name}' == principal.username)">
			<%@include file="themeCloudList.jsp" %>
		</sec:authorize>
	</c:forEach>
</div>