/* Minimalistic Drag'N'Drop Framework
 * programmed by Robert Illers
 * 
 * Description:
 * 
 * What you need:
 * 1. a hidden, empty span with a higher z-level and the id='dnd_mouseText'
 * example: <span class='dnd_mouseText' id='dnd_mouseText'></span>
 * 2. nodes, defined by adding a class='dnd_node' and an id='xyz', that needs to be unique and an html 
 * element in the element with this class, that contains the text that should be added to the dnd_mouseText
 * example: <span class='dnd_node' id='123'>Drag this:<span>DragMe</span></span>  
 * 3. if you want to drop a node on an area, you need to give this are the class='dnd_target' and set an unique id for it 
 * example: <span class='dnd_target' id='324'>Drop nodes here</span>
 */

/* global variables ----------------------------------------------------------*/

var dnd_mouseDown = 0;
var dnd_mouseX = 0;
var dnd_mouseY = 0;
var dnd_node1 = 0;
var dnd_node2 = 0;
var dnd_overNode = 0;
var dnd_overTarget = 0;
var dnd_target1 = 0;

/* actions ***/

/*var dnd_node_action = 0;*/
var dnd_node_action = 'handleThemeCloudForm.do';
/*var dnd_target_action = 0;*/
var dnd_target_action = 'handleThemeCloudForm.do';

/* /actions ***/

/* global variables ----------------------------------------------------------*/

/* add events ----------------------------------------------------------------*/

window.addEvent('domready',function() {
	
	document.html.onmousedown = function() {
		dnd_mouseDown = 1;
		dnd_mousehandler_onmousedown();
	}
	document.html.onmouseup = function() {
		dnd_mouseDown = 0;
		dnd_mousehandler_onmouseup();
	}
	document.html.onmousemove = function(e) {
		dnd_mouseX = document.all ? window.event.x : e.pageX;
		dnd_mouseY = document.all ? window.event.y : e.pageY;
		dnd_mousehandler_onmousemove();
	}
	
	var dnd_targets = $$('.dnd_target');
	dnd_targets.each(function(dnd_target, i) {
		dnd_target.onmouseover = function() {
			dnd_target_onmouseover(dnd_target);
		}
		dnd_target.onmouseup = function() {
			dnd_target_onmouseup();
		}
		dnd_target.onmouseout = function() {
			dnd_target_onmouseout();
		}
	});
	
	var dnd_nodes = $$('.dnd_node');
	dnd_nodes.each(function(dnd_node, i) {
		dnd_node.onmousedown = function() {
			dnd_node_onmousedown(dnd_node);
			return false;
		}
		dnd_node.onmouseout = function() {
			dnd_node_onmouseout(dnd_node);
		}
		dnd_node.onmouseover = function() {
			dnd_node_onmouseover(dnd_node);
		}
		dnd_node.onmouseup = function() {
			dnd_node_onmouseup();
		}
	});
});

/* /add events ---------------------------------------------------------------*/

/* methods for executing additional code on mouse events ---------------------*/
function dnd_mousehandler_onmousedown() {
	try {
	} catch (E) {}
}

function dnd_mousehandler_onmouseup() {
	try {
		dnd_onmouseup();
	} catch (E) {}
}

function dnd_mousehandler_onmousemove() {
	try {
		dnd_onmousemove();
	} catch (E) {}
}
/* /methods for executing additional code on mouse events --------------------*/

/* dragAndDrop global methods ------------------------------------------------*/
 
function dnd_onmousemove() {
	if (dnd_node1 != 0) {
		dnd_moveText();
	}
}

function dnd_onmouseup() {
	dnd_hideText();
	
	if (dnd_node1 != 0 && dnd_node2 == 0 && dnd_target1 == 0 && dnd_overNode == 0 && dnd_overTarget == 0) {
		window.location.href=dnd_target_action+"?task=moveNode&dnd_node1="+dnd_node1;
	}
	
	dnd_node1 = 0;
	dnd_node2 = 0;
	dnd_target1 = 0;
	dnd_overNode = 0;
	dnd_overTarget = 0;
}

/* /dragAndDrop global methods -----------------------------------------------*/

/* dragAndDrop target methods ------------------------------------------------*/

function dnd_target_onmouseover(target) {
	dnd_target1 = target.id;
	dnd_overTarget = 1;
}

function dnd_target_onmouseout() {
	dnd_target1 = 0;
	dnd_overTarget = 0;
}

function dnd_target_onmouseup() {
	if (dnd_overTarget == 1 && dnd_node1 != 0 && dnd_target_action != 0) {
		window.location.href=dnd_target_action+"?task=moveNode&dnd_node1="+dnd_node1+"&dnd_target1="+dnd_target1;
	}
	dnd_node1 = 0;
	dnd_node2 = 0;
	dnd_target1 = 0;
	dnd_overNode = 0;
	dnd_overTarget = 0;
}

/* /dragAndDrop target methods -----------------------------------------------*/

/* dragAndDrop node methods --------------------------------------------------*/

function dnd_node_onmousedown(node) {
	if (dnd_node1 == 0) {
		dnd_node1 = node.id;
	}
}

function dnd_node_onmouseout(node) {
	var dragndropTextElement = $('dnd_mouseText');
	if (dragndropTextElement != null && dnd_overNode == 0 && dnd_mouseDown == 1) {
		dragndropTextElement.innerHTML = node.childNodes[1].innerHTML;
		dragndropTextElement.style.visibility = "visible";
	}
	dnd_overNode = 0;
	dnd_node2 = 0;
}

function dnd_node_onmouseover(node) {
	if (dnd_node1 != 0 && node.id != dnd_node1) {
		dnd_node2 = node.id;
	}
	if (dnd_node1 != 0 && dnd_node2 != 0 && dnd_mouseDown == 1) {
		dnd_overNode = 1;
	}
}

function dnd_node_onmouseup() {
	if (dnd_overNode == 1 && dnd_node_action != 0) {
		window.location.href=dnd_node_action+"?task=moveNode&dnd_node1="+dnd_node1+"&dnd_node2="+dnd_node2;
	}
	dnd_node1 = 0;
	dnd_node2 = 0;
	dnd_target1 = 0;
	dnd_overNode = 0;
	dnd_overTarget = 0;
}

/* /dragAndDrop node methods -------------------------------------------------*/

/* dnd_mouseText -------------------------------------------------------------*/

function dnd_moveText() {
	var dragndropTextElement = $('dnd_mouseText');
	if (dragndropTextElement != null) {
		dragndropTextElement.style.left = (dnd_mouseX + 15) + "px";
		dragndropTextElement.style.top = (dnd_mouseY + 5) + "px";
	}
}

function dnd_hideText() {
	var dragndropTextElement = $('dnd_mouseText');
	if (dragndropTextElement != null) {
		dragndropTextElement.style.visibility = "hidden";
	}
}

/* /dnd_mouseText ------------------------------------------------------------*/