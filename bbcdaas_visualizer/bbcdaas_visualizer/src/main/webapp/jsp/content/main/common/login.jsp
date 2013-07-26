<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>

<c:set var="handleLoginFormControl" value="<%=VisualizerConstants.FORWARD_HANDLE_LOGIN_FORM_CONTROL %>" />

<b>
	<spring:message code="project.login"/>:
</b>
<br/><br/>

<form method="post" action="${handleLoginFormControl}">
	<div>
		<label for="username"><spring:message code="project.username"/>:</label><br/>
		<input name="username" size="15" id="username"/><br/>
		<label for="password"><spring:message code="project.password"/>:</label><br/>
		<input type="password" size="15" id="password" name="password"/><br/>
		<input type="submit" name="submit" value='<spring:message code="project.login.submit"/>'/>
	</div>
</form>
