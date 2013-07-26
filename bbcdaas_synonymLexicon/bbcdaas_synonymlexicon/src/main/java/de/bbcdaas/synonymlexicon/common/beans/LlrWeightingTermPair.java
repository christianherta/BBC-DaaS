package de.bbcdaas.synonymlexicon.common.beans;

/**
 *
 * @author Robert Illers
 */
public class LlrWeightingTermPair implements Comparable {
	
	private double llrWeighting;
	private String term;
	
	public LlrWeightingTermPair(double llrWeighting, String term) {
		
		this.llrWeighting = llrWeighting;
		this.term = term;
	}

	@Override
	public int compareTo(Object o) {
		
		LlrWeightingTermPair llrO = (LlrWeightingTermPair)o;
		if (llrO.getLlrWeighting() == this.llrWeighting) {
			return 0;
		} else if (llrO.getLlrWeighting() > this.getLlrWeighting()) {
			return -1;
		} else {
			return 1;
		}
	}

	public double getLlrWeighting() {
		return llrWeighting;
	}

	public String getTerm() {
		return term;
	}
}
