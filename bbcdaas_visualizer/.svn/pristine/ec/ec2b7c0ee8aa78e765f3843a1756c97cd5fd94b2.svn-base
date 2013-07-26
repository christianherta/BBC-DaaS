package de.bbcdaas.visualizer.utils;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 * Helper class for displaying attributes from session.
 *
 * @author Robert Illers
 */
public final class ScopeInfo {

    private transient static final Logger log = Logger.getLogger(ScopeInfo.class);

	private ScopeInfo() {}

	// shows complete session with attribut values
    public static void showSessionAttributesWithValues(HttpServletRequest request) {

        HttpSession session = request.getSession();

        String attributeName;
        String attributeValue;

        for(Enumeration e = session.getAttributeNames(); e.hasMoreElements(); ) {
            attributeName = (String)e.nextElement();
            attributeValue = session.getAttribute(attributeName).toString();
            log.debug(attributeName + " = " + attributeValue);
	}
    }

	// show the names of the attributes in session
    public static void showSessionAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession();

		log.debug("list of current session attributes:");
        for(Enumeration e = session.getAttributeNames(); e.hasMoreElements(); ) {
            log.debug(e.nextElement().toString());
		}
		log.debug("end session attribute list.");
	}

	// show the names of the attributes in session
    public static void showRequestAttributes(HttpServletRequest request) {

		log.debug("list of current request attributes:");
        for(Enumeration e = request.getAttributeNames(); e.hasMoreElements(); ) {
            log.debug(e.nextElement().toString());
		}
		log.debug("end request attribute list.");
	}

	public static void showRequestParameterNames(HttpServletRequest request) {

		log.debug("list of current request parameter:");
		for (Enumeration e = request.getParameterNames();e.hasMoreElements();) {
			log.debug(e.nextElement().toString());
		}
		log.debug("end request parameter list.");

	}

	// shows the value of a specific session attribute
	public static void showASessionAttribut(HttpServletRequest request, String attrName) {

		try {
			HttpSession session = request.getSession();

			Object anAttribut = session.getAttribute(attrName);
			if (anAttribut != null) {
				if (anAttribut.getClass() == String.class) {
					log.debug(attrName+" = " + anAttribut);
				} else {
					log.debug(attrName+" = " + anAttribut.toString());
				}
			} else {
				log.debug(attrName+" = null");
			}
		} catch (Exception ex) {
			log.error("Error getting "+attrName, ex);
		}

    }

}