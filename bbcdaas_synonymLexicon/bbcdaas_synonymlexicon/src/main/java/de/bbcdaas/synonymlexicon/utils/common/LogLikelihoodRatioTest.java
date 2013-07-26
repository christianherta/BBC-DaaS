package de.bbcdaas.synonymlexicon.utils.common;
/**
 * Implementation of Ted Dunning's 'Acurate Methods for the
 * Statistics of Surprise and Coincidence'
 * @author Christian Herta
 */
public final class LogLikelihoodRatioTest {
	
	private LogLikelihoodRatioTest() {};
	
    /**
     *
     * @param termFreq1 häufigkeit von a
     * @param termFreq2 häufigkeit von b
     * @param occurredTogether occured together
     * @param total anzahl term-clouds
     * @return
     */
    public static double computeSyntagmaticRelation(double termFreq1, double termFreq2,
		double occurredTogether, double total) throws IllegalArgumentException {

		double k1 = occurredTogether;
		double k2 = termFreq1 - occurredTogether;
		double n1 = termFreq2;
		double n2 = total - termFreq2;
		double p1 = k1/n1;
		double p2 = k2/n2;
		double llrt;
		try {
			llrt = logLikelihoodRatioTest(k1,n1,p1,k2,n2,p2);
		} catch(IllegalArgumentException ex) {
			throw new IllegalArgumentException("Error Calculating llr, termFreq1="+
				termFreq1+" termFreq2="+termFreq2+" occurredTogether="+occurredTogether+" total="+total, ex);
		}
		if (p1 < p2) {
			return (-1 * llrt);
		}
		return llrt;
	}

    /**
     *
     * @param k1
     * @param n1
     * @param p1
     * @param k2
     * @param n2
     * @param p2
     * @return
     */
    private static double logLikelihoodRatioTest(double k1, double n1, double p1,
		double k2, double n2, double p2) throws IllegalArgumentException {

		double l1 = 0, l2 = 0, l3 = 0, l4 = 0;
		double p = (k1 + k2)/(n1 + n2);

		try {
			l1 = LR (k1,n1,p1);
		} catch(IllegalArgumentException ex) {
			throw new IllegalArgumentException("Error calculating l1", ex);
		}
		try {
			l2 = LR (k2,n2,p2);
		} catch(IllegalArgumentException ex) {
			throw new IllegalArgumentException("Error calculating l2", ex);
		}
		try {
			l3 = LR (k1,n1,p);
		} catch(IllegalArgumentException ex) {
			throw new IllegalArgumentException("Error calculating l3", ex);
		}
		try {
			l4 = LR (k2,n2,p);
		} catch(IllegalArgumentException ex) {
			throw new IllegalArgumentException("Error calculating l4", ex);
		}

		double llr = 2 * ( l1 + l2 - l3 -l4);
		return llr;
	}

    /**
     *
     * @param k
     * @param n
     * @param p (0..1) percentual 
     * @return
     */
    private static double LR(double k, double n, double p) throws IllegalArgumentException {

		if (n == k) {
			return k * Math.log(p);
		}
		if (k == 0) {
			return n * Math.log(1 - p);
		}

		Double r =  k * Math.log(p) + (n-k) * Math.log(1-p);

		if (r.equals(Double.NaN)) {
			throw new IllegalArgumentException("In LR with k=" +k+" n="+n+" p="+p);
		}
		return r;
	}
}