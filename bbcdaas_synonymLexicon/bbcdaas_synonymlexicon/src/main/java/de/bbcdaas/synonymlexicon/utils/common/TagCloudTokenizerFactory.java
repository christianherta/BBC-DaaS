package de.bbcdaas.synonymlexicon.utils.common;

import java.util.StringTokenizer;

/**
 *
 * @author Robert Illers
 */
public class TagCloudTokenizerFactory {
	
	public static enum TAGCLOUD_INPUT_FORMAT {
		AUTODETECT, ONLY_TAGS, ENTITY_AND_TAGS
	}

	/**
	 * 
	 * @param tagCloudInputFormat
	 * @param line
	 * @param tagDelimiter
	 * @param entityToTagCloudDelimiter
	 * @return 
	 */
	public static StringTokenizer getStringTokenizer(TAGCLOUD_INPUT_FORMAT tagCloudInputFormat, String line,
		String tagDelimiter, String entityToTagCloudDelimiter) {
		
		TAGCLOUD_INPUT_FORMAT detectedTagCloudInputFormat = tagCloudInputFormat;
		StringTokenizer tokenizer = null;
		
		if (detectedTagCloudInputFormat == TAGCLOUD_INPUT_FORMAT.AUTODETECT) {
			
			if (line.split(entityToTagCloudDelimiter).length > 1) {
				detectedTagCloudInputFormat = TAGCLOUD_INPUT_FORMAT.ENTITY_AND_TAGS;
			} else {
				detectedTagCloudInputFormat = TAGCLOUD_INPUT_FORMAT.ONLY_TAGS;
			}
		}
		
		switch(detectedTagCloudInputFormat) {
			
			case ONLY_TAGS:
			
				tokenizer = new StringTokenizer(line, tagDelimiter);
			break;
			case ENTITY_AND_TAGS:
				
				String[] lineSplitted = line.split(entityToTagCloudDelimiter);
				tokenizer = new StringTokenizer(lineSplitted[1], tagDelimiter);
			break;
		}
		return tokenizer;
	}
}
