<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.disambiguationweb.constants.Constants" %>

<c:set var="scoredWikipediaURIsSortedByTermValue" value="<%=Constants.KEY_SCORED_WIKIPEDIA_URIS_SORTED_BY_TERMVALUE %>" />
<c:set var="scoredWikipediaURIsSortedByScore" value="<%=Constants.KEY_SCORED_WIKIPEDIA_URIS_SORTED_BY_SCORE %>" />

<c:if test="${not empty sessionScope[scoredWikipediaURIsSortedByTermValue]}">	
	<div style="background-color: lightgray;">
		<b><spring:message code="wikipedia.output.documentsSortedByTermValue"/></b>
		<br/><br/>
		<c:set var="latestTermValue" value="" />
		<c:set var="i" value="0" />
		<c:forEach var="scoredWikipediaURI" items="${sessionScope[scoredWikipediaURIsSortedByTermValue]}">
			<c:if test="${latestTermValue ne scoredWikipediaURI.termValue}">
				<c:if test="${i ne 0}">
					<hr/>
				</c:if>
				<b><c:out value="${scoredWikipediaURI.termValue}"/>:</b><br/>
				<c:set var="latestTermValue" value="${scoredWikipediaURI.termValue}" />
			</c:if>
			<a href='<c:out value="${scoredWikipediaURI.uri}"/>' target="_blank"><c:out value="${scoredWikipediaURI.uri}" /></a><br/>
			<c:out value="${scoredWikipediaURI.score}"/><br/><br/>
			<c:set var="i" value="${i + 1}" />
		</c:forEach>
	</div>
</c:if>

<c:if test="${not empty sessionScope[scoredWikipediaURIsSortedByScore]}">
	<div style="background-color: lightblue;">
		<b><spring:message code="wikipedia.output.documentsSortedByScore"/></b>
		<br/><br/>
		<c:forEach var="scoredWikipediaURI" items="${sessionScope[scoredWikipediaURIsSortedByScore]}">
			<a href='<c:out value="${scoredWikipediaURI.uri}"/>' target="_blank"><c:out value="${scoredWikipediaURI.uri}" /></a><br/>
			<c:out value="${scoredWikipediaURI.score}"/><br/><br/>
		</c:forEach>
	</div>
</c:if>