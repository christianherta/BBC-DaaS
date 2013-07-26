package de.bbcdaas.uima_components.synonym_lexicon_output_reader;

import de.bbcdaas.common.util.FileReader;
import de.bbcdaas.uima_components.cas_types.filelinemetadata.FileLineMetadata;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.log4j.Logger;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

/**
 * Reads the paradigm output file of the synonym lexicon hadoop pipeline (job 13).
 * @author Robert Illers
 */
public class SynonymLexiconOutputReader extends CollectionReader_ImplBase {

	private static Logger logger = Logger.getLogger(SynonymLexiconOutputReader.class);
	public static final String PARAM_INPUTDIR = "inputDir";
	private File inputDirectory;
	private ArrayList<File> paradigmRelationInputFiles = new ArrayList<File>();
	private int currentFileIndex = 0;
	private Integer lineNumber = 0;
	private int percentageRead = 0;
	private boolean finished = false;
	private FileReader fileReader = new FileReader();

	/**
	 * 
	 * @throws ResourceInitializationException 
	 */
	@Override
	public void initialize() throws ResourceInitializationException {
	
		this.currentFileIndex = 0;
		this.percentageRead = 0;
		this.finished = false;
		this.paradigmRelationInputFiles.clear();
		this.fileReader.closeFile();
		this.inputDirectory = new File(((String) this.getConfigParameterValue(PARAM_INPUTDIR)).trim());
		
		if (!this.inputDirectory.exists() || !this.inputDirectory.isDirectory()) {
            throw new ResourceInitializationException(ResourceConfigurationException.DIRECTORY_NOT_FOUND,
				new Object[] { PARAM_INPUTDIR, this.getMetaData().getName(), this.inputDirectory.getPath() });
        }
        logger.info("input directory: "+this.inputDirectory.getAbsolutePath());
		
		File[] allInputFiles = this.inputDirectory.listFiles();
		
		if (allInputFiles.length == 0) {
			
            logger.error("No files in input directory.");
			throw new ResourceInitializationException();
        }

        Arrays.sort(allInputFiles);
		
		for (File inputFile : allInputFiles) {

		   if (!inputFile.isDirectory()) {
			   this.paradigmRelationInputFiles.add(inputFile);
		   }
		}
		
		logger.info("Number of input files to read: "+this.paradigmRelationInputFiles.size());
	} 
	 
	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws CollectionException 
	 */
	@Override
	public boolean hasNext() throws IOException, CollectionException {
		
		if (!this.finished) {
            this.finished = this.currentFileIndex < this.paradigmRelationInputFiles.size();
        }
		return !this.finished;
	}
	
	/**
	 * 
	 * @param cas
	 * @throws IOException
	 * @throws CollectionException 
	 */
	@Override
	public void getNext(CAS cas) throws IOException, CollectionException {
		
		if (!this.fileReader.isFileOpened()) {
			
			this.fileReader.openFile(this.paradigmRelationInputFiles.get(this.currentFileIndex));
			this.lineNumber = 0;
		}
		
		String paradigmRelationString = this.fileReader.readln();
		
		if (paradigmRelationString == null) {
			
			this.fileReader.closeFile();
			this.currentFileIndex++;
			if (this.paradigmRelationInputFiles.size() > this.currentFileIndex) {
				
				this.fileReader.openFile(this.paradigmRelationInputFiles.get(this.currentFileIndex));
				this.lineNumber = 0;
			} else {
				
				this.finished = true;
				return;
			}
		} else {
			this.lineNumber++;
		}
		
		try {
			
			JCas jcas = cas.getJCas();
			jcas.setDocumentText(paradigmRelationString);
			FileLineMetadata metadata = new FileLineMetadata(jcas);
			metadata.setLineNumber(this.lineNumber.toString());
			jcas.addFsToIndexes(metadata);
		} catch (CASException e) {
            throw new CollectionException(e);
        }
		
		this.percentageRead = this.reportProgress(this.currentFileIndex,
			this.paradigmRelationInputFiles.size(), this.percentageRead);
	}

	/**
	 * 
	 * @return 
	 */
	@Override
	public Progress[] getProgress() {
		return new Progress[] { new ProgressImpl(this.currentFileIndex, 
			this.paradigmRelationInputFiles.size(), Progress.ENTITIES) };
	}

	/**
	 * 
	 * @throws IOException 
	 */
	@Override
	public void close() throws IOException {
		
		this.finished = true;
		this.fileReader.closeFile();
	}	
	
	/**
	 * 
	 * @throws ResourceConfigurationException 
	 */
	@Override
    public void reconfigure() throws ResourceConfigurationException {
        
		super.reconfigure();
		try {
			this.initialize();
		} catch(ResourceInitializationException ex) {
			throw new ResourceConfigurationException(ex);
		}
    }
	
	/**
	 * 
	 * @return 
	 */
	public int getNumberOfInputFiles() {
        return this.paradigmRelationInputFiles.size();
    }
	
	/**
	 * 
	 * @param actual
	 * @param total
	 * @param percentage
	 * @return 
	 */
	private int reportProgress(long actual, long total, int percentage) {

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