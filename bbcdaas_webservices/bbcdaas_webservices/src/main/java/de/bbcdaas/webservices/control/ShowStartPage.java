package de.bbcdaas.webservices.control;

import de.bbcdaas.webservices.constants.Constants;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author Robert Illers
 */
public final class ShowStartPage implements Controller {

	@Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<String> localhostURLs = new ArrayList<String>();
		localhostURLs.add("0.0.0.0"); // for remote servers
		localhostURLs.add("0:0:0:0:0:0:0:1"); // ip v6
		localhostURLs.add("127.0.0.1"); // local loop

		StringBuilder websiteURL = new StringBuilder();
		if (request != null) {
			websiteURL.append("http://");
			if (localhostURLs.contains(request.getLocalName())) {
				websiteURL.append("localhost");
			} else {
				websiteURL.append(request.getLocalName());
			}
			websiteURL.append(":").append(request.getLocalPort());
			websiteURL.append(request.getContextPath());
			request.getSession().getServletContext().setAttribute(Constants.KEY_WEBSITE_URL, websiteURL.toString());
		}
        return new ModelAndView("page.webservices");
    }
}