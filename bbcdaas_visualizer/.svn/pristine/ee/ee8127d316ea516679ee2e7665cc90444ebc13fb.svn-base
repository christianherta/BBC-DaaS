<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>

<c:set var="forward_startpage" value="<%=VisualizerConstants.FORWARD_SHOW_STARTPAGE_CONTROL %>" />

<spring:message code="processing.title"/>
<br/><br/>
<input type="button" 
	   name="startProcessingButton" 
	   value="<spring:message code="processing.startProcessing"/>"
	   onmouseup="startProcessing();this.disabled = 1;"/>
<span id="ajax_processState"></span>
<br/><br/>
<a href="${forward_startpage}"><spring:message code="project.backToStartpage"/></a>
