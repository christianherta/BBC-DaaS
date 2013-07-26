package de.bbcdaas.synonymlexicon.utils.hadoop;

import de.bbcdaas.synonymlexicon.common.constants.SynLexConstants;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.log4j.Logger;
/**
 * An implementation of hadoop data access utils for simplifying purpose and
 * to centralize the usage of the hadoop file system.
 * @author Robert Illers
 */
public final class DataAccessUtils {

	private static Logger log = Logger.getLogger(DataAccessUtils.class);

	private DataAccessUtils() {};

	/**
	 * Important: copy files to classpath before handling the configuration to
	 * a new job, or the job will not know the files. 
	 * @param path
	 * @param conf
	 * @param fileNameFilter
	 * @throws IOException 
	 */
	public static void addFilePathToDistributedCache(Path path, Configuration conf, String fileNameFilter) throws IOException {
		
		FileSystem fs = path.getFileSystem(conf);
		FileStatus[] fileStatus = fs.listStatus(path);
		for (FileStatus status : fileStatus) {
			
			if (fileNameFilter != null && !fileNameFilter.isEmpty() && status.getPath().getName().contains(fileNameFilter) ||
				fileNameFilter == null ||
				fileNameFilter.isEmpty()) {
				
				DistributedCache.addFileToClassPath(status.getPath(), conf, fs);
			}
		}
	}
	
	/**
	 * 
	 * @param context
	 * @param pathStringFilter
	 * @param fileNameFilter
	 * @return
	 * @throws IOException 
	 */
	public static List<Path> getFilePathsFromDistributedCache(Mapper.Context context,
		String pathFilter, String fileNameFilter) throws IOException {
		
		List<Path> paths = new ArrayList<Path>();
		URI[] cacheFileUris = context.getCacheFiles();
		
		if (cacheFileUris == null) {
			return paths;
		}
		for (URI cacheFileUri : cacheFileUris) {
			
			Path path = new Path(cacheFileUri);
			
			if (pathFilter != null && !pathFilter.isEmpty() && path.toString().contains(pathFilter) ||
				pathFilter == null ||
				pathFilter.isEmpty()) {
			
				if (fileNameFilter != null && !fileNameFilter.isEmpty() && path.getName().contains(fileNameFilter) ||
					fileNameFilter == null || 
					fileNameFilter.isEmpty()) {

					paths.add(path);
				}
			}
		}
		return paths;
	}
	
	/**
	 * Gets the files data as a string object from a hdfs located file.
	 * @param conf hadoop configuration, needed for getting file system
	 * @param path absolute hdfs path to file
	 * @return String object containing file data (UTF-8)
	 */
	public static String getFileDataFromHDFS(Configuration conf, Path filePath) {

		StringBuilder fileData = new StringBuilder();
		FileSystem hdfs;

		try {
			hdfs = FileSystem.get(conf);
		} catch(IOException ex) {
			log.error("Error getting hdfs", ex);
			return fileData.toString();
		}

		BufferedReader reader = null;
		try {

			reader = new BufferedReader(new InputStreamReader(hdfs.open(filePath)));
			String line;
			while ((line = reader.readLine()) != null){
				fileData.append(line);
			}
		} catch(IOException ex) {
			log.error(ex);
			fileData = new StringBuilder();
		} finally {
			IOUtils.closeStream(reader);
		}
		return fileData.toString();
	}

	/**
	 * Writes the files data UTF formatted into a new file in the hdfs.
	 * @param conf hadoop configuration, needed for getting file system
	 * @param pathString absolute hdfs path to new file
	 * @param fileData String object containing file data
	 * @param dataType data type of the file data (1: string, 2: int)
	 * @return true if no error occured
	 */
	public static void addFileDataToHDFS(Configuration conf, Path path, String fileData) {

		FileSystem hdfs;
		try {
			hdfs = FileSystem.get(conf);
		} catch(IOException ex) {
			log.error("Error getting hdfs", ex);
			return;
		}

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(hdfs.create(path, true)));
			writer.write(fileData);
		} catch(IOException ex) {
			log.error(ex);
		} finally {
			IOUtils.closeStream(writer);
		}
	}

	/**
	 *
	 * @param conf hadoop configuration, needed for getting file system
	 * @param path
	 * @return
	 */
	public static boolean deletePathFromHDFS(Configuration conf, Path path) {

		FileSystem hdfs;
		try {
			hdfs = FileSystem.get(conf);
		} catch(IOException ex) {
			log.error("Error getting hdfs", ex);
			return false;
		}

		// delete file or delete directory recursivly
		try {
			hdfs.delete(path, true);
		} catch(IOException ex) {
			log.error(ex);
			return false;
		}

		return true;
	}

	/**
	 *
	 * @param file
	 * @return
	 */
	public static boolean deletePathFromLocal(Configuration conf, Path path) {

		FileSystem lfs;

		try {
			lfs = FileSystem.getLocal(conf);
		} catch(IOException ex) {
			log.error("Error getting local fs", ex);
			return false;
		}

		// delete file or delete directory recursivly
		try {
			lfs.delete(path, true);
		} catch(IOException ex) {
			log.error(ex);
			return false;
		}
		
		return true;
	}

	/**
	 * Copies a paths content to a local path.
	 * @param conf hadoop configuration, needed for getting file system
	 * @param hdfsSourcePath remote source path
	 * @param localDestPath local path
	 * @param deleteSource if true, sources will be deleted after copy process (move)
	 * @throws IOException
	 */
	public static void copyToLocalFile(Configuration conf, Path hdfsSourcePath,
		Path localDestPath, boolean deleteSource) throws IOException {

		FileSystem hdfs = null;
		try {
			hdfs = FileSystem.get(conf);
		} catch(IOException ex) {
			log.error("Error getting hdfs");
			throw ex;
		}
		if (hdfs != null) {
			hdfs.copyToLocalFile(deleteSource, hdfsSourcePath, localDestPath);

		}
	}
	
	/**
	 * 
	 * @param inputPath
	 * @param conf
	 * @return
	 * @throws IOException 
	 */
	public static List<MapFile.Reader> getMapFilePartReaders (Path inputPath, Configuration conf) throws IOException {
		
		List<MapFile.Reader> readers = new ArrayList<MapFile.Reader>();
		
		FileSystem fs = inputPath.getFileSystem(conf);
		FileStatus[] fileStatus = fs.listStatus(inputPath);
		Map<String, Path> partDirs = new TreeMap<String, Path>(); 
		for (FileStatus status : fileStatus) {
			if (status.isDirectory() && status.getPath().getName().startsWith("part")) {
				partDirs.put(status.getPath().getName(), status.getPath());
			}
		}
		// map file not splitted
		if (partDirs.isEmpty()) {
			readers.add(new MapFile.Reader(inputPath, conf));
		}
		// map file splitted
		else {
			for (Map.Entry<String, Path> partDir : partDirs.entrySet()) {
				readers.add(new MapFile.Reader(partDir.getValue(), conf));
			}
		}
		return readers;
	}
	
	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param <P>
	 * @param readers
	 * @param key
	 * @param value
	 * @param partitioner the partitioner used in the job who created the map files
	 * @return 
	 */
	public static <K extends Writable, V extends Writable, P extends Partitioner> MapFile.Reader getMapFilePartReaderByKey(List<MapFile.Reader> readers, K key, V value, P partitioner) {
		return readers.get(partitioner.getPartition(key, value, readers.size()));
	}
	
	/**
	 * 
	 * @param inputPath
	 * @param conf
	 * @return
	 * @throws IOException 
	 */
	public static List<SequenceFile.Reader> getSequenceFilePartReaders(Path inputPath, Configuration conf) throws IOException {
		
		List<SequenceFile.Reader> readers = new ArrayList<SequenceFile.Reader>();
		
		FileSystem fs = inputPath.getFileSystem(conf);
		FileStatus[] fileStatus = fs.listStatus(inputPath);
		Map<String, Path> partFiles = new TreeMap<String, Path>(); 
		for (FileStatus status : fileStatus) {
			if (status.isFile() && status.getPath().getName().startsWith("part")) {
				partFiles.put(status.getPath().getName(), status.getPath());
			}
		}
		
		for (Map.Entry<String, Path> partfile : partFiles.entrySet()) {
			readers.add(new SequenceFile.Reader(conf, SequenceFile.Reader.file(partfile.getValue())));
		}
		
		return readers;
	}
	
	/**
	 * 
	 * @param <K>
	 * @param <V>
	 * @param <P>
	 * @param readers
	 * @param key
	 * @param value
	 * @param partitioner the partitioner used in the job who created the sequence files
	 * @return 
	 */
	public static <K extends Writable, V extends Writable, P extends Partitioner> SequenceFile.Reader getSequenceFilePartReaderByKey(List<SequenceFile.Reader> readers, K key, V value, P partitioner) {
		return readers.get(partitioner.getPartition(key, value, readers.size()));
	}
	
	/**
	 * 
	 * @param conf
	 * @param outputDirBaseName
	 * @param outputLocalDir
	 * @param jobOutputDir
	 * @param inputRecordsCountFileName 
	 */
	public static void copyResultToLocalFileSystem(Configuration conf, String outputDirBaseName,
		String outputLocalDir, String jobOutputDir, String inputRecordsCountFileName) {
		
		try {
			DataAccessUtils.copyToLocalFile(conf, PathBuilder.buildAsPath(outputDirBaseName, jobOutputDir),
				PathBuilder.buildAsPath(outputLocalDir, jobOutputDir), false);
		} catch (IOException ex) {
			log.error(PathBuilder.buildAsString(outputDirBaseName, jobOutputDir) + "' not found in hdfs.", ex);
		}
		
		if (inputRecordsCountFileName != null) {
			
			try {

				DataAccessUtils.copyToLocalFile(conf, PathBuilder.buildAsPath(outputDirBaseName,
					SynLexConstants.JOB_COUNTER_FOLDER, jobOutputDir, inputRecordsCountFileName),
					PathBuilder.buildAsPath(outputLocalDir, SynLexConstants.JOB_COUNTER_FOLDER, 
					jobOutputDir, inputRecordsCountFileName), false);
			} catch(IOException ex) {
				log.error(PathBuilder.buildAsString(outputDirBaseName, SynLexConstants.
					JOB_COUNTER_FOLDER, jobOutputDir, inputRecordsCountFileName) + "' not found in hdfs.", ex);
			}
		}	
	}
}