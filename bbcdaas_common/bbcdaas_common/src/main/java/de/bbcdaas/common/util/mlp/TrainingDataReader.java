package de.bbcdaas.common.util.mlp;

import de.bbcdaas.common.beans.mlp.Cloud;
import de.bbcdaas.common.beans.mlp.Term;
import de.bbcdaas.common.util.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;

/**
 *
 * @author Robert Illers
 */
public class TrainingDataReader {

	private static Logger logger = Logger.getLogger(TrainingDataReader.class);
	private List<Term> dictionaryTerms = new ArrayList<Term>();
	private List<Cloud> clouds = new ArrayList<Cloud>();
	private Map<Integer, Vector> cloudVectores = new HashMap<Integer, Vector>();

	public static void main(String[] args) {

		String termDictionary_path = null;
		String trainingData_path = null;
		if (args != null && args.length != 0) {
			trainingData_path = args[0];
			logger.info("TrainingData path from stdin parameter: "+trainingData_path);
			if (args.length > 1) {
				termDictionary_path = args[1];
				logger.info("termDictionary path from stdin parameter: "+termDictionary_path);
			}
		} else {
			logger.info("No stdin parameter specified.");
		}

		if (trainingData_path == null) {
			trainingData_path = "trainingData_0.txt";
		}

		if (termDictionary_path == null) {
			termDictionary_path = "dictionary_0.txt";
		}

		TrainingDataReader reader = new TrainingDataReader();
		reader.readTermDictionary(termDictionary_path);
		reader.readTermCloudTrainingFile(trainingData_path);

	}

	/**
	 *
	 * @return
	 */
	public Map<Integer, Vector> getCloudVectores() {
		return cloudVectores;
	}

	/**
	 *
	 * @param cloudVectores
	 */
	public void setCloudVectores(Map<Integer, Vector> cloudVectores) {
		this.cloudVectores = cloudVectores;
	}

	/**
	 *
	 * @return
	 */
	public List<Term> getDictionaryTerms() {
		return dictionaryTerms;
	}

	/**
	 *
	 * @param dictionaryTerms
	 */
	public void setDictionaryTerms(List<Term> dictionaryTerms) {
		this.dictionaryTerms = dictionaryTerms;
	}

	/**
	 *
	 * @return
	 */
	public List<Cloud> getClouds() {
		return clouds;
	}

	/**
	 *
	 * @param clouds
	 */
	public void setClouds(List<Cloud> clouds) {
		this.clouds = clouds;
	}

	/**
	 *
	 * @param path
	 */
	public void readTermDictionary(String path) {

		FileReader reader = new FileReader();
		reader.openFile(path);

		String line;
		this.dictionaryTerms.clear();
		do {

			line = reader.readln();
			if (line != null) {
				String[] splitted = line.split("::");
				int newTermId = Integer.parseInt(splitted[0]);
				String termValue = splitted[1];
				int termId = Integer.parseInt(splitted[2]);
				Term term = new Term();
				term.setNewId(newTermId);
				term.setOldId(termId);
				term.setValue(termValue);
				this.dictionaryTerms.add(term);
			}

		} while(line != null);

		reader.closeFile();
	}

	/**
	 *
	 * @param path
	 */
	public void readTermCloudTrainingFile(String path) {

		FileReader reader = new FileReader();
		reader.openFile(path);

		String line;
		this.clouds.clear();
		do {

			line = reader.readln();
			if (line != null) {
				Cloud cloud = new Cloud();
				String[] splitted1 = line.split("::");
				int cloudId = Integer.parseInt(splitted1[0]);
				cloud.setId(cloudId);
				String[] splitted2 = splitted1[1].split(",");
				List<Term> terms = new ArrayList<Term>();
				for (int i = 0; i <= splitted2.length;i++) {
					Term term = new Term();
					String oldId_String = splitted2[i];
					if (oldId_String.contains("oldID_")) {
						String [] splitted3 = oldId_String.split("_");
						term.setOldId(Integer.parseInt(splitted3[1]));
					} else {
						term.setNewId(Integer.parseInt(splitted2[i]));
					}
					terms.add(term);
				}
				cloud.setTerms(terms);
				this.clouds.add(cloud);
			}

		} while(line != null);

		reader.closeFile();
	}

	/**
	 *
	 */
	public boolean convertTrainingDataToMahoutVectores() {

		if (!this.clouds.isEmpty() && !this.dictionaryTerms.isEmpty()) {

			for (Cloud cloud : this.clouds) {
				Vector cloudVector = new DenseVector();
				for (int i = 1; i <= this.dictionaryTerms.size();i++) {
					for (Term term : cloud.getTerms()) {
						double vectorValue = 0;
						if (term.getNewId() == i) {
							vectorValue = 1;
						}
						cloudVector.set(i, vectorValue);
					}
				}
				this.cloudVectores.put(cloud.getId(), cloudVector);
			}
		} else {
			logger.error("Data not initilized.");
			return false;
		}
		return true;
	}
}
