<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>
<%@page import="de.bbcdaas.visualizer.constants.FileOutputConstants" %>

<c:set var="forward_startpage" value="<%=VisualizerConstants.FORWARD_SHOW_STARTPAGE_CONTROL %>" />
<c:set var="forward_startStatisticsWriter" value="<%=FileOutputConstants.FORWARD_START_STATISTICS_WRITER_CONTROL %>" />

<spring:message code="statistics.title"/>
<br/><br/>
<input type="button" 
	   name="startTagHandlerButton" 
	   value="<spring:message code="statistics.startStatisticsWriter"/>"
	   onmouseup="startStatisticsWriter();this.disabled = 1;"/>
<span id="ajax_statisticWriterState"></span>
<br/><br/>
<a href="${forward_startpage}"><spring:message code="project.backToStartpage"/></a>
