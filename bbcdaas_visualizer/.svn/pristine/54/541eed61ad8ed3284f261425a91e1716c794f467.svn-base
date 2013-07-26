<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>

<c:set var="sessionContainer" value="<%=VisualizerConstants.KEY_SESSION_CONTAINER %>" />
<c:set var="user" value="<%=VisualizerConstants.KEY_USER %>" />
<c:set var="role_tester" value="<%=VisualizerConstants.ROLE_TESTER %>" />

<div class="eval_randomGroups">
	<b><spring:message code="evaluation.randomGroups"/>:</b>
	<br/>
	<select id="randomTermGroups" size="5" style="min-width: 100px;">
		<c:forEach var="randomTermGroup" items="${sessionScope[sessionContainer].evaluationSessionBean.randomTermGroups}">
			<option value="${randomTermGroup.groupID}" 
					onmouseup='randomTermGroupSelected(${randomTermGroup.groupID});
				<c:if test="${sessionScope[user].role eq role_tester}">clearRatedTerms();</c:if>'
				<c:if test="${randomTermGroup.groupID eq sessionScope[sessionContainer].evaluationSessionBean.selectedRandomTermsGroupID}">selected="selected"</c:if>>
				<c:if test="${randomTermGroup.groupLabel eq ''}">
					<spring:message code="evaluation.randomGroups.group"/> <c:out value="${randomTermGroup.groupID}"/>
				</c:if>
				<c:if test="${randomTermGroup.groupLabel ne ''}">
					<c:out value="${randomTermGroup.groupLabel}"/> (<spring:message code="evaluation.randomGroups.group"/> <c:out value="${randomTermGroup.groupID}"/>)
				</c:if>
			</option>
		</c:forEach>
	</select>
</div>