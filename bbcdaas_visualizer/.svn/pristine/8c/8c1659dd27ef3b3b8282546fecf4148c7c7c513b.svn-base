<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>

<c:set var="websiteURL" value="<%=VisualizerConstants.KEY_WEBSITE_URL %>"/>

<div style="background-color: turquoise;height: 300px; overflow: auto;overflow-y: auto;overflow-x: hidden;">
	<b>
		<spring:message code="themeClouds.helptext.caption"/>:
	</b>
	<br/><br/>
	<spring:message code="themeClouds.helptext.description"/>
	<br/><br/>
	<b>
		<spring:message code="themeClouds.helptext.formula.caption" />:
	</b>
	<br/><br/>
	<spring:message code="themeClouds.helptext.formula.description"/>
	<br/><br/>
	<b>
		<spring:message code="themeClouds.helptext.formula.download"/>:
	</b>
	<a href="${applicationScope[websiteURL]}/download/syntagCloudCalcDescription.pdf" target="_blank">syntagCloudCalcDescription.pdf</a>
	<br/><br/>
	<b>
		<spring:message code="themeClouds.helptext.formula.short.caption"/>:
	</b>
	<br/><br/>
	<div>
		<b>1.</b> Zu den topRelatedTermen zu den Termen der ThemeCloud werden scores berechnet:<br/> 
		score = &sum;(1..n) 1 / (<b>a</b> * <b>topRelatedTermRank</b>_n) + <b>b</b>);<br/>
		<b>2.</b> Die topRelatedTerme werden nach ihrer Score sortiert<br/>
		<b>3.</b> Es werden maximal die Anzahl an ThemeCloud Termen multipliziert mit <b>syntagmaticEntityTermFactor</b> an mit scores versehenen Termen für die ThemeCloud verwendet<br/><br/>
		<b>topRelatedTermRank Berechnung:</b><br/> 
		 Rank 1: höchster topRelatedTermSyntag-Wert, aufsteigend in 1er Schritten<br/>
		<b>Berechnung topRelatedTermSyntag:</b><br/>
		 termFrequency > minTermFrequency <br/>
		 termMatrixEntrySyntag >= minSyntagmaticValue <br/>
		 topRelatedTermSyntag = termMatrixEntrySyntag<br/>
		<b>Berechnung termMatrixEntrySyntag:</b><br/>
		 termMatrixEntrySyntag = logLikelihoodRatioTest(termMatrixEntryTerm1Frequency,
		 termMatrixEntryTerm2Frequency, termMatrixEntryCoocurrence, nbEntities)<br/>
	</div>
</div>