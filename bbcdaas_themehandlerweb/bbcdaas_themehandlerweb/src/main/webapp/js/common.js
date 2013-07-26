window.addEvent('domready',function() {

	init_formMenu();
});

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