function ajaxRequest(url, dataObj, target) {
	new Request({noCache: true, encoding :'utf-8', url: url, timeout:10000, method: 'get', evalScripts: true, data: dataObj,
		onComplete: function(response) {$(target).set('html',response);}
	}).send();
}

function ajaxFormRequest(url, form, target) {
	new Request.HTML({noCache: true, encoding :'utf-8', timeout:10000, url: url, update:$(target)}).post($(form));
}