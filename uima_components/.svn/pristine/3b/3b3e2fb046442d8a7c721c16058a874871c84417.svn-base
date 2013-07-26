package de.bbcdaas.uima_components.arc_collection_reader;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import de.bbcdaas.uima_components.cas_types.documentmetadata.DocumentMetadata;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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
import org.archive.io.ArchiveRecord;
import org.archive.io.ArchiveRecordHeader;

/**
 * <p>
 * The ArcCollectionReader reads a collection of heritrix archive files (ARC file format) from a configurable
 * input directory initializes new CAS instances with the documents HTML content.
 * <p>
 * The input directory is scanned for files ending in '*.arc.gz' during initialization of the collection
 * reader. The reader then iterates over all the heritrix archive files and over the web documents contained
 * in each archive file.
 *
 * <h2>Input</h2>
 * <p>
 * None
 *
 * <h2>Output</h2>
 * <p>
 * The reader sets the document text of the default view of the CAS with the web documents HTML content. In
 * addition, it sets the following to Feature Structures:
 *
 * <ul>
 * <item><b>DocumentMetadata</b> - this structure contains features for the document url, a unique id
 * created by computing the md5 hash sum for this url, information about the source arc file name and the
 * mime type of the web document</item> <item><b>SourceDocumentInformation</b> - this structure contains
 * the uri of the web document</item>
 * </ul>
 *
 * <h2>Parameters</h2>
 * <table>
 * <tr>
 * <td>InputDirectory</td>
 * <td>the path to the input directory</td>
 * </tr>
 * <tr>
 * <td>ProcessedArcsLogfile</td>
 * <td>the path and name of the logfile parameter in the UIMA descriptor used to log the processed arc file
 * status</td>
 * </tr>
 * </table>
 *
 * @author herta
 */
public class ArcCollectionReader extends CollectionReader_ImplBase {

    private static Logger logger = Logger.getLogger(ArcCollectionReader.class);
    private final CharsetDetector charsetDetector = new CharsetDetector();
    // the name of the input directory parameter in the UIMA descriptor
    public static final String PARAM_INPUTDIR = "InputDirectory";
    // the path and name of the logfile parameter in the UIMA descriptor used
	// to log the processed arc file
    public static final String PARAM_PROCESSEDARCSLOG = "ProcessedArcsLogfile";
    private int mCurrentIndex = 0;
	private int percentageRead = 0;
    ArrayList<File> mAllArcFiles = new ArrayList<File>();
    // internaly used iterator over records in one arc-file
    private Iterator<ArchiveRecord> arcFileIterator = null;
    // a counter of all records processed by this reader
    private int recordNumber = 0;
    private File inputDirectory;
    private List<String> alreadyProcessedFiles = new ArrayList<String>();
    // file to store logging information about processed arc files
    File processedArcLogfile;
    // will be set to true in close()
    private boolean isFinished = false;

    /**
     * file filter for "arc.gz" files
     * @author
     */
    public static class ArcGzFilter implements FilenameFilter {

        private static final String ARC_GZ_FILEENDING = "arc.gz";

        /**
         * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
         */
		@Override
        public boolean accept(File dir, String name) {
            if (name.endsWith(ARC_GZ_FILEENDING)) {
                return true;
            }
            return false;
        }
    }

    /**
     * @see org.apache.uima.collection.CollectionReader_ImplBase#initialize()
     */
    @Override
    public void initialize() throws ResourceInitializationException {

		this.mCurrentIndex = 0;
        this.mAllArcFiles.clear();
        // initialize input directory parameter
        this.inputDirectory = new File(((String) this.getConfigParameterValue(PARAM_INPUTDIR)).trim());
        // if input directory does not exist or is not a directory, throw exception
        if (!this.inputDirectory.exists() || !this.inputDirectory.isDirectory()) {
            throw new ResourceInitializationException(ResourceConfigurationException.DIRECTORY_NOT_FOUND,
				new Object[] { PARAM_INPUTDIR, this.getMetaData().getName(), this.inputDirectory.getPath() });
        }
        logger.info("Reading heritrix arc-files from input directory: "+this.inputDirectory.getAbsolutePath());

        // initialize the processed Arc-Logfile parameter
        String processedArcLogPath = (String) this.getConfigParameterValue(PARAM_PROCESSEDARCSLOG);
		if (processedArcLogPath != null) {
            this.processedArcLogfile = new File(processedArcLogPath);
			if (!this.processedArcLogfile.exists()) {
				try {
					this.processedArcLogfile.createNewFile();
				} catch(IOException ex) {
					logger.error("logfile for processed arc-files '"+this.processedArcLogfile.getAbsolutePath()+"' could not be created.", ex);
					this.processedArcLogfile = null;
				}
 			}
        }

		// try to get a list of all already processed arc-files.
        this.alreadyProcessedFiles = this.readLogOfProcessedFiles(this.processedArcLogfile);

        // get a (sorted) list of files (not subdirectories) in the specified directory
        File[] allArcFiles = this.inputDirectory.listFiles(new ArcGzFilter());

        if (allArcFiles.length == 0) {
            logger.error("No files with extension "+ArcGzFilter.ARC_GZ_FILEENDING+" found in input directory.");
			throw new ResourceInitializationException();
        }

        Arrays.sort(allArcFiles);

        for (File arcfile : allArcFiles) {

			if (!arcfile.isDirectory()) {

				String arcFileName = arcfile.getName();
				// check that arcfiles name is not on the list of already processed files
                if (!this.alreadyProcessedFiles.contains(arcFileName)) {
                    this.mAllArcFiles.add(arcfile);
				}
            }
        }
		logger.info("Number of arcfiles to read: "+this.mAllArcFiles.size());

        // initialize current iterator
        if (this.mAllArcFiles.isEmpty()) {
			logger.info("No new files to read.");
		} else {
            try {
                this.arcFileIterator = new ArcFileIterator(this.mAllArcFiles.get(this.mCurrentIndex));
            } catch (IOException e) {
                throw new ResourceInitializationException(e);
            }
        }
    }

    /**
     * @see org.apache.uima.collection.CollectionReader#getNext(org.apache.uima.cas.CAS)
     */
	@Override
    public void getNext(CAS acas) throws IOException, CollectionException {
        getNext(acas, true);
    }

    /**
     * skips a document in this reader without reading the content into a new cas
     * @throws IOException
     * @throws CollectionException
     */
    public void skipCas() throws IOException, CollectionException {
        this.getNext(null, false);
    }

	/**
	 *
	 * @param acas
	 * @param fillCas
	 * @throws IOException
	 * @throws CollectionException
	 */
    private void getNext(CAS acas, boolean fillCas) throws IOException, CollectionException {

		String currentArcfileName = this.mAllArcFiles.get(this.mCurrentIndex).getName();
        ArchiveRecord record = null;
		
        if (this.arcFileIterator.hasNext()) {
            record = this.arcFileIterator.next();
        }
		
        if (record != null) {
			
            ArchiveRecordHeader header = record.getHeader();
            String mimetype = header.getMimetype();
            // skip header
            record.skip(header.getContentBegin());
            // dump content of record in outputstream
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            record.dump(outStream);
            // find the document's encoding and convert it's bytes into a string.
            String documentText = this.getDocumentString(outStream.toByteArray());
            outStream.close();

            // check mime type and skip document if its not matching
            if (!mimetype.toLowerCase().startsWith("text/html")) {
                logger.error("!mimetype.toLowerCase().startsWith('text/html')");
				getNext(acas, fillCas);
                return;
            }

            if (fillCas) {
            	fillContentOfCAS(acas, currentArcfileName, header, mimetype, documentText);
            }
            this.recordNumber++;
        }

        // if iterator has no next record, delete it and increase count for Arc-File to process
        if (!this.arcFileIterator.hasNext()) {
            // log current arcfile name for checkpointing
            this.markArcFileAsProcessed(currentArcfileName);
            this.mCurrentIndex++;
            if (this.mCurrentIndex < this.mAllArcFiles.size()) {
				// log progress
				if (this.mCurrentIndex % 5 == 0) {
					System.out.print(".");
				}
				this.percentageRead = this.reportProgress(this.mCurrentIndex, this.mAllArcFiles.size(), this.percentageRead);
                this.arcFileIterator = new ArcFileIterator(this.mAllArcFiles.get(this.mCurrentIndex));
            }
        }
    }

	/**
	 *
	 * @param acas
	 * @param currentArcfileName
	 * @param header
	 * @param mimetype
	 * @param documentText
	 * @throws CollectionException
	 */
    private void fillContentOfCAS(CAS acas, String currentArcfileName, ArchiveRecordHeader header,
            String mimetype, String documentText) throws CollectionException {

		// fill content in CAS
        try {
            JCas jcas = acas.getJCas();
            jcas.setDocumentText(documentText);
            DocumentMetadata metadata = new DocumentMetadata(jcas);
            String docUrl = header.getUrl();
            String id = header.getUrl();
            metadata.setDocumentID(id);
            metadata.setDocumentURL(docUrl);
            metadata.setSource(currentArcfileName);
            metadata.setMimeType(mimetype);
            jcas.addFsToIndexes(metadata);
            //logger.info(currentArcfileName + ":" + this.recordNumber + ", document url: " + docUrl);
		} catch (CASException e) {
            throw new CollectionException(e);
        }
    }

    /**
     * Gives byteOS.toByteArray into charsetDetector and uses the returned
	 * charset to convert the byte array into a string.
     * @param bytes
     * @return the converted String
     */
    String getDocumentString(byte[] bytes) {

		CharsetMatch detectedCharset;

		try {
            this.charsetDetector.setText(bytes);
            detectedCharset = this.charsetDetector.detect();
        } catch (Exception e) {
            return new String(bytes);
        }

		String charsetName = null;

		if (detectedCharset != null) {
			charsetName = detectedCharset.getName();
		}

		if (charsetName != null) {
            try {
                return new String(bytes, charsetName);
            } catch (UnsupportedEncodingException e) {
                return new String(bytes);
            }
        }
        return new String(bytes);
    }

    /**
     * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#close()
     */
	@Override
    public void close() {
        this.isFinished = true;
    }

    /**
     * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#getProgress()
     */
	@Override
    public Progress[] getProgress() {
        return new Progress[] { new ProgressImpl(this.mCurrentIndex, this.mAllArcFiles.size(),
                Progress.ENTITIES) };
    }

    /**
     * @see org.apache.uima.collection.base_cpm.BaseCollectionReader#hasNext()
     */
	@Override
    public boolean hasNext() {

        if (this.isFinished) {
            return false;
        }

        boolean result = false;

		// check if we have more arc files in the input directory to process
        if (this.mCurrentIndex < this.mAllArcFiles.size()) {
            // if yes, check if there is any record waiting in the current arc file
            if (this.arcFileIterator != null) {
                result = this.arcFileIterator.hasNext();
            }
        }
        return result;
    }

    /**
     * @see org.apache.uima.collection.CollectionReader_ImplBase#reconfigure()
     */
    @Override
    public void reconfigure() throws ResourceConfigurationException {
        super.reconfigure();
        this.isFinished = false;
    }

    /**
     * @return the number of ARC-Files this reader has to process
     */
    int getNumberOfArcFiles() {
        return this.mAllArcFiles.size();
    }

    /**
     * This method adds the given arc file name to a file called "processedArcs" in the arc-files input
     * folder. If no such file exists yet in the input folder, it is created there
     * @param arcfileName the name of the processed arc-file
     */
    void markArcFileAsProcessed(String arcfileName) {

		// check if the parameter for reader was set
        if (this.processedArcLogfile != null) {

			PrintStream outputFile = null;
			try {
				outputFile = new PrintStream(new FileOutputStream(this.processedArcLogfile, true));
				outputFile.println(arcfileName);
			} catch (FileNotFoundException e) {
				logger.error("error writing to file file '" + this.processedArcLogfile.getAbsoluteFile(), e);
			} finally {
				if (outputFile != null) {
					outputFile.close();
				}
			}
		}
    }

    /**
     * reads the lines in the current file and returns it to be used as list of
	 * already processed arcfiles
     * @param processedFileLog
     * @return a list of already processed arc files.
     */
    public List<String> readLogOfProcessedFiles(File processedFileLog) {

		List<String> processedFileList = new ArrayList<String>();

		if (this.processedArcLogfile != null) {

			try {
				BufferedReader logFileReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(processedFileLog)));
				String line;
				while((line = logFileReader.readLine()) != null) {
					processedFileList.add(line);
				}
				logFileReader.close();
			} catch (IOException e) {
				logger.error("Could not read logfile containing processed arc-files. Continuing with empty list."+e.getMessage());
			}
		}
        return processedFileList;
    }

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
