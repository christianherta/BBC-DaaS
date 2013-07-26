<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@page import="de.bbcdaas.disambiguationweb.constants.Constants" %>

<c:set var="forward_startpage" value="<%=Constants.FORWARD_STARTPAGE_CONTROLLER %>" />

<span id="dnd_mouseText" class="dnd_mouseText"></span>
<span id="header_title">
    <a href="${forward_startpage}">
		<spring:message code="header.title"/>
	</a>
</span>
<br/>

<span id="header_languageSelection">
	<spring:message code="header.displayLanguage"/>:
	<a href="?lang=en">
		en
	</a>
	|
	<a href="?lang=de">
		de
	</a>
	|
	<a href="?lang=ru">
		ru
	</a>
</span>