<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>
<%@page import="de.bbcdaas.taghandler.constants.ProcessingConstants" %>

<c:set var="sessionContainer" value="<%=VisualizerConstants.KEY_SESSION_CONTAINER %>" />
<c:set var="default_maxTopRelatedTerms" value="<%=ProcessingConstants.DEFAULT_MAX_TOP_RELATED_TERMS %>"/>

<c:if test="${not empty sessionScope[sessionContainer].syntagCloudSessionBean.entities}">
	
	<table border="0">
		<tr>
			<td>
				<b>
					<spring:message code='syntagClouds.a'/>:
				</b>
			</td>
			<td>
				<input type="text" name="factor_a" id="factor_a" value="${sessionScope[sessionContainer].syntagCloudSessionBean.a}"/>
				<spring:message code='syntagClouds.a.default'/>
			</td>
		</tr>
		<tr>
			<td>
				<b>
					<spring:message code='syntagClouds.b'/>:
				</b>
			</td>
			<td>
				<input type="text" name="factor_b" id="factor_b" value="${sessionScope[sessionContainer].syntagCloudSessionBean.b}"/>
				<spring:message code='syntagClouds.b.default'/>
			</td>
		</tr>
		<tr>
			<td>
				<b>
					<spring:message code='syntagClouds.syntagmaticEntityTermFactor'/>:
				</b>
			</td>
			<td>
				<input type="text" name="syntagmaticEntityTermFactor" id="syntagmaticEntityTermFactor" value="${sessionScope[sessionContainer].syntagCloudSessionBean.syntagmaticEntityTermFactor}"/>
				<spring:message code='syntagClouds.syntagmaticEntityTermFactor.default'/>
			</td>
		</tr>
		<tr>
			<td>
				<b>
					<spring:message code='syntagClouds.minSyntag'/>:
				</b>
			</td>
			<td>
				<input type="text" name="minSyntag" id="minSyntag" value="${sessionScope[sessionContainer].syntagCloudSessionBean.minSyntag}"/>
				<spring:message code='syntagClouds.minSyntag.default'/>
			</td>
		</tr>
		<tr>
			<td>
				<b>
					<spring:message code='syntagClouds.maxTopRelatedTerms'/>:
				</b>
			</td>
			<td>
				<c:out value="${default_maxTopRelatedTerms}"/>
			</td>
		</tr>
	</table>
	<input type="button" name="computeSyntagClouds" id="computeSyntagClouds" 
		   value="<spring:message code='syntagClouds.computeSyntagClouds'/>"
		   onmouseup="computeSyntagClouds();this.onmouseup = null;"/>

	<c:if test="${sessionScope[sessionContainer].syntagCloudSessionBean.syntagCloudsCalculatedForCurrentRandomEntity eq true}" >
	
		<br/><br/>

		<table border="0">
			<col style="min-width: 150px;"/>
			<col style="max-width: 60%;"/>
			<tr>
				<c:set var="i" value="1"/>
				<c:forEach var="field" items="${sessionScope[sessionContainer].syntagCloudSessionBean.selectedEntity.fields}">
					<td>
						<b>
							<spring:message code="syntagClouds.syntagTermToFieldTerms" /><c:out value="${i}"/>:
						</b>
					</td>
					<td>
						<c:set var="j" value="0"/>
						<c:forEach var="syntagTerm" items="${field.syntagmaticTerms}">
							<c:set var="j" value="${j + 1}"/>
						</c:forEach>
						<c:if test="${j > 0}">
							<c:set var="j" value="0"/>
							<c:forEach var="syntagTerm" items="${field.syntagmaticTerms}">
								<c:if test="${j ne 0}">,</c:if>
								<c:out value="${syntagTerm.value} "/>
								<c:set var="j" value="1"/>
							</c:forEach>
						</c:if>
						<c:if test="${j eq 0}">
							<span style="color: red;"><spring:message code="syntagClouds.noSyntagTermsFound" /></span>
						</c:if>	
					</td>
					<c:set var="i" value="${i + 1}"/>
				</c:forEach>
			</tr>
		</table>
	</c:if>
</c:if>