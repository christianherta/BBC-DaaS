package de.bbcdaas.common.beans;

import java.io.Serializable;

/**
 * Bean that represents a term. 
 * @author Robert Illers
 */
public final class Term implements Comparable, Serializable {

	public static final int RATING_NONE = 0;
	public static final int RATING_BAD = 1;
	public static final int RATING_GOOD = 2;
	public static final int RATING_DONTKNOW = 3;
	
	// id of the term in the db
	private int id = 0;
	// another id of the term NOT used in db
	private int secondId = 0;
	// string value of the term
	private String value;
	// how often did the term occur in the context db
    private int localFrequency = 0;
	// how often did the term occur in the termLexicon db
    private int totalFrequency = 0;
	// storage for computed syntag values for this term
	private float syntag = 0;
	// user rating for this term
	private int rating = RATING_NONE;
	// if true term is added by user and not already a top related term
	private boolean added = false;
	// if true term is added by the system in an existing term group automatically
	private boolean fake = false;
	// user generated weighting
	private float weighting = 0;
	
    /**
     * constructor
     */
    public Term() {}
    
    /**
     * constructor
     * @param id 
     */
    public Term(int id) {
        this.id = id;
    }

	/**
	 * 
	 * @return 
	 */
	public int getSecondId() {
		return secondId;
	}

	/**
	 * 
	 * @param secondId 
	 */
	public void setSecondId(int secondId) {
		this.secondId = secondId;
	}
	
    /**
     * constructor
     * @param value 
     */
	public Term(String value) {
		this.value = value;
	}
	
    /**
     * constructor
     * @param value
     * @param id 
     */
	public Term(String value, int id) {
		this.id = id;
		this.value = value;
	}
	
    /**
     * setter of the term id
     * @param id 
     */
	public void setId(int id) {
		this.id = id;
	}
    
    /**
     * getter of the term id
     * @return termID
     */
	public int getId() {
		return id;
	}

    /**
     * setter of the term value
     * @param value 
     */
	public void setValue(String value) {
		this.value = value;
	}
    
    /**
     * getter of the term value
     * @return term value
     */
	public String getValue() {
		return value;
	}
    
    /**
     * setter for the term local frequencey
     * @param localFrequency 
     */
    public void setLocalFrequency(int localFrequency) {
        this.localFrequency = localFrequency;
    }
    
    /**
     * getter for the term local frequency
     * @return local term frequency
     */
    public int getLocalFrequency() {
        return localFrequency;
    }
	
	/**
     * setter for the term total frequencey
     * @param totalFrequency 
     */
    public void setTotalFrequency(int totalFrequency) {
        this.totalFrequency = totalFrequency;
    }
    
    /**
     * getter for the term total frequency
     * @return total term frequency
     */
    public int getTotalFrequency() {
        return totalFrequency;
    }

	/**
	 * 
	 * @return 
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
     * compares the term with another object. if the argument object is a term that has the same
     * term value, the terms are equal
     * @param obj
     * @return true if terms are equal
     */
	@Override
	public boolean equals(Object obj) {
		
		if (obj != null && this.getValue() != null && obj instanceof Term) {
			Term t = (Term)obj;
			return t.getValue() != null && t.getValue().equalsIgnoreCase(this.getValue());
		}
		return false;
	}

	/**
	 * Rating is a general value set by the user that puts the term in some sort
	 * of group. Rating can be set more detailed by using @see #Weighting .
	 * @return rating
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * 
	 * @param rating 
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}

	/**
	 * Weighting is is a value set by the user that indicated how good this term
	 * is. Weighting is a subdivision of @see #Rating .
	 * @return 
	 */
	public float getWeighting() {
		return weighting;
	}

	/**
	 * 
	 * @param weighting 
	 */
	public void setWeighting(float weighting) {
		this.weighting = weighting;
	}
	
    /**
     * generates a hash code using the terms value
     * @return hash code
     */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 73 * hash + (this.value != null ? this.value.hashCode() : 0);
		return hash;
	}
    
    /**
     * two terms are equal if they have the same value (minimum criterion)
     * @param term the term to compare to
     * @return the value 0 if term values are equal; a value less than 0 if the term value is lexicographically
     * less than the value of the term argument; and a value greater than 0 if the term value is lexicographically greater than the value of the term argument.
     */
	@Override
    public int compareTo(Object o) {
        return this.value.compareTo(((Term)o).getValue());
    }
    
    /**
     * a term object is valid, if it has a value and an id and no fake term
     * @return true if term is valid
     */
    public boolean isValid() {
        return this.id != 0 && this.value != null && !this.fake;
    }

	/**
	 * Is true if this terms was added by the user manually.
	 * @return 
	 */
	public boolean isAdded() {
		return added;
	}

	/**
	 * 
	 * @param added 
	 */
	public void setAdded(boolean added) {
		this.added = added;
	}

	/**
	 * Fake is true if this term is not in the right context and only added
	 * to test if users do not rate randomly terms.
	 * @return 
	 */
	public boolean isFake() {
		return fake;
	}

	/**
	 * 
	 * @param fake 
	 */
	public void setFake(boolean fake) {
		this.fake = fake;
	}
}