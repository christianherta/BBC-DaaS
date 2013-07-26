package de.bbcdaas.disambiguation.wikipedia.tools;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.beans.document.Field;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.disambiguation.core.connector.lucene.LuceneConnector;
import de.bbcdaas.disambiguation.evaluation.core.GoldStandard;
import de.bbcdaas.disambiguation.evaluation.core.Tag;
import de.bbcdaas.disambiguation.evaluation.core.TagCloud;
import de.bbcdaas.disambiguation.wikipedia.dataimport.lucene.WikiContentHandler;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiConstants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.Version;

/**
 * Enhances the GoldStandard xml file containing tagclouds with tag names and
 * expected uris with nearBy URIs. A nearby-URI could be an URI from a
 * wikipedia disambiguation page.
 * @author Robert Illers
 */
public class GoldStandardEnhancer {

	private static final Logger logger = Logger.getLogger(GoldStandardEnhancer.class);
	public static final int ENHANCEMENTTYPE_DISAMBIGUATION_URI = 1;

	private int enhancementType;
	private LuceneConnector connector;
	// resource path
	private String xmlInputPath = "";
	// external path
	private String xmlOutputPath = "";
	private String uriPrefix = "";

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		Options options = new Options();
		options.addOption("help", false, "Shows the help");
		options.addOption("xmlOutputPathEn", true, "path to enhanced goldstandard xml output file");
		CommandLineParser parser = new PosixParser();
		HelpFormatter helpFormater = new HelpFormatter();

		try {

			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("help")) {
				helpFormater.printHelp("java -jar [fileName]", options);
			}

			if (cmd.hasOption("xmlOutputPathEn")) {

				String xmlOutputPath = cmd.getOptionValue("xmlOutputPathEn");
				LuceneConnector connector = new LuceneConnector(Version.LUCENE_35, WikiConstants.LUCENE_INDEX_PATH,
					new GermanAnalyzer(Version.LUCENE_35), null, 0);

				Map<String, String> params = new HashMap<String, String>();
				params.put("xmlInputPath", new StringBuilder().
					append(WikiConstants.DATA_FOLDER_NAME).append(File.separator).
					append(WikiConstants.DATA_EVALUATION_SUBFOLDER_NAME).append(File.separator).
					append(WikiConstants.DATA_EVALUATION_GOLDSTANDARD_FILE_NAME).toString());
				params.put("uriPrefix", "http://de.wikipedia.org/wiki/");
				if (xmlOutputPath != null && !xmlOutputPath.isEmpty()) {
					params.put("xmlOutputPath", xmlOutputPath);
				}

				GoldStandardEnhancer enhancer = new GoldStandardEnhancer(ENHANCEMENTTYPE_DISAMBIGUATION_URI, params);
				enhancer.setLuceneConnector(connector);
				try {
					enhancer.enhance();
				} catch (Exception ex) {
					logger.error(ex);
				}
			}
		} catch(ParseException ex) {
			logger.error(ex);
		}
	}

	/**
	 * Constructor
	 * @param enhancementType
	 * @param params
	 */
	public GoldStandardEnhancer(int enhancementType, Map<String, String> params) {

		this.enhancementType = enhancementType;
		Map<String, String> theParams = new HashMap<String, String>();
		if (params != null) {
			theParams.putAll(params);
		}
		switch(this.enhancementType) {
			case ENHANCEMENTTYPE_DISAMBIGUATION_URI:
				for (Map.Entry<String, String> param : theParams.entrySet()) {
					if (param.getKey().equals("xmlInputPath")) {
						this.xmlInputPath = param.getValue();
					} else
					if (param.getKey().equals("xmlOutputPath")) {
						this.xmlOutputPath = param.getValue();
					} else
					if (param.getKey().equals("uriPrefix")) {
						this.uriPrefix = param.getValue();
					}
				}
				break;
		}
	}

	/**
	 *
	 * @param connector
	 */
	public void setLuceneConnector(LuceneConnector connector) {
		this.connector = connector;
	}

	/**
	 * start the goldstandard enhance process
	 */
	public void enhance() throws Exception {

		switch(this.enhancementType) {
			case ENHANCEMENTTYPE_DISAMBIGUATION_URI:
				this.disambugiationURIEnhancer();
				break;
		}
	}

	/**
	 *
	 * @throws ApiException
	 * @throws JAXBException
	 */
	private void disambugiationURIEnhancer() throws ApiException, JAXBException {

		if (this.connector == null) {
			logger.error("Lucene Connector not set, abort.");
			return;
		}

		if (this.xmlInputPath == null || this.xmlInputPath.isEmpty()) {
			logger.error("'xmlInputPath' to old goldstandard xml not set, abort.");
			return;
		}

		if (this.xmlOutputPath == null || this.xmlOutputPath.isEmpty()) {
			logger.error("'xmlOutputPath' to new goldstandard xml not set, abort.");
			return;
		}

		if (this.uriPrefix == null || this.uriPrefix.isEmpty()) {
			logger.error("'uriPrefix' not set, abort.");
			return;
		}

		this.connector.getLuceneAPI().openConnection();

		JAXBContext jaxbContext = JAXBContext.newInstance(GoldStandard.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty("jaxb.formatted.output", true);
		InputStream input = getClass().getClassLoader().getResourceAsStream(this.xmlInputPath);
		GoldStandard goldStandard = (GoldStandard)unmarshaller.unmarshal(input);

		try {
			input.close();
		} catch (IOException ex) {
			logger.error(ex);
		}

		OutputStream output = null;
		try {
			File outputFile = new File(this.xmlOutputPath);
			logger.debug("Path to xml output file: "+outputFile.getAbsolutePath());
			if (!outputFile.exists()) {
				logger.debug("File does not exist");
				if (!outputFile.createNewFile()) {
					logger.error("File could not be created");
				}
			}
			output = new FileOutputStream(outputFile);
		} catch (FileNotFoundException ex) {
			logger.error(ex);
		} catch (IOException ex) {
			logger.error(ex);
		}

		for (TagCloud cloud  : goldStandard.getClouds()) {
			for (Tag tag : cloud.getTags()) {

				logger.debug("---------------------");
				logger.debug("Tag: "+tag.getName());
				List<String> nearByURIs = new ArrayList<String>();
				BooleanQuery query = new BooleanQuery();
				Query w1Query = new WildcardQuery(new Term(WikiContentHandler.FIELD_TITLE, tag.getName().toLowerCase()));
				Query w2Query = new WildcardQuery(new Term(WikiContentHandler.FIELD_TITLE,
					new StringBuilder(tag.getName().toLowerCase()).append(" (*)").toString()));
				query.add(w1Query, BooleanClause.Occur.SHOULD);
				query.add(w2Query, BooleanClause.Occur.SHOULD);
				List<Document> result = this.connector.getLuceneAPI().searchForDocuments(query, 999);

				for (Document document : result) {

					Field disURIsField = document.getFieldByName(WikiContentHandler.FIELD_ALTERNATIVE_TITLES);

					if (disURIsField != null) {

						String[] altTitles = disURIsField.getValue().split(Field.FIELD_VALUE_LIST_SEPARATOR);

						for (String altTitle : altTitles) {

							altTitle = altTitle.trim();
							altTitle = altTitle.toLowerCase();
							nearByURIs.add(new StringBuilder(this.uriPrefix).append(altTitle).toString());
						}
						break;
					}
				}

				for (String nearByURI : nearByURIs) {
					logger.debug(nearByURI);
				}
				tag.setNearByUris(nearByURIs);

			}
		}

		marshaller.marshal( goldStandard, output);
		try {
			output.close();
		} catch (IOException ex) {
			logger.debug(ex);
		}

		this.connector.getLuceneAPI().closeConnection();
	}
}
