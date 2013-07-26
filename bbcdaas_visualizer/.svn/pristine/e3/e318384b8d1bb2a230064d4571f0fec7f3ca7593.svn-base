<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>
<%@page import="de.bbcdaas.common.beans.Term" %>

<c:set var="sessionContainer" value="<%=VisualizerConstants.KEY_SESSION_CONTAINER %>" />
<c:set var="rating_bad" value="<%=Term.RATING_BAD %>" />
<c:set var="rating_good" value="<%=Term.RATING_GOOD %>" />
<c:set var="rating_dontknow" value="<%=Term.RATING_DONTKNOW %>" />

<div class="eval_ratedTerms">
	
	<form id="sampleForm" method="post">
		
		<div class="eval_ratedTermsTable">
			
			<input type="hidden" id="task" name="task"/> 
			
			<b>
				<spring:message code="evaluation.relatedTerms"/>
				<c:forEach var="randomTermGroup" items="${sessionScope[sessionContainer].evaluationSessionBean.randomTermGroups}">
					<c:if test="${randomTermGroup.groupID eq sessionScope[sessionContainer].evaluationSessionBean.selectedRandomTermsGroupID}">
						<c:forEach var="randomTerm" items="${randomTermGroup.termToTermsGroups}">
							<c:if test="${randomTerm.term.id eq sessionScope[sessionContainer].evaluationSessionBean.selectedRandomTermID}">
								"<c:out value="${randomTerm.term.value}"/>":
							</c:if>
						</c:forEach>
					</c:if>
				</c:forEach>
			</b>
			<table border="1">
				<colgroup>
					<col style="width: 150px;"/>
					<col style="width: 80px;"/>
					<col style="width: 80px;"/>
					<col style="width: 80px;"/>
				</colgroup>
				<thead>
					<tr>
						<th style="text-align: left;"><spring:message code="evaluation.term"/></th>
						<th style="text-align: left;"><spring:message code="evaluation.good"/></th>
						<th style="text-align: left;"><spring:message code="evaluation.bad"/></th>
						<th style="text-align: left;"><spring:message code="evaluation.dontknow"/></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="randomTermGroup" items="${sessionScope[sessionContainer].evaluationSessionBean.randomTermGroups}">
						<c:if test="${randomTermGroup.groupID eq sessionScope[sessionContainer].evaluationSessionBean.selectedRandomTermsGroupID}">
							<c:forEach var="randomTerm" items="${randomTermGroup.termToTermsGroups}">
								<c:if test="${randomTerm.term.id eq sessionScope[sessionContainer].evaluationSessionBean.selectedRandomTermID}">
									<c:forEach var="relatedTerm" items="${randomTerm.relatedTerms}">
										<c:if test="${relatedTerm.added eq false}">
											<tr>
												<td style="text-align: left;">
													<c:out value="${relatedTerm.value}"/>
												</td>
												<td style="text-align: center;">
													<input type="radio" name="rating_${relatedTerm.id}" value="${rating_good}"
														<c:if test="${relatedTerm.rating eq rating_good}">checked="checked"</c:if>
														<c:if test="${sessionScope[sessionContainer].evaluationSessionBean.currentSelectedRatingSaved eq true}">disabled="disabled"</c:if>/>
												</td>
												<td style="text-align: center;">
													<input type="radio" name="rating_${relatedTerm.id}" value="${rating_bad}"
														<c:if test="${relatedTerm.rating eq rating_bad}">checked="checked"</c:if>
														<c:if test="${sessionScope[sessionContainer].evaluationSessionBean.currentSelectedRatingSaved eq true}">disabled="disabled"</c:if>/>
												</td>
												<td style="text-align: center;">
													<input type="radio" name="rating_${relatedTerm.id}" value="${rating_dontknow}"
														<c:if test="${relatedTerm.rating eq rating_dontknow}">checked="checked"</c:if>
														<c:if test="${sessionScope[sessionContainer].evaluationSessionBean.currentSelectedRatingSaved eq true}">disabled="disabled"</c:if>/>
												</td>
											</tr>
										</c:if>
									</c:forEach>
								</c:if>
							</c:forEach>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
		</div>
				
		<br/>
					
		<c:set var="hasAddedTerms" value="0"/>
		<c:forEach var="randomTermGroup" items="${sessionScope[sessionContainer].evaluationSessionBean.randomTermGroups}">
			<c:if test="${randomTermGroup.groupID eq sessionScope[sessionContainer].evaluationSessionBean.selectedRandomTermsGroupID}">
				<c:forEach var="randomTerm" items="${randomTermGroup.termToTermsGroups}">
					<c:if test="${randomTerm.term.id eq sessionScope[sessionContainer].evaluationSessionBean.selectedRandomTermID}">
						<c:forEach var="relatedTerm" items="${randomTerm.relatedTerms}">
							<c:if test="${relatedTerm.added eq true}">
								<c:set var="hasAddedTerms" value="1"/>
							</c:if>
						</c:forEach>
					</c:if>
				</c:forEach>
			</c:if>
		</c:forEach>
		
		<div class="eval_addedTermsTable">
			<b>
				<spring:message code="evaluation.addedTerms"/>:
			</b>
			
			<%-- button and input field for adding new terms to topRelatedTerms list --%>
			<div id="eval_addNewRatedTerm">
				<input type="text" id="searchString" name="value" size="20" 
					   <%-- suggesting removed
					   <c:if test="${sessionScope[sessionContainer].currentSelectedRatingSaved eq false}">
						onkeyup="searchSuggestions(event, this.value);"
					   </c:if>
					   /suggesting removed --%>
						onkeydown="if (keyPressed(event,13)) {return false;}"
					   <c:if test="${sessionScope[sessionContainer].evaluationSessionBean.currentSelectedRatingSaved eq true}">
						disabled="disabled"
					   </c:if>/>
				<input type="button" value='<spring:message code="evaluation.addTerm"/>' 
					   <c:if test="${sessionScope[sessionContainer].evaluationSessionBean.currentSelectedRatingSaved eq false}">
						onmouseup="$('task').value='addTerm';ajaxFormRequest('handleEvaluationForm.do', 'sampleForm', 'ajax_ratedTerms');"
					   </c:if>
					   <c:if test="${sessionScope[sessionContainer].evaluationSessionBean.currentSelectedRatingSaved eq true}">
						disabled="disabled"
					   </c:if>/>
			</div>
			<%-- /button and input field for adding new terms to topRelatedTerms list --%>

			<%-- suggesting removed
			<div class="searchOutput">
				<div id="ajax_searchSuggestionResult" class="searchSuggestionResult hidden"></div>
			</div>
			/suggesting removed --%>
			
			<c:if test="${hasAddedTerms eq 1}">
				<table border="1">
					<c:forEach var="randomTermGroup" items="${sessionScope[sessionContainer].evaluationSessionBean.randomTermGroups}">
						<c:if test="${randomTermGroup.groupID eq sessionScope[sessionContainer].evaluationSessionBean.selectedRandomTermsGroupID}">
							<c:forEach var="randomTerm" items="${randomTermGroup.termToTermsGroups}">
								<c:if test="${randomTerm.term.id eq sessionScope[sessionContainer].evaluationSessionBean.selectedRandomTermID}">
									<c:forEach var="relatedTerm" items="${randomTerm.relatedTerms}">
										<c:if test="${relatedTerm.added eq true}">
											<tr>
												<td>
													<c:out value="${relatedTerm.value}"/>
												</td>
												<td>
													<input type="button" value="X" name="${relatedTerm.id}" 
														   title='<spring:message code="evaluation.removeTerm" /> "${relatedTerm.value}"'
														   <c:if test="${sessionScope[sessionContainer].evaluationSessionBean.currentSelectedRatingSaved eq true}">
																disabled="disabled"
														   </c:if>
														   <c:if test="${sessionScope[sessionContainer].evaluationSessionBean.currentSelectedRatingSaved eq false}">
																onmouseup="$('task').value='removeTerm';$('termToRemoveID').value=this.name;ajaxFormRequest('handleEvaluationForm.do', 'sampleForm', 'ajax_ratedTerms');"
														   </c:if>/>
												</td>
											</tr>
										</c:if>
									</c:forEach>
								</c:if>
							</c:forEach>
						</c:if>
					</c:forEach>
				</table>
				<input type="hidden" name="termToRemoveID" id="termToRemoveID" />
			</c:if>
			<c:if test="${hasAddedTerms eq 0}">
				<br/>
				<spring:message code="evaluation.noAddedTerms" />
			</c:if>	
		</div>
			
		<br/><br/>
		
		<div>
			<%-- save sample button --%>
			<input type="button" name="saveSample" value='<spring:message code="evaluation.saveSample"/>'
				   <c:if test="${sessionScope[sessionContainer].evaluationSessionBean.currentSelectedRatingSaved eq false}">
					   onmouseup="if (confirm('<spring:message code="evaluation.confirmSaveSample"/>')) {$('task').value='saveSample';ajaxFormRequest('handleEvaluationForm.do', 'sampleForm', 'ajax_ratedTerms');this.onmouseup = null;}"
				   </c:if>
				   <c:if test="${sessionScope[sessionContainer].evaluationSessionBean.currentSelectedRatingSaved eq true}">
					disabled="disabled"
				   </c:if>/>
		</div>
	</form>
		
</div>