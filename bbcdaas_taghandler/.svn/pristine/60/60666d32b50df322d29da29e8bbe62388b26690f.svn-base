package de.bbcdaas.taghandler.compute.score;

import de.bbcdaas.common.beans.RankListEntry;
import de.bbcdaas.taghandler.exception.ProcessException;
import java.util.List;
/**
 *
 * @author Christian Herta
 */
public interface RankListEntryScorer {

	/**
	 * Compute the score for a term with the corresponding rankListEntries.
	 * All RankListEntries must have the same RankListEntry.term
	 * @param rankListEntries
	 * @return RankListEntry
	 * @throws DataInconsistencyException 
	 */
	public RankListEntry computeScore(List<RankListEntry> rankListEntries, float a, float b) throws ProcessException ;
	
	/**
	 * Merge all entries with identical terms and compute the score for each term 
	 * @param rankListEntries
	 * @return List<RankListEntry>
	 */
	public List<RankListEntry> computeAllScores (List<RankListEntry> rankListEntries,
		float a, float b) throws ProcessException ;
		
	
		
}