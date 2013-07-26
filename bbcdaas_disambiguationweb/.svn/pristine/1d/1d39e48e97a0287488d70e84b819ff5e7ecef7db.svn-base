/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bbcdaas.disambiguationweb.beans.membership;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robil
 */
public class WikipediaScoringParams implements Serializable {
	
	private Integer maxTermDocuments = null;
	private Integer maxTermDocumentsPerPattern = null;
	private Float multimatchingDocumentsRatingAddend = null;
	private Float alternativeURIRating = null;
    private List<String> candidateFinderNames = new ArrayList<String>();
    private List<Boolean> candidateFinder = new ArrayList<Boolean>();
	private List<String> documentScorerNames = new ArrayList<String>();
	private List<Float> documentScorerWeightings = new ArrayList<Float>();
    
	public Integer getMaxTermDocuments() {
		return maxTermDocuments;
	}

	public void setMaxTermDocuments(Integer maxTermDocuments) {
		this.maxTermDocuments = maxTermDocuments;
	}

	public Integer getMaxTermDocumentsPerPattern() {
		return maxTermDocumentsPerPattern;
	}

	public void setMaxTermDocumentsPerPattern(Integer maxTermDocumentsPerPattern) {
		this.maxTermDocumentsPerPattern = maxTermDocumentsPerPattern;
	}

	public Float getMultimatchingDocumentsRatingAddend() {
		return multimatchingDocumentsRatingAddend;
	}

	public void setMultimatchingDocumentsRatingAddend(Float multimatchingDocumentsRatingAddend) {
		this.multimatchingDocumentsRatingAddend = multimatchingDocumentsRatingAddend;
	}

	public Float getAlternativeURIRating() {
		return alternativeURIRating;
	}

	public void setAlternativeURIRating(Float alternativeURIRating) {
		this.alternativeURIRating = alternativeURIRating;
	}

	public List<String> getDocumentScorerNames() {
		return documentScorerNames;
	}

	public void setDocumentScorerNames(List<String> documentScorerNames) {
		this.documentScorerNames = documentScorerNames;
	}

    public List<String> getCandidateFinderNames() {
        return candidateFinderNames;
    }

    public void setCandidateFinderNames(List<String> candidateFinderNames) {
        this.candidateFinderNames = candidateFinderNames;
        this.candidateFinder.clear();
        for (int i = 0; i<this.candidateFinderNames.size();i++) {
            this.candidateFinder.add(true);
        }
    }

    public List<Boolean> getCandidateFinder() {
        return candidateFinder;
    }

    public void setCandidateFinder(List<Boolean> candidateFinder) {
        this.candidateFinder = candidateFinder;
    }

	public List<Float> getDocumentScorerWeightings() {
		return documentScorerWeightings;
	}

	public void setDocumentScorerWeightings(List<Float> documentScorerWeightings) {
		this.documentScorerWeightings = documentScorerWeightings;
	}
}
