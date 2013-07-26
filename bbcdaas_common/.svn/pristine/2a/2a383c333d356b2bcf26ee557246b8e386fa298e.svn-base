package de.bbcdaas.common.beans;

import java.io.Serializable;

/**
 * Represents a row in the termMatrix. Only the IDs of the terms are stored in the termMatrix directly.
 * @author Christian Herta
 * @author Robert Illers
 */
public final class TermMatrixEntry implements Serializable {
		
	// first term, its id is smaller than the second one
	private Term term1;
	// second term, its id is bigger than the first one
	private Term term2;
	// determines how often these two terms occur together in a cloud
	private int coocurrence;
	private float syntag;

	/**
	 * 
	 * @return int 
	 */
	public int getCoocurrence() {
		return coocurrence;
	}

	/**
	 * 
	 * @param coocurrence 
	 */
	public void setCoocurrence(int coocurrence) {
		this.coocurrence = coocurrence;
	}

	/**
	 * 
	 * @return int 
	 */
	public float getSyntag() {
		return syntag;
	}

	/**
	 * 
	 * @param syntag 
	 */
	public void setSyntag(float syntag) {
		this.syntag = syntag;
	}

    /**
     * 
     * @return 
     */
    public Term getTerm1() {
        return term1;
    }
    
    /**
     * 
     * @param term1 
     */
    public void setTerm1(Term term1) {
        this.term1 = term1;
    }
    
    /**
     * 
     * @return 
     */
    public Term getTerm2() {
        return term2;
    }
    
    /**
     * 
     * @param term2 
     */
    public void setTerm2(Term term2) {
        this.term2 = term2;
    }
    
	/**
	 * 
	 * @return
	 */
	public int getTermId1() {
        if (term1 != null) {
            return term1.getId();
        }
        return 0;
	}

	/**
	 * 
	 * @param termId1 
	 */
	public void setTermId1(int termId1) {
		if (term1 == null) {
            term1 = new Term();
        }
        term1.setId(termId1);
	}

	/**
	 * 
	 * @return
	 */
	public int getTermId2() {
		if (term2 != null) {
            return term2.getId();
        }
        return 0;
	}

	/**
	 * 
	 * @param termId2 
	 */
	public void setTermId2(int termId2) {
		if (term2 == null) {
            term2 = new Term();
        }
        term2.setId(termId2);
	}
		
}
