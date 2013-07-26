<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.disambiguationweb.constants.Constants " %>

<c:set var="categoryContext" value="<%=Constants.KEY_WIKIPEDIA_CATEGORY_CONTEXT %>"/>

Category: <c:out value="${sessionScope[categoryContext].categoryName}" />
<br/>
Parents:<br/>
<c:forEach var="parentName" items="${sessionScope[categoryContex].categoryParentNames}">
	<c:out value="${parentName}," />
</c:forEach>
<br/>
Childs:<br/>
<c:forEach var="childName" items="${sessionScope[categoryContext].categoryDirectChildNames}">
	<c:out value="${childName}," />
</c:forEach>