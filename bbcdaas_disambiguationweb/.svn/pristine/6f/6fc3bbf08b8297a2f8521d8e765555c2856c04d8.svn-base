// checks if a specific key is pressed, identified by its keycode
function keyPressed(event, keycode) {
	if (!event) {
		event = window.event;
	}
	var kcode;
	if (event.which) {
		kcode = event.which;
	} else if (event.keyCode) {
		kcode = event.keyCode;
	}
	if (kcode == keycode) {
		return true;
	} else {
		return false;
	}
}

// starts a search for suggestion terms and makes the suggestion menu visible
function searchSuggestions(event, searchString, selectionTarget, top, left, index) {
	
	if (!keyPressed(event,13) && searchString.length > 2 && !keyPressed(event,8)) {
		
		$('ajax_searchSuggestionResult').addClass('hidden');
		var arr = [top, left, index];
		ajaxGetRequest('suggestTerms.do', {'searchString' : encodeURIComponent(searchString), 'selectionTarget' : selectionTarget},
			['ajax_searchSuggestionResult'], false, false, function(top, left, index) {
				$('ajax_searchSuggestionResult').style.top = (top + index * 21) + "px";
				$('ajax_searchSuggestionResult').style.left = left + "px";
				$('ajax_searchSuggestionResult').removeClass('hidden'); 
			}.bind(null, arr));
	} 
}

function collectFieldValues(name, startID) {
	
	if (startID == undefined) {
		startID = 0;
	}
	
	var valueCollection = "";
	for (var i = startID;$(name+'_'+i) != undefined; i++) {
		if (i != startID) {
			valueCollection = valueCollection + ",";
		}
		if ($(name+'_'+i).value != "") {
			valueCollection = valueCollection + encodeURIComponent($(name+'_'+i).value);
		} else {
			valueCollection = valueCollection + " ";
		}
	}
	return valueCollection;
}

function addDisambiguationTerm() {
		
		var fieldValues = collectFieldValues('searchString_disambiguation');
		$('ajax_searchResult').addClass('hidden'); 
		ajaxRequest('addDisambiguationTerm.do', {'terms' : fieldValues}, 'disambiguationInputs');
}

function removeDisambiguationTerm(index) {
		var fieldValues = collectFieldValues('searchString_disambiguation');
		$('ajax_searchResult').addClass('hidden'); 
		ajaxRequest('removeDisambiguationTerm.do', {'index' : index , 'terms' : fieldValues}, 'disambiguationInputs');
}