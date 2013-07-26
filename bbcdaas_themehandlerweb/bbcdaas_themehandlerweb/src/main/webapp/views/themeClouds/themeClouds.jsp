<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.themehandlerweb.constants.Constants" %>

<c:set var="default_maxTopRelatedTerms" value="<%=Constants.DEFAULT_MAX_TOP_RELATED_TERMS %>"/>
<c:set var="handleThemeCloudFormControl" value="<%=Constants.FORWARD_HANDLE_THEMECLOUDS %>"/>
<c:set var="sessionContainer" value="<%=Constants.KEY_SESSION_CONTAINER %>"/>

<b>
	<spring:message code="themeClouds.title"/>
</b>

<br/><br/>

<form:form method="post" name="themeCloudForm" action="${handleThemeCloudFormControl}" >
	
	<div>
		
		<input type="hidden" name="task" id="task"/>
		<c:set var="themeCloudCreator_active" value="${sessionScope[sessionContainer].themeCloudSessionBean.themeCloudCreatorActive}"/>
		<input type="hidden" name="themeCloudCreatorActive" id="themeCloudCreator_active" value="${themeCloudCreator_active}"/>
		<c:set var="themeCloudViewer_active" value="${sessionScope[sessionContainer].themeCloudSessionBean.themeCloudViewerActive}"/>
		<input type="hidden" name="themeCloudViewerActive" id="themeCloudViewer_active" value="${themeCloudViewer_active}"/>
		
		<c:set var="themeCloudTermCount" value="0"/>
		<c:forEach var="themeCloudTerm" items="${sessionScope[sessionContainer].themeCloudSessionBean.themeCloud}">
			<c:set var="themeCloudTermCount" value="${themeCloudTermCount + 1}"/>
		</c:forEach>
		
		<c:set var="syntagCloudTermCount" value="0"/>
		<c:forEach var="syntagCloudTerm" items="${sessionScope[sessionContainer].themeCloudSessionBean.syntagTerms}">
			<c:set var="syntagCloudTermCount" value="${syntagCloudTermCount + 1}"/>
		</c:forEach>
		
		<div class="formmenu" id="formmenu">
			<span class='formlink <c:if test="${themeCloudCreator_active eq 1}">active</c:if>'
				  onclick="if ($('themeCloudCreator_active').value=='0') {
					      $('themeCloudCreator').removeClass('hidden');
						  $('themeCloudCreator_active').value='1';
						  $('themeCloudViewer').addClass('hidden');
						  $('themeCloudViewer_active').value='0';
					  }">
				<spring:message code="themeClouds.createNewThemeCloud"/>
			</span>
			<span class='formlink <c:if test="${themeCloudViewer_active eq 1}">active</c:if>'
				  onclick="if ($('themeCloudViewer_active').value=='0') {
					      $('themeCloudViewer').removeClass('hidden');
						  $('themeCloudViewer_active').value='1';
						  $('themeCloudCreator').addClass('hidden');
						  $('themeCloudCreator_active').value='0';
					  }">
				<spring:message code="themeClouds.showThemeClouds"/>
			</span>
		</div>	
		<%-- theme cloud creator --%>
		<%@include file="themeCloudCreator/themeCloudCreator.jsp" %>
		<%-- theme cloud viewer --%>		
		<%@include file="themeCloudViewer/themeCloudViewer.jsp" %>
	</div>

</form:form>