package de.bbcdaas.common.beans;

import java.io.Serializable;
import java.util.Comparator;
/**
 * bean that represents a rank list entry
 * used to calculate topSyntagmaticTerms
 * 
 * @author Christian Herta
 */
public final class RankListEntry implements Serializable {

    private Term term;
	private int rank;
	private float score = 0f;

	/**
	 * setter for the rank value
	 * @param rank 
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}
    
    /**
	 * getter for the entry rank 
	 * @return the rank 
	 */
	public int getRank() {
		return rank;
	}

    /**
	 * setter for the entry score
	 * @param score 
	 */
	public void setScore(float score) {
		this.score = score;
	}
    
	/**
	 * getter for the entry score
	 * @return the score
	 */
	public float getScore() {
		return score;
	}

	/**
	 * getter for the term
	 * @return term
	 */
	public Term getTerm() {
		return term;
	}

	/**
	 * setter for the term
	 * @param term 
	 */
	public void setTerm(Term term) {
		this.term = term;
    }
	
	/**
	 * Compares two RankListEntries by its term values
	 */
	public static class TermComparator implements Comparator<RankListEntry> {
		
		/**
		 * Compares two RankListEntries by its term values
		 * @param rankListEntry1
		 * @param rankListEntry2
		 * @return the value 0 if term values are equal; a value less than 0 if the term value is lexicographically
         * less than the value of the term argument; and a value greater than 0 if the term value is lexicographically greater than the value of the term argument.
		 */
		@Override
		public int compare(RankListEntry rankListEntry1, RankListEntry rankListEntry2) {
			return rankListEntry1.getTerm().compareTo(rankListEntry2.getTerm());
        }
	}
	
	/**
	 * Compares two RankListEntries by score
	 */
	public static class ScoreComparator implements Comparator<RankListEntry> {
		
		/**
		 * Compares two RankListEntries by score
		 * @param rankListEntry1
		 * @param rankListEntry2
		 * @return the value 0 if the score of the entries are equal; the value -1 if the score of the second argument term is less than the first arguemnt term;
         * the value 1 if the score of the first argument term is less than the second arguemnt term;
		 */
		@Override
		public int compare(RankListEntry rankListEntry1, RankListEntry rankListEntry2) {
			
			if (rankListEntry1.getScore() > rankListEntry2.getScore()) {
                return -1;
			}
			if (rankListEntry1.getScore() < rankListEntry2.getScore()) {
				return 1;
			}
			return 0;
		}
	}
}