package de.bbcdaas.taghandler.compute.relation.syntagmatic;

import de.bbcdaas.common.beans.RankListEntry;
import de.bbcdaas.common.beans.Term;
import de.bbcdaas.taghandler.compute.relation.RelationCompute;
import de.bbcdaas.taghandler.compute.score.RankListEntryScorer;
import de.bbcdaas.taghandler.exception.ProcessException;
import java.util.List;
/**
 * Interface for all syntagmatic term relation computing algorithms.
 * @author Christian Herta
 */
public interface SyntagmaticRelationCompute extends RelationCompute {
	
	/**
	 * 
	 * @return A
	 */
	public float getA();

	/**
	 * 
	 * @param a 
	 */
	public void setA(float a);

	/**
	 * 
	 * @return B
	 */
	public float getB();

	/**
	 * 
	 * @param b 
	 */
	public void setB(float b);
	
    /**
	 * injected by spring
	 * @param rankListEntryScorer 
	 */
	public void setRankListEntryScorer(RankListEntryScorer rankListEntryScorer);
    
    /**
	 * injected by spring
	 * @param minSyntagmaticValue 
	 */
	public void setMinSyntagmaticValue(float minSyntagmaticValue);
    
    /**
     * 
     * @return 
     */
    public float getMinSyntagmaticValue();
    
    /**
	 * injected by spring
	 * @param syntagmaticEntityTermFactor 
	 */
	public void setSyntagmaticEntityTermFactor(float syntagmaticEntityTermFactor);
    
    /**
     * 
     * @return 
     */
    public float getSyntagmaticEntityTermFactor();
    
    /**
	 * 
	 * @param offset
	 * @param readStep
	 * @param commitStep
	 * @return 
	 */
	public int computeSyntagmaticRelations(int offset, int readStep, int commitStep);
    
	/**
     * 
     * @param a
     * @param b
     * @param ab
     * @return 
     */
	public float computeSyntagmaticRelation(long a, long b, long ab, long nbEntities);
    
    /**
	 * 
	 */
    public void computeAllTopSyntagmaticTerms() throws ProcessException;
	
	/**
	 * 
	 * @param terms
	 * @param minSyntag
	 * @return a list of rank list entries
	 */
	public List<RankListEntry> computeFieldTopSyntagmaticTerms(List<Term> terms, float minSyntag,
		float syntagmaticEntityTermFactor, float a, float b) throws ProcessException;
}
