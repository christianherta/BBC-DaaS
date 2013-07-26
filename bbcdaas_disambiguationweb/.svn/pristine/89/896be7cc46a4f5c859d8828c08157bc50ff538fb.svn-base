<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<td style="vertical-align:top;">
    <div style="margin-right: 20px;margin-top: 30px;">
        <b><spring:message code="wikipedia.config.caption"/></b><br/><br/>
        <div style="overflow: auto;height:570px;">
            <table>
                <c:if test="${empty sessionScope[params]}">
                    <tr>
                        <td colspan="2">
                            <spring:message code="wikipedia.config.noConfigData"/>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${not empty sessionScope[params]}">
                    <tr>
                        <td colspan="2">
                            <span style="font-style: italic;">
                                1.<spring:message code="wikipedia.config.candidateSearch"/>:
                            </span>
                            <br/><br/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            CandidateFinder:
                        </td>
                        <td>
                            <c:set var="candidateFinderIndex" value="0"/>
                            <c:forEach var="candidateFinderName" items="${sessionScope[params].candidateFinderNames}">
                                <c:out value="${candidateFinderName}"/>
                                <input type="checkbox" 
									   value="${candidateFinderName}"
                                       name="candidateFinder"
									   class="candidateFinder"
                                       <c:if test="${sessionScope[params].candidateFinder[candidateFinderIndex] eq true}">checked="checked"</c:if>/>
                                <c:set var="candidateFinderIndex" value="${candidateFinderIndex + 1}"/>
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            maxTermDocuments:
                        </td>
                        <td>
                            <input type="text" 
                                name="maxTermDocuments" 
                                id="maxTermDocuments"
                                value="${sessionScope[params].maxTermDocuments}"><br/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            maxTermDocumentsPerPattern:
                        </td>
                        <td>
                            <input type="text" 
                                name="maxTermDocumentsPerPattern" 
                                id="maxTermDocumentsPerPattern"
                                value="${sessionScope[params].maxTermDocumentsPerPattern}"><br/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            alternativeURIRating:
                        </td>
                        <td>
                            <input type="text" 
                                name="alternativeURIRating" 
                                id="alternativeURIRating"
                                value="${sessionScope[params].alternativeURIRating}"><br/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <br/>
                            <spring:message code="wikipedia.config.patternRatings"/>:<br/>
                        </td>
                    </tr>
                    <c:forEach var="aPattern" items="${sessionScope[pattern]}">
                        <tr>
                            <td>
                                <c:out value="${aPattern.pattern}"/>:
                            </td>
                            <td>
                                <input type="text" 
                                    name="patternRating[${aPattern.id}]" 
                                    id="patternRating_${aPattern.id}"
                                    value="${aPattern.rating}"><br/>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td colspan="2">
                            <br/>
                            <span style="font-style: italic;">
                            2.<spring:message code="wikipedia.config.scoring"/>:
                            </span>
                            <br/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <br/>
                            multimatchingDocumentsRatingAddend:
                        </td>
                        <td>
                            <br/>
                            <input type="text" 
                                name="multimatchingDocumentsRatingAddend" 
                                id="multimatchingDocumentsRatingAddend"
                                value="${sessionScope[params].multimatchingDocumentsRatingAddend}"><br/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <br/>
                            <spring:message code="wikipedia.config.scoring.scorer"/>:<br/>
                        </td>
                    </tr>
                    <c:set var="scorerWeightingIndex" value="0"/>
                    <c:forEach var="documentScorerName" items="${sessionScope[params].documentScorerNames}">
                        <tr>
                            <td>
                                <c:out value="'${documentScorerName}'"/>:
                            </td>
                            <td>
                                <input type="text" 
                                    value="${sessionScope[params].documentScorerWeightings[scorerWeightingIndex]}"
                                    name="scorerWeighting[${scorerWeightingIndex+1}]"
                                    id="scorerWeighting_${scorerWeightingIndex+1}"/>
                            </td>
                        </tr>	
                        <c:set var="scorerWeightingIndex" value="${scorerWeightingIndex + 1}"/>
                    </c:forEach>
                </c:if>
            </table>
        </div>
    </div>
</td>