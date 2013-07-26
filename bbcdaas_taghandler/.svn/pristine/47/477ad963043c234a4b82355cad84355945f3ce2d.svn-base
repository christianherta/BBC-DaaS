package de.bbcdaas.taghandler.compute.score;

import de.bbcdaas.common.beans.RankListEntry;
import de.bbcdaas.common.beans.Term;
import de.bbcdaas.taghandler.exception.ProcessException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 *
 * @author Christian Herta
 */
public final class SimpleRankListEntryScorer implements RankListEntryScorer {

	/**
	 * Compute the score for a term with the corresponding rankListEntries.
	 * All RankListEntries must have the same RankListEntry.term
	 * @param rankListEntries
	 * @return RankListEntry
	 * @throws DataInconsistencyException 
	 */
	@Override
	public RankListEntry computeScore(List<RankListEntry> rankListEntries, float a, float b) throws ProcessException {
		
		if (rankListEntries.isEmpty()) {
			throw new ProcessException("Parameter rankListEntries is empty",ProcessException.ERROR_CODE_COMPUTE_TOP_SYNTAGMATIC_TERMS);
		}
		if (rankListEntries.get(0).getTerm() == null) {
			throw new ProcessException("First term in rankListEntries list is null",ProcessException.ERROR_CODE_COMPUTE_TOP_SYNTAGMATIC_TERMS);
		}
		
		RankListEntry r = new RankListEntry();
		r.setTerm(rankListEntries.get(0).getTerm());
		float score = 0;
		
		for(RankListEntry rankListEntry : rankListEntries) {
			
			score += 1.f / (a * (float)(rankListEntry.getRank()) + b);
		}
		
		if (score == Float.NaN) {
			throw new ProcessException("score is not a number",ProcessException.ERROR_CODE_COMPUTE_TOP_SYNTAGMATIC_TERMS);
		} else if (score == Float.NEGATIVE_INFINITY || score == Float.POSITIVE_INFINITY) {
			throw new ProcessException("score is infinite",ProcessException.ERROR_CODE_COMPUTE_TOP_SYNTAGMATIC_TERMS);
		}
		r.setScore(score);
		return r;
	}
        
        /**
	 * Merge all entries with identical terms and compute the score for each term 
	 * @param rankListEntries
	 * @return List<RankListEntry>
	 */
    @Override
	public List<RankListEntry> computeAllScores (List<RankListEntry> rankListEntries,
		float a, float b) throws ProcessException {
		
		if (rankListEntries.isEmpty()) {
			return rankListEntries;
		}
		
		Collections.sort(rankListEntries, new RankListEntry.TermComparator());
		ArrayList<RankListEntry> mergedList = new ArrayList<RankListEntry>();
		Term currentTerm = rankListEntries.get(0).getTerm();
		ArrayList<RankListEntry> currentRankListEntry = new ArrayList<RankListEntry>();

		currentRankListEntry.add(rankListEntries.get(0));
		
		for (int i=0; i < rankListEntries.size(); i++) {
			
			if (!currentTerm.equals(rankListEntries.get(i).getTerm()) ||
				i == rankListEntries.size()-1) {
				
				mergedList.add(this.computeScore(currentRankListEntry, a, b));
				
				currentRankListEntry.clear();
				currentTerm = rankListEntries.get(i).getTerm();
			}
			currentRankListEntry.add(rankListEntries.get(i));
		}
		return mergedList;
	}
}
