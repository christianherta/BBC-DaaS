package de.bbcdaas.taghandler.compute.relation;

import de.bbcdaas.taghandler.exception.ProcessException;

/**
 * Interface for all term relation computing algorithms.
 * @author Christian Herta
 */
public interface RelationCompute {
    
	/**
	 * Defines a min value for the frequency a term occures in source fields.
	 * @param minTermFrequency 
	 */
	public void setMinTermFrequency(int minTermFrequency);
	
    /**
     * Setter for the minFrequency, @see #setMinTermFrequency
     * @return minTermFrequency
     */
    public int getMinTermFrequency();
    
	/**
	 * Defines how much percent of the found top relatedTerms should be used for
	 * futher computations.
	 * @param maxPercentageTopTerms 
	 */
	public void setMaxPercentageTopTerms(float maxPercentageTopTerms);
	
    /**
     * 
     * @return maxPercentageTopTerms
     */
    public float getMaxPercentageTopTerms();
    
	/**
	 * 
	 * @param minNbCorrelatedTerms 
	 */
	public void setMinNbCorrelatedTerms(int minNbCorrelatedTerms);
	
    /**
     * 
     * @return minNbCorrelatedTerms
     */
    public int getMinNbCorrelatedTerms();
    
	/**
	 * 
	 * @param maxTopRelatedTerms
	 */
	public void setMaxTopRelatedTerms(int maxTopRelatedTerms);
	
    /**
     * 
     * @return maxTopRelatedTerms
     */
    public int getMaxTopRelatedTerms();
    
	/**
	 * Computes the terms who are related to a specific term.
	 */
	public void computeTopRelatedTerms() throws ProcessException;
}