package de.bbcdaas.common.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a term cloud that contains terms of a specific field and the topSyntagmaticTerms of this terms.
 * The scores are related to the topSyntagmaticTerms, the first score is the score of the first topSyntagmaticTerm.
 * @author Robert Illers
 */
public final class TermCloud implements Serializable {
    
    private int entityID;
	private String entityName;
	private int fieldType;
	private int fieldID;
    private List<Term> terms = new ArrayList<Term>();
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
	public int getFieldID() {
		return fieldID;
	}

	/**
	 * 
	 * @param fieldID 
	 */
	public void setFieldID(int fieldID) {
		this.fieldID = fieldID;
	}
	
	/**
	 * 
	 * @return 
	 */
	public int getFieldType() {
		return fieldType;
	}

	/**
	 * 
	 * @param fieldType 
	 */
	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}
	
	/**
	 * 
	 * @return 
	 */
    public int getEntityID() {
        return entityID;
    }

	/**
	 * 
	 * @param id 
	 */
    public void setEntityID(int id) {
        this.entityID = id;
    }

	/**
	 * 
	 * @return 
	 */
    public String getEntityName() {
        return entityName;
    }

	/**
	 * 
	 * @param name 
	 */
    public void setEntityName(String name) {
        this.entityName = name;
    }

    /**
	 * 
	 * @return List<String>
	 */
	public List<Term> getTerms() {
		return terms;
	}
	
    /**
     * 
     * @return 
     */
    public List<String> getTermValues() {
        List<String> termValues = new ArrayList<String>();
        for (Term term : terms) {
            termValues.add(term.getValue());
        }
        return termValues;
    }
    
	/**
	 * 
	 * @param terms 
	 */
	public void setTerms(List<Term> terms) {
		this.terms = terms;
	}
	
    /**
     * 
     * @param term 
     */
    public void addTerm(Term term) {
        this.terms.add(term);
    }
    
	/**
	 * 
	 * @param term 
	 */
	public void addTerm(String value) {
		terms.add(new Term(value));
	}
	
	/**
	 * 
	 * @param term
	 * @param id 
	 */
	public void addTerm(String term, int id) {
		terms.add(new Term(term, id));
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
        this.terms.clear();
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
     * 
     */
    public void clearSyntagmaticTerms() {
        this.syntagmaticTerms.clear();
    }
}
