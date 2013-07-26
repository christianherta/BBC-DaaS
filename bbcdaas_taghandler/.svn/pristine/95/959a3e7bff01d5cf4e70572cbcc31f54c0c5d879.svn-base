package de.bbcdaas.taghandler.compute.statistic.syntagmatic;
/**
 * Implementation of Ted Dunning's 'Acurate Methods for the
 * Statistics of Surprise and Coincidence'
 *
 * @author Christian Herta
 */
public final class LogLikelihoodRatioTest implements SyntagmaticStatisticalTest {
    
    /**
     * 
     * @param a
     * @param b
     * @param ab
     * @param total
     * @return 
     */
	@Override
    public float computeSyntagmaticRelation(long a, long b, long ab, long total) {

		double k1 = (double)ab;
		double k2 = (double)a - (double)ab;
		double n1 = (double)(b); 
		double n2 = (double)(total - b);
		double p1 = k1/n1;
		double p2 = k2/n2;
		float llrt = (float) logLikelihoodRatioTest(k1,n1,p1,k2,n2,p2);
		if (p1 < p2) return (-1 * llrt);
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
    private static double logLikelihoodRatioTest(double k1, double n1, double p1, double k2, double n2, double p2) {
		
		double p = (k1 + k2)/(n1 + n2);
		double L1 = LR (k1,n1,p1);
		double L2 = LR (k2,n2,p2);
		double L3 = LR (k1,n1,p);
		double L4 = LR (k2,n2,p);
		double llr = 2 * ( L1 + L2 - L3 -L4);
		return llr;
	}
    
    /**
     * 
     * @param k
     * @param n
     * @param p
     * @return 
     */
    private static double LR(double k, double n, double p) {
		
		if (n == k) return k * Math.log(p);
		if (k == 0) return n * Math.log(1 - p);
		
		Double r =  k * Math.log(p) + (n-k) * Math.log(1-p);
		
		if (r.equals(Double.NaN)) {
			throw new IllegalArgumentException("In LR with k=" +k + " n="+n +" p="+p);
		}
		return r;
	}
}
