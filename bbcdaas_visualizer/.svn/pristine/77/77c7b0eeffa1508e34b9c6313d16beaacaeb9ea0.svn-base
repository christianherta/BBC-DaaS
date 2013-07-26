package de.bbcdaas.visualizer.control.search;

import de.bbcdaas.common.beans.TagCloudItem;
import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.common.util.TagCloudItemComparator;
import de.bbcdaas.taghandler.constants.ProcessingConstants;
import de.bbcdaas.taghandler.dao.TagHandlerDao;
import de.bbcdaas.taghandler.dao.TermLexiconDao;
import de.bbcdaas.visualizer.constants.VisualizerConstants;
import de.bbcdaas.visualizer.control.BaseController;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.ModelAndView;
/**
 *
 * @author Robert Illers
 */
public class PerformSearch extends BaseController {

	private TermLexiconDao taghandler_termLexiconDao;
        private TagHandlerDao taghandlerDao;
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@Override
    public ModelAndView handleRequestImpl(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
		String searchType = request.getParameter(VisualizerConstants.KEY_SEARCH_TYPE);
		String searchString = request.getParameter(VisualizerConstants.KEY_SEARCH_STRING);
		
		this.taghandler_termLexiconDao = (TermLexiconDao)new ClassPathXmlApplicationContext("applicationContext.xml").
                    getBean("taghandler_termlexiconDao");
                this.taghandlerDao = (TagHandlerDao)new ClassPathXmlApplicationContext("applicationContext.xml").
                    getBean("taghandlerDao");
                
		if (searchType != null && searchString != null) {
			
			searchString = searchString.trim();
			searchString = searchString.toLowerCase();
			searchString = URLDecoder.decode(searchString, "UTF-8");
			request.getSession().setAttribute(VisualizerConstants.KEY_SEARCH_TYPE, searchType);
			
			// search for top related terms
			if (searchType.equals(VisualizerConstants.SEARCH_TYPE_TOP_RELATED_TERMS)) {
				List<TagCloudItem> result;
				result = this.searchForTopRelatedTerms(searchString, 10, VisualizerConstants.TAGCLOUD_MAX_FONTSIZE);
				request.getSession().setAttribute("noSearchResult", result.isEmpty());
				Collections.sort(result, new TagCloudItemComparator(2));
				request.getSession().setAttribute("searchResult", result);
				List<TagCloudItem> tagCloud = new ArrayList<TagCloudItem>(result);
				Collections.sort(tagCloud, new TagCloudItemComparator(1));
				request.getSession().setAttribute("tagCloud", tagCloud);
			} else

			// search for term suggestions
			if (searchType.equals(VisualizerConstants.SEARCH_TYPE_TERM_SUGGESTION)){
				List<Term> result;
				result = this.searchForTerms(searchString, 10);
				request.getSession().setAttribute("noSearchResult", result.isEmpty());
				request.getSession().setAttribute("suggestionList", result);
			} 
		} else {
			logger.error("parameter null");
		}
		
        return new ModelAndView(VisualizerConstants.AJAX_SHOW_SEARCH_RESULT_JSP);
    } 
        
    /**
    * 
    * @param termFragment
    * @param maxResults
    * @return 
    */
    private List<Term> searchForTerms(String termFragment, int maxResults) {

        List<String> termValues = new ArrayList<String>();
		logger.debug("termFragment: "+termFragment);
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
		logger.debug("Found terms: "+terms.toString());
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
					// get a selection of toprelatedTerms, randomly selected
                    topRelatedTerms = taghandlerDao.getTopRelatedTerms(terms.get(0).getId(), maxResults, minSyntag, false, false);
					logger.debug("Found random top related terms: ");
                    for (Term topRelatedTerm : topRelatedTerms) {
                            Term lexiconTerm = taghandler_termLexiconDao.getTerm(topRelatedTerm.getId(), false);
                            if (lexiconTerm == null) {
                                    logger.error("Term with ID='"+topRelatedTerm.getId()+"' is not in taghandler termLexicon.");
                            } else {
                                    topRelatedTerm.setValue(lexiconTerm.getValue());
									logger.debug(lexiconTerm.getValue());
                            }
                    }
					
            } else {
				logger.debug("No term found for term fragment '"+searchString+"'");
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
