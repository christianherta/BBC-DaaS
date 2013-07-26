<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>

<c:set var="forward_startpage" value="<%=VisualizerConstants.FORWARD_SHOW_STARTPAGE_CONTROL %>" />
<c:set var="searchType_topRelatedTerms" value="<%=VisualizerConstants.SEARCH_TYPE_TOP_RELATED_TERMS %>" />

<b>
	<spring:message code="search.title"/>
</b>

<div class="searchInput">
	<spring:message code="search.searchForTopRelatedTerms"/>:
	<input type="text" name="searchString" id="searchString" onkeyup="if (keyPressed(event,13)) {performSearch('${searchType_topRelatedTerms}', this.value)} else {$('ajax_searchResult').addClass('hidden');searchSuggestions(event, this.value)}"/>
	<input type="button" 
		   name="performSearch" 
		   value="<spring:message code="search.performSearch"/>"
		   onmouseup="performSearch('${searchType_topRelatedTerms}', $('searchString').value)"/>
</div>
<div class="searchOutput">
	<div id="ajax_searchResult" class="searchResult"></div>
	<div id="ajax_searchSuggestionResult" class="searchSuggestionResult hidden"></div>
</div>
