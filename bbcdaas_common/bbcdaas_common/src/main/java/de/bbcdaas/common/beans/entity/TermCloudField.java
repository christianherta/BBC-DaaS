package de.bbcdaas.common.beans.entity;

import de.bbcdaas.common.beans.Term;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Bean that represents a field of an entity that contains a termCloud.
 * @author Robert Illers
 */
public final class TermCloudField implements Serializable {

    private int fieldID;
	private int fieldType = 1;
    private List<Term> termCloudTerms = new ArrayList<Term>();
    private List<Term> syntagmaticTerms = new ArrayList<Term>();
	private List<Float> syntagTermScores = new ArrayList<Float>();
	
	/**
	 * 
	 * @return 
	 */
	public List<Float> getScores() {
		return syntagTermScores;
	}

	/**
	 * 
	 * @param scores 
	 */
	public void setScores(List<Float> scores) {
		this.syntagTermScores = scores;
	}
    
	/**
	 * 
	 * @param score 
	 */
	public void addScore(Float score) {
		this.syntagTermScores.add(score);
	}
	
	/**
	 * 
	 * @return 
	 */
    public int getID() {
        return fieldID;
    }
    
	/**
	 * 
	 * @param id 
	 */
    public void setID(int id) {
        this.fieldID = id;
    }

	public int getFieldType() {
		return fieldType;
	}

	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}
    
    /**
	 * 
	 * @return List<String>
	 */
	public List<Term> getTerms() {
		return termCloudTerms;
	}
	
    /**
     * 
     * @return 
     */
    public List<String> getTermValues() {
        List<String> termValues = new ArrayList<String>();
        for (Term term : termCloudTerms) {
            termValues.add(term.getValue());
        }
        return termValues;
    }
    
	/**
	 * 
	 * @param terms 
	 */
	public void setTerms(List<Term> terms) {
		this.termCloudTerms = terms;
	}
	
    /**
     * 
     * @param term 
     */
    public void addTerm(Term term) {
        this.termCloudTerms.add(term);
    }
    
	/**
	 * 
	 * @param term 
	 */
	public void addTerm(String value) {
		termCloudTerms.add(new Term(value));
	}
	
	/**
	 * 
	 * @param term
	 * @param id 
	 */
	public void addTerm(String term, int id) {
		termCloudTerms.add(new Term(term, id));
	}
	
    /**
     * 
     * @param values 
     */
	public void addTerms(List<String> values) {
		if (values != null) {
            for (String value : values) {
                addTerm(value);
            }
        }
	}
    
    /**
     * 
     */
    public void clearTerms() {
        this.termCloudTerms.clear();
    }
    
	/**
	 * 
	 * @return List<String>
	 */
	public List<Term> getSyntagmaticTerms() {
		return syntagmaticTerms;
	}
    
    /**
     * 
     * @return 
     */
    public List<String> getSyntagmaticTermValues() {
        List<String> termValues = new ArrayList<String>();
        for (Term term : syntagmaticTerms) {
            termValues.add(term.getValue());
        }
        return termValues;
    }
	
    /**
     * 
     * @param syntagmaticTerms 
     */
	public void setSyntagmaticTerms(List<Term> syntagmaticTerms) {
		this.syntagmaticTerms = syntagmaticTerms;
	}
    
    /**
     * 
     * @param term 
     */
    public void addSyntagmaticTerm(Term term) {
        this.syntagmaticTerms.add(term);
    }
    
    /**
     * 
     * @param value 
     */
    public void addSyntagmaticTerm(String value) {
        this.syntagmaticTerms.add(new Term(value));
    }
    
    /**
     * 
     * @param values 
     */
	public void addSyntagmaticTerms(List<String> values) {
		if (values != null) {
            for (String value : values) {
                addSyntagmaticTerm(value);
            }
        }
	}
    
    /**
     * 
     * @param value
     * @param id 
     */
    public void addSyntagmaticTerm(String value, int id) {
        this.syntagmaticTerms.add(new Term(value, id));
    }
    
    /**
     * Removes all syntagmatic terms from the term cloud field object.
     */
    public void clearSyntagmaticTerms() {
        this.syntagmaticTerms.clear();
    }
    
}
