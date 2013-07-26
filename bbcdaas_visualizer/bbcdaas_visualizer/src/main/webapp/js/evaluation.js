/* */
function clearRatedTerms() {
	$('ajax_ratedTerms').set('html',"");
}

/* */
function addTerm(value) {
	ajaxRequest('handleEvaluationForm.do', {'task' : 'addTerm', 'value' : value}, 'ajax_ratedTerms');
}

/* */
function randomTermSelected_tester(termID) {
	ajaxRequest('handleEvaluationForm.do', {'task' : 'selectRandomTerm', 'id' : termID}, 'ajax_ratedTerms');
}

function randomTermSelected_admin() {
	$('removeRandomTerm').removeAttribute('disabled');
}

/* */
function newRandomGroup() {
	ajaxRequest('handleEvaluationForm.do', {'task' : 'addNewRandomTermsGroup', 'numberOfRandomTerms' : $('numberOfRandomTerms').value,
		'groupLabel' : $('groupLabel').value, 'minTopTermSyntag' : $('minTopTermSyntag').value}, 'ajax_randomTermGroups');
}

/* */
function removeRandomGroup() {
	ajaxRequest('handleEvaluationForm.do', {'task' : 'removeRandomTermsGroup', 'groupID' : $('randomTermGroups').getSelected()[0].value}, 'ajax_randomTermGroups');
}

function removeRandomTerm() {
	ajaxRequest('handleEvaluationForm.do', {'task' : 'removeRandomTerm', 'id' : $('randomTerms').getSelected()[0].value,
		'groupID' : $('randomTermGroups').getSelected()[0].value}, 'ajax_randomTerms');
}

/* */
function randomTermGroupSelected(rtgID) {
	ajaxRequest('handleEvaluationForm.do', {'task' : 'selectRandomTermsGroup', 'id' : rtgID}, 'ajax_randomTerms');
}