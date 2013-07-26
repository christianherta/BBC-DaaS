<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>

<b>
	<spring:message code="syntagClouds.title"/>
</b>

<br/><br/><br/>

<div id="ajax_randomEntity">
	<%@include file="subforms/sub_randomEntity.jsp" %>
</div>

<br/><br/>