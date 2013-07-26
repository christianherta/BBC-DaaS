<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>

<c:set var="sessionContainer" value="<%=VisualizerConstants.KEY_SESSION_CONTAINER %>" />

<table border="0">
	<tr>
		<td style="padding-right: 10px;">
			<input type="button" name="getRandomEntity" id="getRandomEntity" 
				   value="<spring:message code='syntagClouds.getRandomEntity'/>"
				   onmouseup="getRandomEntity();this.onmouseup = null;"/>
		</td>
		<td style="padding-left: 10px;border-left: 1px solid black;">
			<input type="text" name="term1" 
				   id="term1" value="${sessionScope[sessionContainer].syntagCloudSessionBean.term1Value}"
				   <c:if test="${sessionScope[sessionContainer].syntagCloudSessionBean.term1Valid eq false}">style="color:red;"</c:if>
				   onkeydown="this.style.color = 'black';" /><br/>
			<input type="text" name="term2" 
				   id="term2" value="${sessionScope[sessionContainer].syntagCloudSessionBean.term2Value}"
				   <c:if test="${sessionScope[sessionContainer].syntagCloudSessionBean.term2Valid eq false}">style="color:red;"</c:if>
				   onkeydown="this.style.color = 'black';"/><br/>
			<input type="text" name="term3" 
				   id="term3" value="${sessionScope[sessionContainer].syntagCloudSessionBean.term3Value}"
				   <c:if test="${sessionScope[sessionContainer].syntagCloudSessionBean.term3Valid eq false}">style="color:red;"</c:if>
				   onkeydown="this.style.color = 'black';"/>
		</td>
		<td>
			<input type="button" name="searchForEntity" id="searchForEntity" 
				   value="<spring:message code='syntagClouds.searchForEntities'/>"
				   onmouseup="searchForEntity();this.onmouseup = null;"/>
		</td>
		<td style="vertical-align: top;padding-left: 10px;">
			<spring:message code="syntagClouds.numberOfFoundEntities" />:<c:out value=" ${sessionScope[sessionContainer].syntagCloudSessionBean.numberOfFoundEntities}"/><br/>
			<spring:message code="syntagClouds.selectedEntity" />:<c:out value=" ${sessionScope[sessionContainer].syntagCloudSessionBean.entityIndex + 1}"/>
		</td>
		<td>
			<input type="button" value="<" onmouseup="selectPrevEntity();this.onmouseup = null;"/><input type="button" value=">" onmouseup="selectNextEntity();this.onmouseup = null;"/>
		</td>
	</tr>
</table>
	
<br/>
<b>
	<spring:message code='syntagClouds.entity'/>:
</b>
<br/><br/>

<c:if test="${not empty sessionScope[sessionContainer].syntagCloudSessionBean.entities}">
	
	<table border="0">
		<tr>
			<td>
				<b>
					<spring:message code='syntagClouds.entityName'/>:
				</b>
			</td>
			<td>
				<c:out value="${sessionScope[sessionContainer].syntagCloudSessionBean.selectedEntity.name}" />
			</td>
		</tr>
		<tr>
			<c:set var="i" value="1"/>
			<c:forEach var="field" items="${sessionScope[sessionContainer].syntagCloudSessionBean.selectedEntity.fields}">
				<td>
					<b>
						<spring:message code='syntagClouds.entityField'/><c:out value="${i}"/>:
					</b>
				</td>
				<td>
					<c:set var="j" value="0"/>
					<c:forEach var="term" items="${field.terms}">
						<c:if test="${j ne 0}">,</c:if>
						<c:out value="${term.value}"/>
						<c:set var="j" value="1"/>
					</c:forEach>
				</td>
				<c:set var="i" value="${i + 1}"/>
			</c:forEach>
		</tr>
	</table>
</c:if>
	
<br/>
<hr/>
<br/>
	
<div id="ajax_entitySyntagClouds">
	<%@include file="sub_entitySyntagClouds.jsp" %>
</div>