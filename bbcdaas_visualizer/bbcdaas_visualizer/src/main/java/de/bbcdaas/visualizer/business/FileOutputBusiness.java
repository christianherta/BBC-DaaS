package de.bbcdaas.visualizer.business;

import de.bbcdaas.taghandler.writer.mahout.TrainingDataWriter;
import de.bbcdaas.visualizer.constants.FileOutputConstants;
import java.io.File;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Robert Illers
 */
public final class FileOutputBusiness {

	private transient HttpServletRequest request;
	private final Logger logger = Logger.getLogger(FileOutputBusiness.class);

	public FileOutputBusiness(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Returns the real path to the output directories
	 * @param outtputPathType
	 * @return
	 */
	public String getFileOutputPath(int outputPathType) {

		StringBuilder path = new StringBuilder();
		File dirTest;
		StringBuilder webappPath = new StringBuilder().append(request.getSession().getServletContext().getRealPath("/")).append("..");
		String folderName;

		switch(outputPathType) {

			case FileOutputConstants.PATH_TYPE_TRAINING_DATA_OUTPUT:
				folderName = FileOutputConstants.FOLDER_NAME_TRAINING_DATA;
				break;
			case FileOutputConstants.PATH_TYPE_STATISTICS_OUTPUT:
				folderName = FileOutputConstants.FOLDER_NAME_STATISTICS;
				break;
			default:
				folderName = null;

		}

		if (folderName != null) {

			path.append(webappPath).append(File.separator).append(folderName);
			dirTest = new File(path.toString());

			if (!dirTest.exists()) {
				logger.error("path '"+path.toString()+"'does not exist");
			}
		}
		return path.toString();
	}

	/**
	 *
	 * @param maxNumberOfTerms
	 * @return
	 */
	public int createTermDictionary(int maxNumberOfTerms) {

		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		TrainingDataWriter trainingDataWriter = (TrainingDataWriter)classPathXmlApplicationContext.getBean("trainingDataWriter");
		return trainingDataWriter.createTermDictionary(this.getFileOutputPath(FileOutputConstants.PATH_TYPE_TRAINING_DATA_OUTPUT),
			maxNumberOfTerms);
	}

	/**
	 *
	 * @param dictionaryID
	 * @param minMatchingTerms
	 */
	public void createTrainingDataOutput(int dictionaryID, int minMatchingTerms) {

		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		TrainingDataWriter trainingDataWriter = (TrainingDataWriter)classPathXmlApplicationContext.getBean("trainingDataWriter");
		trainingDataWriter.createTermCloudTrainingFile(this.getFileOutputPath(FileOutputConstants.PATH_TYPE_TRAINING_DATA_OUTPUT), dictionaryID, minMatchingTerms);
	}

}
