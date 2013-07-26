<%@page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.themehandlerweb.constants.Constants" %>

<c:set var="handleUserManagementForm" value="<%=Constants.FORWARD_HANDLE_USER_MANAGEMENT_FORM_CONTROL %>" />

<form method="post" id="userManagementForm" action="${handleUserManagementForm}">

	<div>
		
		<input type="hidden" id="task" name="task"/> 
		
		<table border="0">
			<tr>
				<td>
					<%@include file="../../common/subforms/sub_userList.jsp" %>
				</td>
				<td style="padding-left: 10px;padding-top: 20px;">
					<input type="button" name="deleteUser" value='<spring:message code="userManagement.deleteSelectedUser"/>'
						   onmouseup="if (confirm('<spring:message code="userManagement.confirmDeleteUser"/>')) {$('task').value='deleteUser';ajaxFormRequest('${handleUserManagementForm}', 'userManagementForm', 'ajax_userAddNDelete');this.onmouseup = null;}"/>
				</td>
			</tr>
		</table>
		<br/>

		<b>
			<spring:message code="userManagement.addUser"/>:
		</b>

		<br/>
		
		<div style="float:left;">
			
			<table border="0">
				<tr>
					<td>
						<spring:message code="project.user" />:
					</td>
					<td>
						<input name="username" size="15" id="username"
							   onkeydown="if (keyPressed(event,13)) {return false;}"/>
					</td>
				</tr>
				<tr>
					<td>
						<spring:message code="project.password" />:
					</td>
					<td>
						<input type="password" size="15" id="password" name="password"
							   onkeydown="if (keyPressed(event,13)) {return false;}"/>
					</td>
				</tr>
				<tr>
					<td>
						<spring:message code="project.passwortRepeat" />:
					</td>
					<td>
						<input type="password" size="15" id="passwordRepeat" name="passwordRepeat"
							   onkeydown="if (keyPressed(event,13)) {return false;}"/>
					</td>
				</tr>
			</table><br/>
			<input type="button" value='<spring:message code="userManagement.addUser"/>'
				   onmouseup="$('task').value='addUser';ajaxFormRequest('${handleUserManagementForm}', 'userManagementForm', 'ajax_userAddNDelete');"/>
		</div>
		<div style="margin-left: 10px;padding-top: 3px;color: red;">
			<spring:message code="project.admin" />:
			<input type="checkbox" name="isAdmin" value="1" />
		</div>
	</div>
</form>