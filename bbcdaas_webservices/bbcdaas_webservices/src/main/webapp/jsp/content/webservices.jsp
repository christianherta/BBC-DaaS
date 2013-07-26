<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page import="de.bbcdaas.webservices.constants.Constants" %>
<%@page import="de.bbcdaas.webservices.api.taghandler.TagHandlerWebserviceURIs" %>
<%@page import="de.bbcdaas.webservices.api.themehandler.ThemeHandlerWebserviceURIs" %>

<c:set var="websiteURL" value="<%=Constants.KEY_WEBSITE_URL %>"/>
<c:set var="restServices_getTerm" value="<%=TagHandlerWebserviceURIs.RESTSERVICE_GET_TERM %>"/>
<c:set var="restServices_searchForTerms" value="<%=TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TERMS %>"/>
<c:set var="restServices_searchForTRTsJSON" value="<%=TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TRTS_JSON %>"/>
<c:set var="restServices_searchForTRTsHTML" value="<%=TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TRTS_HTML %>"/>
<c:set var="restServices_getSyntagTermCloud" value="<%=TagHandlerWebserviceURIs.RESTSERVICE_GET_SYNTAG_TERM_CLOUD %>"/>
<c:set var="restServices_searchForEntity" value="<%=TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_ENTITY %>"/>
<c:set var="restServices_getAllThemeClouds" value="<%=ThemeHandlerWebserviceURIs.RESTSERVICE_GET_ALL_THEMECLOUDS %>"/>
<c:set var="restServices_saveNewThemeCloud" value="<%=ThemeHandlerWebserviceURIs.RESTSERVICE_SAVE_NEW_THEMECLOUD %>"/>
<c:set var="restServices_deleteThemeCloud" value="<%=ThemeHandlerWebserviceURIs.RESTSERVICE_DELETE_THEMECLOUD %>"/>

<h2>
	BBC-DaaS Webservices
</h2>
GET & POST - Requests can be send using browser adress-line. Copy given urls into it and fill out the parameter.<br/>
Example: .../webservices/services/taghandler/searchForTerms?term=java<br/><br>

<div style="background-color: lightgray;">
	<b>
		TagHandler Services:
	</b> 
	<br/><br/>
	(GET) services/<c:out value="${restServices_getTerm}"/>?termValue=[value]<br/>
	(GET) services/<c:out value="${restServices_searchForTerms}"/>?term=[term]<br/>
	<hr/>
	term: <input type="text" id="searchForTermsTerm"/><br/>
	<input type="button" onmouseup="searchForTerms()" 
		   value="Send get-Request using JSONP"/><br/>
	<hr/><br/>
	(GET) services/<c:out value="${restServices_searchForTRTsJSON}"/>?term=[value]<br/>
	(GET) services/<c:out value="${restServices_searchForTRTsHTML}"/>?term=[value]<br/>
	(GET) services/<c:out value="${restServices_getSyntagTermCloud}"/>?themeCloudTerms=[value],...,[value]&amp;minSyntag=[integer]&amp;syntagmaticEntityTermFactor=[float]&amp;a=[float]&amp;b=[float]<br/>
	(GET) services/<c:out value="${restServices_searchForEntity}"/>?terms=[value1],[value2],[value3]<br/><br/>

	<a href="${applicationScope[websiteURL]}/download/taghandlerWebserviceDescription.pdf" target="_blank">taghandlerWebserviceDescription.pdf</a>
</div>
<br/><br/>
<div style="background-color: lightgray;">
	<b>
		ThemeHandler Services:
	</b> 
	<br/><br/>
	(GET) services/<c:out value="${restServices_getAllThemeClouds}"/><br/>
	<br/>
	(PUT) services/<c:out value="${restServices_saveNewThemeCloud}"/>?themeCloudName=[name]&amp;terms=[value],[rating],[weighting];...;[value],[rating],[weighting]&amp;username=[username]<br/>
	<hr/>
	ThemeCloudName: <input type="text" id="themeCloudNamePut"/><br/>
	Terms (Pattern: [value],[rating],[weighting];...;[value],[rating],[weighting]): <input type="text" id="terms"/><br/>
	UserName: <input type="text" id="userName"/><br/>
	<input type="button" onmouseup="putSomething()" value="Send put request"/><br/>
	<hr/><br/>
	(DELETE) services/<c:out value="${restServices_deleteThemeCloud}"/>?themeCloudName=[name]
	<hr/>
	ThemeCloudName: <input type="text" id="themeCloudNameDelete"/><br/>
	<input type="button" onmouseup="deleteSomething()" value="Send delete request"/><br/>
	<hr/>
</div>

<script type="text/javascript">
		
		function putSomething() {
			$.ajax({
				url: 'services/'+'<c:out value="${restServices_saveNewThemeCloud}"/>'+'?themeCloudName='+
					$("#themeCloudNamePut").val()+'&terms='+$("#terms").val()+'&username='+$("#userName").val(),
				type: 'PUT',
				success: function( response ) {
					alert('put successful');
				},
				error: function(xhr, ajaxOptions, thrownError) {
					alert(thrownError);
				}
			});
		}
		
		function deleteSomething() {
			$.ajax({
				url: 'services/'+'<c:out value="${restServices_deleteThemeCloud}"/>'+'?themeCloudName='+
					$("#themeCloudNameDelete").val(),
				type: 'DELETE',
				success: function( response ) {
					alert('delete successful');
				},
				error: function(xhr, ajaxOptions, thrownError) {
					alert(thrownError);
				}
			});
		}
		
		/* --- the following functions needs to stay in its own script area in order
		 * to let jsonp work properly. Do not put them into a js-file. ---
		 */
		var searchForTermsURL = "http://bbcdaas.f4.htw-berlin.de:8080/webservices/services/taghandler/searchForTermsJSONP";
		
		
		function handleSearchForTermsResult(result, term) {
			
			var resultString = "";
			$.each(result, function (i, resultItem) {
				$.each(resultItem, function(j, resultTerm) {
					if (j != 0) {
						resultString = resultString + ",";
					}
					resultString = resultString + resultTerm;
				});
			});
			alert('result for term \''+term+'\': '+resultString);
		}
		
		/* 
		* search for terms by a given term fragment. webservice needs to support 
		* jsonp with callback functionName 'jsonp'
		 */
		function searchForTerms() {
		
			$.ajax({
					type: "GET",
					url: searchForTermsURL + "?term=" + $("#searchForTermsTerm").val() + "&callback=jsonp",
					timeout: 10000,
					contentType: "application/json",
					dataType: "jsonp",
					jsonpCallback: "jsonp",
					cache: false,
					success: function(data) {
						handleSearchForTermsResult(data, $("#searchForTermsTerm").val());
					},
					error: function(request, error) {
						alert('Request on webservice \'searchForTerms\' failed. request = ' + request + ', error = ' + error);
					}
			});
		}
		/*--- /the following functions needs to stay in its own script area in order
		 * to let jsonp work properly. Do not put them into a js-file. ---
		 */
</script>