<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<b><spring:message code="wikipedia.searchForURIs" /></b>

<br/><br/>

<spring:message code="wikipedia.term"/>*:

<input type="text"
        name="searchString"
        id="searchString"
        onkeyup="searchSuggestions(event, this.value, 'searchString', 211, 1050, 0);
			if (keyPressed(event,13)) {searchForWikipediaURIs($('searchString').value,
					'<spring:message code='wikipedia.searchInProgress'/>...',
					'<spring:message code='wikipedia.noResult'/>');}"
/>

<input type="button"
        name="performSearch"
                id="performSearch"
        value="<spring:message code="wikipedia.searchForURIs"/>"
        onmouseup="searchForWikipediaURIs($('searchString').value,
					'<spring:message code='wikipedia.searchInProgress'/>...',
					'<spring:message code='wikipedia.noResult'/>');"/>

&nbsp;
*<spring:message code="wikipedia.wildcardHint"/>

<br/><br/>
<div class="searchResult hidden" id="ajax_searchResultURIs" style="max-width: 580px;"></div>
