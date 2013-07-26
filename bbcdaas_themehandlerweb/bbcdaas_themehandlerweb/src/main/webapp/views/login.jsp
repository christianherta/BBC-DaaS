<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<h3>
	<spring:message code="project.login" />
</h3>
 
<c:if test="${not empty error}">
	<div class="errorblock">
		Your login attempt was not successful, try again.<br /> Caused :
		${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
	</div>
</c:if>

<form name='f' action="<c:url value='j_spring_security_check' />" method='post'>

	<table>
		<tr>
			<td><spring:message code="project.user" />:</td>
			<td><input type='text' name='j_username' value=''>
			</td>
		</tr>
		<tr>
			<td><spring:message code="project.password" />:</td>
			<td><input type='password' name='j_password' />
			</td>
		</tr>
		<tr>
			<td colspan='2'><input name="submit" type="submit"
				value="<spring:message code="project.login" />" />
			</td>
		</tr>
	</table>

</form>