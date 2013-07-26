<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>

<c:set var="websiteURL" value="<%=VisualizerConstants.KEY_WEBSITE_URL %>" />
<c:set var="logoutUserControl" value="<%=VisualizerConstants.FORWARD_LOGOUT_USER_CONTROL %>" />

<spring:message code="evaluation.loggedInAs"/>: "<c:out value='${sessionScope.user.name}'/>"

<input type="button" name="logout" 
		   value='<spring:message code="project.logout"/>' 
		   onmouseup="window.location.href='${applicationScope[websiteURL]}/${logoutUserControl}';"/>

<hr/>