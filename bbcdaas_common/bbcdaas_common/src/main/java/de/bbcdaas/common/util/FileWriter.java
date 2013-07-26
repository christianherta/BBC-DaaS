package de.bbcdaas.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import org.apache.log4j.Logger;
/**
 * Generic file writer for easier accessing files.
 * @author Robert Illers
 */
public final class FileWriter {

	private Logger logger = Logger.getLogger(FileWriter.class);
	private PrintWriter writer;

	/**
	 * Opens the handle to a file to write.
	 * @param outputFileName
	 * @param append
	 * @return true if file has been opened successful
	 */
	public boolean openFile(String outputFileName, boolean append) {
		try {
			writer = new PrintWriter(new FileOutputStream(new File(outputFileName), append));
		} catch (FileNotFoundException ex) {
			logger.error(ex.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Closes the handle to a file opened with @ #openFile .
	 */
	public void closeFile() {
		if (writer != null) {
			writer.flush();
			writer.close();
		}
	}

	/**
	 * Writes a line into the opnened file.
	 * @param line
	 */
	public void println(String line) {

            if (writer != null) {
                writer.println(line);
            } else {
                logger.error("No file set");
            }
	}

        /**
         * Deletes a directory with its content.
         * @param path
         * @return true if deleted successful
         */
        public boolean deleteDirectory(File path) {

            if (path.isDirectory() && path.exists()) {

                File[] files = path.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
            return (path.delete());
  }

}
