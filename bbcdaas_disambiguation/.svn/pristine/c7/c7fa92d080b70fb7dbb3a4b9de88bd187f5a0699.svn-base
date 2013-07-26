package de.bbcdaas.disambiguation.wikipedia.dataimport.lucene;

import de.bbcdaas.common.util.FileReader;
import de.bbcdaas.common.util.SaxParser;
import de.bbcdaas.disambiguation.core.connector.lucene.LuceneConnector;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiConstants;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

/**
 * WikiDataImporter, using the WikiContentHandler.
 * @author Robert Illers
 */
public final class WikiDataImporter {

	public static final Logger logger = Logger.getLogger(WikiDataImporter.class);

    /**
	 * Main method invoked on starting application that starts a data import.
	 * @param args first parameter is an optional path to the config file
	 */
	public static void main(String[] args) {

		Options options = new Options();
		options.addOption("help", false, "Shows the help");
		options.addOption("propertiesFilePathLi", true, "path to lucene import properties file");
		CommandLineParser parser = new PosixParser();
		HelpFormatter helpFormater = new HelpFormatter();

		try {

			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("help")) {
				helpFormater.printHelp("java -jar [fileName]", options);
			}

			if (cmd.hasOption("propertiesFilePathLi")) {

				Configuration config = new FileReader().readPropertiesConfig(cmd.getOptionValue("propertiesFilePathLi"),
					FileReader.FILE_OPENING_TYPE.ABSOLUTE, FileReader.FILE_OPENING_TYPE.RELATIVE, false);

				if (config != null) {

					logger.info("Using parameter from external file...");

					Version luceneVersion = Version.LUCENE_35;
					String xmlFilePath = config.getString("xmlFilePath");
					String uri = config.getString("uri");
					String indexPath = config.getString("indexPath");
					Set<String> stopwords = new TreeSet(Arrays.asList(config.getStringArray("stopwords")));
					Set<String> startElements = new TreeSet(Arrays.asList(config.getStringArray("startElements")));
					Map<String, Set<String>> exclusions = new HashMap<String, Set<String>>();
					int i = 0;
					for (String startElement : startElements) {
						exclusions.put(startElement, new TreeSet(Arrays.asList(config.
							getStringArray(new StringBuilder("exclusions_").append(i).toString()))));
						i++;
					}
					String standardAnalyzerName = config.getString("standardAnalyzer", "StandardAnalyzer");
					String[] fieldAnalyzerNames = config.getStringArray("fieldAnalyzer");
					int limitedDocsCount = config.getInt("limitedDocsCount");
					int limitedTokenCount = config.getInt("limitedTokenCount");
					String categoryPattern = config.getString("categoryPattern");
					String redirectPattern = config.getString("redirectPattern");
					String categoryTitlePattern = config.getString("categoryTitlePattern");
					String alternativeURIsPattern = config.getString("alternativeURIsPattern");
					List<String> keywordPattern = Arrays.asList(config.getStringArray("keywordPattern"));

					logger.info("Parameters read from config file:");
					logger.info(new StringBuilder("xmlFilePath: ").append(xmlFilePath).toString());
					logger.info(new StringBuilder("uri: ").append(uri).toString());
					logger.info(new StringBuilder("indexPath: ").append(indexPath).toString());
					logger.info(new StringBuilder("stopwords: ").append(stopwords.toString()).toString());
					logger.info(new StringBuilder("startElements: ").append(startElements.toString()).toString());
					for (Entry<String, Set<String>> anExclusions : exclusions.entrySet()) {
						logger.info(new StringBuilder(anExclusions.getKey()).append(": ").append(anExclusions.getValue().toString()).toString());
					}
					logger.info(new StringBuilder("limitedDocsCount: ").append(limitedDocsCount).toString());
					logger.info(new StringBuilder("limitedTokenCount: ").append(limitedTokenCount).toString());
					logger.info(new StringBuilder("redirectPattern: ").append(redirectPattern).toString());
					logger.info(new StringBuilder("alternativeURIsPattern: ").append(alternativeURIsPattern).toString());
					logger.info(new StringBuilder("keywordPattern: ").append(keywordPattern).toString());

					// set the standard analyzer
					Analyzer standardAnalyzer;
					if (standardAnalyzerName.equals("StandardAnalyzer")) {
						standardAnalyzer = new StandardAnalyzer(luceneVersion);
					} else
					if (standardAnalyzerName.equals("GermanAnalyzer")) {
						standardAnalyzer = new GermanAnalyzer(luceneVersion, stopwords);
					} else
					if (standardAnalyzerName.equals("EnglishAnalyzer")) {
						standardAnalyzer = new EnglishAnalyzer(luceneVersion, stopwords);
					}else {
						standardAnalyzer = new StandardAnalyzer(luceneVersion);
					}
					logger.info(new StringBuilder("standardAnalyzer: ").append(standardAnalyzer.getClass().toString()).toString());

					Map<String, Analyzer> fieldAnalyzerMap = new HashMap<String, Analyzer>();
					if (fieldAnalyzerNames != null) {
						for (String fieldAnalyzerName : fieldAnalyzerNames) {
							String[] fieldAnalyzerName_splitted = fieldAnalyzerName.split(":");
							// set the field analyzer
							Analyzer fieldAnalyzer;
							if (fieldAnalyzerName_splitted[1].equals("StandardAnalyzer")) {
								fieldAnalyzer = new StandardAnalyzer(luceneVersion);
							} else
							if (fieldAnalyzerName_splitted[1].equals("GermanAnalyzer")) {
								fieldAnalyzer = new GermanAnalyzer(luceneVersion, stopwords);
							} else
							if (fieldAnalyzerName_splitted[1].equals("EnglishAnalyzer")) {
								fieldAnalyzer = new EnglishAnalyzer(luceneVersion, stopwords);
							}else {
								fieldAnalyzer = new StandardAnalyzer(luceneVersion);
							}
							fieldAnalyzerMap.put(fieldAnalyzerName_splitted[0], fieldAnalyzer);
							logger.info("Added Analyzer of type '"+fieldAnalyzer.getClass().toString()+
								"' for field '"+fieldAnalyzerName_splitted[0]+"'");
						}
					}

					xmlToLuceneImport(luceneVersion, xmlFilePath, uri, indexPath,
						standardAnalyzer, fieldAnalyzerMap, limitedDocsCount, limitedTokenCount,
						startElements, exclusions, redirectPattern, categoryPattern, categoryTitlePattern, alternativeURIsPattern, keywordPattern);
				}
			} else {
				logger.info("Using default parameter...");
				WikiDataImporter.xmlToLuceneImportDE();
			}

		} catch(ParseException ex) {
			logger.error(ex);
		}
	}

    /**
     * Imports Wikipedia data from dewiki-20120630-pages-meta-current.xml into a lucene index.
     */
    public static void xmlToLuceneImportDE() {

        String xmlFilePath = "/var/wikipedia/downloads/dewiki-20120630-pages-meta-current.xml";
        String uri = "http://de.wikipedia.org/wiki/";
        String indexPath = WikiConstants.LUCENE_INDEX_PATH;
		Version luceneVersion = Version.LUCENE_35;
        Set<String> stopwords = new TreeSet(Arrays.asList(new String[] {
			"ab", "aber", "ähnlich", "alle", "allein", "allem", "allen", "aller",
			"allerdings", "allerlei", "alles", "allmählich", "allzu", "als",
			"alsbald", "also", "am", "an", "and", "ander", "andere", "anderem",
			"anderen", "anderer", "andererseits", "anderes", "anderm", "andern",
			"andernfalls", "anders", "anstatt", "auch", "auf", "aus", "ausgenommen",
			"ausser", "außer", "ausserdem", "außerdem", "außerhalb", "bald", "bei",
			"beide", "beiden", "beiderlei", "beides", "beim", "beinahe", "bereits",
			"besonders", "besser", "beträchtlich", "bevor", "bezüglich", "bin",
			"bis", "bisher", "bislang", "bist", "bloß", "bsp.", "bzw", "ca", "ca.",
			"content", "da", "dabei", "dadurch", "dafür", "dagegen", "daher", "dahin",
			"damals", "damit", "danach", "daneben", "dann", "daran", "darauf", "daraus",
			"darin", "darüber", "darüberhinaus", "darum", "darunter", "das", "daß", "dass",
			"dasselbe", "davon", "davor", "dazu", "dein", "deine", "deinem", "deinen",
			"deiner", "deines", "dem", "demnach", "demselben", "den", "denen", "denn",
			"dennoch", "denselben", "der", "derart", "derartig", "derem", "deren",
			"derer", "derjenige", "derjenigen", "derselbe", "derselben", "derzeit",
			"des", "deshalb", "desselben", "dessen", "desto", "deswegen", "dich",
			"die", "diejenige", "dies", "diese", "dieselbe", "dieselben", "diesem",
			"diesen", "dieser", "dieses", "diesseits", "dir", "direkt", "direkte",
			"direkten", "direkter", "doch", "dort", "dorther", "dorthin", "drauf",
			"drin", "drüber", "drunter", "du", "dunklen", "durch", "durchaus", "eben",
			"ebenfalls", "ebenso", "eher", "eigenen", "eigenes", "eigentlich", "ein",
			"eine", "einem", "einen", "einer", "einerseits", "eines", "einfach",
			"einführen", "einführte", "einführten", "eingesetzt", "einig", "einige",
			"einigem", "einigen", "einiger", "einigermaßen", "einiges", "einmal",
			"eins", "einseitig", "einseitige", "einseitigen", "einseitiger", "einst",
			"einstmals", "einzig", "entsprechend", "entweder", "er", "erst", "es",
			"etc", "etliche", "etwa", "etwas", "euch", "euer", "eure", "eurem",
			"euren", "eurer", "eures", "falls", "fast", "ferner", "folgende",
			"folgenden", "folgender", "folgendes", "folglich", "fuer", "für",
			"gab", "ganze", "ganzem", "ganzen", "ganzer", "ganzes", "gänzlich",
			"gar", "gegen", "gemäss", "ggf", "gleich", "gleichwohl", "gleichzeitig",
			"glücklicherweise", "hab", "habe", "haben", "haette", "hast", "hat",
			"hätt", "hatte", "hätte", "hatten", "hätten", "hattest", "hattet",
			"heraus", "herein", "hier", "hiermit", "hiesige", "hin", "hinein",
			"hinten", "hinter", "hinterher", "höchstens", "http", "ich", "igitt",
			"ihm", "ihn", "ihnen", "ihr", "ihre", "ihrem", "ihren", "ihrer", "ihres",
			"im", "immer", "immerhin", "in", "indem", "indessen", "infolge", "innen",
			"innerhalb", "ins", "insofern", "inzwischen", "irgend", "irgendeine",
			"irgendwas", "irgendwen", "irgendwer", "irgendwie", "irgendwo", "ist",
			"ja", "jährig", "jährige", "jährigen", "jähriges", "je", "jed", "jede",
			"jedem", "jeden", "jedenfalls", "jeder", "jederlei", "jedes", "jedoch",
			"jemand", "jene", "jenem", "jenen", "jener", "jenes", "jenseits", "jetzt",
			"kam", "kann", "kannst", "kaum", "kein", "keine", "keinem", "keinen",
			"keiner", "keinerlei", "keines", "keineswegs", "klar", "klare", "klaren",
			"klares", "klein", "kleinen", "kleiner", "kleines", "koennen", "koennt",
			"koennte", "koennten", "komme", "kommen", "kommt", "konkret", "konkrete",
			"konkreten", "konkreter", "konkretes", "können", "künftig", "leider", "man",
			"manche", "manchem", "manchen", "mancher", "mancherorts", "manches",
			"manchmal", "mehr", "mehrere", "mein", "meine", "meinem", "meinen",
			"meiner", "meines", "mich", "mir", "mit", "mithin", "muessen", "muesst",
			"muesste", "muss", "muß", "müssen", "musst", "mußt", "müßt", "musste",
			"müsste", "müßte", "mussten", "müssten", "nach", "nachdem", "nachher",
			"nachhinein", "nächste", "nahm", "nämlich", "natürlich", "neben", "nebenan",
			"nehmen", "nicht", "nichts", "nie", "niemals", "niemand", "nirgends",
			"nirgendwo", "noch", "nötigenfalls", "nun", "nur", "ob", "oben", "oberhalb",
			"obgleich", "obschon", "obwohl", "oder", "oft", "per", "plötzlich",
			"schließlich", "schon", "sehr", "sehrwohl", "sein", "seine", "seinem",
			"seinen", "seiner", "seines", "seit", "seitdem", "seither", "selber",
			"selbst", "sich", "sicher", "sicherlich", "sie", "sind", "so", "sobald",
			"sodass", "sodaß", "soeben", "sofern", "sofort", "sogar", "solange", "solch",
			"solche", "solchem", "solchen", "solcher", "solches", "soll", "sollen",
			"sollst", "sollt", "sollte", "sollten", "solltest", "somit", "sondern",
			"sonst", "sonstwo", "sooft", "soviel", "soweit", "sowie", "sowohl",
			"tatsächlich", "tatsächlichen", "tatsächlicher", "tatsächliches",
			"trotzdem", "übel", "über", "überall", "überallhin", "überdies",
			"übermorgen", "übrig", "übrigens", "ueber", "um", "umso", "unbedingt",
			"und", "unmöglich", "unmögliche", "unmöglichen", "unmöglicher", "uns",
			"unser", "unsere", "unserem", "unseren", "unserer", "unseres", "unter",
			"usw", "viel", "viele", "vielen", "vieler", "vieles", "vielleicht",
			"vielmals", "völlig", "vom", "von", "vor", "voran", "vorher", "vorüber",
			"während", "währenddessen", "wann", "war", "wär", "wäre", "waren", "wären",
			"warst", "warum", "was", "weder", "weil", "weiß", "weiter", "weitere",
			"weiterem", "weiteren", "weiterer", "weiteres", "weiterhin", "welche",
			"welchem", "welchen", "welcher", "welches", "wem", "wen", "wenig", "wenige",
			"weniger", "wenigstens", "wenn", "wenngleich", "wer", "werde", "werden",
			"werdet", "weshalb", "wessen", "wichtig", "wie", "wieder", "wieso", "wieviel",
			"wiewohl", "will", "willst", "wir", "wird", "wirklich", "wirst", "wo",
			"wodurch", "wogegen", "woher", "wohin", "wohingegen", "wohl", "wohlweislich",
			"womit", "woraufhin", "woraus", "worin", "wurde", "würde", "wurden",
			"würden", "zB", "z.B.", "zahlreich", "zeitweise", "zu", "zudem", "zuerst",
			"zufolge", "zugleich", "zuletzt", "zum", "zumal", "zur", "zurück",
			"zusammen", "zuviel", "zwar", "zwischen", "nbsp", "ref", "redirect"}));
		Analyzer standardAnalyzer = new GermanAnalyzer(luceneVersion, stopwords);
		Map<String, Analyzer> fieldAnalyzerMap = new HashMap<String, Analyzer>();
		// analyzer for keywords
		//fieldAnalyzerMap.put(WikiLuceneContentHandler.FIELD_KEYWORDS, new StandardAnalyzer(luceneVersion));
        int limitedDocsCount = -1;
        int limitedTokenCount = 500;
        Set<String> startElements = new TreeSet(Arrays.asList(new String[] {
            WikiContentHandler.titleTagName, WikiContentHandler.textTagName}));
        Map<String, Set<String>> exclusions = new HashMap<String, Set<String>>();
        exclusions.put(WikiContentHandler.titleTagName,
            new TreeSet(Arrays.asList(new String[] {"Benutzer:", "Portal:",
			"Diskussion:", "Benutzer Diskussion:", "Wikipedia:", "Wikipedia Diskussion:",
			"Datei:","Vorlage:","Hilfe:","Hilfe Diskussion:","MediaWiki:",
			"Portal Diskussion:","Vorlage Diskussion:","Kategorie Diskussion:",
			"Medium:","Spezial:", "Datei Diskussion:","MediaWiki Diskussion:","Kategorie Diskussion:"})));
        exclusions.put(WikiContentHandler.textTagName,
            new TreeSet(Arrays.asList(new String[] {"{{Begriffsklärung}}"})));
        String redirectPattern = "^#(?:redirect|weiterleitung) \\[\\[([^\\[]*)\\]\\]";
		String categoryTitlePattern = "Kategorie:";
		String categoryPattern = "(\\[\\[Kategorie:)([a-zA-Zäöüß ]+)(|[a-zA-Zäöüß ]+\\]\\])";
        String alternativeURIsPattern = "\\[\\[([^\\[:]*?)\\]\\]";

		List<String> keywordPattern = new ArrayList<String>();
		// [[Keyword]]
		keywordPattern.add("([\\[]{2})([a-zA-Zäöüß ]+)([\\]]{2})");
		// [[Keyword|Label]]
		keywordPattern.add("([\\[]{2})([a-zA-Zäöüß ]+)([|]{1}[a-zA-Zäöüß ]+[\\]]{2})");

		logger.info("Parameters:");
		logger.info(new StringBuilder("xmlFilePath: ").append(xmlFilePath).toString());
		logger.info(new StringBuilder("uri: ").append(uri).toString());
		logger.info(new StringBuilder("indexPath: ").append(indexPath).toString());
		logger.info(new StringBuilder("stopwords: ").append(stopwords.toString()).toString());
		logger.info(new StringBuilder("startElements: ").append(startElements.toString()).toString());
		for (Map.Entry<String, Set<String>> anExclusions : exclusions.entrySet()) {
			logger.info(new StringBuilder(anExclusions.getKey()).append(": ").append(anExclusions.getValue().toString()).toString());
		}
		logger.info(new StringBuilder("limitedDocsCount: ").append(limitedDocsCount).toString());
		logger.info(new StringBuilder("limitedTokenCount: ").append(limitedTokenCount).toString());
		logger.info(new StringBuilder("redirectPattern: ").append(redirectPattern).toString());
		logger.info(new StringBuilder("categoryTitlePattern: ").append(categoryTitlePattern).toString());
		logger.info(new StringBuilder("categoryPattern: ").append(categoryPattern).toString());
		logger.info(new StringBuilder("alternativeURIsPattern: ").append(alternativeURIsPattern).toString());
		logger.info(new StringBuilder("keywordPattern: ").append(keywordPattern).toString());
		logger.info(new StringBuilder("standardAnalyzer: ").append(standardAnalyzer.getClass().toString()).toString());
		if (!fieldAnalyzerMap.isEmpty()) {
			logger.info("FieldAnalyzer:");
			for (Entry<String, Analyzer> fieldAnalyzer : fieldAnalyzerMap.entrySet()) {
				logger.info("Field '"+fieldAnalyzer.getKey()+"', Analyzer: '"+fieldAnalyzer.getValue().getClass().toString()+"'");
			}
		}

        xmlToLuceneImport(luceneVersion, xmlFilePath, uri, indexPath, standardAnalyzer, fieldAnalyzerMap,
            limitedDocsCount, limitedTokenCount, startElements, exclusions, redirectPattern, categoryPattern, categoryTitlePattern,
            alternativeURIsPattern, keywordPattern);
    }

	/**
     * Imports Wikipedia data from an xml source into a lucene index.
     * @param xmlFilePath
     * @param uri
     * @param indexPath
     * @param analyzerLanguageID
     * @param stopwords
     * @param limitedDocsCount
     * @param limitedTokenCount
     * @param startElements
     * @param exclusions
     * @param redirectPattern
     * @param alternativeURIsPAttern
     */
	public static void xmlToLuceneImport(Version luceneVersion, String xmlFilePath, String uriPrefix, String indexPath,
        Analyzer standardAnalyzer, Map<String, Analyzer> fieldAnalyzer, int limitedDocsCount,
        int limitedTokenCount, Set<String> startElements, Map<String, Set<String>> exclusions,
        String redirectPattern, String categoryPattern, String categoryTitlePattern,
		String alternativeURIsPattern, List<String> keywordPattern) {

		LuceneConnector connector = new LuceneConnector(luceneVersion,
            indexPath, standardAnalyzer, fieldAnalyzer, limitedTokenCount);

		WikiContentHandlerConfig config = new WikiContentHandlerConfig(connector);

		config.setUriPrefix(uriPrefix);
		config.setLimitedDocsCount(limitedDocsCount);
		config.setStartElements(startElements);
		config.setTitleExclusions(exclusions.get(WikiContentHandler.titleTagName));
		config.setTextExclusions(exclusions.get(WikiContentHandler.textTagName));
		config.setRedirectPattern(redirectPattern);
		config.setCategoryPattern(categoryPattern);
		config.setCategoryTitlePattern(categoryTitlePattern);
		config.setAlternativeURIsPattern(alternativeURIsPattern);
		config.setKeywordPattern(keywordPattern);

		try {
			WikiContentHandler wikiContentHandler = new WikiContentHandler(config);
			SaxParser parser = new SaxParser(xmlFilePath, wikiContentHandler);
			parser.parse();
		} catch (IOException ex) {
			logger.error(ex.getMessage());
		}
	}
}
