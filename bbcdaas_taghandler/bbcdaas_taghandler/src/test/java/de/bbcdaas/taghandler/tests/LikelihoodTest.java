package de.bbcdaas.taghandler.tests;

import de.bbcdaas.taghandler.compute.relation.syntagmatic.SimpleSyntagmaticRelationCompute;
import de.bbcdaas.taghandler.compute.statistic.syntagmatic.LogLikelihoodRatioTest;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
/**
 *
 *
 * @author Christian Herta
 */
public class LikelihoodTest extends TestCase {

	private Logger logger = Logger.getLogger(LikelihoodTest.class);

	public void testLogLikelihoodRatio() {

		logger.info("Test: testLogLikelihoodRatio()");
		SimpleSyntagmaticRelationCompute syntagCompute = new SimpleSyntagmaticRelationCompute();
        syntagCompute.setSyntagmaticStatisticalTest(new LogLikelihoodRatioTest());
		//original values from dunning paper
		int total = 29114 + 2442 + 111 + 110;
		double lr1 = syntagCompute.computeSyntagmaticRelation(2552l, 221l, 110l, total);
		double lr2 = syntagCompute.computeSyntagmaticRelation(221l, 2552l,110l, total);
		assertEquals(lr1, 270.72d, 0.1d);
		assertEquals(lr1, lr2, 0.001d);
	}
}