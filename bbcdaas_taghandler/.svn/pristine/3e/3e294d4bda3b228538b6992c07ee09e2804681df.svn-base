package de.bbcdaas.taghandler;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.common.beans.entity.TermCloudField;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.common.util.PerformanceUtils;
import de.bbcdaas.taghandler.cleaner.EntityCleaner;
import de.bbcdaas.taghandler.compute.relation.syntagmatic.SyntagmaticRelationCompute;
import de.bbcdaas.taghandler.dao.TagHandlerDao;
import de.bbcdaas.taghandler.exception.ProcessException;
import de.bbcdaas.taghandler.reader.EntityReader;
import de.bbcdaas.taghandler.reducer.TermDataReducer;
import de.bbcdaas.taghandler.writer.EntityWriter;
import java.util.List;
import org.apache.log4j.Logger;
/**
 *
 * @author Christian Herta
 * @author Robert Illers
 */
public final class TagHandlerImpl implements TagHandler {

	private static Logger logger = Logger.getLogger(TagHandlerImpl.class);
	// injected implementations of the reader, cleaner and writer of the readEntities process
    private EntityReader entityReader;
	private TermDataReducer termDataReducer;
	private EntityCleaner entityCleaner;
	private EntityWriter entityWriter;
    // sets how many entities should be read at once from the source.
    // the more memory a single entity needs, the less this value should be.
	private int ENTITY_READ_STEP = 0;
	// number of max entities that should be read from a source in the readEntities process
    private int maxEntities = -1;
	// injected implementations of the dao objects
	private TagHandlerDao taghandlerDao;
    // injected implementation of the relation computing
	private SyntagmaticRelationCompute syntagmaticRelationCompute;
    // switches for choosing if subprocesses of handleTag() should be executed
    private boolean useSingleTermLexicon = true;
    private boolean readEntities = false;
	private boolean reduceTermData = false;
	private boolean computeSyntagmaticRelations = false;
    private boolean computeTopRelatedTerms = false;
	private boolean computeTopSyntagmaticTerms = false;
	private PerformanceUtils pu;
	private int processID = 0;

	/**
	 *
	 */
	public TagHandlerImpl() {

		this.pu = new PerformanceUtils();
	}

    /**
     *
     * @param useSingleTermLexicon
     */
    @Override
    public void setUseSingleTermLexicon(boolean useSingleTermLexicon) {
        this.useSingleTermLexicon = useSingleTermLexicon;
    }

	/**
	 * injected
	 * @param entityReader
	 */
	@Override
	public void setEntityReader(EntityReader entityReader) {
		this.entityReader = entityReader;
		this.ENTITY_READ_STEP = entityReader.getEntityReadStep();

	}

	/**
	 *
	 * @param termDataReducer
	 */
	@Override
	public void setTermDataReducer(TermDataReducer termDataReducer) {
		this.termDataReducer = termDataReducer;
	}

	/**
	 * injected
	 * @param entityCleaner
	 */
	@Override
	public void setEntityCleaner(EntityCleaner entityCleaner) {
		this.entityCleaner = entityCleaner;
	}

	/**
	 * injected
	 * @param entityWriter
	 */
	@Override
	public void setEntityWriter(EntityWriter entityWriter) {
		this.entityWriter = entityWriter;
	}

	/**
	 * injected
	 * @param maxEntities
	 */
	public void setMaxEntities(int maxEntities) {
		this.maxEntities = maxEntities;
	}

	/**
	 *
	 * @param taghandlerDao
	 */
	@Override
	public void setTaghandlerDao(TagHandlerDao taghandlerDao) {
		this.taghandlerDao = taghandlerDao;
	}

    /**
     *
     * @param readEntities
     */
	@Override
    public void setReadEntities(boolean readEntities) {
        this.readEntities = readEntities;
    }

	/**
	 *
	 * @param reduceTermData
	 */
	@Override
    public void setReduceTermData(boolean reduceTermData) {
        this.reduceTermData = reduceTermData;
    }

    /**
     *
     * @param computeSyntagmaticRelations
     */
	@Override
    public void setComputeSyntagmaticRelations(boolean computeSyntagmaticRelations) {
        this.computeSyntagmaticRelations = computeSyntagmaticRelations;
    }

    /**
     *
     * @param computeTopRelatedTerms
     */
	@Override
    public void setComputeTopRelatedTerms(boolean computeTopRelatedTerms) {
        this.computeTopRelatedTerms = computeTopRelatedTerms;
    }

	/**
	 *
	 * @param computeTopSyntagmaticTerms
	 */
	@Override
	public void setComputeTopSyntagmaticTerms(boolean computeTopSyntagmaticTerms) {
        this.computeTopSyntagmaticTerms = computeTopSyntagmaticTerms;
    }

	/**
	 *
	 * @param relationCompute
	 */
	public void setSyntagmaticRelationCompute(SyntagmaticRelationCompute relationCompute) {
		syntagmaticRelationCompute = relationCompute;
	}

	/**
	 *
	 */
	@Override
	public void run() {

		handleTags();
	}

    /**
     *
     */
	@Override
    public void handleTags() {

        /* log configuration informations */
        logger.info("<<--BBC-DaaS - Building Blocks for Cloud Data-as-a-Service-->>");
        logger.info("<<--------------- TagHandler 1.0 --------------------------->>");
        logger.info("");
        logger.info("Parameter (set in parameter.properties):");
        logger.info("-------------------------------------------");
        logger.info("useSingleTermLexicon = "+useSingleTermLexicon);
        logger.info("readEntities (read, clean, write) = "+readEntities);
		logger.info("reduceTermData = "+reduceTermData);
		if (reduceTermData) {
			logger.info("WARNING: The following steps will be ignored because of running the reduce.");
			logger.info("Runs these steps with reduced data or disable reduce option.");
		}
        logger.info("computeSyntagmaticRelations = "+computeSyntagmaticRelations);
        logger.info("computeTopRelatedTerms = "+computeTopRelatedTerms);
		logger.info("computeTopSyntagmaticTerms = "+computeTopSyntagmaticTerms);
        logger.info("");

        if (readEntities) {
            logger.info("Settings for entity read process:");
            logger.info("----------------------------------");
            logger.info("maxEntities: "+maxEntities);
			logger.info("entityReadStep: "+entityReader.getEntityReadStep());
            logger.info("");
            logger.info("Settings for entity clean process:");
            logger.info("-----------------------------------");
            logger.info("maxWordLength: "+this.entityCleaner.getMaxWordLength());
            logger.info("maxWordsInTerm: "+this.entityCleaner.getMaxWordsInTerm());
            logger.info("minTermCount: "+this.entityCleaner.getMinTermCount());
            logger.info("minTermLength: "+this.entityCleaner.getMinTermLength());
            List<String> forbiddenStringList = this.entityCleaner.getForbiddenStringsAsList();
            StringBuilder forbiddenStrings = new StringBuilder();
            int i = 0;
            for (String forbiddenString: forbiddenStringList) {
                if (i != 0) {
                    forbiddenStrings.append(",");
                }
                forbiddenStrings.append(forbiddenString);
                i++;
            }
            logger.info("forbiddenStrings: "+forbiddenStrings);
            logger.info("");
            logger.info("Settings for entity writer:");
            logger.info("----------------------------");
            logger.info("");
        }

		if (reduceTermData) {
			logger.info("Settings for reduce term data:");
			logger.info("-------------------------------");
			logger.info("minTermFrequency: "+this.termDataReducer.getMinTermFrequency());
			logger.info("minTermCount: "+this.termDataReducer.getMinTermCount());
			logger.info("");
		}

		// do not compute if data has been reduced (or computation will be done with base data)
		// database setting needs to be reconfigured to use reduced data
		else {

			if (computeSyntagmaticRelations) {
				logger.info("Settings for syntagmatic relation computing:");
				logger.info("---------------------------------------------");
				logger.info("");
			}

			if (computeTopRelatedTerms) {
				logger.info("Settings for top related terms computing:");
				logger.info("-------------------------------------------");
				logger.info("maxTopRelatedTerms: "+this.syntagmaticRelationCompute.getMaxTopRelatedTerms());
				logger.info("MinTermFrequency: "+this.syntagmaticRelationCompute.getMinTermFrequency());
				logger.info("minSyntagmaticValue: "+this.syntagmaticRelationCompute.getMinSyntagmaticValue());
				logger.info("maxPercentageTopTerms: "+this.syntagmaticRelationCompute.getMaxPercentageTopTerms());
				logger.info("minNbCorrelatedTerms: "+this.syntagmaticRelationCompute.getMinNbCorrelatedTerms());
				logger.info("");
			}

			if (computeTopSyntagmaticTerms) {
				logger.info("Settings for top syntagmatic terms computing:");
				logger.info("-------------------------------------------");
				logger.info("syntagmaticEntityTermFactor: "+this.syntagmaticRelationCompute.getSyntagmaticEntityTermFactor());
				logger.info("a: "+this.syntagmaticRelationCompute.getA());
				logger.info("b: "+this.syntagmaticRelationCompute.getB());
				logger.info("");
			}
		}
        /* /log configuration informations */

		this.processID = 0;
		this.processID = this.insertNewProcessParameter(readEntities, computeSyntagmaticRelations,
			computeTopRelatedTerms, computeTopSyntagmaticTerms, this.syntagmaticRelationCompute.getMaxTopRelatedTerms(),
			this.syntagmaticRelationCompute.getMaxPercentageTopTerms(), this.syntagmaticRelationCompute.getMinNbCorrelatedTerms(),
			this.syntagmaticRelationCompute.getMinTermFrequency(), this.syntagmaticRelationCompute.getMinSyntagmaticValue(),
			this.syntagmaticRelationCompute.getSyntagmaticEntityTermFactor(), this.syntagmaticRelationCompute.getA(),
			this.syntagmaticRelationCompute.getB());
		logger.info("Process with ID:"+this.processID+" started.");
        /* run selected tasks */
		int errorCode = 0;
		try {
			this.readEntities(readEntities);
			this.reduceTerms(reduceTermData);
			if (!reduceTermData) {
				this.computeSyntagmaticRelations(computeSyntagmaticRelations);
				this.computeTopRelatedTerms(computeTopRelatedTerms);
				this.computeTopSyntagmaticTerms(computeTopSyntagmaticTerms);
			}
		} catch(ProcessException e) {
			errorCode = e.getErrorCode();
			logger.error(e);
		}
		/* /run selected tasks */
		try {
			taghandlerDao.updateProcessParameter(this.processID, errorCode);
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
		}

		this.taghandlerDao.closeConnection(true);
        logger.info("Complete running time: "+pu.getRunningTimer()+" ms.");
    }

	/**
	 *
	 */
	private void clearTopSyntagmaticTerms() throws ProcessException {

		logger.info("clear top syntagmatic terms from database...");
		pu.startTimer();
		try {
		taghandlerDao.clearTopSyntagmaticTerms();
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
			throw new ProcessException(ProcessException.ERROR_CODE_COMPUTE_TOP_SYNTAGMATIC_TERMS);
		}
		logger.info("Time needed: "+pu.getTimeAndStop()+" ms.");
	}

    /**
     *
     */
    private void clearStoredData() throws ProcessException {

        logger.info("clear stored data from database...");
		pu.startTimer();

		try {
			taghandlerDao.clearAllTables();
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
			throw new ProcessException(ProcessException.ERROR_CODE_READ_ENTITIES);
		}
		logger.info("Time needed: "+pu.getTimeAndStop()+" ms.");
    }

	/**
	 *
	 */
	private void clearSyntagmaticRelations() throws ProcessException {

		logger.info("clear stored syntagmatic relations from database...");
        pu.startTimer();
		try {
		taghandlerDao.clearSyntagmaticRelations();
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
			throw new ProcessException(ProcessException.ERROR_CODE_COMPUTE_SYNTAGMATIC_RELATIONS);
		}
		logger.info("Time needed: "+pu.getTimeAndStop()+" ms.");
	}

	/**
	 *
	 */
	private void clearTopRelatedTermsTable() throws ProcessException {

		logger.info("clear stored top related terms from database...");
        pu.startTimer();
		try {
			taghandlerDao.clearTopRelatedTermTable();
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
			throw new ProcessException(ProcessException.ERROR_CODE_COMPUTE_TOP_RELATED_TERMS);
		}
        logger.info("Time needed: "+pu.getTimeAndStop()+" ms.");
	}

	/**
	 *
	 * @param readEntities
	 * @param computeSyntagmaticRelations
	 * @param computeTopRelatedTerms
	 * @param computeSyntagmaticTerms
	 * @return
	 */
	private int insertNewProcessParameter(Boolean readEntities, Boolean computeSyntagmaticRelations,
			Boolean computeTopRelatedTerms, Boolean computeSyntagmaticTerms, Integer maxTopRelatedTerms,
		Float maxPercentageTopTerms, Integer minNbCorrelatedTerms, Integer minTermFrequency,
		Float minSyntagmaticValue, Float syntagmaticEntityTermFactor, Float a, Float b) {

		int id = 0;
		try {
			id = taghandlerDao.insertNewProcessParameter(readEntities, computeSyntagmaticRelations,
				computeTopRelatedTerms, computeSyntagmaticTerms, maxTopRelatedTerms, maxPercentageTopTerms,
				minNbCorrelatedTerms, minTermFrequency, minSyntagmaticValue, syntagmaticEntityTermFactor, a, b);
		} catch(DaoException e) {
			logger.error(e.getCompleteMessage());
		}
		return id;
	}

	/**
	 *
	 */
	@Override
	public void readEntities() throws ProcessException {

		this.readEntities(true);
	}

	/**
	 *
	 * @return boolean
	 */
	private void readEntities(boolean run) throws ProcessException {

		if (run) {

			this.clearStoredData();

			logger.info("begin reading entities...");
			pu.startTimer();
			List<Entity> entities;
			int nbOfReadEntities;
			int nbOfTotalReadEntities = 0;
			int nbOfTotalUseableEntities = 0;
			do {
				if (maxEntities != -1 &&
					(nbOfTotalUseableEntities + ENTITY_READ_STEP > maxEntities)) {
					entityReader.setEntityReadStep(maxEntities - nbOfTotalUseableEntities);
				}
				entities = entityReader.readEntities();
                System.out.print(".");
				nbOfReadEntities = entities.size();
				nbOfTotalReadEntities += nbOfReadEntities;
				//logger.reportReadEntities(entities);
				if (entityCleaner != null) {
					entities = entityCleaner.clean(entities);
                    System.out.print(".");
				}

				nbOfTotalUseableEntities += entities.size();
				entityWriter.setNbOfTotalReadEntities(nbOfTotalReadEntities);
				//logger.reportReadEntities(entities);
				entityWriter.writeEntities(entities);
				System.out.print(".");
				entities.clear();
			} while(nbOfReadEntities == entityReader.getEntityReadStep() &&
					(maxEntities == -1 || maxEntities - nbOfTotalUseableEntities > 0));
			if (maxEntities != -1 && (maxEntities - nbOfTotalUseableEntities <= 0)) {
				logger.info("Reading stopped, maxEntities reached.");
			}
			this.entityReader.closeReader();
			logger.info("Done reading process. Number of read entities: "+nbOfTotalReadEntities);
			logger.info("Number of useable entities: "+nbOfTotalUseableEntities);
			logger.info("Time needed: "+pu.getTimeAndStop()+" ms.");
		}
	}

	/**
	 *
	 */
	@Override
	public void computeSyntagmaticRelations() throws ProcessException {

		this.computeSyntagmaticRelations(true);
	}

	/**
	 *
	 */
	private void computeSyntagmaticRelations(boolean run) throws ProcessException {

		if (run) {

			this.clearSyntagmaticRelations();
			logger.info("compute syntagmatic relations...");
			// begin with the first term matrix entry
			int offset = 0;
			// read 2000 term matrix entries at once
			int readStep = 2000;
			// write result every 10000 relations computed
			int commitStep = 10000;
			pu.startTimer();
			int numberOfComputations = syntagmaticRelationCompute.computeSyntagmaticRelations(offset, readStep, commitStep);
			logger.info(numberOfComputations+" relations computed. Time needed: "+pu.getTimeAndStop()+" ms.");
		}
	}

	/**
	 *
	 * @throws ProcessException
	 */
	@Override
	public void reduceTerms() throws ProcessException {

		this.reduceTerms(true);
	}

	/**
	 *
	 * @param run
	 * @throws ProcessException
	 */
	private void reduceTerms(boolean run) throws ProcessException {

		if (run) {

			logger.info("begin reduce of terms...");
			pu.startTimer();
			this.termDataReducer.reduceTermData();
			logger.info("Time needed: "+pu.getTimeAndStop()+" ms.");
		}
	}

	/**
	 *
	 */
	@Override
	public void computeTopRelatedTerms() throws ProcessException {

		this.computeTopRelatedTerms(true);
	}

	/**
	 *
	 */
	private void computeTopRelatedTerms(boolean run) throws ProcessException {

		if (run) {

			this.clearTopRelatedTermsTable();
			logger.info("compute top related terms...");
			pu.startTimer();
			syntagmaticRelationCompute.computeTopRelatedTerms();
			logger.info("Time needed: "+pu.getTimeAndStop()+" ms.");
		}
	}

	/**
	 *
	 */
	@Override
	public void computeTopSyntagmaticTerms() throws ProcessException {

		this.computeTopSyntagmaticTerms(true);
	}

	/**
	 *
	 */
	private void computeTopSyntagmaticTerms(boolean run) throws ProcessException {

		if (run) {

			this.clearTopSyntagmaticTerms();
			logger.info("compute top syntagmatic terms...");
			pu.startTimer();
			syntagmaticRelationCompute.computeAllTopSyntagmaticTerms();
			logger.info("Time needed: "+pu.getTimeAndStop()+" ms.");
		}
	}

	/**
     * Logs a list of entities to the debug channel.
     * @param entities
     */
	private void reportReadEntities(List<Entity> entities) {

        if (logger.isDebugEnabled()) {
            for (Entity entity : entities) {
                logger.debug("EntityName: "+entity.getName());
                for (TermCloudField field : entity.getFields()) {
                    logger.debug("FieldType: "+field.getFieldType());
                    for (Term term : field.getTerms()) {
                        logger.debug(term.getValue());
                    }
                }
                logger.debug("--------------------------");
            }
        }
    }

	/**
     *
     * @param actual
     * @param total
     * @param percentage
     * @return
     */
	public static int reportProgress(long actual, long total, int percentage) {

		int newPercentageRead = percentage;
		// progress output (from 0% to 100% of source file)
		if (actual > total / 100 * percentage + total / 100) {

			newPercentageRead = (int)(actual * 100 / total);
			if (newPercentageRead == percentage) {
				newPercentageRead++;
			}
			logger.info("Progress: "+newPercentageRead+"% ("+actual+"/"+total+")");
		}
		return newPercentageRead;
	}
}