<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.disambiguationweb.constants.Constants" %>

<c:set var="categoryContext" value="<%=Constants.KEY_WIKIPEDIA_CATEGORY_CONTEXT %>" />

<input type="text"
	name="categoryName"
	id="categoryName"
	size="30"
 />

<input type="button" id="performGetCategoryContext"
 value="Get Category Context"
 <c:if test="${not empty sessionScope[categoryContext]}">
 onmouseup="wikipediaGetCategoryContext('<spring:message code='wikipedia.searchInProgress'/>...',
										  '<spring:message code='wikipedia.noResult'/>');"
 </c:if>
 <c:if test="${empty sessionScope[categoryContext]}">
	 disabled="disabled"
 </c:if>/>

<div class="searchResult hidden" id="ajax_categoryContextResult" style="max-width: 550px;"></div>