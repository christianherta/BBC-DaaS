function getRandomEntity() {
	ajaxRequest('handleSyntagCloudsForm.do', {'task' : 'getRandomEntity'}, 'ajax_randomEntity');
}

function searchForEntity() {
	ajaxRequest('handleSyntagCloudsForm.do', {'task' : 'searchForEntity', 'term1' : $('term1').value,
		'term2' : $('term2').value, 'term3' : $('term3').value}, 'ajax_randomEntity');
}

function computeSyntagClouds() {
	ajaxRequest('handleSyntagCloudsForm.do', {'task' : 'computeSyntagClouds', 'minSyntag' : $('minSyntag').value,
		'syntagmaticEntityTermFactor' : $('syntagmaticEntityTermFactor').value, 'factor_a' : $('factor_a').value, 'factor_b' : $('factor_b').value}, 'ajax_entitySyntagClouds');
}

function selectPrevEntity() {
	ajaxRequest('handleSyntagCloudsForm.do', {'task' : 'selectPrevEntity'}, 'ajax_randomEntity');
}

function selectNextEntity() {
	ajaxRequest('handleSyntagCloudsForm.do', {'task' : 'selectNextEntity'}, 'ajax_randomEntity');
} 