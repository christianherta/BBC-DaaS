package de.bbcdaas.common.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Represents the topRelatedTerms of a term
 * @author Christian Herta
 * @author Robert Illers
 */
public final class TermToTermsGroup implements Serializable {
    
    private Term term;
	private boolean saved = false;
    private List<Term> relatedTerms = new ArrayList<Term>();

    /**
     * 
     * @return 
     */
    public Term getTerm() {
        return term;
    }

    /**
     * 
     * @param term 
     */
    public void setTerm(Term term) {
        this.term = term;
    }

    /**
     * 
     * @return 
     */
    public List<Term> getRelatedTerms() {
        return relatedTerms;
    }

    /**
     * 
     * @param topRelatedTerms 
     */
    public void setRelatedTerms(List<Term> relatedTerms) {
        this.relatedTerms = relatedTerms;
    }
    
    /**
     * 
     * @return 
     */
    public List<String> getRelatedTermValues() {
        List<String> termValues = new ArrayList<String>();
        for (Term relatedTerm : this.relatedTerms) {
            termValues.add(relatedTerm.getValue());
        }
        return termValues;
    }

	/**
	 * 
	 * @return 
	 */
	public boolean isSaved() {
		return saved;
	}

	/**
	 * 
	 * @param saved 
	 */
	public void setSaved(boolean saved) {
		this.saved = saved;
	}
}
