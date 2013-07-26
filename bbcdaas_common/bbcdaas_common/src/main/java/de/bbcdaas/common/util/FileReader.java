package de.bbcdaas.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * Generic file reader implementation for easier accessing files.
 * @author Robert Illers
 */
public class FileReader {

	// files can not be get from jar, get url instead
	public static enum FILE_OPENING_TYPE {
		ABSOLUTE, RELATIVE
	}
	
	private static Logger logger = Logger.getLogger(FileReader.class);
	private BufferedReader reader;
	private boolean fileOpened = false;

	/**
	 * Opens a handle to a specific file.
	 * @param inputFileName
	 * @return
	 */
	public boolean openFile(String inputFileName) {

		try {
			if (!this.fileOpened) {
				this.reader = new BufferedReader(new java.io.FileReader(inputFileName));
				this.fileOpened = true;
			} else {
				logger.error("Error in openFile(): A file has already been opened"
					+ " with this reader, close it first or use other reader.");
				return false;
			}
		} catch (FileNotFoundException ex) {
			logger.error(ex.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param inputFile
	 * @return 
	 */
	public boolean openFile(File inputFile) {
		
		try {
			if (!this.fileOpened) {
				this.reader = new BufferedReader(new java.io.FileReader(inputFile));
				this.fileOpened = true;
			} else {
				logger.error("Error in openFile(): A file has already been opened"
					+ " with this reader, close it first or use other reader.");
				return false;
			}
		} catch (FileNotFoundException ex) {
			logger.error(ex.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Closes the handle to the with @see #openFile opended file.
	 */
	public void closeFile() {
		if (this.reader != null) {
			try {
				this.fileOpened = false;
				this.reader.close();
			} catch (IOException ex) {
				logger.error(ex);
			}
		}
	}

	/**
	 * Reads a line from an opened file
	 * @return line
	 */
	public String readln() {

		String line = null;
		try {
			if (this.fileOpened) {
				line = reader.readLine();
			} else {
				logger.error("Error in readln(): File not opened.");
			}
		} catch (IOException ex) {
			logger.error(ex);
		}
		return line;
	}
	
	/**
	 * 
	 * @return 
	 */
	public boolean isFileOpened() {
		return this.fileOpened;
	}

	/**
	 * 
	 * @param path
	 * @param first
	 * @param second
	 * @param readFromJar
	 * @return 
	 */
	public PropertiesConfiguration readPropertiesConfig(String path, 
		FILE_OPENING_TYPE first, FILE_OPENING_TYPE second, boolean readFromJar) {
		
		PropertiesConfiguration config = null;
	
		File file = this.readFile(path, first, second);
		if (file != null) {
			try {
				config = new PropertiesConfiguration(file);
			} catch (ConfigurationException ex) {
				logger.error(ex);
			}
		} else 
		if (readFromJar) {
			
			URL url = this.getUrlByRelativePath(path, true);
			if (url != null) {
				try {
					config = new PropertiesConfiguration(url);
				} catch (ConfigurationException ex) {}
			}
		}
		if (config == null) {
			// TODO: throw exception
			String errorMsg = "Configuration file at absolute path '"+path+"' can not be opened.";
			logger.error(errorMsg);
	
		}
		return config;
	}
	
	/**
	 * Opens a file by a path, trying 3 ways to find it in the given order.
	 * @param path a string possible representing a kind of path to a file
	 * @param first possible values: ABSOLUTE, REALTIVE, JAR
	 * @param second possible values: ABSOLUTE, REALTIVE, JAR
	 * @param third possible values: ABSOLUTE, REALTIVE, JAR
	 * @return File or null
	 */
	public File readFile(String path, FILE_OPENING_TYPE first, FILE_OPENING_TYPE second) {
		
		File file = null;
		if (path != null && !path.isEmpty()) {
		
			switch(first) {
				case ABSOLUTE:
					file = new File(path);
				break;
				case RELATIVE:
					file = this.readFileRel(path, false);
				break;
			}

			if (file == null || !file.isFile() || !file.canRead()) {
				switch(second) {
					case ABSOLUTE:
						file = new File(path);
					break;
					case RELATIVE:
						file = this.readFileRel(path, false);
					break;
				}
			}
		}
		return file;
	}
	
	/**
	 * Read a file relative to the jar file
	 * @param relativePath relative path to a file relative from a jar or inside a jar
	 * @param insideJar if true the file will be opened from inside the jar
	 * @return file
	 */
	private File readFileRel(String relativePath, boolean insideJar) {

		File file = null;
		URL url = this.getUrlByRelativePath(relativePath, insideJar);
		
		if (url != null) {
			try {
				
				URI uri = url.toURI();
				file = new File(uri);
			} catch(URISyntaxException ex) {
				file = new File(url.getPath());
			} catch(IllegalArgumentException ex) {
				file = new File(url.getPath());
			}		
		}
		return file;
	}

	/**
	 * Gets the url of a relative path, supporting some incomplete paths and some
	 * system depending behaviors
	 * @param relativePath
	 * @param insideJar 
	 * @return url
	 */
	public URL getUrlByRelativePath(String relativePath, boolean insideJar) {

		// important: resource files are not a java.io.file in java for some reason!!!
		// creating a uri from a resource url is not possible
		// java.io.file wants a path or an uri -> does not work with resource paths
		// got this insign after hours of trying...
		URL url = null;
		// internal identifier for the method of finding the file, used for debugging
		int varCode = 0;
		int i = 0;
		boolean debug = false;
		// try to open file inside jar
		if (insideJar) {
			// variant 1
			i++;
			url = this.getClass().getResource(relativePath);
			if (url != null) {
				varCode = i;
				if (debug) {logger.info("varCode: "+varCode+", url: "+url.toString());}
			}
			// variant 2
			i++;
			if (varCode == 0) {
				url = this.getClass().getResource(new StringBuilder("\\").append(relativePath).toString());
				if (url != null) {
					varCode = i;
					if (debug) {logger.info("varCode: "+varCode+", url: "+url.toString());}
				}
			}
			// variant 3
			i++;
			if (varCode == 0) {
				url = this.getClass().getResource(new StringBuilder("/").append(relativePath).toString());
				if (url != null) {
					varCode = i;
					if (debug) {logger.info("varCode: "+varCode+", url: "+url.toString());}
				}
			}
			// variant 4
			i++;
			if (varCode == 0) {
				url = this.getClass().getResource(new StringBuilder("\\src\\main\\resources\\").append(relativePath).toString());
				if (url != null) {
					varCode = i;
					if (debug) {logger.info("varCode: "+varCode+", url: "+url.toString());}
				}
			}
			// variant 5
			i++;
			if (varCode == 0) {
				url = this.getClass().getResource(new StringBuilder("/src/main/resources/").append(relativePath).toString());
				if (url != null) {
					varCode = i;
					if (debug) {logger.info("varCode: "+varCode+", url: "+url.toString());}
				}
			}
			// variant 6
			i++;
			if (varCode == 0) {
				url = this.getClass().getResource(new StringBuilder("resources/").append(relativePath).toString());
				if (url != null) {
					varCode = i;
					if (debug) {logger.info("varCode: "+varCode+", url: "+url.toString());}
				}
			}
			// variant 7
			i++;
			if (varCode == 0) {
				url = this.getClass().getResource(new StringBuilder("resources\\").append(relativePath).toString());
				if (url != null) {
					varCode = i;
					if (debug) {logger.info("varCode: "+varCode+", url: "+url.toString());}
				}
			}
			/* ------------ class loader variants ------- */
			// variant 8
			i++;
			if (varCode == 0) {
				url = this.getClass().getClassLoader().getResource(relativePath);
				if (url != null) {
					varCode = i;
					if (debug) {logger.info("varCode: "+varCode+", url: "+url.toString());}
				}
			}
			// variant 9
			i++;
			if (varCode == 0) {
				url = this.getClass().getClassLoader().getResource(new StringBuilder("/").append(relativePath).toString());
				if (url != null) {
					varCode = i;
					if (debug) {logger.info("varCode: "+varCode+", url: "+url.toString());}
				}
			}
			// variant 10
			i++;
			if (varCode == 0) {
				url = this.getClass().getClassLoader().getResource(new StringBuilder("\\").append(relativePath).toString());
				if (url != null) {
					varCode = i;
					if (debug) {logger.info("varCode: "+varCode+", url: "+url.toString());}
				}
			}
			// variant 11
			i++;
			if (varCode == 0) {
				url = this.getClass().getClassLoader().getResource(new StringBuilder("resources/").append(relativePath).toString());
				if (url != null) {
					varCode = i;
					if (debug) {logger.info("varCode: "+varCode+", url: "+url.toString());}
				}
			}
			// variant 12
			i++;
			if (varCode == 0) {
				url = this.getClass().getClassLoader().getResource(new StringBuilder("resources\\").append(relativePath).toString());
				if (url != null) {
					varCode = i;
					if (debug) {logger.info("varCode: "+varCode+", url: "+url.toString());}
				}
			}
			// variant 13
			i++;
			if (varCode == 0) {
				url = this.getClass().getClassLoader().getResource(new StringBuilder("/resources/").append(relativePath).toString());
				if (url != null) {
					varCode = i;
					if (debug) {logger.info("varCode: "+varCode+", url: "+url.toString());}
				}
			}
			// variant 14
			i++;
			if (varCode == 0) {
				url = this.getClass().getClassLoader().getResource(new StringBuilder("\\resources\\").append(relativePath).toString());
				if (url != null) {
					varCode = i;
					if (debug) {logger.info("varCode: "+varCode+", url: "+url.toString());}
				}
			}
		} else {
			// variant 15
			i++;
			String jarFolder = this.getJarFolder();
			File file = new File(new StringBuilder().append(jarFolder).append(File.separator).
					append(relativePath).toString());
			varCode = file.isFile() && file.canRead() ? i : 0;
			// variant 16
			i++;
			if (varCode == 0) {
				file = new File(new StringBuilder().append(jarFolder).append(relativePath).toString());
				varCode = file.isFile() && file.canRead() ? i : 0;
			}
			
			if (varCode != 0) {
				try {
					url = file.toURI().toURL();
				} catch (MalformedURLException ex) {
					
					logger.error(ex);
					url = null;
					varCode = 0;
				}
			}
		}
		
		if (varCode == 0 || url == null) {
			
			logger.debug("no url found for relative path "+relativePath+", insideJar = "+insideJar);
			return null;
		} else {
			
			logger.debug("getUrlByRelativePath() result: "+url.toString()+", varCode: "+varCode);
			return url;
		} 
	}
	
	/**
	 * 
	 * @return 
	 */
	public String getJarFolder() {
		
		try {
		
			CodeSource codeSource = this.getClass().getProtectionDomain().getCodeSource();
			File jarFile = new File(codeSource.getLocation().toURI().getPath());
			if (jarFile.isFile()) {
				return jarFile.getParentFile().getPath();
			} else {
				return this.getJarFolder2();
			}
		} catch (Exception ex) {
			return this.getJarFolder2();
		}
	}
	
	/**
	 * 
	 * @return 
	 */
	private String getJarFolder2() {
		
		String name = this.getClass().getName().replace('.', '/');
		String pathConstruct = this.getClass().getResource("/"+name+".class").toString();
		pathConstruct = pathConstruct.replace('/', File.separatorChar);
		pathConstruct = pathConstruct.substring(0, pathConstruct.indexOf(".jar")+4);
		pathConstruct = pathConstruct.substring(pathConstruct.lastIndexOf(':')-1);
		return pathConstruct.substring(0, pathConstruct.lastIndexOf(File.separatorChar)+1);
	}
}
