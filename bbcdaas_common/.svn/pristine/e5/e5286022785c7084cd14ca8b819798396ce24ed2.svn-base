package de.bbcdaas.common.beans.document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Bean representing a set of data (could be an entity, a lucene document,
 * a book, ... anything)
 * @author Robert Illers
 */
public class Document implements Comparable<Document>, Serializable {

	private final transient static Logger logger = Logger.getLogger(Document.class);

	// document types
	public static final int DOCUMENT_TYPE_UNDEFINED = 0;
	public static final int DOCUMENT_TYPE_ARTICLE = 1;
	public static final int DOCUMENT_TYPE_REDIRECT = 2;
	public static final int DOCUMENT_TYPE_DISAMBIGUATION = 3;

	// the identifier of the document, different documents have different ids
    private String id;
	// the name of the document representet by an URI
	private String uri;
	// a String representing this document, e.g. a term
	private String surfaceForm;
	// the scores of the document given by document scorer
	private List<Float> scores = new ArrayList<Float>();
	// combined scores calculated from scoring combiners
	private List<Float> combinedScores = new ArrayList<Float>();
    // document fields containing content data
	private List<Field> fields = new ArrayList<Field>();
	// list of surfaceForms that are also in the documents title or in the documents keywords
	// Example: Input surfaceForms: "Programmierung", "Java"
	//			 Title: "Programmierung in Java", current surfaceForm: "Programmierung"
	//           multimatching surfaceForm: "Java"
	private List<String> multimatchingSurfaceForms = new ArrayList<String>();
	// stores how many keywords of the keywords of all documents from another termValue
	// match with the keywords of this document (surfaceForm, <documentID, keywordMatchCount>)
	private Map<String, Map<Integer, Integer>> termDocsKeywordMatches = new HashMap<String, Map<Integer, Integer>>();
	// document type
	private Integer type = DOCUMENT_TYPE_UNDEFINED;
	// document candidate ratings
	private Map<Class, Float> candidateRatings = new LinkedHashMap<Class, Float>();
	private Boolean inserted = false;

    /**
     * Constructor that creates a document with an undefined id
     */
    public Document() {
		this.id = "undefined";
	}

    /**
	* Constructor
	* @param id
	*/
    public Document(int id) {
        this.id = Integer.toString(id);
    }

	/**
	 * Constructor
	 * @param id
	 */
	public Document(String id) {
		this.id = id;
	}

	/**
	 * Sets ratings for the document that indicates if the document is a good
	 * candidate, this values are set by document candidate finders
	 * @return
	 */
	public Map<Class, Float> getCandidateRatings() {
		return candidateRatings;
	}

	/**
	 *
	 * @param candidateFinderClass
	 * @return
	 */
	public Float getCandidateRating(Class candidateFinderClass) {
		return candidateRatings.get(candidateFinderClass);
	}

	/**
	 *
	 * @param candidateRatings
	 */
	public void addCandidateRating(Class candidateFinderClass, float candidateRating) {
		this.candidateRatings.put(candidateFinderClass, candidateRating);
	}

    /**
     *
     * @param newCandidateRatings
     */
    public void addCandidateRatings(Map<Class, Float> newCandidateRatings) {
        this.candidateRatings.putAll(candidateRatings);
    }

    /**
	* Returns the id of the document as int
	* @return id
	*/
    public int getID() {
		try {
			return Integer.parseInt(this.id);
		} catch(NumberFormatException e) {
			logger.error(e.getMessage());
			return 0;
		}
    }

	/**
	 * Returns the id of the document as String
	 * @return id
	 */
	public String getIDString() {
		return this.id;
	}

  /**
   * Adds a new field containing data
   * @param name
   * @param value
   * @param store
   * @param analyze
   * @param termVectors
   */
    public void addField(String name, String value, boolean store,
        boolean analyze, boolean termVectors) {

        Field field = new Field(name, store, analyze, termVectors);
        field.setValue(value);
		Field existingField = this.getFieldByName(name);
		if (existingField != null) {
			this.fields.remove(existingField);
		}
        this.fields.add(field);
    }

   /**
    * Adds a new field containing data
    * @param name
    * @param values
    * @param store
    * @param analyze
    * @param termVectors
    */
    public void addField(String name, List<String> values, boolean store,
        boolean analyze, boolean termVectors) {

        Field field = new Field(name, store, analyze, termVectors);
        field.setValues(values);
		Field existingField = this.getFieldByName(name);
		if (existingField != null) {
			this.fields.remove(existingField);
		}
        this.fields.add(field);
    }

    /**
     * Returns all document fields
     * @return fields
     */
    public List<Field> getFields() {
        return this.fields;
    }

    /**
     *
     * @param name
     * @return field
     */
    public Field getFieldByName(String name) {

        for (Field field : fields) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

	/**
	 * The documents scores.
	 * @return scores
	 */
	public List<Float> getScores() {
		return scores;
	}

	/**
	 * Adds a score to the list of document scores
	 * @param score
	 */
	public void addScore(Float score) {
		this.scores.add(score);
	}

	/**
	 * If two documents are compared, te one with the highest total score will
	 * be rated as bigger.
	 * @param o another document to compare to
	 * @return 0 if total scores are equal, -1 if score of this object is higher
	 * and 1 otherwhise.
	 */
	@Override
	public int compareTo(Document o) {

		if (o.getCombinedScore() == this.getCombinedScore()) {
			return 0;
		}
		if (o.getCombinedScore() < this.getCombinedScore()) {
			return -1;
		} else {
			return 1;
		}
	}

	/**
	 * Compares the document by an other document, they are different if they
	 * have different ids
	 * @param obj
	 * @return true if documents are equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == Document.class) {
			return this.id.equals(((Document)obj).id);
		}
		return false;
	}

	/**
	 * Returns the documents hashCode based on the hashCode of the documents id
	 * @return hashCode
	 */
	@Override
	public int hashCode() {
		if (this.id != null) {
			return this.id.hashCode();
		}
		return 0;
	}

	/**
	 *
	 * @return
	 */
	public List<String> getMultimatchingSurfaceForms() {
		return multimatchingSurfaceForms;
	}

	/**
	 *
	 * @param multimatchingSurfaceForm
	 */
	public void addMultimatchingSurfaceForm(String multimatchingSurfaceForm) {
		this.multimatchingSurfaceForms.add(multimatchingSurfaceForm);
	}

	/**
	 *
	 */
	public void clearMultimatchingSurfaceForms() {
		this.multimatchingSurfaceForms.clear();
	}

	/**
	 *
	 * @return
	 */
	public String getSurfaceForm() {
		return surfaceForm;
	}

	/**
	 *
	 * @param surfaceForm
	 */
	public void setSurfaceForm(String surfaceForm) {
		this.surfaceForm = surfaceForm;
	}

	/**
	 *
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 *
	 * @param type
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 *
	 * @return
	 */
	public Map<String, Map<Integer, Integer>> getTermDocsKeywordMatches() {
		return termDocsKeywordMatches;
	}

	/**
	 *
	 * @param termValue
	 * @param docID
	 * @param numberOfMatchingKeywords
	 */
	public void addTermDocsKeywordMatch(String termValue, Integer docID, Integer numberOfMatchingKeywords) {

		if (!this.termDocsKeywordMatches.containsKey(termValue)) {
			this.termDocsKeywordMatches.put(termValue, new HashMap<Integer, Integer>());
		}
		this.termDocsKeywordMatches.get(termValue).put(docID, numberOfMatchingKeywords);
	}

	/**
	 *
	 */
	public void clearTermDocsKeywordMatches() {
		this.termDocsKeywordMatches.clear();
	}

	/**
	 *
	 * @return
	 */
	public Float getCombinedScore() {
		if (combinedScores.isEmpty()) {
			return 0.0f;
		}
		return combinedScores.get(0);
	}

	/**
	 *
	 * @param combinedScore
	 */
	public void addCombinedScore(Float combinedScore) {
		this.combinedScores.add(combinedScore);
	}

	/**
	 *
	 * @return
	 */
	public List<Float> getCombinedScores() {
		return this.combinedScores;
	}

	/**
	 *
	 * @return
	 */
	public String getUri() {
		return uri;
	}

	/**
	 *
	 * @param uri
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	public Boolean getInserted() {
		return inserted;
	}

	public void setInserted(Boolean inserted) {
		this.inserted = inserted;
	}
}