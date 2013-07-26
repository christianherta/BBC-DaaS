function createTermDictionary() {
	ajaxRequest('handleFileOutputForm.do', {'task' : 'createTermDictionary', 'maxNumberOfTerms' : $('maxNumberOfTerms').value}, 'ajax_termDictionary');
}

function createTrainingDataOutput() {
	ajaxRequest('handleFileOutputForm.do', {'task' : 'createTrainingDataOutput', 'minMatchingTerms' : $('minMatchingTerms').value}, 'ajax_trainingData');
}