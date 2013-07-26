package de.bbcdaas.taghandler.writer.statistics;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.TermMatrixEntry;
import de.bbcdaas.common.beans.TermToTermsGroup;
import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.common.beans.entity.TermCloudField;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.common.util.FileWriter;
import de.bbcdaas.common.util.PerformanceUtils;
import de.bbcdaas.taghandler.dao.TagHandlerDao;
import de.bbcdaas.taghandler.dao.TermLexiconDao;
import java.util.List;
import org.apache.log4j.Logger;
/**
 * Implementation of an statistic fie writer for taghandler data.
 * @author Robert Illers
 */
public final class SimpleStatisticWriter implements StatisticWriter {

    private TagHandlerDao taghandlerDao;
    private TermLexiconDao taghandler_termLexiconDao;
    private String statisticOutputDirectory;
    private String termLexiconOutputFile;
    private String cooccurrenceOutputFile;
    private String topRelatedTermsOutputFile;
    private String entityOutputFileName;
    private Logger logger = Logger.getLogger(SimpleStatisticWriter.class);
    private boolean writeEntityOutput = false;
    private boolean writeTermLexiconOutput = false;
    private boolean writeCooccurrenceOutput = false;
    private boolean writeTopRelatedTermsOutput = false;
    private PerformanceUtils pu;

    /**
	 * Dao accessing the data of a specific taghandler context.
	 * @param taghandlerDao
	 */
	public void setTaghandlerDao(TagHandlerDao taghandlerDao) {
		this.taghandlerDao = taghandlerDao;
	}

	/**
	 * Dao accessing the taghandler termlexicon conating all terms read.
	 * @param taghandler_termLexiconDao
	 */
	public void setTagHandlerTermLexiconDao(TermLexiconDao taghandler_termLexiconDao) {
		this.taghandler_termLexiconDao = taghandler_termLexiconDao;
	}

	/**
	 * If set to true the statistics writer will generate a file containing
         * entities and its data.
	 * @param writeEntityOutput
	 */
	public void setWriteEntityOutput(boolean writeEntityOutput) {
		this.writeEntityOutput = writeEntityOutput;
	}

	/**
	 * If set to true statistics writer will generate a file containing the
         * taghandlers termlexicon data.
	 * @param writeTermLexiconOutput
	 */
	public void setWriteTermLexiconOutput(boolean writeTermLexiconOutput) {
		this.writeTermLexiconOutput = writeTermLexiconOutput;
	}

	/**
	 * If set to true statistics writer will generate a file containing the
         * term matrix data with the cooccurrent terms.
	 * @param writeCooccurrenceOutput
	 */
	public void setWriteCooccurrenceOutput(boolean writeCooccurrenceOutput) {
		this.writeCooccurrenceOutput = writeCooccurrenceOutput;
	}

	/**
	 * If set to true statistics writer will generate an output file containing
         * Terms with its top related terms.
	 * @param writeTopRelatedTermsOutput
	 */
	public void setWriteTopRelatedTermsOutput(boolean writeTopRelatedTermsOutput) {
		this.writeTopRelatedTermsOutput = writeTopRelatedTermsOutput;
	}

	/**
	 * Starts the statistics write process.
	 */
	@Override
	public void writeStatistics() {

            this.pu = new PerformanceUtils();

            /* log configuration informations */
            logger.info("<<--BBC-DaaS - Building Blocks for Cloud Data-as-a-Service-->>");
            logger.info("<<------------- StatisticWriter 1.0 ------------------------>>");
            logger.info("");
            logger.info("Parameter (set in parameter.properties):");
            logger.info("-------------------------------------------");
            logger.info("writeEntityOutput= "+writeEntityOutput);
            logger.info("writeTermLexiconOutput= "+writeTermLexiconOutput);
            logger.info("writeCooccurrenceOutput= "+writeCooccurrenceOutput);
            logger.info("writeTopRelatedTermsOutput= "+writeTopRelatedTermsOutput);
            logger.info("");
            /* /log configuration informations */

            if (writeEntityOutput) {
                    entityOutput();
            }

            if (writeTermLexiconOutput) {
                    termLexiconOutput();
            }

            if (writeCooccurrenceOutput) {
                    cooccurrenceOutput();
            }

            if (writeTopRelatedTermsOutput) {
                    topRelatedTermsOutput();
            }
            logger.info("Complete running time: "+pu.getRunningTimer()+" ms.");
	}

    /**
     * Starts the write process for the entity output using following pattern:
     * entityName::FieldName:terms[term1,term2,...,termX]syntagmaticTerms[term1,term2,...,termY];
     */
    private void entityOutput() {

        // entityName::FieldName:terms[term1,term2,...,termX]syntagmaticTerms[term1,term2,...,termY];

		logger.info("write entities to file...");
		pu.startTimer();
        FileWriter writer = new FileWriter();
        StringBuilder savePath = new StringBuilder().append(statisticOutputDirectory).append("/").
            append(this.entityOutputFileName);
		writer.openFile(savePath.toString(), false);
        int readStep = 500;
        int readPosition = 0;
        List<Entity> entities;
		try {
			taghandlerDao.openConnection();
			taghandler_termLexiconDao.openConnection();
			do {
				entities = taghandlerDao.getEntities(readPosition, readStep, false);
				for (Entity entity : entities) {
					StringBuilder line = new StringBuilder();
					line.append(entity.getName()).append("::");
					for (TermCloudField field : entity.getFields()) {
						line.append(field.getFieldType()).append(":");
						line.append("terms[");
						if (!field.getTerms().isEmpty()) {
							for (Term term : field.getTerms()) {
								line.append(term.getValue()).append(",");
							}
							line.deleteCharAt(line.length()-1);
						}
						line.append("]syntagmaticTerms[");
						if (!field.getSyntagmaticTerms().isEmpty()) {
							for (Term term : field.getSyntagmaticTerms()) {
								Term lexiconTerm = this.taghandler_termLexiconDao.
									getTerm(term.getId(), false);
								if (lexiconTerm != null) {
									line.append(lexiconTerm.getValue()).append(",");
								} else {
									logger.error("term with id='"+term.getId()+
										"' is not in term lexicon, skipping.");
								}
							}
							line.deleteCharAt(line.length()-1);
						}
						line.append("];");
					}
					writer.println(line.toString());
				}
				readPosition += readStep-1;
			} while(entities != null && !entities.isEmpty());
			taghandlerDao.closeConnection(false);
			taghandler_termLexiconDao.closeConnection(false);
		} catch(DaoException e) {
			logger.error(e.getMessage());
			System.exit(1);
		}
        writer.closeFile();
		logger.info("Time needed: "+pu.getTimeAndStop()+" ms.");
    }

    /**
     * Starts the write process for the term lexicon output.
     */
    private void termLexiconOutput() {

		logger.info("write termLexicon to file...");
		pu.startTimer();
        FileWriter writer = new FileWriter();
        StringBuilder savePath = new StringBuilder().append(statisticOutputDirectory).append("/").
            append(this.termLexiconOutputFile);
		writer.openFile(savePath.toString(), false);
        int readStep = 1000;
        int readPosition = 0;
        List<Term> terms;
		try {
			do {
				terms = taghandler_termLexiconDao.getTermsSortedByTotalFrequency(readPosition, readStep);
				for (Term term : terms) {
					StringBuilder line = new StringBuilder();
					line.append(term.getValue()).append(",").append(term.getTotalFrequency());
					writer.println(line.toString());
				}
				readPosition += readStep-1;
			} while(terms != null && !terms.isEmpty());
		} catch(DaoException e) {
			logger.error(e.getMessage());
			System.exit(1);
		}
        writer.closeFile();
		logger.info("Time needed: "+pu.getTimeAndStop()+" ms.");
    }

    /**
     * Starts the writing process for the term matrix output.
     */
    private void cooccurrenceOutput() {

		logger.info("write cooccurrence pairs to file...");
		pu.startTimer();
        FileWriter writer = new FileWriter();
        StringBuilder savePath = new StringBuilder().append(statisticOutputDirectory).append("/").
            append(this.cooccurrenceOutputFile);
		writer.openFile(savePath.toString(), false);
        int readStep = 1000;
        int readPosition = 0;
        List<TermMatrixEntry> matrixEntries;
		try {
			taghandlerDao.openConnection();
			taghandler_termLexiconDao.openConnection();
			do {
				matrixEntries = taghandlerDao.getTermMatrixEntries(readPosition, readStep, false);
				for (TermMatrixEntry matrixEntry : matrixEntries) {
					Term lexiconTerm1 = taghandler_termLexiconDao.getTerm(matrixEntry.getTerm1().getId());
					Term lexiconTerm2 = taghandler_termLexiconDao.getTerm(matrixEntry.getTerm2().getId());
					if (lexiconTerm1 != null && lexiconTerm2 != null) {
						StringBuilder line = new StringBuilder();
						line.append(lexiconTerm1.getValue()).append(",").
						append(lexiconTerm2.getValue()).append(",").
						append(matrixEntry.getCoocurrence()).append(",").
						append(matrixEntry.getSyntag());
						writer.println(line.toString());
					} else {
						if (lexiconTerm1 == null) {
							logger.error("term1 with id='"+matrixEntry.getTerm1().getId()+
								"' is not in term lexicon, matrix entry skipped.");
						}
						if (lexiconTerm2 == null) {
							logger.error("term2 with id='"+matrixEntry.getTerm2().getId()+
								"' is not in term lexicon, matrix entry skipped.");
						}
					}
				}
				readPosition += readStep-1;
			} while(matrixEntries != null && !matrixEntries.isEmpty());
			taghandlerDao.closeConnection(false);
			taghandler_termLexiconDao.closeConnection(false);
		} catch(DaoException e) {
			logger.error(e.getMessage());
			System.exit(1);
		}
        writer.closeFile();
		logger.info("Time needed: "+pu.getTimeAndStop()+" ms.");
    }

    /**
     * Starts the write process for the top related terms data.
     */
    private void topRelatedTermsOutput() {

		logger.info("write topRelatedTerms to file...");
		pu.startTimer();
        FileWriter writer = new FileWriter();
        StringBuilder savePath = new StringBuilder().append(statisticOutputDirectory).append("/").
            append(this.topRelatedTermsOutputFile);
		writer.openFile(savePath.toString(), false);
        int readStep = 1000;
        int readPosition = 0;
		// ignore minSyntag (set to 0)
		int minSyntag = 0;
        List<TermToTermsGroup> entries;
		try {
			taghandler_termLexiconDao.openConnection();
			do {

				entries = taghandlerDao.getTopRelatedTerms(readPosition, readStep, minSyntag);
				for (TermToTermsGroup entry : entries) {
					StringBuilder line = new StringBuilder();
					Term lexiconTerm = taghandler_termLexiconDao.getTerm(entry.getTerm().getId());
					if (lexiconTerm != null) {
						line.append(lexiconTerm.getValue()).append(": ");
						for (Term topRelatedTerm : entry.getRelatedTerms()) {
							Term lexiconTopRelatedTerm = taghandler_termLexiconDao.getTerm(topRelatedTerm.getId());
							int i = 0;
							if (lexiconTopRelatedTerm != null) {
								if (i != 0) {
									line.append(",");
								}
								line.append(lexiconTopRelatedTerm.getValue());
								i++;
							} else {
								logger.error("TopRelatedTerm with id='"+topRelatedTerm.getId()+
									"' related to term '"+lexiconTerm.getValue()+"' not found in term lexicon, skipping.");
							}
						}
						writer.println(line.toString());
					} else {
						logger.error("Term with id='"+entry.getTerm().getId()+"' not found in term lexicon, skipping.");
					}
				}
				readPosition += readStep-1;
			} while(entries != null && !entries.isEmpty());
			taghandler_termLexiconDao.closeConnection(false);
		} catch(DaoException e) {
			logger.error(e.getMessage());
			System.exit(1);
		}
        writer.closeFile();
		logger.info("Time needed: "+pu.getTimeAndStop()+" ms.");
    }

    /**
     * Sets the path of the directory the statictsics files should be written to.
     * @param path
     */
    public void setStatisticOutputDirectory(String path) {
        statisticOutputDirectory = path;
    }

    /**
     *
     * @param fileName
     */
    public void setTermLexiconOutputFileName(String fileName) {
        this.termLexiconOutputFile = fileName;
    }

    /**
     *
     * @param fileName
     */
    public void setCooccurrenceOutputFileName(String fileName) {
        this.cooccurrenceOutputFile = fileName;
    }

    /**
     * Sets the name of the file the top related term output should be written.
     * @param fileName
     */
    public void setTopRelatedTermsOutputFileName(String fileName) {
        this.topRelatedTermsOutputFile = fileName;
    }

    /**
     * Sets the name of the file the entity output should be written.
     * @param fileName
     */
    public void setEntityOutputFileName(String fileName) {
        this.entityOutputFileName = fileName;
    }

    /**
    * Run method for starting process as an thread.
    */
    @Override
    public void run() {
        this.writeStatistics();
    }

}
