<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.disambiguationweb.constants.Constants" %>

<c:set var="suggestionList" value="<%=Constants.KEY_SUGGESTION_LIST %>" />
<c:set var="selectionTarget" value="<%=Constants.KEY_SELECTION_TARGET %>" />

<c:if test="${not empty sessionScope[suggestionList]}">  
    <c:forEach var="term" items="${sessionScope[suggestionList]}">
        <div onmouseup="$('${sessionScope[selectionTarget]}').value=this.innerHTML;$('ajax_searchSuggestionResult').addClass('hidden');"
             onmouseover="$('ajax_searchSuggestionResult').removeClass('hidden');"
            class="suggest_link"><c:out value="${term.value}" /></div>
    </c:forEach>
</c:if> 