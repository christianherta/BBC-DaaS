<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>

<c:set var="sessionContainer" value="<%=VisualizerConstants.KEY_SESSION_CONTAINER %>"/>
<c:set var="user" value="<%=VisualizerConstants.KEY_USER %>"/>
<c:set var="role_admin" value="<%=VisualizerConstants.ROLE_ADMIN %>"/>

<div id="themeCloudViewer" style="background: lightgrey;height: 670px; width: 60%;overflow: auto;padding-top: 10px;"
	 <c:if test="${themeCloudViewer_active eq 0}">class='hidden'</c:if>>

	<input type="hidden" name="selectedThemeCloudID" id="selectedThemeCloudID"/>
	<c:set var="themeCloudCount" value="0"/>
	<c:forEach var="themeCloud" items="${sessionScope[sessionContainer].themeCloudSessionBean.themeClouds}">
		<c:set var="themeCloudCount" value="${themeCloudCount + 1}"/>
	</c:forEach>
	<c:if test="${themeCloudCount eq 0}" >
		<span style="color:red">
			<spring:message code="themeClouds.noThemeClouds"/>
		</span>
	</c:if>
	<c:forEach var="themeCloud" items="${sessionScope[sessionContainer].themeCloudSessionBean.themeClouds}">
		<c:if test="${not empty sessionScope[user] && (sessionScope[user].role eq role_admin || sessionScope[user].ID eq themeCloud.user.ID)}">
			<div style="border: black solid 1px;width: 620px;margin-left: 5px;padding: 5px;margin-bottom: 5px;">
				<b>
					<spring:message code="themeClouds.themeCloud"/>:
				</b>
				<c:out value="${themeCloud.themeCloudName}"/>
				, <spring:message code="themeClouds.themeCloudCreatorName"/><c:out value=" ${themeCloud.user.name}"/><br/>

				<table border="0" style="width: 100%;">
					<tr>
						<td>
							<div style="background-color: white;margin-bottom: 5px;padding: 1;min-height: 50px;width: 500px;">
								<c:set var="i" value="1"/>
								<c:forEach var="term" items="${themeCloud.terms}">
									<c:if test="${term.rating eq 0}">
										<c:if test="${i ne 1}">, </c:if>
										<c:out value="${term.value}  (${term.weighting})"/>
										<c:set var="i" value="${i + 1}"/>
									</c:if>
								</c:forEach>
							</div>
							<div style="background-color: gray;margin-bottom: 5px;padding: 1;min-height: 50px;width: 500px;">
								<c:set var="i" value="1"/>
								<c:forEach var="term" items="${themeCloud.terms}">
									<c:if test="${term.rating eq 1}">
										<c:if test="${i ne 1}">, </c:if>
										<c:out value="${term.value}"/>
										<c:set var="i" value="${i + 1}"/>
									</c:if>
								</c:forEach>
							</div>
							<div style="background-color: red;padding: 1;margin-bottom: 15px;min-height: 50px;width: 500px;">
								<c:set var="i" value="1"/>
								<c:forEach var="term" items="${themeCloud.terms}">
									<c:if test="${term.rating eq 2}">
										<c:if test="${i ne 1}">, </c:if>
										<c:out value="${term.value}"/>
										<c:set var="i" value="${i + 1}"/>
									</c:if>
								</c:forEach>
							</div>
						</td>
						<td style="vertical-align: middle;text-align: center;">
							<input type="submit" value='<spring:message code="themeClouds.editThemeCloud"/>' name="editThemeCloud" 
								   style="margin-bottom: 5px;"
								   onmouseup="$('task').value='editThemeCloud';$('selectedThemeCloudID').value='${themeCloud.id}'"/><br/>
							<input type="submit" value='<spring:message code="themeClouds.deleteThemeCloud"/>' name="deleteThemeCloud"
								   onmouseup="if (confirm('<spring:message code="themeClouds.deleteThemeCloudQuestion"/>')) {$('task').value='deleteThemeCloud';$('selectedThemeCloudID').value='${themeCloud.id}';}"/>
						</td>
					</tr>
				</table>
			</div>
		</c:if>
	</c:forEach>
</div>