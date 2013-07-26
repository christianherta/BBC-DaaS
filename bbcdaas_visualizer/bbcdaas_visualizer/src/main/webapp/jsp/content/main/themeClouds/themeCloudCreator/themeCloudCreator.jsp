<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="de.bbcdaas.visualizer.constants.VisualizerConstants" %>

<c:set var="sessionContainer" value="<%=VisualizerConstants.KEY_SESSION_CONTAINER %>"/>

<div id="themeCloudCreator" style='width: 60%;min-height: 400px;background-color: lightgray;'
	 <c:if test="${themeCloudCreator_active eq 0}">class='hidden'</c:if>>
	<table border="0" style="width: 100%;" id="themeCloudCreatorTable">
		<colgroup>
			<col style="width: 50%"/>
			<col style="width: 50%"/>
		</colgroup>
		<tr>
			<%-- input for theme cloud --%>
			<td style="vertical-align: top;padding-top: 5px;">
				<b>
					<spring:message code="themeClouds.inputTerm"/>:
				</b>
				<input type="text" name="inputTerm" style="width: 200px;" onkeydown="if (keyPressed(event,13)) {
																					   $('task').value='addNewTerm';
																					   document.themeCloudForm.submit();
																				   }"/>
				<input type="submit" name="addNewTerm" value="<spring:message code='themeClouds.addTermsToThemeCloud'/>"
								   onmouseup="$('task').value='addNewTerm';" style="margin-left:50px;"/>
				<hr/>
				<table border ="0" style="width: 100%;">
					<tr>
						<td>
							<%-- syntag term cloud as table with rating & weighting inputs --%>
							<table border="0">
								<tr>
									<td colspan="5" style="height: 20px;vertical-align: bottom;">
										<b>
											<spring:message code="themeClouds.syntagCloud"/>:
										</b>
									</td>
								</tr>
								<c:set var="syntagTermIndex" value="0"/>
								<c:if test="${syntagCloudTermCount eq 0}">
									<tr>
										<td colspan="5" style="color:red;">
											<spring:message code="themeClouds.noSyntagCloud"/>
										</td>
									</tr>	
								</c:if>
								<c:forEach var="syntagTerm" items="${sessionScope[sessionContainer].themeCloudSessionBean.syntagTerms}">
									<tr>
										<td colspan="2">
											<c:out value="${syntagTerm.value}"/>
										</td>
										<td style="width: 30px;text-align: center;background-color: white;">
											<input type="radio" name="rating_${syntagTermIndex}" value="0" checked="checked"/>
										</td>
										<td style="width: 30px;text-align: center;background-color: gray;">
											<input type="radio" name="rating_${syntagTermIndex}" value="1"/>
										</td>
										<td style="width: 30px;text-align: center;background-color: red;">
											<input type="radio" name="rating_${syntagTermIndex}" value="2"/>
										</td>	
									</tr>
									<c:set var="syntagTermIndex" value="${syntagTermIndex + 1}"/>
								</c:forEach>
							</table>
							<%-- /syntag term cloud as table with rating & weighting inputs --%>
						</td>
						<td style="padding-left: 10px;vertical-align: center;">
							<c:if test="${syntagCloudTermCount ne 0}">
								<input type="submit" name="addSyntagCloudTerms" value="<spring:message code='themeClouds.addTermsToThemeCloud'/>"
									   onmouseup="$('task').value='addSyntagCloudTerms';"/>
							</c:if>
						</td>
					</tr>
				</table>

				<hr/>

				<%-- syntagCloud parameter --%>
				<input type="hidden" name="syntagCloudParameterVisible" id="syntagCloudParameterVisible" value="${sessionScope[sessionContainer].themeCloudSessionBean.syntagCloudParameterVisible}"/>
				<span id="showSyntagCloudParameter" style="float:right;cursor: pointer;font-size: 12px;text-decoration: underline;"
					  onmouseup="$('syntagCloudParameter').removeClass('hidden');
								 $('hideSyntagCloudParameter').removeClass('hidden');
								 $('showSyntagCloudParameter').addClass('hidden');
								 $('syntagCloudParameterVisible').value = '1';">
					<spring:message code="themeClouds.showSyntagCloudParameter"/>
				</span>
				<span id="hideSyntagCloudParameter" style="float:right;cursor: pointer;font-size: 12px;text-decoration: underline;"
					  <c:if test="${sessionScope[sessionContainer].themeCloudSessionBean.syntagCloudParameterVisible eq 0}">class="hidden"</c:if>
					  onmouseup="$('syntagCloudParameter').addClass('hidden');
								 $('hideSyntagCloudParameter').addClass('hidden');
								 $('showSyntagCloudParameter').removeClass('hidden');
								 $('syntagCloudParameterVisible').value = '0';">
					<spring:message code="themeClouds.hideSyntagCloudParameter"/>
				</span><br/>
				<div id="syntagCloudParameter"
					 <c:if test="${sessionScope[sessionContainer].themeCloudSessionBean.syntagCloudParameterVisible eq 0}">class="hidden"</c:if>>
				<b>
					<spring:message code="themeClouds.syntagCloudParameter"/>:
				</b>
				<table border="0">
					<tr>
						<td>
							<b>
								<spring:message code='syntagClouds.a'/>:
							</b>
						</td>
						<td>
							<input type="text" name="factor_a" id="factor_a" value="${sessionScope[sessionContainer].themeCloudSessionBean.a}"/>
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
							<input type="text" name="factor_b" id="factor_b" value="${sessionScope[sessionContainer].themeCloudSessionBean.b}"/>
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
							<input type="text" name="syntagmaticEntityTermFactor" id="syntagmaticEntityTermFactor" value="${sessionScope[sessionContainer].themeCloudSessionBean.syntagmaticEntityTermFactor}"/>
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
							<input type="text" name="minSyntag" id="minSyntag" value="${sessionScope[sessionContainer].themeCloudSessionBean.minSyntag}"/>
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
					<tr>
						<td colspan="2" style="text-align: right;">
							<input type="submit" value="<spring:message code='themeClouds.calcSyntagCloud'/>" 
								   onmouseup="$('task').value='calcSyntagCloud';"
								   <c:if test="${themeCloudTermCount eq 0}">disabled="disabled"</c:if>/>
							<br/>
						</td>
					</tr>
				</table>
				<%-- /syntagCloud parameter --%>

				<%-- helptext --%>
				<%@include file="themeCloudCreator_helptext.jsp" %>
			</td>
			<%-- input for theme cloud --%>

			<%-- theme cloud --%>
			<td style="border-left: 1px solid black;padding-left: 10px;padding-top: 5px;height: 100%; vertical-align: top;">
				<div>
					<b>
						<spring:message code="themeClouds.themeCloudName"/>:
					</b>
					<input type="text" name="themeCloudName" value="${sessionScope[sessionContainer].themeCloudSessionBean.themeCloudName}"
						   style="width: 250px;" onkeypress="if (keyPressed(event,13)) {return false;}"/>
					<br/><br/>
					<b>
						<spring:message code="themeClouds.themeCloud"/>:
					</b>
					<input style="float:right" type="radio" name="themeCloudViewType" value="0"
						   onchange="if (this.checked) {$('blockStyleThemeCloud').addClass('hidden');$('tableStyleThemeCloud').removeClass('hidden')};"/>
					<input style="float:right" type="radio" name="themeCloudViewType" value="1" checked="checked"
						   onchange="if (this.checked) {$('blockStyleThemeCloud').removeClass('hidden');$('tableStyleThemeCloud').addClass('hidden')};"/>
					<br/>
				</div>

				<div style="overflow: auto;height: 500px;">

					<c:if test="${themeCloudTermCount eq 0}">
						<div style="color:red;">
							<spring:message code="themeClouds.noThemeCloudTerms"/>
						</div>
					</c:if>

					<c:if test="${themeCloudTermCount ne 0}">

						<%-- colored themeCloud (block style) --%>
						<div id="blockStyleThemeCloud">
							<div style="background-color: white;margin-bottom: 5px;padding: 1;min-height: 100px;" class="dnd_target" id="dnd_target_white">
								<c:set var="themeCloudTermIndex" value="0"/>
								<c:forEach var="themeCloudTerm" items="${sessionScope[sessionContainer].themeCloudSessionBean.themeCloud}">
									<c:if test="${themeCloudTerm.rating eq 0}">
										<c:if test="${themeCloudTermIndex ne 0}">, </c:if>
										<span class="dnd_node" id="${themeCloudTerm.id}" name="node_${themeCloudTerm.id}"
											  ondblclick="$('weightingView_${themeCloudTerm.id}').addClass('hidden');$('weightingInput_${themeCloudTerm.id}').removeClass('hidden');$('weightingInput_${themeCloudTerm.id}').focus();">
											<span>
												<c:out value="${themeCloudTerm.value}"/>
											</span>
											<span id="weightingView_${themeCloudTerm.id}">
												<c:out value=" (${themeCloudTerm.weighting})"/>
											</span>
										</span>
										<input type="text" name="weightingInput_${themeCloudTerm.id}" id="weightingInput_${themeCloudTerm.id}" 
											   value="${themeCloudTerm.weighting}" 
											   style="width: 30px;" class="hidden"
											   onkeydown="if (keyPressed(event,13)) {$('task').value='setWeighting';document.themeCloudForm.submit();}"/>
										<c:set var="themeCloudTermIndex" value="${themeCloudTermIndex + 1}"/>
									</c:if>
								</c:forEach>
							</div>
							<div style="background-color: gray;margin-bottom: 5px;padding: 1;min-height: 50px;" class="dnd_target" id="dnd_target_gray">
								<c:set var="i" value="1"/>
								<c:forEach var="themeCloudTerm" items="${sessionScope[sessionContainer].themeCloudSessionBean.themeCloud}">
									<c:if test="${themeCloudTerm.rating eq 1}">
										<c:if test="${i ne 1}">, </c:if>
										<span class="dnd_node" id="${themeCloudTerm.id}">
											<span><c:out value="${themeCloudTerm.value}"/></span>
										</span>
										<c:set var="i" value="${i + 1}"/>
									</c:if>
								</c:forEach>
							</div>
							<div style="background-color: red;padding: 1;margin-bottom: 15px;min-height: 50px;" class="dnd_target" id="dnd_target_red">
								<c:set var="i" value="1"/>
								<c:forEach var="themeCloudTerm" items="${sessionScope[sessionContainer].themeCloudSessionBean.themeCloud}">
									<c:if test="${themeCloudTerm.rating eq 2}">
										<c:if test="${i ne 1}">, </c:if>
										<span class="dnd_node" id="${themeCloudTerm.id}">
											<span><c:out value="${themeCloudTerm.value}"/></span>
										</span>
										<c:set var="i" value="${i + 1}"/>
									</c:if>
								</c:forEach>
							</div>
							<spring:message code="themeClouds.dragAndDropHint"/>	
						</div>
						<%-- /colored themeCloud (block style) --%>

						<br/>

						<%-- colored themeCloud (table style) --%>
						<div id="tableStyleThemeCloud" class="hidden">
							<table border="0" style="width: 100%;">
								<colgroup>
									<col style="width: 34%;"/>
									<col style="width: 33%;"/>
									<col style="width: 33%;"/>
								</colgroup>
								<c:forEach var="coloredThemeCloud" items="${sessionScope[sessionContainer].themeCloudSessionBean.coloredThemeCloud}">
									<tr>
										<td style="background-color: white;">
											<c:out value="${coloredThemeCloud.whiteTerm.value}"/>
											<c:if test="${coloredThemeCloud.whiteTerm.weighting ne 0}">
												<c:out value="(${coloredThemeCloud.whiteTerm.weighting})"/>
											</c:if>
										</td>
										<td style="background-color: gray;">
											<c:out value="${coloredThemeCloud.grayTerm.value}"/>
										</td>
										<td style="background-color: red;">
											<c:out value="${coloredThemeCloud.redTerm.value}"/>
										</td>
									</tr>
								</c:forEach>
							</table>
						</div>
						<%-- /colored themeCloud (table style) --%>
					</c:if>
				</div>

				<%-- save and reset buttons --%>
				<div style="text-align: center;padding-top: 10px;padding-bottom: 5px;">
							<input type="button" name="saveThemeCloud" value="<spring:message code='themeClouds.saveThemeCloud'/>"
												 onmouseup="if (confirm('<spring:message code="themeClouds.saveThemeCloudQuestion"/>')) {$('task').value='save';document.themeCloudForm.submit();}"
												 <c:if test="${themeCloudTermCount eq 0}">disabled="disabled"</c:if>/>
							<input type="button" name="resetThemeCloud" value="<spring:message code='themeClouds.resetThemeCloud'/>"
												 onmouseup="if (confirm('<spring:message code="themeClouds.resetThemeCloudQuestion"/>')) {$('task').value='reset';document.themeCloudForm.submit();}"
												 <c:if test="${themeCloudTermCount eq 0}">disabled="disabled"</c:if>/>
				</div>
				<%-- /save and reset buttons --%>
			</td>		
			<%-- /theme cloud --%>
		</tr>
	</table>
</div>