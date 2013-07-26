<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>

<c:set var="searchType_topRelatedTerms" value="<%=VisualizerConstants.SEARCH_TYPE_TOP_RELATED_TERMS %>" />
<c:set var="searchType_suggestion" value="<%=VisualizerConstants.SEARCH_TYPE_TERM_SUGGESTION %>" />
<c:set var="maxFontSize" value="<%=VisualizerConstants.TAGCLOUD_MAX_FONTSIZE %>" />

<c:if test="${searchType eq searchType_topRelatedTerms && noSearchResult ne true}" >
	<%--<spring:message code="search.searchResult" />: ${model.searchResult}--%>
	<div class="tagCloud">
		<c:set var="lineLength" value="0"/>
		<c:forEach var="tagCloudItem" items="${tagCloud}">
			<a class="
			<c:if test="${tagCloudItem.fontSize le maxFontSize/4}" >
				tag1
			</c:if>
			<c:if test="${tagCloudItem.fontSize le maxFontSize/2 && tagCloudItem.fontSize gt maxFontSize/4}" >
				tag2
			</c:if>
			<c:if test="${tagCloudItem.fontSize le 3*(maxFontSize/4) && tagCloudItem.fontSize gt maxFontSize/2}" >
				tag3
			</c:if>
			<c:if test="${tagCloudItem.fontSize gt 3*(maxFontSize/4)}" >
				tag4
			</c:if>
				" style="font-size: ${tagCloudItem.fontSize}em">${tagCloudItem.value}
			</a>
			<c:set var="lineLength" value="${lineLength + fn:length(tagCloudItem.value)}"/>
			<c:if test="${lineLength gt 20}">
				<br/>
				<c:set var="lineLength" value="0"/>
			</c:if>
		</c:forEach>
	</div>
	<br/><br/>
	<c:set var="i" value="0"/>
	<c:forEach var="tagCloudItem" items="${searchResult}">
		<c:if test="${i ne 0}"><br/></c:if>
		<c:out value="${tagCloudItem.value} (${tagCloudItem.syntaq})"/>
		<c:set var="i" value="${i + 1}"/>
	</c:forEach>
</c:if>
	
<c:if test="${searchType eq searchType_suggestion && model.noSearchResult ne true}" >
	<c:forEach var="term" items="${suggestionList}">
		<div onmouseup="setSearch(this.innerHTML)"
			 class="suggest_link"><c:out value="${term.value}" /></div>
	</c:forEach>
</c:if>
<c:if test="${searchType eq searchType_suggestion && noSearchResult eq true}" >
	<spring:message code="search.noSuggestTermFound" />
</c:if>