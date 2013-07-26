<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<b><spring:message code="wikipedia.disambiguation" /></b>
	
	<br/><br/>
	
	<c:set var="inputTermIndex" value="0"/>
	<c:forEach var="disambiguationTermValue" items="${sessionScope.disambiguationTermValues}">
	
		<c:if test="${inputTermIndex != 0}">
			<br/>
		</c:if>
		<spring:message code="wikipedia.term"/> <c:out value="${inputTermIndex+1}" />:
		<input type="text" 
		   name="searchString_disambiguation[${inputTermIndex}]" 
		   id="searchString_disambiguation_${inputTermIndex}" 
		   value="${sessionScope.disambiguationTermValues[inputTermIndex]}"
		   size="30"
		   onkeyup="searchSuggestions(event, this.value, 'searchString_disambiguation_${inputTermIndex}', 210, 165, ${inputTermIndex+1})"
		/>
		<c:if test="${inputTermIndex != 0 && inputTermIndex != 1}">
			<a class="removeButton" onmouseup="removeDisambiguationTerm(${inputTermIndex});">
				<img src="images/spacer.gif" alt="" />
			</a>
		</c:if>
		<c:set var="inputTermIndex" value="${inputTermIndex + 1}"/>
	</c:forEach>
	
	<a class="addButton" onmouseup="addDisambiguationTerm();">
		<img src="images/spacer.gif" alt="" />
	</a>
		
	<br/><br/>
	
	