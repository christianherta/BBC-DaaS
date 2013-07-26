<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>

<c:set var="forward_startpage" value="<%=VisualizerConstants.FORWARD_SHOW_STARTPAGE_CONTROL %>" />

<span class="headline_header">
    <a href="${forward_startpage}"><spring:message code="project.title"/></a>
</span>
<br/>
<c:if test="${sessionScope.user ne null}">
    <spring:message code="evaluation.loggedInAs"/>: "<c:out value='${sessionScope.user.name}'/>"
</c:if>

<span style="float: right;margin-right: 10px;">
	
	<spring:message code="project.displayLanguage"/>:
	<a href="?lang=en">en</a>
	|
	<a href="?lang=de">de</a>
	|
	<a href="?lang=ru">ru</a>
</span>