package de.bbcdaas.webservices.services.themehandler;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.ThemeCloud;
import de.bbcdaas.common.beans.User;
import de.bbcdaas.themehandler.business.ThemeHandlerBusiness;
import de.bbcdaas.themehandler.dao.impl.jpa.ThemeHandlerDaoImpl;
import de.bbcdaas.webservices.api.themehandler.ThemeHandlerWebserviceURIs;
import de.bbcdaas.webservices.api.themehandler.beans.ThemeCloudResult;
import de.bbcdaas.webservices.constants.Constants;
import de.bbcdaas.webservices.services.RestServices;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Robert Illers
 */
@Path(ThemeHandlerWebserviceURIs.RESTSERVICE_BASE)
public final class ThemeHandlerRestServices extends RestServices {

	private ThemeHandlerBusiness business = new ThemeHandlerBusiness(new ThemeHandlerDaoImpl("PU_themeHandler"));

	/**
	 *
	 * @return
	 */
	@GET
	@Path(ThemeHandlerWebserviceURIs.RESTSERVICE_GET_ALL_THEMECLOUDS)
	@Produces(MediaType.APPLICATION_JSON)
	public ThemeCloudResult getAllThemeClouds() {

		Configuration webservicesConfiguration = this.getWebservicesConfiguration();
		List<String> disabledWebservices = Arrays.asList(webservicesConfiguration.getStringArray(Constants.CONFIG_PARAM_DISABLED_WEBSERVICES_THEMEHANDLER));

		if (disabledWebservices.isEmpty() ||
			disabledWebservices.contains(Constants.CONFIG_KEYWORD_NONE) ||
			!disabledWebservices.contains(ThemeHandlerWebserviceURIs.RESTSERVICE_GET_ALL_THEMECLOUDS)) {

			ThemeCloudResult result = new ThemeCloudResult();
			result.setThemeClouds(this.business.getThemeClouds());
			return result;
		}
		logger.info("Disabled Webservice "+ThemeHandlerWebserviceURIs.RESTSERVICE_GET_ALL_THEMECLOUDS+" called.");
		return null;
	}

	/**
	 *
	 * @param themeCloudName
	 */
	@DELETE
	@Path(ThemeHandlerWebserviceURIs.RESTSERVICE_DELETE_THEMECLOUD)
	public void deleteThemeCloud(@QueryParam(ThemeHandlerWebserviceURIs.RESTSERVICE_DELETE_THEMECLOUD_PARAM_THEMECLOUD_NAME) String themeCloudName) {

		Configuration webservicesConfiguration = this.getWebservicesConfiguration();
		List<String> disabledWebservices = Arrays.asList(webservicesConfiguration.getStringArray(Constants.CONFIG_PARAM_DISABLED_WEBSERVICES_THEMEHANDLER));

		if (disabledWebservices.isEmpty() ||
			disabledWebservices.contains(Constants.CONFIG_KEYWORD_NONE) ||
			!disabledWebservices.contains(ThemeHandlerWebserviceURIs.RESTSERVICE_DELETE_THEMECLOUD)) {

			this.business.deleteThemeCloudByName(themeCloudName);
		} else {
			logger.info("Disabled Webservice "+ThemeHandlerWebserviceURIs.RESTSERVICE_DELETE_THEMECLOUD+" called.");
		}
	}

	/**
	 *
	 * @param themeCloudName
	 * @param terms
	 * @param userName
	 */
	@PUT
	@Path(ThemeHandlerWebserviceURIs.RESTSERVICE_SAVE_NEW_THEMECLOUD)
	public void saveNewThemeCloud(
		@QueryParam(ThemeHandlerWebserviceURIs.RESTSERVICE_SAVE_NEW_THEMECLOUD_PARAM_THEMECLOUD_NAME) String themeCloudName,
		@QueryParam(ThemeHandlerWebserviceURIs.RESTSERVICE_SAVE_NEW_THEMECLOUD_PARAM_TERMS) String terms,
		@QueryParam(ThemeHandlerWebserviceURIs.RESTSERVICE_SAVE_NEW_THEMECLOUD_PARAM_USERNAME) String userName) {

		Configuration webservicesConfiguration = this.getWebservicesConfiguration();
		List<String> disabledWebservices = Arrays.asList(webservicesConfiguration.getStringArray(Constants.CONFIG_PARAM_DISABLED_WEBSERVICES_THEMEHANDLER));

		if (disabledWebservices.isEmpty() ||
			disabledWebservices.contains(Constants.CONFIG_KEYWORD_NONE) ||
			!disabledWebservices.contains(ThemeHandlerWebserviceURIs.RESTSERVICE_SAVE_NEW_THEMECLOUD)) {

			User user = new User();
			user.setName(userName);

			ThemeCloud themeCloud = new ThemeCloud();
			themeCloud.setThemeCloudName(themeCloudName);
			themeCloud.setUser(user);

			// terms pattern : value,rating,weighting;value,rating,weighting;...;value,rating,weighting

			String[] termTriples = terms.split(";");
			for (String termTriple : termTriples) {
				Term term = new Term();
				String[] termAttr = termTriple.split(",");
				term.setValue(termAttr[0]);
				term.setRating(Integer.parseInt(termAttr[1]));
				term.setWeighting(Float.parseFloat(termAttr[2]));
				themeCloud.getTerms().add(term);
			}

			this.business.saveNewThemeCloud(themeCloud);
		} else {
			logger.info("Disabled Webservice "+ThemeHandlerWebserviceURIs.RESTSERVICE_SAVE_NEW_THEMECLOUD+" called.");
		}
	}
}
