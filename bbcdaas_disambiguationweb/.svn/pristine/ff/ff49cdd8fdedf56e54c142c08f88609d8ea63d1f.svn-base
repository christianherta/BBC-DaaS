<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.disambiguationweb.constants.Constants" %>
<%@page import="de.bbcdaas.common.beans.document.Document" %>

<c:set var="wikipediaURIs" value="<%=Constants.KEY_WIKIPEDIA_URIS %>" />
<c:set var="documentType_article" value="<%=Document.DOCUMENT_TYPE_ARTICLE %>" />
<c:set var="documentType_disambiguation" value="<%=Document.DOCUMENT_TYPE_DISAMBIGUATION %>" />
<c:set var="documentType_redirect" value="<%=Document.DOCUMENT_TYPE_REDIRECT %>" />

<c:if test="${not empty sessionScope[wikipediaURIs]}">
	
	<br/>
	<c:forEach var="wikipediaURI" items="${sessionScope[wikipediaURIs]}">
		<a href="<c:out value="${wikipediaURI.uri}" />" target="_blank"><c:out value="${wikipediaURI.uri}" /></a>
		<br/>		
		<b>
			<spring:message code="wikipedia.documentType" />:
		</b>
		<c:if test="${wikipediaURI.documentType eq documentType_article}">
			<spring:message code="wikipedia.documentType.article" />
		</c:if>
		<c:if test="${wikipediaURI.documentType eq documentType_disambiguation}">
			<spring:message code="wikipedia.documentType.disambiguation" />
		</c:if>
		<c:if test="${wikipediaURI.documentType eq documentType_redirect}">
			<spring:message code="wikipedia.documentType.redirect" />
		</c:if>
		<br/>
		<c:if test="${wikipediaURI.keywords.size() ne 0}">
			<b>
				<spring:message code="wikipedia.keywords" />:
			</b>
			&nbsp;
			<c:set var="i" value="0"/>
			<c:forEach var="keyword" items="${wikipediaURI.keywords}">
				<c:if test="${i ne 0}">
					<c:out value=", "/>
				</c:if>
				<c:out value="${keyword}"/>
				<c:set var="i" value="1"/>
			</c:forEach>
			<br/>
		</c:if>
			<br/>
	</c:forEach>

</c:if>