<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>

<br/><br/>

<table border="0">
	<tr>
		<td>
			<b>
				<spring:message code="fileOutput.maxNumberOfTerms"/>:
			</b>
		</td>
		<td>
			<input type="text" id="maxNumberOfTerms" />
		</td>
	</tr>
</table>
<input type="button" value='<spring:message code="fileOutput.createTermDictionary" />' 
	   onmouseup="this.onmouseup = null;this.style.disabled = true;createTermDictionary();" />