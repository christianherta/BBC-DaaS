package de.bbcdaas.webservices.services.taghandler;

import de.bbcdaas.common.beans.RankListEntry;
import de.bbcdaas.common.beans.TagCloudItem;
import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.taghandler.compute.relation.syntagmatic.SyntagmaticRelationCompute;
import de.bbcdaas.taghandler.constants.ProcessingConstants;
import de.bbcdaas.taghandler.dao.TagHandlerDao;
import de.bbcdaas.taghandler.dao.TermLexiconDao;
import de.bbcdaas.taghandler.exception.ProcessException;
import de.bbcdaas.webservices.api.taghandler.TagHandlerWebserviceURIs;
import de.bbcdaas.webservices.api.taghandler.beans.EntitiesResult;
import de.bbcdaas.webservices.api.taghandler.beans.SyntagCloudResult;
import de.bbcdaas.webservices.api.taghandler.beans.TRTsResult;
import de.bbcdaas.webservices.api.taghandler.beans.TermsResult;
import de.bbcdaas.webservices.constants.Constants;
import de.bbcdaas.webservices.services.RestServices;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.commons.configuration.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * RESTful Webservices of the TagHandler.
 * @author Robert Illers
 */
@Path(TagHandlerWebserviceURIs.RESTSERVICE_BASE)
public final class TagHandlerRestServices extends RestServices {

	private TagHandlerDao taghandlerDao;
	private TermLexiconDao taghandler_termLexiconDao;
	private SyntagmaticRelationCompute syntagmaticRelationCompute;

	public TagHandlerRestServices() {

            ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
            this.taghandlerDao = (TagHandlerDao)classPathXmlApplicationContext.getBean("taghandlerDao");
            this.taghandler_termLexiconDao = (TermLexiconDao)classPathXmlApplicationContext.getBean("taghandler_termlexiconDao");
            this.syntagmaticRelationCompute = (SyntagmaticRelationCompute)classPathXmlApplicationContext.getBean("syntagmaticRelationCompute");
	}

	/**
	 * This method produces a json String capsulated in a javascript method for
	 * Jsonp calls with callback method jsonp()
	 * @param inputTermValueFragment
	 * @return String containing json embedded in a method
	 */
	@GET
	@Path(TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TERMS_JSONP)
	@Produces(MediaType.TEXT_PLAIN)
	public String searchForTermsJSONP(@QueryParam(TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TERMS_JSONP_PARAM_TERM) String inputTermValueFragment) {

		Configuration webservicesConfiguration = this.getWebservicesConfiguration();
		List<String> disabledWebservices = Arrays.asList(webservicesConfiguration.getStringArray(Constants.CONFIG_PARAM_DISABLED_WEBSERVICES_TAGHANDLER));

		if (disabledWebservices.isEmpty() ||
			disabledWebservices.contains(Constants.CONFIG_KEYWORD_NONE) ||
			!disabledWebservices.contains(TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TERMS_JSONP)) {

			StringBuilder jsonp = new StringBuilder();
			jsonp.append("jsonp(");

			List<Term> terms = this.searchForTerms(inputTermValueFragment, 10);

			jsonp.append("{\"result\":[");
			int i = 0;
			for (Term term : terms) {
					if (i != 0) {
							jsonp.append(",");
					}
					jsonp.append("\"").append(term.getValue()).append("\"");
					i++;
			}
			jsonp.append("]}");

			jsonp.append(")");
			return jsonp.toString();
		}
		logger.info("Disabled Webservice "+TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TERMS_JSONP+" called.");
		return null;
	}

	/**
	 * gets a list of terms with a value beginning with inputTermValueFragment.
	 * @param inputTermValueFragment
	 * @return
	 */
	@GET
	@Path(TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TERMS)
	@Produces(MediaType.APPLICATION_JSON)
	public TermsResult searchForTerms(@QueryParam(TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TERMS_PARAM_TERM) String inputTermValueFragment) {

		Configuration webservicesConfiguration = this.getWebservicesConfiguration();
		List<String> disabledWebservices = Arrays.asList(webservicesConfiguration.getStringArray(Constants.CONFIG_PARAM_DISABLED_WEBSERVICES_TAGHANDLER));

		if (disabledWebservices.isEmpty() ||
			disabledWebservices.contains(Constants.CONFIG_KEYWORD_NONE) ||
			!disabledWebservices.contains(TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TERMS)) {

			TermsResult searchResult = new TermsResult();
			searchResult.setTermValue(inputTermValueFragment);
			searchResult.setTerms(this.searchForTerms(inputTermValueFragment, 10));
			return searchResult;
		}
		logger.info("Disabled Webservice "+TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TERMS+" called.");
		return null;
	}

	/**
	 * Gets a term object from the term lexicon or an empty list if term could not be found.
	 * @param termValue
	 * @return TermsResult
	 */
	@GET
	@Path(TagHandlerWebserviceURIs.RESTSERVICE_GET_TERM)
	@Produces(MediaType.APPLICATION_JSON)
	public TermsResult getTerm(@QueryParam(TagHandlerWebserviceURIs.RESTSERVICE_GET_TERM_PARAM_TERMVALUE) String termValue) {

		Configuration webservicesConfiguration = this.getWebservicesConfiguration();
		List<String> disabledWebservices = Arrays.asList(webservicesConfiguration.getStringArray(Constants.CONFIG_PARAM_DISABLED_WEBSERVICES_TAGHANDLER));

		if (disabledWebservices.isEmpty() ||
			disabledWebservices.contains(Constants.CONFIG_KEYWORD_NONE) ||
			!disabledWebservices.contains(TagHandlerWebserviceURIs.RESTSERVICE_GET_TERM)) {

			TermsResult result = new TermsResult();
			try {
				Term term = this.taghandler_termLexiconDao.getTerm(termValue);
				result.setTermValue(termValue);
				if (term != null) {
					result.getTerms().add(term);
				}
			} catch(DaoException e) {
				logger.error(e);
			}
			return result;
		}
		logger.info("Disabled Webservice "+TagHandlerWebserviceURIs.RESTSERVICE_GET_TERM+" called.");
		return null;
	}

	/**
	 *
	 * @param terms_String
	 * @return EntitiesResult
	 */
	@GET
	@Path(TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_ENTITY)
	@Produces(MediaType.APPLICATION_JSON)
	public EntitiesResult searchForEntities(@QueryParam(TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_ENTITY_PARAM_TERMS) String terms_String) {

		Configuration webservicesConfiguration = this.getWebservicesConfiguration();
		List<String> disabledWebservices = Arrays.asList(webservicesConfiguration.getStringArray(Constants.CONFIG_PARAM_DISABLED_WEBSERVICES_TAGHANDLER));

		if (disabledWebservices.isEmpty() ||
			disabledWebservices.contains(Constants.CONFIG_KEYWORD_NONE) ||
			!disabledWebservices.contains(TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_ENTITY)) {

			String[] splitted = terms_String.split(",");
			List<Term> termParams = new ArrayList<Term>();

			for (int i = 0; i < splitted.length;i++) {
				Term term = new Term(splitted[i]);
				termParams.add(term);
			}

			List<Term> terms = new ArrayList<Term>();
			try {
				for (Term term : termParams) {
					Term foundTerm = taghandler_termLexiconDao.getTerm(term.getValue());
					if (foundTerm != null) {
						terms.add(foundTerm);
					}
				}
			} catch(DaoException e) {
				logger.error(e);
			}

			EntitiesResult searchResult = new EntitiesResult();
			List<Entity> entities = new ArrayList<Entity>();

					try {
						// dont search without restrictive terms (too many results)
						if (terms != null && !terms.isEmpty()) {
								entities = taghandlerDao.getEntitiesByFieldTerms(terms);
						}
			} catch(DaoException e) {
						logger.error(e.getMessage());
			}

			searchResult.setTest(entities);
			return searchResult;
		}
		logger.info("Disabled Webservice "+TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_ENTITY+" called.");
		return null;
	}

	/**
	 * Returns the syntagmatic tag cloud to a given tag cloud.
	 * @param themeCloudTermIDs
	 * @param minSyntag_String
	 * @param syntagmaticEntityTermFactor_String
	 * @param a_String
	 * @param b_String
	 * @return SyntagCloudResult
	 */
	@GET
	@Path(TagHandlerWebserviceURIs.RESTSERVICE_GET_SYNTAG_TERM_CLOUD)
	@Produces(MediaType.APPLICATION_JSON)
	public SyntagCloudResult getSyntagTermCloud(
		@QueryParam(TagHandlerWebserviceURIs.RESTSERVICE_GET_SYNTAG_TERM_CLOUD_PARAM_THEMECLOUDTERMS) String themeCloudTermIDs,
		@QueryParam(TagHandlerWebserviceURIs.RESTSERVICE_GET_SYNTAG_TERM_CLOUD_PARAM_MINSYNTAG) String minSyntag_String,
		@QueryParam(TagHandlerWebserviceURIs.RESTSERVICE_GET_SYNTAG_TERM_CLOUD_PARAM_SYNTAGMATICENTITYTERMFACTOR) String syntagmaticEntityTermFactor_String,
		@QueryParam(TagHandlerWebserviceURIs.RESTSERVICE_GET_SYNTAG_TERM_CLOUD_PARAM_A) String a_String,
		@QueryParam(TagHandlerWebserviceURIs.RESTSERVICE_GET_SYNTAG_TERM_CLOUD_PARAM_B) String b_String) {

		Configuration webservicesConfiguration = this.getWebservicesConfiguration();
		List<String> disabledWebservices = Arrays.asList(webservicesConfiguration.getStringArray(Constants.CONFIG_PARAM_DISABLED_WEBSERVICES_TAGHANDLER));

		if (disabledWebservices.isEmpty() ||
			disabledWebservices.contains(Constants.CONFIG_KEYWORD_NONE) ||
			!disabledWebservices.contains(TagHandlerWebserviceURIs.RESTSERVICE_GET_SYNTAG_TERM_CLOUD)) {

			String[] splitted = themeCloudTermIDs.split(",");
			List<Term> themeCloudTermsParams = new ArrayList<Term>();
			SyntagCloudResult result = new SyntagCloudResult();

			for (int i = 0; i < splitted.length;i++) {
				Term term = new Term(splitted[i]);
				result.getThemeCloudTermValues().add(splitted[i]);
				themeCloudTermsParams.add(term);
			}

			float minSyntag = Float.parseFloat(minSyntag_String);
			float syntagmaticEntityTermFactor = Float.parseFloat(syntagmaticEntityTermFactor_String);
			float a = Float.parseFloat(a_String);
			float b = Float.parseFloat(b_String);

			List<Term> themeCloudTerms = new ArrayList<Term>();
			List<RankListEntry> syntagTermCloudEntries = null;
			try {
				for (Term term : themeCloudTermsParams) {
					Term foundTerm = taghandler_termLexiconDao.getTerm(term.getValue());
					if (foundTerm != null) {
						themeCloudTerms.add(foundTerm);
					}
				}
				syntagTermCloudEntries = this.syntagmaticRelationCompute.computeFieldTopSyntagmaticTerms(themeCloudTerms,
					minSyntag, syntagmaticEntityTermFactor, a, b);
			} catch(DaoException e) {
				logger.error(e);
			} catch(ProcessException e) {
				logger.error(e);
			}

			if (syntagTermCloudEntries != null) {
				for (RankListEntry rankListEntry : syntagTermCloudEntries) {
					result.getSyntagCloudTerms().add(rankListEntry.getTerm());
				}
			}
			return result;
		}
		logger.info("Disabled Webservice "+TagHandlerWebserviceURIs.RESTSERVICE_GET_SYNTAG_TERM_CLOUD+" called.");
		return null;
	}

	/**
	 * Returns the  tag cloud representing the top related terms of the input
	 * term as json string.
	 * @param inputTermValue
	 * @return TRTsResult
	 */
	@GET
	@Path(TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TRTS_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public TRTsResult searchForTRTsJSON(@QueryParam(TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TRTS_JSON_PARAM_TERM) String inputTermValue) {

		Configuration webservicesConfiguration = this.getWebservicesConfiguration();
		List<String> disabledWebservices = Arrays.asList(webservicesConfiguration.getStringArray(Constants.CONFIG_PARAM_DISABLED_WEBSERVICES_TAGHANDLER));

		if (disabledWebservices.isEmpty() ||
			disabledWebservices.contains(Constants.CONFIG_KEYWORD_NONE) ||
			!disabledWebservices.contains(TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TRTS_JSON)) {

			TRTsResult searchResult = new TRTsResult();
			searchResult.setTerm(inputTermValue);
			List<TagCloudItem> tagCloud = this.searchForTopRelatedTerms(inputTermValue, 10, 1);

			for (TagCloudItem tagCloudItem : tagCloud) {
				searchResult.getTopRelatedTerms().add(tagCloudItem.getValue());
			}
			return searchResult;
		}
		logger.info("Disabled Webservice "+TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TRTS_JSON+" called.");
		return null;
	}

	/**
	 * Returns a html formatted tag cloud representing the top related terms of
	 * the input term.
	 * @param inputTermValue
	 * @return String containing html
	 */
	@GET
	@Path(TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TRTS_HTML)
	@Produces(MediaType.TEXT_HTML)
	public String searchForTRTsHTML(@QueryParam(TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TRTS_HTML_TERM) String inputTermValue) {

		Configuration webservicesConfiguration = this.getWebservicesConfiguration();
		List<String> disabledWebservices = Arrays.asList(webservicesConfiguration.getStringArray(Constants.CONFIG_PARAM_DISABLED_WEBSERVICES_TAGHANDLER));

		if (disabledWebservices.isEmpty() ||
			disabledWebservices.contains(Constants.CONFIG_KEYWORD_NONE) ||
			!disabledWebservices.contains(TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TRTS_HTML)) {

			final float TAGCLOUD_MIN_FONTSIZE = 0.5f;
			// real max fontsize is TAGCLOUD_MAX_FONTSIZE + TAGCLOUD_MIN_FONTSIZE
			final float TAGCLOUD_MAX_FONTSIZE = 3.0f;
			final int MAX_LINE_LENGTH = 20;
			StringBuilder htmlOutput = new StringBuilder();
			htmlOutput.append("<div ");
			htmlOutput.append("style='display:table;background: #303030;padding:10px 5px 10px 5px;'>");
			// get the top related terms
			List<TagCloudItem> tagCloud = this.searchForTopRelatedTerms(inputTermValue, 10, (int)TAGCLOUD_MAX_FONTSIZE);
			Collections.sort(tagCloud);
			int lineLenght = 0;
			for (TagCloudItem tagCloudItem : tagCloud) {

				htmlOutput.append("<a ");
				htmlOutput.append("style='margin-right:5px;text-decoration:none;font-size: ").append(tagCloudItem.getFontSize()+TAGCLOUD_MIN_FONTSIZE).append("em;");
				if (tagCloudItem.getFontSize() <= TAGCLOUD_MAX_FONTSIZE/4) {
					htmlOutput.append("color:#505050;");
				} else
				if (tagCloudItem.getFontSize() <= TAGCLOUD_MAX_FONTSIZE/2 && tagCloudItem.getFontSize() > TAGCLOUD_MAX_FONTSIZE/4) {
					htmlOutput.append("color:#909090;");
				} else
				if (tagCloudItem.getFontSize() <= 3*(TAGCLOUD_MAX_FONTSIZE/4) && tagCloudItem.getFontSize() > TAGCLOUD_MAX_FONTSIZE/2) {
					htmlOutput.append("color:#E0E0E0;");
				} else
				if (tagCloudItem.getFontSize() > 3*(TAGCLOUD_MAX_FONTSIZE/4)) {
					htmlOutput.append("color:#E0E0E0;font-weight: bold;");
				}
				htmlOutput.append("'>");
				htmlOutput.append(tagCloudItem.getValue());
				htmlOutput.append("</a>");
				lineLenght += tagCloudItem.getValue().length();
				if (lineLenght > MAX_LINE_LENGTH) {
					htmlOutput.append("<br/>");
					lineLenght = 0;
				}
			}
			htmlOutput.append("</div>");
			return htmlOutput.toString();
		}
		logger.info("Disabled Webservice "+TagHandlerWebserviceURIs.RESTSERVICE_SEARCH_FOR_TRTS_HTML+" called.");
		return null;
	}

	/*-------------------- privat methods ------------------------------------*/

	/**
	*
	* @param termFragment
	* @param maxResults
	* @return
	*/
	private List<Term> searchForTerms(String termFragment, int maxResults) {

		List<String> termValues = new ArrayList<String>();
		try {
				termValues = this.taghandler_termLexiconDao.getTermValues(termFragment, maxResults);
				this.taghandler_termLexiconDao.closeConnection(true);
		} catch(DaoException e) {
				this.logger.error(e.getMessage());
		}
		List<Term> terms = new ArrayList<Term>();
		for (String value : termValues) {
				terms.add(new Term(value));
		}
		return terms;
	}

	/**
	*
	* @param searchString
	* @param maxResults
	* @param maxTagCloudFontSize
	* @return
	*/
	private List<TagCloudItem> searchForTopRelatedTerms(String searchString, int maxResults, int maxTagCloudFontSize) {

		List <Term> topRelatedTerms = new ArrayList<Term>();
		float minSyntag = ProcessingConstants.DEFAULT_MIN_TOP_TERM_SYNTAG;
		// already sorted
		try {
				taghandler_termLexiconDao.openConnection();
				taghandlerDao.openConnection();
				List<Term> terms = taghandler_termLexiconDao.getTerms(searchString, 1, false);
				if (!terms.isEmpty()) {
						topRelatedTerms = taghandlerDao.getTopRelatedTerms(terms.get(0).getId(), maxResults, minSyntag, false, false);
						for (Term topRelatedTerm : topRelatedTerms) {
								Term lexiconTerm = taghandler_termLexiconDao.getTerm(topRelatedTerm.getId(), false);
								if (lexiconTerm == null) {
										logger.error("Term with ID='"+topRelatedTerm.getId()+"' is not in taghandler termLexicon.");
								} else {
										topRelatedTerm.setValue(lexiconTerm.getValue());
								}
						}
				}
				taghandlerDao.closeConnection(true);
				taghandler_termLexiconDao.closeConnection(true);
		} catch(DaoException e) {
				logger.error(e.getMessage());
		}
		List<TagCloudItem> result = new ArrayList<TagCloudItem>();

		float syntagMin = 1;
		float syntagMax = 1;

		// throw away terms if more than maxResults
		if (topRelatedTerms.size() > maxResults) {
				topRelatedTerms.subList(maxResults-1, topRelatedTerms.size()-1).clear();
		}

		// get syntagMax and syntagMin
		for (Term topRelatedTerm : topRelatedTerms) {
				if (topRelatedTerm.getSyntag() > syntagMax) {
						syntagMax = topRelatedTerm.getSyntag();
				}
				if (topRelatedTerm.getSyntag() < syntagMin) {
						syntagMin = topRelatedTerm.getSyntag();
				}
		}

		/* construct tagCloudItems for each term */
		for (Term topRelatedTerm : topRelatedTerms) {

				TagCloudItem item = new TagCloudItem();
				item.setSyntaq(topRelatedTerm.getSyntag());
				item.setValue(topRelatedTerm.getValue());

				// fontSize standardization
				if (item.getSyntaq() > syntagMin) {
						BigDecimal fontSize = new BigDecimal((float)maxTagCloudFontSize * (item.getSyntaq() - syntagMin) / (syntagMax - syntagMin));
						fontSize = fontSize.setScale( 2, BigDecimal.ROUND_HALF_UP );
						item.setFontSize(fontSize.floatValue());
				} else {
						item.setFontSize(1);
				}

				result.add(item);
		}
		/* /construct tagCloudItems for each term */
		return result;
	}
}
