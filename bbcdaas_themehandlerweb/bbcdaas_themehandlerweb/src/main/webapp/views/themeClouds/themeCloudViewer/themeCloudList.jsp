<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div style="border: black solid 1px;width: 620px;margin-left: 5px;padding: 5px;margin-bottom: 5px;">
	<b>
		<spring:message code="themeClouds.themeCloud"/>:
	</b>
	<c:out value="${themeCloud.themeCloudName}"/>
	, <spring:message code="themeClouds.themeCloudCreatorName"/><c:out value=" ${themeCloud.user.name}"/><br/>

	<table border="0" style="width: 100%;">
		<tr>
			<td>
				<div style="background-color: white;margin-bottom: 5px;padding: 1px;min-height: 50px;width: 500px;">
					<c:set var="i" value="1"/>
					<c:forEach var="term" items="${themeCloud.terms}">
						<c:if test="${term.rating eq 0}">
							<c:if test="${i ne 1}">, </c:if>
							<c:out value="${term.value}  (${term.weighting})"/>
							<c:set var="i" value="${i + 1}"/>
						</c:if>
					</c:forEach>
				</div>
				<div style="background-color: gray;margin-bottom: 5px;padding: 1px;min-height: 50px;width: 500px;">
					<c:set var="i" value="1"/>
					<c:forEach var="term" items="${themeCloud.terms}">
						<c:if test="${term.rating eq 1}">
							<c:if test="${i ne 1}">, </c:if>
							<c:out value="${term.value}"/>
							<c:set var="i" value="${i + 1}"/>
						</c:if>
					</c:forEach>
				</div>
				<div style="background-color: red;padding: 1px;margin-bottom: 15px;min-height: 50px;width: 500px;">
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
					   onmouseup="$('task').value='editThemeCloud';$('selectedThemeCloudName').value='${themeCloud.themeCloudName}';"/><br/>
				<input type="submit" value='<spring:message code="themeClouds.deleteThemeCloud"/>' name="deleteThemeCloud"
					   onmouseup="if (confirm('<spring:message code="themeClouds.deleteThemeCloudQuestion"/>')) {$('task').value='deleteThemeCloud';$('selectedThemeCloudName').value='${themeCloud.themeCloudName}';}"/>
			</td>
		</tr>
	</table>
</div>