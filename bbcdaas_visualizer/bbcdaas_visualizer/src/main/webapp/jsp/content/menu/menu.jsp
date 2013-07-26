<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>
<%@page import="de.bbcdaas.visualizer.constants.FileOutputConstants" %>
<%@page import="de.bbcdaas.visualizer.constants.SyntagCloudsConstants" %>
<%@page import="de.bbcdaas.visualizer.constants.EvaluationConstants" %>
<%@page import="de.bbcdaas.visualizer.constants.ThemeCloudConstants" %>

<c:set var="forward_login" value="<%=VisualizerConstants.FORWARD_SHOW_LOGIN_FORM_CONTROL %>" />
<c:set var="forward_logout" value="<%=VisualizerConstants.FORWARD_LOGOUT_USER_CONTROL %>" />
<c:set var="forward_help" value="<%=VisualizerConstants.FORWARD_SHOW_HELP_PAGE_CONTROL %>" />
<c:set var="forward_statistics" value="<%=FileOutputConstants.FORWARD_SHOW_STATISTICSPAGE_CONTROL %>" />
<c:set var="forward_processing" value="<%=VisualizerConstants.FORWARD_SHOW_PROCESSING_PAGE_CONTROL %>" />
<c:set var="forward_search" value="<%=VisualizerConstants.FORWARD_SHOW_SEARCH_PAGE_CONTROL %>" />
<c:set var="forward_evaluation" value="<%=EvaluationConstants.FORWARD_SHOW_EVALUATION_PAGE_CONTROL %>" />
<c:set var="forward_syntagClouds" value="<%=SyntagCloudsConstants.FORWARD_SHOW_SYNTAG_CLOUDS_PAGE_CONTROL %>" />
<c:set var="forward_userManagement" value="<%=VisualizerConstants.FORWARD_USER_MANAGEMENT_CONTROL %>" />
<c:set var="forward_fileOutput" value="<%=FileOutputConstants.FORWARD_SHOW_FILE_OUTPUT_CONTROL %>" />
<c:set var="forward_themeClouds" value="<%=ThemeCloudConstants.FORWARD_SHOW_THEMECLOUD_PAGE_CONTROL %>" />
<c:set var="param_enableProcessing" value="<%=VisualizerConstants.PARAM_ENABLE_PROCESSING %>" />
<c:set var="param_enableStatistics" value="<%=VisualizerConstants.PARAM_ENABLE_STATISTICS %>" />
<c:set var="param_enableSearch" value="<%=VisualizerConstants.PARAM_ENABLE_SEARCH %>" />
<c:set var="param_enableEvaluation" value="<%=VisualizerConstants.PARAM_ENABLE_EVALUATION %>" />
<c:set var="param_enableSyntagClouds" value="<%=VisualizerConstants.PARAM_ENABLE_SYNTAG_CLOUDS %>" />
<c:set var="param_enableThemeClouds" value="<%=VisualizerConstants.PARAM_ENABLE_THEME_CLOUDS %>" />
<c:set var="param_enableFileOutput" value="<%=VisualizerConstants.PARAM_ENABLE_FILE_OUTPUT %>" />
<c:set var="role_admin" value="<%=VisualizerConstants.ROLE_ADMIN %>" />

<div style="position: relative;height: 100%;">
	
	<%-- general menu option each user can access --%>
	<c:if test="${applicationScope.menuConfiguration[param_enableSearch] eq true}">
			<br/>
			<a class="menuLink" href="${forward_search}"><spring:message code="menu.search"/></a>
	</c:if>

	<c:if test="${applicationScope.menuConfiguration[param_enableSyntagClouds] eq true}">
			<br/>
			<a class="menuLink" href="${forward_syntagClouds}"><spring:message code="menu.syntagClouds"/></a>
	</c:if>
	<%-- /general menu option each user can access --%>

	<br/>        

	<%-- login and logout options --%>
	<c:if test="${sessionScope.user eq null}">
		<br/>
		<a class="menuLink" href="${forward_login}"><spring:message code="menu.login"/></a>   
	</c:if>

	<c:if test="${sessionScope.user ne null}">
		<br/>
		<a class="menuLink" href="${forward_logout}"><spring:message code="menu.logout"/></a>   
	</c:if>
	<%-- /login and logout options --%>

	<br/>  

	<%-- options for logged in users --%>
	<c:if test="${sessionScope.user ne null && applicationScope.menuConfiguration[param_enableEvaluation] eq true}">
			<br/>
			<a class="menuLink" href="${forward_evaluation}"><spring:message code="menu.evaluation"/></a>
	</c:if>

	<c:if test="${sessionScope.user ne null && applicationScope.menuConfiguration[param_enableThemeClouds] eq true}">
			<br/>
			<a class="menuLink" href="${forward_themeClouds}"><spring:message code="menu.themeClouds"/></a>
	</c:if>

	<%-- options only for admins --%>
	<c:if test="${sessionScope.user ne null && sessionScope.user.role eq role_admin}">

		<br/>
		<a class="menuLink" href="${forward_userManagement}"><spring:message code="menu.userManagement"/></a>

		<c:if test="${applicationScope.menuConfiguration[param_enableFileOutput] eq true}">

			<br/>
			<a class="menuLink" href="${forward_fileOutput}"><spring:message code="menu.fileOutput"/></a>
		</c:if>

		<c:if test="${applicationScope.menuConfiguration[param_enableProcessing] eq true}">

			<br/>
			<a class="menuLink" href="${forward_processing}"><spring:message code="menu.processing"/></a>
		</c:if>

		<c:if test="${applicationScope.menuConfiguration[param_enableStatistics] eq true}">

			<br/>
			<a class="menuLink" href="${forward_statistics}"><spring:message code="menu.statistics"/></a>
		</c:if>
	</c:if>
	<%-- /options only for admins --%>
	
	<%-- /options for logged in users --%>
	<div style="position: absolute;bottom: 10px;">
		<a class="menuLink" href="${forward_help}"><spring:message code="menu.help"/></a>
	</div>
</div>