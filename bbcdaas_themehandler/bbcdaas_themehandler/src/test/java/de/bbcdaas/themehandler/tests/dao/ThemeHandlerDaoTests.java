package de.bbcdaas.themehandler.tests.dao;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.ThemeCloud;
import de.bbcdaas.common.beans.User;
import de.bbcdaas.themehandler.business.ThemeHandlerBusiness;
import de.bbcdaas.themehandler.dao.impl.jpa.ThemeHandlerDaoImpl;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;

/**
 *
 * @author Robert Illers
 */
public class ThemeHandlerDaoTests {

	private Logger logger = Logger.getLogger(this.getClass());
	private ThemeHandlerBusiness business;

	/**
	 * Prepares the test case.
	 * @throws Exception
	 */
    @Before
    public void setUp() throws Exception {
		this.business = new ThemeHandlerBusiness(new ThemeHandlerDaoImpl());
	}

	/**
	 *
	 * @throws Exception
	 */
	//@Test
    public void test1() throws Exception {

		logger.debug("Test1:");
		User user = new User();
		user.setId(1);
		user.setName("Test1User");
		user.setRole(1);

		List<Term> terms = new ArrayList<Term>();

		Term term1 = new Term("java");
		term1.setRating(1);
		term1.setWeighting(1.0f);
		Term term2 = new Term("c++");
		term2.setRating(1);
		term2.setWeighting(1.1f);
		Term term3 = new Term("Blumen");
		term3.setRating(3);

		terms.add(term1);
		terms.add(term2);
		terms.add(term3);

		ThemeCloud themeCloud = new ThemeCloud();
		themeCloud.setThemeCloudName("Test1ThemeCloud");
		themeCloud.setUser(user);
		themeCloud.setTerms(terms);
		business.saveNewThemeCloud(themeCloud);

		List<ThemeCloud> themeClouds;

		themeClouds = business.getThemeClouds();
		assertTrue(!themeClouds.isEmpty());

		for (ThemeCloud aThemeCloud : themeClouds) {
			logger.info("aThemeCloud.getThemeCloudName(): "+aThemeCloud.getThemeCloudName());
			logger.info("aThemeCloud.getUser().getName(): "+aThemeCloud.getUser().getName());
			logger.info("---terms:---");
			for (Term aTerm : aThemeCloud.getTerms()) {
				logger.info("aTerm.getValue(): "+aTerm.getValue());
				logger.info("aTerm.getRating(): "+aTerm.getRating());
				logger.info("aTerm.getWeighting(): "+aTerm.getWeighting());
			}
		}

		for (ThemeCloud aThemeCloud : themeClouds) {
			business.deleteThemeCloudByName(aThemeCloud.getThemeCloudName());
		}

		themeClouds = business.getThemeClouds();
		assertTrue(themeClouds.isEmpty());
	}

	/**
	 *
	 * @throws Exception
	 */
	@After
    public void tearDown() throws Exception {

	}
}
