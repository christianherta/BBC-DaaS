<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.themehandlerweb.constants.Constants" %>
<c:set var="forward_startpage" value="<%=Constants.FORWARD_STARTPAGE %>" />

<c:redirect url="/${forward_startpage}" />