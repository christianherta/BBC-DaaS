package de.bbcdaas.taghandler.reader;

import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.taghandler.TagHandler;
import de.bbcdaas.taghandler.exception.ProcessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.log4j.Logger;

/**
 * Base class for all entity file reader.
 * @author Robert Illers
 */
public abstract class AbstractEntityFileReader implements EntityReader {

	protected Logger logger = Logger.getLogger(this.getClass());
	protected String inputFolderName = "";
	protected String inputFolderPath = "";
	protected long numberOfLines;
	protected long numberOfTotalReadLines;
	protected int currentFileNumber = 1;
	protected int percentageRead;
	protected Scanner scanner = null;
	protected Reader reader = null;
	protected InputStream inputStream = null;
	protected int ENTITY_READ_STEP = 1000;
	protected String ENTITY_DELIMITER = "\\s*[\n]\\s*";

	/**
	 *
	 * @param entityReadStep
	 */
	@Override
	public void setEntityReadStep(int entityReadStep) {
		ENTITY_READ_STEP = entityReadStep;
	}

	/**
	 *
	 * @return long
	 */
	@Override
	public int getEntityReadStep() {
		return ENTITY_READ_STEP;
	}

	/**
	 * injected
	 * @param _entityDelimiter
	 */
	public void setEntityDelimiter(String entityDelimiter) {
		ENTITY_DELIMITER = entityDelimiter;
	}

    /**
     *
     * @return
     */
    public String getEntityDelimiter() {
        return this.ENTITY_DELIMITER;
    }

	/**
	 *
	 * @return
	 * @throws ProcessException
	 */
	@Override
	public abstract List<Entity> readEntities() throws ProcessException;

	/**
	 *
	 * @param inputFolderName
	 */
	public void setInputFolderName(String inputFolderName) {
		this.inputFolderName = inputFolderName;
	}

	/**
	 * closes the input stream, the reader and the scanner associated to a
	 * open file.
	 */
	@Override
	public void closeReader() {

		if (scanner != null) {
			this.scanner.close();
			this.scanner = null;
			try {
				this.reader.close();
				this.inputStream.close();
			} catch(IOException ex) {
				logger.error(ex.getMessage());
			}
		}
	}

	/**
	 * Returns all file names of files in the given input directory.
	 * @return List of file names
	 */
	protected List<String> getFileNamesInInputFolder() {

		List<String> fileNames = new ArrayList<String>();
		inputFolderPath = new StringBuilder().append(this.inputFolderName).
			append(File.separator).toString();
		File folder = new File(inputFolderPath);
		if (!folder.isDirectory()) {
			CodeSource codeSource = TagHandler.class.getProtectionDomain().getCodeSource();
			File jarFile;
			String jarDir = "";
			try {
				jarFile = new File(codeSource.getLocation().toURI().getPath());
				jarDir = jarFile.getParentFile().getPath();
			} catch (URISyntaxException ex) {
				logger.error(ex.getMessage());
			}
			inputFolderPath = new StringBuilder().append(jarDir).append(File.separator).
				append(this.inputFolderName).append(File.separator).toString();
			folder = new File(inputFolderPath);
		}
		if (folder.isDirectory()) {
			for (File file : folder.listFiles()) {
				if (file.isFile()) {
					fileNames.add(file.getName());
				}
			}
		} else {
			logger.error("Folder '"+this.inputFolderName+"' does not exist.");
		}
		return fileNames;
	}

	/**
	 * Opens an input stream, a reader and a scanner to a file.
	 * @param inputFileName
	 * @throws ProcessException
	 */
	protected String openReader(String inputFileName) throws ProcessException {

		// check if file is at given path, if not get it from classpath
		if (scanner == null) {

            logger.info("Opening file '"+inputFileName+"'...");

			String inputFilePath;
			if (!inputFolderName.isEmpty()) {
				inputFilePath = new StringBuilder().append(inputFolderName).
					append(File.separator).append(inputFileName).toString();
			} else {
				inputFilePath = new StringBuilder().append(inputFileName).toString();
			}
			logger.info("inputFilePath: "+inputFilePath);
			// get jar path
			CodeSource codeSource = TagHandler.class.getProtectionDomain().getCodeSource();
			File jarFile;
			String jarDir = "";
			try {
				jarFile = new File(codeSource.getLocation().toURI().getPath());
				jarDir = jarFile.getParentFile().getPath();
				//logger.debug("jarDir: "+jarDir);
			} catch (URISyntaxException ex) {
				logger.error(ex.getMessage());
			}

			// open stream
			inputStream = getClass().getClassLoader().getResourceAsStream(inputFilePath);
			if (inputStream != null) {
				reader = new InputStreamReader(inputStream);
				inputFolderPath = new StringBuilder().append(this.inputFolderName).
					append(File.separator).toString();
				//logger.debug("inputFolderPath: "+inputFolderPath);
			} else {
				try {
					inputFolderPath = new StringBuilder().append(jarDir).append(File.separator).
						append(this.inputFolderName).append(File.separator).toString();
					//logger.debug("inputFolderPath: "+inputFolderPath);
					inputStream = new FileInputStream(new StringBuilder().append(jarDir).append(File.separator).append(inputFilePath).toString());
					reader = new InputStreamReader(inputStream, "UTF-8");
				} catch (IOException ex) {
					throw new ProcessException(ex.getMessage(), ProcessException.ERROR_CODE_READ_ENTITIES);
				}
			}

			// get number of lines with inputStreamReader
			numberOfLines = getNumberOfLines(reader);
            logger.info("numberOfLines: "+numberOfLines);

			// close stream and reset it
			try {
				reader.close();
				inputStream.close();
			} catch(IOException ex) {
				logger.error(ex.getMessage());
			}

			// open stream again
			inputStream = getClass().getClassLoader().getResourceAsStream(inputFilePath);
			if (inputStream != null) {
				reader = new InputStreamReader(inputStream);
			} else {
				try {
					inputStream = new FileInputStream(new StringBuilder().append(jarDir).append(File.separator).append(inputFilePath).toString());
					reader = new InputStreamReader(inputStream, "UTF-8");
				} catch (IOException ex) {
					throw new ProcessException(ex.getMessage(), ProcessException.ERROR_CODE_READ_ENTITIES);
				}
			}

			// open file scanner
			scanner = new Scanner(reader).useDelimiter(ENTITY_DELIMITER);
			//logger.debug("Scanner opened.");
			percentageRead = 0;
			numberOfTotalReadLines = 0;
		}
		return inputFolderPath;
	}

	/**
	 * Reads the number of lines of a file
	 * @param reader
	 * @return number of lines in file
	 */
	protected long getNumberOfLines(Reader reader) throws ProcessException {

		long count = 0;
		LineNumberReader ln = new LineNumberReader(reader);
		try {

			while (ln.readLine() != null){
				count++;
			}
			ln.close();

		} catch (IOException ex) {
			throw new ProcessException(ex.getMessage(), ProcessException.ERROR_CODE_READ_ENTITIES);
		}
		return count;
	}
}