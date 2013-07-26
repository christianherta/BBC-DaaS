window.addEvent('domready',function() {

	init_formMenu();
});

function setSearch(searchString) {
	$('searchString').value = searchString;
	$('ajax_searchSuggestionResult').addClass('hidden');
}

function startStatisticsWriter() {
	ajaxRequest('startStatisticsWriter.do',{},'ajax_statisticWriterState');
}

function startProcessing() {
	ajaxRequest('startProcessing.do',{},'ajax_processState');
}

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

function performSearch(searchType, searchString) {
	ajaxRequest('performSearch.do',{searchType : searchType, searchString : encodeURIComponent(searchString)},'ajax_searchResult');
	$('ajax_searchSuggestionResult').addClass('hidden');
	$('ajax_searchResult').removeClass('hidden');
}

function searchSuggestions(event, searchString) {
	if (searchString.length > 2 && !keyPressed(event,8)) {
		ajaxRequest('performSearch.do',{searchType : 'termSuggestion', searchString : encodeURIComponent(searchString)},'ajax_searchSuggestionResult');
		$('ajax_searchSuggestionResult').removeClass('hidden');
	} else {
		$('ajax_searchSuggestionResult').addClass('hidden');
	}
}

//* initialisation of the form menu animation
function init_formMenu() {
	if($('formmenu')) {
		var formlinks = $$("#formmenu .formlink");
		//var szSmall  = Math.floor($('formmenu').width/(formlinks.length)), szFull  = 126;
		var szSmall  = 180, szFull  = 250;
		if(Browser.Engine.trident4 == true) szSmall += 4; //Boxmodell-fix IE6
		var fx = new Fx.Elements(formlinks, {wait: false, duration: 300, transition: Fx.Transitions.Back.easeOut});
		formlinks.each(function(formlink, i) {
			if(!formlink.hasClass('active')) formlink.setStyle('width',szSmall);
			formlink.addEvents({
				"mouseenter":function(event) {
					var o = {};
					o[i] = {width: [formlink.getStyle("width").toInt(), szFull]}
					formlinks.each(function(other, j) {
						if(i != j) {
							var w = other.getStyle("width").toInt();
							if(w != szSmall) o[j] = {width: [w, szSmall]};
						}
					});
					fx.start(o);
				},
				"click":function(event) {
					$('formmenu').getElements(".active").each(function(el,index){el.removeClass('active');});
					formlink.addClass('active');
				}
			});
		});

		$("formmenu").addEvent("mouseleave", function(event) {
			var o = {};
			formlinks.each(function(formlink, i) {
				if(formlink.hasClass('active')) {
					o[i] = {width: [formlink.getStyle("width").toInt(), szFull]};
				} else {
					o[i] = {width: [formlink.getStyle("width").toInt(), szSmall]};
				}
			});
			fx.start(o);
		})
	}
}