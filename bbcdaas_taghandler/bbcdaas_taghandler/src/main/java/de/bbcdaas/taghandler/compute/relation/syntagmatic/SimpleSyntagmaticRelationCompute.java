package de.bbcdaas.taghandler.compute.relation.syntagmatic;

import de.bbcdaas.common.beans.*;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.taghandler.TagHandlerImpl;
import de.bbcdaas.taghandler.compute.score.RankListEntryScorer;
import de.bbcdaas.taghandler.compute.statistic.syntagmatic.SyntagmaticStatisticalTest;
import de.bbcdaas.taghandler.constants.ProcessingConstants;
import de.bbcdaas.taghandler.dao.TagHandlerDao;
import de.bbcdaas.taghandler.dao.TermLexiconDao;
import de.bbcdaas.taghandler.exception.ProcessException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
/**
 * Implementation of all syntagmatic term relation computing algorithms.
 * @author Christian Herta
 * @author Robert Illers
 */
public final class SimpleSyntagmaticRelationCompute implements SyntagmaticRelationCompute {

    private TagHandlerDao taghandlerDao;
	private TermLexiconDao termLexiconDao;
    private SyntagmaticStatisticalTest syntagmaticStatisticalTest;
	private RankListEntryScorer rankListEntryScorer;

	private float a;
	private float b;
	private float syntagmaticEntityTermFactor = ProcessingConstants.DEFAULT_SYNTAGMATIC_ENTITY_TERM_FACTOR;
    private int maxTopRelatedTerms = ProcessingConstants.DEFAULT_MAX_TOP_RELATED_TERMS;
    private int minLocalTermFrequency = ProcessingConstants.DEFAULT_MIN_TERM_FREQUENCY;
	private float maxPercentageTopTerms = ProcessingConstants.DEFAULT_MAX_PERCENTAGE_TOP_TERMS;
	private float minSyntagmaticValue = ProcessingConstants.DEFAULT_MIN_SYNTAGMATIC_VALUE;
        private boolean useSingleTermLexicon = true;
	// unused
	private int minNbCorrelatedTerms = 4;

    private Logger logger = Logger.getLogger(SimpleSyntagmaticRelationCompute.class);

    /**
	 *
	 * @param taghandlerDao
	 */
	public void setTaghandlerDao(TagHandlerDao taghandlerDao) {
		this.taghandlerDao = taghandlerDao;
	}

	/**
	 *
	 * @param termLexiconDao
	 */
	public void setTermLexiconDao(TermLexiconDao termLexiconDao) {
		this.termLexiconDao = termLexiconDao;
	}

        /**
        *
        * @param useSingleTermLexicon
        */
        public void setUseSingleTermLexicon(boolean useSingleTermLexicon) {
            this.useSingleTermLexicon = useSingleTermLexicon;
        }

    /**
	 *
	 * @param _rankListEntryScorer
	 */
	@Override
	public void setRankListEntryScorer(RankListEntryScorer rankListEntryScorer) {
		this.rankListEntryScorer = rankListEntryScorer;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public float getA() {
		return a;
	}

	/**
	 *
	 * @param a
	 */
	@Override
	public void setA(float a) {
		this.a = a;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public float getB() {
		return b;
	}

	/**
	 *
	 * @param b
	 */
	@Override
	public void setB(float b) {
		this.b = b;
	}

    /**
	 *
	 * @param syntagmaticEntityTermFactor
	 */
	@Override
	public void setSyntagmaticEntityTermFactor(float syntagmaticEntityTermFactor) {
		this.syntagmaticEntityTermFactor = syntagmaticEntityTermFactor;
	}

    /**
     *
     * @return
     */
	@Override
    public float getSyntagmaticEntityTermFactor() {
        return this.syntagmaticEntityTermFactor;
    }

    /**
	 *
	 * @param maxTop
	 */
	@Override
	public void setMaxTopRelatedTerms(int maxTop) {
		maxTopRelatedTerms = maxTop;
	}

    /**
     *
     * @return
     */
	@Override
    public int getMaxTopRelatedTerms() {
        return this.maxTopRelatedTerms;
    }

    /**
	 *
	 * @param minTermFrequency
	 */
	@Override
	public void setMinTermFrequency(int minTermFrequency) {
		this.minLocalTermFrequency = minTermFrequency;
	}

    /**
     *
     * @return
     */
	@Override
    public int getMinTermFrequency() {
        return this.minLocalTermFrequency;
    }

    /**
	 *
	 * @param _maxPercentageTopTerms
	 */
	@Override
	public void setMaxPercentageTopTerms(float maxPercentageTopTerms) {
		this.maxPercentageTopTerms = maxPercentageTopTerms;
	}

    /**
     *
     * @return
     */
	@Override
    public float getMaxPercentageTopTerms() {
        return this.maxPercentageTopTerms;
    }

    /**
	 *
	 * @param _minNbCorrelatedTerms
	 */
	@Override
	public void setMinNbCorrelatedTerms(int minNbCorrelatedTerms) {
		this.minNbCorrelatedTerms = minNbCorrelatedTerms;
	}

    /**
     *
     * @return
     */
	@Override
    public int getMinNbCorrelatedTerms() {
        return this.minNbCorrelatedTerms;
    }

	/**
	 *
	 * @param _minSyntagmaticValue
	 */
	@Override
	public void setMinSyntagmaticValue(float minSyntagmaticValue) {
		this.minSyntagmaticValue = minSyntagmaticValue;
	}

    /**
     *
     * @return
     */
	@Override
    public float getMinSyntagmaticValue() {
        return this.minSyntagmaticValue;
    }

    public void setSyntagmaticStatisticalTest(SyntagmaticStatisticalTest test) {

        this.syntagmaticStatisticalTest = test;
    }

    /**
     *
     * @param a
     * @param b
     * @param ab
     * @param nbEntities
     * @return
     */
	@Override
    public float computeSyntagmaticRelation(long a, long b, long ab, long nbEntities) {

        return this.syntagmaticStatisticalTest.computeSyntagmaticRelation(a, b, ab, nbEntities);
    }

	/**
	 *
	 * @param term
	 * @param matrixEntries
	 */
	private boolean computeTopRelatedTerms(Term term, List<TermMatrixEntry> termMatrixEntries) {

		int topRelatedTermsSize = (int)(maxPercentageTopTerms * (float) termMatrixEntries.size() * 0.01);
		topRelatedTermsSize = maxTopRelatedTerms < topRelatedTermsSize ? maxTopRelatedTerms : topRelatedTermsSize;
		topRelatedTermsSize = termMatrixEntries.size() < topRelatedTermsSize ? termMatrixEntries.size() : topRelatedTermsSize;

		List<Term> topRelatedTerms = new ArrayList<Term>();
		List<Float> syntags = new ArrayList<Float>();

		try {
			int i = 1;
			for (TermMatrixEntry termMatrixEntry : termMatrixEntries) {

				if (termMatrixEntry.getSyntag() >= minSyntagmaticValue) {
					topRelatedTerms.add(taghandlerDao.getTerm(this.
                        getTermMatrixEntryAssociateID(term.getId(), termMatrixEntry), false));
					syntags.add(termMatrixEntry.getSyntag());
					i++;
				}
				if (i > topRelatedTermsSize) {
					break;
				}
			}

			if (!topRelatedTerms.isEmpty()) {

				TermToTermsGroup topRelatedTermsEntry = new TermToTermsGroup();
				topRelatedTermsEntry.setTerm(term);
				topRelatedTermsEntry.setRelatedTerms(topRelatedTerms);
				taghandlerDao.insertTopRelatedTerms(term.getId(), topRelatedTerms, syntags, false);
			}
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
			return false;
		}
		return true;
	}

	/**
	 * Gets the id of the term that is in cooccurrence with the current term
	 * @param termId
	 * @param termMatrixEntry
	 * @return termId
	 */
	private int getTermMatrixEntryAssociateID(int termId, TermMatrixEntry termMatrixEntry) {
		return termMatrixEntry.getTermId1() != termId ? termMatrixEntry.getTermId1() : termMatrixEntry.getTermId2();
	}

	/**
	 *
	 */
	@Override
	public void computeTopRelatedTerms() throws ProcessException {

		int maxTermId, termId;
		long nbTerms;

		try {

			taghandlerDao.openConnection();
			maxTermId = taghandlerDao.getMaxTermID(false);
			nbTerms = taghandlerDao.getNumberOfTerms(false);
			termId = taghandlerDao.getMinTermID(false);

			logger.info("Number of Terms: "+nbTerms);
			logger.info("MinTermId: "+termId);
			logger.info("MaxTermId: "+maxTermId);
			int percentageComputed = 0;

            int i = 1;
			for (; termId <= maxTermId; termId++) {

				Term term = taghandlerDao.getTerm(termId, false);

				if (term != null) {

					if (term.getLocalFrequency() >= minLocalTermFrequency) {

						List<TermMatrixEntry> matrixEntries = taghandlerDao.getRelatedTermMatrixEntries(termId, false);
						if (!matrixEntries.isEmpty()) {
							computeTopRelatedTerms(term, matrixEntries);
						}
					}
				}

				if (i % 100 == 0) {
					System.out.print(".");
				}
				percentageComputed = TagHandlerImpl.reportProgress(i, nbTerms, percentageComputed);
                i++;
			}
			taghandlerDao.closeConnection(false);
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
			throw new ProcessException(ProcessException.ERROR_CODE_COMPUTE_TOP_RELATED_TERMS);
		}
	}

    /**
     *
     * @return
     */
	@Override
    public int computeSyntagmaticRelations(int offset, int readStep, int commitStep) {

		long nbEntities, nbMatrixEntries;
        int counter = 0;
        float sr;
        int percentageComputed = 0;
		List<TermMatrixEntry> termMatrixEntries;

		try {
			nbEntities = taghandlerDao.getNumberOfEntities();
			nbMatrixEntries = taghandlerDao.getNumberOfTermMatrixEntries();

			taghandlerDao.openConnection();
			do {
				termMatrixEntries = this.taghandlerDao.getTermMatrixWithTermData(offset, readStep, false);

				for (TermMatrixEntry termMatrixEntry : termMatrixEntries) {
					if (termMatrixEntry.getTermId1() != termMatrixEntry.getTermId2()) {

						sr = computeSyntagmaticRelation(termMatrixEntry.getTerm1().getLocalFrequency(),
							termMatrixEntry.getTerm2().getLocalFrequency(),
							termMatrixEntry.getCoocurrence(), nbEntities);
						termMatrixEntry.setSyntag(sr);
					}
				}

				// do not commit every read step...
				taghandlerDao.insertSyntagmaticRelations(termMatrixEntries, false, false);
				if ((counter + termMatrixEntries.size()) % commitStep == 0) {
					System.out.print(".");
					// ...but every commitStep termMatrix entries (performance)
					taghandlerDao.commit();
				}
				counter+= termMatrixEntries.size();
				percentageComputed = TagHandlerImpl.reportProgress(counter, nbMatrixEntries, percentageComputed);
				offset += termMatrixEntries.size();
			} while (!termMatrixEntries.isEmpty());
			taghandlerDao.closeConnection(false);
		} catch(DaoException e) {
			logger.error("last offset commited: "+offset+". Error: "+e.getCompleteMessage());
		}
        return counter;
    }

	/**
	 *
	 */
	@Override
    public void computeAllTopSyntagmaticTerms() throws ProcessException {

		long nbFields;
		int percentageComputed = 0;
		int nbReadTermClouds;
		int readStep = 200;
		int counter = 0;
		//float minSyntag = ProcessingConstants.DEFAULT_MIN_TOP_TERM_SYNTAG;
		float minSyntag = 0 ;

		try {

			nbFields = taghandlerDao.getNumberOfFields();
			do {
				List<TermCloud> termClouds = taghandlerDao.getTermClouds(counter, readStep);
				nbReadTermClouds = termClouds.size();
				counter += nbReadTermClouds;
				for (TermCloud termCloud : termClouds) {

					// clear old values
					termCloud.getSyntagmaticTerms().clear();

					List<RankListEntry> topSyntagmaticTerms = computeFieldTopSyntagmaticTerms(termCloud.getTerms(), minSyntag,
						this.syntagmaticEntityTermFactor, this.a, this.b);

					for (RankListEntry entry : topSyntagmaticTerms) {
						termCloud.addSyntagmaticTerm(entry.getTerm());
						termCloud.addScore(entry.getScore());
					}

					if (termCloud.getSyntagmaticTerms().size() > 0) {
						taghandlerDao.insertTopSyntagmaticTerms(termCloud);
					}
				}
				percentageComputed = TagHandlerImpl.reportProgress(counter, nbFields, percentageComputed);
			} while(nbReadTermClouds == readStep);
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
			throw new ProcessException(ProcessException.ERROR_CODE_COMPUTE_TOP_SYNTAGMATIC_TERMS);
		}
    }

    /**
	 * Computes the top syntagmatic terms for the terms of a field (termCloud)
	 * @param terms
	 * @return
	 */
	@Override
	public List<RankListEntry> computeFieldTopSyntagmaticTerms(List<Term> terms, float minSyntag,
		float syntagmaticEntityTermFactor, float a, float b) throws ProcessException {

		List<RankListEntry> rankListEntries = new ArrayList<RankListEntry>();
		// compute the max number of term in the syntagmaticTermCloud
		int maxNumberOfTopSyntagmaticTerms = (int)((float)terms.size() *
			syntagmaticEntityTermFactor);

		try {

			taghandlerDao.openConnection();
                        if (this.useSingleTermLexicon) {
                            termLexiconDao.openConnection();
                        }

			for (Term term : terms) {

				List<Term> topRelatedTerms = taghandlerDao.
					getTopRelatedTerms(term.getId(), minSyntag, false);

                                if (this.useSingleTermLexicon) {
                                    for (Term topRelatedTerm : topRelatedTerms) {

                                            Term lexiconTerm = termLexiconDao.getTerm(topRelatedTerm.getId(), false);
                                            if (lexiconTerm == null) {
                                                    logger.error("Term with ID='"+topRelatedTerm.getId()+"' is not in termLexicon.");
                                                    throw new ProcessException(ProcessException.ERROR_CODE_COMPUTE_SYNTAGMATIC_RELATIONS);
                                            } else {
                                                    topRelatedTerm.setValue(lexiconTerm.getValue());
                                            }
                                    }
                                }

				// rank the topRelatedTerms in ascending order, 1 is the best rank.
				if (!topRelatedTerms.isEmpty()) {

					int rank = 1;
					for (Term topRelatedTerm : topRelatedTerms) {

						RankListEntry rankListEntry = new RankListEntry();
						rankListEntry.setRank(rank);
						rankListEntry.setTerm(topRelatedTerm);
						rankListEntries.add(rankListEntry);
						rank++;
					}
				}
			}

			taghandlerDao.closeConnection(false);
                         if (this.useSingleTermLexicon) {
                            termLexiconDao.closeConnection(false);
                         }
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
		}

		// merge and compute the score for each term
		rankListEntries = rankListEntryScorer.computeAllScores(rankListEntries, a, b);

		List<RankListEntry> topSyntagmaticTerms = new ArrayList<RankListEntry>();

		if (!rankListEntries.isEmpty()) {

			// sort the list according to the scores
			Collections.sort(rankListEntries, new RankListEntry.ScoreComparator());

			// reduce the max number of topSyntagmaticTerms to the number of existing terms
			maxNumberOfTopSyntagmaticTerms = maxNumberOfTopSyntagmaticTerms <
				rankListEntries.size() ? maxNumberOfTopSyntagmaticTerms : rankListEntries.size();

			int i = 0;
			for (RankListEntry rankListEntry : rankListEntries) {

				if (i < maxNumberOfTopSyntagmaticTerms) {

					if (!terms.contains(rankListEntry.getTerm())) {
						topSyntagmaticTerms.add(rankListEntry);
						i++;
					}
				} else {
					break;
				}
			}
		}
		return topSyntagmaticTerms;
	}
}