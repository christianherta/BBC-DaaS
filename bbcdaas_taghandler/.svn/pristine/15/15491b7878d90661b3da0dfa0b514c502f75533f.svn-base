package de.bbcdaas.taghandler.writer.mahout;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.entity.TermCloudField;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.common.util.FileReader;
import de.bbcdaas.common.util.FileWriter;
import de.bbcdaas.common.util.PerformanceUtils;
import de.bbcdaas.taghandler.dao.TagHandlerDao;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Writer that creates files that can be used for mahout import.
 * @author Robert Illers
 */
public final class TrainingDataWriterImpl implements TrainingDataWriter {

	private TagHandlerDao taghandlerDao;
	private PerformanceUtils pu;
	private Logger logger = Logger.getLogger(TrainingDataWriterImpl.class);
	private String dictionaryOutputFileName;
	private String trainingDataOutputFileName;

	/**
	 *
	 * @param taghandlerDao
	 */
	public void setTaghandlerDao(TagHandlerDao taghandlerDao) {
		this.taghandlerDao = taghandlerDao;
	}

	/**
     *
     * @param dictionaryOutputFileName
     */
    public void setDictionaryOutputFileName(String dictionaryOutputFileName) {
        this.dictionaryOutputFileName = dictionaryOutputFileName;
    }

	/**
     *
     * @param trainingDataOutputFileName
     */
    public void setTrainingDataOutputFileName(String trainingDataOutputFileName) {
        this.trainingDataOutputFileName = trainingDataOutputFileName;
    }

	/**
	 *
	 * @param outputPath
	 * @param maxNumberOfTerms
	 * @return
	 */
	@Override
	public int createTermDictionary(String outputPath, int maxNumberOfTerms) {

		this.pu = new PerformanceUtils();
		//todo: implement dictionaryID
		int dictionaryID = 0;
		StringBuilder savePath = new StringBuilder().append(outputPath).
			append(File.separator).append(this.dictionaryOutputFileName).
			append("_").append(dictionaryID).append(".txt");

		/* log configuration informations */
        logger.info("<<--BBC-DaaS - Building Blocks for Cloud Data-as-a-Service-->>");
        logger.info("<<------------- TrainingDataWriter 1.0 --------------------->>");
        logger.info("");
		logger.info("write dictionary...");
		logger.info("Parameter:");
        logger.info("-------------------------------------------");
		logger.info("savePath= "+savePath.toString());
		logger.info("maxNumberOfTerms= "+maxNumberOfTerms);
		logger.info("");

		FileWriter writer = new FileWriter();
		writer.openFile(savePath.toString(), false);
		int readStep = 500;
        int readPosition = 0;
		int totalReadTerms = 0;

		List<Term> terms;
		StringBuilder line = new StringBuilder();
		try {
			do {
				if (totalReadTerms + readStep > maxNumberOfTerms) {
					readStep = maxNumberOfTerms - totalReadTerms;
				}
				terms = taghandlerDao.getTermsSortedByLocalFrequency(readPosition, readStep);
				for (Term term : terms) {
					totalReadTerms++;
					// pattern: newID::TermValue::termID
					line.append(totalReadTerms).append("::").append(term.getValue()).append("::").append(term.getId());
					writer.println(line.toString());
					line.delete(0, line.length());
				}
				readPosition += readStep-1;
			} while(terms != null && !terms.isEmpty() && maxNumberOfTerms - totalReadTerms > 0);
		} catch(DaoException e) {
			logger.error(e.getMessage());
			System.exit(1);
		}
		writer.closeFile();

		logger.info("Complete running time: "+pu.getRunningTimer()+" ms.");
		return dictionaryID;
	}

	/**
	 *
	 * @param outputPath
	 * @param dictionaryID
	 */
	@Override
	public void createTermCloudTrainingFile(String outputPath, int dictionaryID, int minMatchingTerms) {

		this.pu = new PerformanceUtils();
		StringBuilder savePath = new StringBuilder().append(outputPath).
			append(File.separator).append(this.trainingDataOutputFileName).
			append("_").append(dictionaryID).append(".txt");

		/* log configuration informations */
        logger.info("<<--BBC-DaaS - Building Blocks for Cloud Data-as-a-Service-->>");
        logger.info("<<------------- TrainingDataWriter 1.0 --------------------->>");
        logger.info("");
		logger.info("write training data...");
		logger.info("Parameter:");
        logger.info("-------------------------------------------");
		logger.info("savePath= "+savePath.toString());
		logger.info("");

		FileWriter writer = new FileWriter();
		writer.openFile(savePath.toString(), false);
		int readStep = 5000;
		int offset = 0;


		StringBuilder line;
		List<TermCloudField> fields;
		try {
			List<Term> dictionaryTerms = this.readTermDictionary(outputPath, dictionaryID);
			do {
				fields = taghandlerDao.getFieldsContainingAtLeastXTerms(dictionaryTerms,
					minMatchingTerms, offset, readStep);

				for (TermCloudField field : fields) {
					line = new StringBuilder().append(field.getID()).append("::");
					for (Term term : field.getTerms()) {
						if (term.getSecondId() == 0) {
							line.append("oldID_").append(term.getId()).append(",");
						} else {
							line.append(term.getSecondId()).append(",");
						}
					}
					line.deleteCharAt(line.length()-1);
					writer.println(line.toString());
				}
				offset += readStep;
			} while(fields.size() != readStep);
		} catch(DaoException e) {
			logger.error(e.getMessage());
			System.exit(1);
		}
		writer.closeFile();
		logger.info("Complete running time: "+pu.getRunningTimer()+" ms.");
	}

	/**
	 *
	 * @return
	 */
	public List<Term> readTermDictionary(String inputPath, int dictionaryID) {

		List<Term> terms = new ArrayList<Term>();
		StringBuilder readPath = new StringBuilder().append(inputPath).
			append(File.separator).append(this.dictionaryOutputFileName).
			append("_").append(dictionaryID).append(".txt");
		FileReader fileReader = new FileReader();
		fileReader.openFile(readPath.toString());

		String line;
		do {
			line = fileReader.readln();
			if (line != null) {
				String[] splitted = line.split("::");
				Term term = new Term();
				term.setSecondId(Integer.parseInt(splitted[0]));
				term.setValue(splitted[1]);
				term.setId(Integer.parseInt(splitted[2]));
				terms.add(term);
			}
		} while(line != null);
		fileReader.closeFile();
		return terms;
	}

}
