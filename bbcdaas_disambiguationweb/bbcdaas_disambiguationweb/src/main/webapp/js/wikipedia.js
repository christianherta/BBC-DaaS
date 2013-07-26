function searchForWikipediaURIs(searchString, waitingMessage, noResultMessage) {

	$('ajax_searchSuggestionResult').addClass('hidden');
	$('performSearch').set('disabled', true);
	$('ajax_searchResultURIs').set('html', waitingMessage);
	$('ajax_searchResultURIs').removeClass('hidden');
	ajaxGetRequest('searchForWikipediaURIs.do',{searchString : encodeURIComponent(searchString)}, ['ajax_searchResultURIs'],
					true, false, function() {
						$('performSearch').set('disabled', false);
					}.bind(null), noResultMessage);
}

function wikipediaLuceneDisambiguation(waitingMessage, noResultMessage) {

	var terms = collectFieldValues('searchString_disambiguation');
	var patternRatings = collectFieldValues('patternRating',1);
	var scorerWeightings = collectFieldValues('scorerWeighting',1);
	var candidateFinderNames = "";
	$$('.candidateFinder').each(function(el) {
		if (el.checked) {
			candidateFinderNames += el.value + ",";
		}
	});
	var termsArray = terms.split(',');
	$('ajax_searchSuggestionResult').addClass('hidden');
	$('performDisambiguation').set('disabled', true);
	$('ajax_searchResult').style.left = "140px";
	$('ajax_searchResult').style.top = (280 + termsArray.length * 21) + "px";
	$('ajax_searchResult').set('html', waitingMessage);
	$('ajax_searchResult').removeClass('hidden');

	ajaxGetRequest('wikipediaLuceneDisambiguation.do', {'terms' : terms,
		'maxTermDocuments' : $('maxTermDocuments').get('value').trim(),
		'maxTermDocumentsPerPattern' : $('maxTermDocumentsPerPattern').get('value').trim(),
		'multimatchingDocumentsRatingAddend' : $('multimatchingDocumentsRatingAddend').get('value').trim(),
		'patternRatings' : patternRatings,
		'scorerWeightings' : scorerWeightings,
		'candidateFinder' : candidateFinderNames,
		'alternativeURIRating' : $('alternativeURIRating').get('value').trim()},
		['ajax_searchResult'], true, false, function() {
			$('performDisambiguation').set('disabled', false);
		}.bind(null), noResultMessage);
}

function wikipediaGetCategoryContext(waitingMessage, noResultMessage) {

	var categoryName = $('categoryName').value;
	$('performGetCategoryContext').set('disabled', true);
	$('ajax_categoryContextResult').set('html', waitingMessage);
	$('ajax_categoryContextResult').removeClass('hidden');

	ajaxGetRequest('getCategoryContext.do', {'categoryName' : categoryName},
		['ajax_categoryContextResult'], true, false, function() {
			$('performGetCategoryContext').set('disabled', false);
		}.bind(null), noResultMessage);
}