<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>

<c:set var="sessionContainer" value="<%=VisualizerConstants.KEY_SESSION_CONTAINER %>" />

<b>
	<spring:message code="evaluation.title"/>
</b>
<br/><br/>

<div class="evaluation_main">

	<%-- random groups --%>
	<div id="ajax_randomTermGroups" class="ajax_randomTermGroups">
		<%@include file="subforms/sub_randomTermGroups.jsp" %>
	</div>
	<%-- /random groups --%>

	<%-- random terms of selected randomTermsGroup --%> 
	<div id="ajax_randomTerms">
		<%@include file="subforms/sub_randomTerms.jsp" %>
	</div>
	<%-- /random terms of selected randomTermsGroup --%>

	<br/>
	
	<%-- top related Terms List plus added Terms and rating --%>
	<div id="ajax_ratedTerms"></div>
	<%-- /top related Terms List plus added Terms and rating --%>
	
</div>