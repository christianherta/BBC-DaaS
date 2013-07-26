<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>

<div id="ajax_termDictionary">
	<%@include file="subforms/sub_termDictionary.jsp" %>
</div>

<br/><br/>

<div id="ajax_trainingData">
	<%@include file="subforms/sub_trainingData.jsp" %>
</div>
	