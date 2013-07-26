package de.bbcdaas.taghandler.reader;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.common.beans.entity.TermCloudField;
import de.bbcdaas.taghandler.exception.ProcessException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads picture tags from a folder containing a list of files using the 
 * following pattern:
 * 
 * [pictureID],
 * [pictureURL],
 * "[mainTag_1|mainTag_2|...|mainTag_n]",
 * "[tag_1_1,tag_1_2,...,tag_1_n]",...,"[tag_m_1,tag_m_2,...,tag_m_n]",
 * lastTag_1, lastTag_2,...,lastTag_n
 * 
 * The reader reads entities by a given read step. If the end of a file is
 * reached, the reader uses the next available file in this folder.
 * @author Robert Illers
 */
public class PixolutionEntityFileReader extends AbstractEntityFileReader {

	// pattern:
	// [pictureID],
	// [pictureURL],
    // "[mainTag_1|mainTag_2|...|mainTag_n]",
    // "[tag_1_1,tag_1_2,...,tag_1_n]",...,"[tag_m_1,tag_m_2,...,tag_m_n]",
	// lastTag_1, lastTag_2,...,lastTag_n
	
	private String PICTURE_ID_TO_URL_SEPARATOR = ",";
	private String PICTURE_URL_TO_MAINTAGCLOUD_SEPARATOR = ",\"";
	private String MAINTAGCLOUD_TO_TAGCLOUDS_SEPARATOR = "\",\"";
	private String TAGCLOUD_TO_TAGCLOUD_SEPARATOR = "\",\"";
	private String TAGCLOUDS_TO_LASTTAGCLOUD_SEPARATOR = "\",";
	private String MAINTAG_TO_MAINTAG_SEPARATOR = "\\|";
	private String TAG_TO_TAG_SEPARATOR = ",";

	/**
	 * 
	 * @return 
	 */
	public String getMainTagCloudToTagCloudsSeparator() {
		return MAINTAGCLOUD_TO_TAGCLOUDS_SEPARATOR;
	}

	/**
	 * injected by Spring
	 * @param mainTagCloudToTagCloudsSeparator 
	 */
	public void setMainTagCloudToTagCloudsSeparator(String mainTagCloudToTagCloudsSeparator) {
		this.MAINTAGCLOUD_TO_TAGCLOUDS_SEPARATOR = mainTagCloudToTagCloudsSeparator;
	}

	/**
	 * 
	 * @return 
	 */
	public String getMainTagToMainTagSeparator() {
		return MAINTAG_TO_MAINTAG_SEPARATOR;
	}

	/**
	 * injected by Spring
	 * @param mainTagToMainTagSeparator 
	 */
	public void setMainTagToMainTagSeparator(String mainTagToMainTagSeparator) {
		this.MAINTAG_TO_MAINTAG_SEPARATOR = mainTagToMainTagSeparator;
	}

	/**
	 * 
	 * @return 
	 */
	public String getPictureIdToURLSeparator() {
		return PICTURE_ID_TO_URL_SEPARATOR;
	}

	/**
	 * injected by Spring
	 * @param pictureIdToURLSeparator 
	 */
	public void setPictureIdToURLSeparator(String pictureIdToURLSeparator) {
		this.PICTURE_ID_TO_URL_SEPARATOR = pictureIdToURLSeparator;
	}

	/**
	 * 
	 * @return 
	 */ 
	public String getPictureUrlToMainTagCloudSeparator() {
		return PICTURE_URL_TO_MAINTAGCLOUD_SEPARATOR;
	}

	/**
	 * injected by Spring
	 * @param pictureUrlToMainTagCloudSeparator 
	 */
	public void setPictureUrlToMainTagCloudSeparator(String pictureUrlToMainTagCloudSeparator) {
		this.PICTURE_URL_TO_MAINTAGCLOUD_SEPARATOR = pictureUrlToMainTagCloudSeparator;
	}

	/**
	 * 
	 * @return 
	 */
	public String getTagCloudsToLastTagCloudSeparator() {
		return TAGCLOUDS_TO_LASTTAGCLOUD_SEPARATOR;
	}

	/**
	 * injected by Spring
	 * @param tagCloudsToLastTagCloudSeparator 
	 */
	public void setTagCloudsToLastTagCloudSeparator(String tagCloudsToLastTagCloudSeparator) {
		this.TAGCLOUDS_TO_LASTTAGCLOUD_SEPARATOR = tagCloudsToLastTagCloudSeparator;
	}

	/**
	 * 
	 * @return 
	 */
	public String getTagCloudToTagCloudSeparator() {
		return TAGCLOUD_TO_TAGCLOUD_SEPARATOR;
	}

	/**
	 * injected by Spring
	 * @param tagCloudToTagCloudSeparator 
	 */
	public void setTagCloudToTagCloudSeparator(String tagCloudToTagCloudSeparator) {
		this.TAGCLOUD_TO_TAGCLOUD_SEPARATOR = tagCloudToTagCloudSeparator;
	}

	/**
	 * 
	 * @return 
	 */
	public String getTagToTagSeparator() {
		return TAG_TO_TAG_SEPARATOR;
	}

	/**
	 * 
	 * @param tagToTagSeparator 
	 */
	public void setTagToTagSeparator(String tagToTagSeparator) {
		this.TAG_TO_TAG_SEPARATOR = tagToTagSeparator;
	}
	
	/**
	 * 
	 * @return
	 * @throws ProcessException 
	 */
	@Override
	public List<Entity> readEntities() throws ProcessException {
		
		List<Entity> entities = new ArrayList<Entity>();
		
		List<String> inputFileNames = this.getFileNamesInInputFolder();
		
		long nbOfCurrentReadEntities = 0;
		String pictureURL;
		String mainTagCloud; // first tag cloud separated by '|'
		List<String> tagClouds = new ArrayList<String>(); // other tag clouds separated by ','
		int currentFileCounter = 0;
		
		for (String inputFileName : inputFileNames) {
			
			currentFileCounter++;
			
			// take current file working on
			if (currentFileCounter == this.currentFileNumber) {
			
				// open stream if closed
				this.openReader(inputFileName);
				
				// read one read-step of entities line by line
				while (scanner.hasNext() && nbOfCurrentReadEntities < ENTITY_READ_STEP) {

					pictureURL = null;
					mainTagCloud = null;
					tagClouds.clear();

					// read the line
					String line = scanner.next();
					//logger.debug("line: "+line);
					
					// split id from line and ignore it
					String[] id_data = line.split(PICTURE_ID_TO_URL_SEPARATOR,2);
					if (id_data.length != 2) {
						//logger.debug("Parsing error, skipping line (Error1)");
						numberOfTotalReadLines++;
						continue;
					}
					
					// split url from line
					String[] url_data = id_data[1].split(PICTURE_URL_TO_MAINTAGCLOUD_SEPARATOR,2);
					if (url_data.length != 2) {
						//logger.debug("Parsing error, skipping line (Error2)");
						numberOfTotalReadLines++;
						continue;
					}
					pictureURL = url_data[0];
					
					// split tagcloud from maintagcloud
					String[] mainTagCloud_data = url_data[1].split(MAINTAGCLOUD_TO_TAGCLOUDS_SEPARATOR,2);
					if (mainTagCloud_data.length != 2) {
						//logger.debug("Parsing error, skipping line (Error3)");
						numberOfTotalReadLines++;
						continue;
					}
					mainTagCloud = mainTagCloud_data[0];
					
					// split into tagclouds
					String [] tagClouds_lastTagCloud = mainTagCloud_data[1].split(TAGCLOUD_TO_TAGCLOUD_SEPARATOR);
					if (tagClouds_lastTagCloud.length > 1) {
						for (int i = 0; i < tagClouds_lastTagCloud.length-1;i++) {
							tagClouds.add(tagClouds_lastTagCloud[i]);
						}
					}
					String tagCloud_lastTagCloud_unsplitted = tagClouds_lastTagCloud[tagClouds_lastTagCloud.length-1];
					String [] tagCloud_lastTagCloud = tagCloud_lastTagCloud_unsplitted.split(TAGCLOUDS_TO_LASTTAGCLOUD_SEPARATOR);
					tagClouds.add(tagCloud_lastTagCloud[0]);
					if (tagCloud_lastTagCloud.length == 2) {
						tagClouds.add(tagCloud_lastTagCloud[1]);
					}

					/* debug logs */
					//logger.debug("pictureID: "+pictureID);
					//logger.debug("pictureURL: "+pictureURL);
					//logger.debug("mainTagCloud: "+mainTagCloud);
					//logger.debug("tagClouds: "+tagClouds);
					/* /debug logs */
					
					List<Term> rawTerms = new ArrayList<Term>();

					// split mainTagCloud into its tags
					String[] mainTags = mainTagCloud.split(MAINTAG_TO_MAINTAG_SEPARATOR);
					for (String mainTag : mainTags) {
						// add new term object
						Term rawTerm = new Term(mainTag);
						rawTerms.add(rawTerm);
					}

					// split tagClouds into its tags
					for (String tagCloud : tagClouds) {
						String[] tags = tagCloud.split(TAG_TO_TAG_SEPARATOR);
						for (String tag : tags) {
							// add new term object
							Term rawTerm = new Term(tag);
							rawTerms.add(rawTerm);
						}
					}

					/* debug logs */
					//StringBuilder rawTerms_String = new StringBuilder();
					//int i = 0;
					//for (Term rawTerm : rawTerms) {
					//	if (i != 0) {
					//		rawTerms_String.append(",");
					//	}
					//	rawTerms_String.append(rawTerm.getValue());
					//	i++;
					//}
					//logger.debug("Terms: "+rawTerms_String);
					//logger.debug("-----------------------------");
					/* /debug logs */
					
					/* build the entity with its fields */
					List<TermCloudField> fields = new ArrayList<TermCloudField>();

					TermCloudField field = new TermCloudField();
					field.setTerms(rawTerms);
					fields.add(field);

					Entity entity = new Entity();
					entity.setName(pictureURL);
					entity.setFields(fields);
					entities.add(entity);
					/* /build the entity with its fields */

					nbOfCurrentReadEntities++;
					numberOfTotalReadLines++;
				}

				// take next file if read step not reached
				if (nbOfCurrentReadEntities < ENTITY_READ_STEP) {
					this.currentFileNumber++;
					this.closeReader();
				} 
				// return result if read step reached
				else {
					break;
				}
			}
		}
		
		//logger.debug("entities.size(): "+entities.size());
		return entities;
	}
	
}
