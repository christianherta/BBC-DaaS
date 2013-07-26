<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page  import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>

<c:set var="processName_processing" value="<%=VisualizerConstants.PROCESSNAME_PROCESSING %>" />
<c:set var="processName_statisticsWriter" value="<%=VisualizerConstants.PROCESSNAME_STATISTICS_WRITER %>" />
<c:set var="sessionContainer" value="<%=VisualizerConstants.KEY_SESSION_CONTAINER %>" />

<c:if test="${sessionScope[sessionContainer].processingSessionBean.name eq processName_statisticsWriter}">
	<c:if test="${sessionScope[sessionContainer].processingSessionBean.running eq true}">
		<spring:message code="statistics.processRunning" />
	</c:if>
	<c:if test="${sessionScope[sessionContainer].processingSessionBean.running eq false}">
		<spring:message code="statistics.processNotRunning" />
	</c:if>
</c:if>

<c:if test="${sessionScope[sessionContainer].processingSessionBean.name eq processName_processing}">
	<c:if test="${sessionScope[sessionContainer].processingSessionBean.running eq true}">
		<spring:message code="processing.processRunning" />
	</c:if>
	<c:if test="${sessionScope[sessionContainer].processingSessionBean.running eq false}">
		<spring:message code="processing.processNotRunning" />
	</c:if>
</c:if>

<c:if test="${sessionScope[sessionContainer].processingSessionBean.error eq true}">
	<spring:message code="project.error" />
</c:if>