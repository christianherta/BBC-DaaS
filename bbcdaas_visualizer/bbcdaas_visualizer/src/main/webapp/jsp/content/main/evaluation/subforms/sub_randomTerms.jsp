<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>

<c:set var="sessionContainer" value="<%=VisualizerConstants.KEY_SESSION_CONTAINER %>" />
<c:set var="user" value="<%=VisualizerConstants.KEY_USER %>" />
<c:set var="role_tester" value="<%=VisualizerConstants.ROLE_TESTER %>" />
<c:set var="role_admin" value="<%=VisualizerConstants.ROLE_ADMIN %>" />

<div>
	<b>
		<spring:message code="evaluation.randomTerms"/>:
	</b>
	<br/>
	<table border="0">
		<tr>
			<td>
				<select name="randomTerms" id="randomTerms" size="5">
					<c:forEach var="randomTermsGroup" items="${sessionScope[sessionContainer].evaluationSessionBean.randomTermGroups}">
						<c:if test="${randomTermsGroup.groupID eq sessionScope[sessionContainer].evaluationSessionBean.selectedRandomTermsGroupID}">
							<c:forEach var="randomTerm" items="${randomTermsGroup.termToTermsGroups}">
								<option value="${randomTerm.term.id}" 
										onmouseup='
									<c:if test="${sessionScope[user].role eq role_tester}">randomTermSelected_tester(${randomTerm.term.id});</c:if>
									<c:if test="${sessionScope[user].role eq role_admin}">randomTermSelected_admin();</c:if>'
									<c:if test="${randomTerm.saved eq true}">style="color: green"</c:if>>
										<c:out value="${randomTerm.term.value}" />
								</option>
							</c:forEach>
						</c:if>
					</c:forEach>
				</select>
			</td>
		<%-- remove random term button --%>
		<c:if test="${sessionScope[user].role eq role_admin}">
			<td>
				<input type="button" name="removeRandomTerm" id="removeRandomTerm" disabled="disabled"
					   value='<spring:message code="evaluation.removeRandomTerm"/>' 
					   onmouseup="if (confirm('<spring:message code="evaluation.confirmRemoveRandomTerm"/>')) {removeRandomTerm();this.onmouseup= null;}"/>
			</td>
		</c:if>
		</tr>
	</table>
</div>
	
<c:if test="${sessionScope[user].role eq role_admin}">
	<br/>
	<div>
		<%-- remove randomGroup button --%>
		<input type="button" id="removeRandomGroup" 
			   name="removeRandomGroup" value='<spring:message code="evaluation.removeRandomGroup"/>' 
			   onmouseup="if (confirm('<spring:message code="evaluation.confirmDeleteRandomGroup"/>')) {removeRandomGroup();this.onmouseup= null;}"/>
		<br/><br/>
		<%-- add randomGroup --%>
		<b>
			<spring:message code="evaluation.newRandomGroup"/>:
		</b>
		<table border="0">
			<tr>
				<td>
					<spring:message code="evaluation.newRandomGroup.groupLabel"/>:
				</td>
				<td>
					<input type="text" name="groupLabel" id="groupLabel"/>
					<spring:message code="evaluation.newRandomGroup.groupLabel.hint"/>
				</td>
			</tr>
			<tr>
				<td>
					<spring:message code="evaluation.newRandomGroup.numberOfRandomterms"/>:
				</td>
				<td>
					<input type="text" name="numberOfRandomTerms" id="numberOfRandomTerms"/>
					<spring:message code="evaluation.newRandomGroup.numberOfRandomterms.default"/>
				</td>
			</tr>
			<tr>
				<td>
					<spring:message code="evaluation.newRandomGroup.minTopTermSyntag"/>:
				</td>
				<td>
					<input type="text" name="minTopTermSyntag" id="minTopTermSyntag"/>
					<spring:message code="evaluation.newRandomGroup.minTopTermSyntag.default"/>
				</td>
			</tr>
		</table>
		<input type="button" name="newRandomGroup" value='<spring:message code="evaluation.newRandomGroup.button"/>' onmouseup="newRandomGroup();this.onmouseup= null;"/>	
	</div>	
</c:if>