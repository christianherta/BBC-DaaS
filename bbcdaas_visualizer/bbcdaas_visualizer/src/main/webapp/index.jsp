<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>
<c:set var="forward_startpage" value="<%=VisualizerConstants.FORWARD_SHOW_STARTPAGE_CONTROL %>" />

<c:redirect url="/${forward_startpage}" />