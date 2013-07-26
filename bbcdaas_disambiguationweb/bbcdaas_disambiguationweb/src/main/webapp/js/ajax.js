// calls a ajax get request
// Parameter:
// url - String with the action or page that handles the request
// parameter - map with the get parameters
// targets - an array of object name strings where the response should be written to
// responseCanBeEmpty - boolean : true -> result of ajax request could be empty and callback method should be executed
// callbackBefore - boolean : true -> callback method is executed before result is written
// callback - a method that should be called if response is not empty or response is empty and responseCanBeEmpty == true.
// responseEmptyMessage - message that is displayed at the target if response is empty and responseCanBeEmpty == true
function ajaxGetRequest(/*required parameters: */url, parameter, targets,
					    /*optional parameters: */responseCanBeEmpty ,callbackBefore, callback, responseEmptyMessage) {
	
	new Request({noCache: true, url: url, encoding :'utf-8', method: 'get', evalScripts: true, data: parameter,
		evalResponse: true, onComplete: function(response) {
			var len=0;
			var i=0;
			if (targets != undefined) {
				len=targets.length;
			}
			if (!response || response.trim() == '') {
				if (responseCanBeEmpty == undefined || !responseCanBeEmpty) {
					return;
				}
				if (responseEmptyMessage != undefined) {
					for (i=0; i<len; i++) {
						$(targets[i]).set('html', responseEmptyMessage);
					}
				}	
			}
			if(callbackBefore != undefined && callbackBefore &&
				callback != undefined && typeof callback == 'function') {				
				callback(); 
			}
			if (response && response.trim() != '') {
				for (i=0; i<len; i++) {
					$(targets[i]).set('html', response);
				}
			}
			if(callbackBefore != undefined && !callbackBefore &&
				callback != undefined && typeof callback == 'function') {
				window.addEvent('domready', function() {
						callback();
				});
			}
		}
	}).send();
}

// calls a ajax post request
// Parameter:
// url - String with the action or page that handles the request
// form - mootool object of a html form
// targets - an array of object name strings where the response should be written to
// responseCanBeEmpty - boolean : true -> result of ajax request could be empty and callback method should be executed
// callbackBefore - boolean : true -> callback method is executed before result is written
// callback - a method that should be called if response is not empty or response is empty and responseCanBeEmpty == true.
// responseEmptyMessage - message that is displayed at the target if response is empty and responseCanBeEmpty == true
function ajaxPostRequest(/*required parameters: */url, form, targets, 
						 /*optional parameters: */responseCanBeEmpty, callbackBefore, callback, responseEmptyMessage) {
	
	new Request({noCache: true, url: url, encoding :'utf-8', evalScripts: true, evalResponse: true,
		onComplete: function(response) {
			var len=0;
			var i=0;
			if (targets != undefined) {
				len=targets.length;
			}
			if (!response || response.trim() == '') {
				if (responseCanBeEmpty == undefined || !responseCanBeEmpty) {
					return;
				}
				if (responseEmptyMessage != undefined) {
					for (i=0; i<len; i++) {
						$(targets[i]).set('html',responseEmptyMessage);
					}
				}
			} 
			if(callbackBefore != undefined && callbackBefore &&
				callback != undefined && typeof callback == 'function') {
				callback();  
			}
			if (response && response.trim() != '') {
				for (i=0; i<len; i++) {
					$(targets[i]).set('html',response);
				}
			}
			if(callbackBefore != undefined && !callbackBefore &&
				callback != undefined && typeof callback == 'function') {
				window.addEvent('domready', function() {
						callback();
				});
			}
		}
	}).post(form);
}

function ajaxRequest(url, dataObj, target) {
	new Request({noCache: true, encoding :'utf-8', url: url, timeout:10000, method: 'get', evalScripts: true, data: dataObj,
		onComplete: function(response) {$(target).set('html',response);}
	}).send();
}

function ajaxRequest_UriSearch(url, dataObj, target, noResultMessage) {
	new Request({noCache: true, encoding :'utf-8', url: url, timeout:10000, method: 'get', evalScripts: true, data: dataObj,
		onComplete: function(response) {
                    if (response == null || response == "") {
                        $(target).set('html', noResultMessage);
                    } else {
                        $(target).set('html',response);
                        $('performSearch').set('disabled', false);
                    }
                }
	}).send();
}

function ajaxFormRequest(url, form, target) {
	new Request.HTML({noCache: true, encoding :'utf-8', timeout:10000, url: url, update:$(target)}).post($(form));
}