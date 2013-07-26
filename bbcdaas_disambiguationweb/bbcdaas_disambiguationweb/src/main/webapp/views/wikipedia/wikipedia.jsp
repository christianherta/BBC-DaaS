<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.disambiguationweb.constants.Constants" %>

<c:set var="pattern" value="<%=Constants.KEY_WIKIPEDIA_SCORING_PATTERN %>"/>
<c:set var="params" value="<%=Constants.KEY_WIKIPEDIA_SCORING_PARAMS %>"/>
<c:set var="forward_wikipediaHelp" value="<%=Constants.FORWARD_WIKIPEDIA_HELP_CONTROLLER %>" />

<div>
	<h3><spring:message code="wikipedia.caption" /></h3>
</div>
<div>
	<a href="${forward_wikipediaHelp}"><spring:message code="wikipedia.help" /></a>
</div>

<table border="0">
	<colgroup>
		<col width="35%"/>
		<col width="30%"/>
		<col width="35%"/>
	</colgroup>
	<tr>
		<%-- disambiguation input fields --%>
		<td style="vertical-align:top;">
			<div class="searchInput" id="disambiguationInputs">
				<%@include file="../common/disambiguationInputs.jsp" %>
			</div>
			<input type="button"
				   id="performDisambiguation"
					value="<spring:message code="wikipedia.disambiguate" />" 
					<c:if test="${not empty sessionScope[params]}">
					onmouseup="wikipediaLuceneDisambiguation('<spring:message code='wikipedia.searchInProgress'/>...',
															 '<spring:message code='wikipedia.noResult'/>');"
					</c:if>
					<c:if test="${empty sessionScope[params]}">
						disabled="disabled"
					</c:if>/>
		</td>
		<%-- /disambiguation input fields --%>
		<%-- configuration fields --%>
        <%@include file="subforms/wikipediaConfiguration.jsp" %>
		<%-- /configuration fields --%>
		<%-- wikipedia url search fields --%>
		<td style="vertical-align:top;border-left: 1px solid black;">
			<div class="searchInput" style="margin-left: 20px;">
				<%@include file="subforms/wikipediaUrlSearch.jsp" %>
			</div>
		</td>
		<%-- /wikipedia url search fields --%>
	</tr>
</table>
    
<div class="searchSuggestionResult hidden" id="ajax_searchSuggestionResult" onmouseout="$(this).addClass('hidden');"></div>
<div class="searchResult hidden" id="ajax_searchResult" style="max-width: 550px;"></div>