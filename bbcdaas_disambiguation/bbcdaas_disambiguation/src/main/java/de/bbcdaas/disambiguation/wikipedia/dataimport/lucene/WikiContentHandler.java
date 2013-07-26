package de.bbcdaas.disambiguation.wikipedia.dataimport.lucene;

import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.disambiguation.core.impl.AbstractContentHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xml.sax.SAXException;

/**
 * Implementation of a SAX Content Handler for Wikipedia Documents that reads from
 * a Wikipedia xml file dump and creates lucene documents.
 * @author Robert Illers
 */
public final class WikiContentHandler extends AbstractContentHandler<WikiContentHandlerConfig> {

	/* ---- index fields ---- */

	// wikipedia article types:
	// article
	public static final String WIKI_ARTICLE_TYPE_ARTICLE = "article";
	// disambiguation
	public static final String WIKI_ARTICLE_TYPE_DISAMBIGUATION = "disambiguation";
	// redirect
	public static final String WIKI_ARTICLE_TYPE_REDIRECT = "redirect";
	// category
	public static final String WIKI_ARTICLE_TYPE_CATEGORY = "category";

	// uri to the article on wikipedia
	// (all)
	public static final String FIELD_URI = "URI";
	// title of the article on wikipedia
	// (all)
	public static final String FIELD_TITLE = "Title";
	// type of the wikipedia article
	public static final String FIELD_WIKI_ARTICLE_TYPE = "WikiArticleType";
	// titles of the article on wikipedia this uri redirects to
	// (redirect)
	public static final String FIELD_REDIRECT_TITLE = "RedirectTitle";
	// titles of alternative articles from a disambiguation article
	// (disambiguation)
	public static final String FIELD_ALTERNATIVE_TITLES = "AlternativeTitles";
	// the lucene-analyzed text of the article
	// (articles)
	public static final String FIELD_CONTENT = "Content";
	// extracted keywords from the articles text
	// (articles)
	public static final String FIELD_KEYWORDS = "Keywords";
	// category names the wikipedia article is in
	// (article, category)
	public static final String FIELD_CATEGORIES = "Categories";

	/* ---- /index fields ---- */

	/* used xml tags in wikipedia dump file */
	public static final String titleTagName = "title";
	private String titleValue;
	public static final String textTagName = "text";
	private String textValue;
	/* /used xml tags in wikipedia dump file */

	/**
	 * Constructor, sets the content handler configuration.
	 * @param config
	 * @throws IOException
	 */
	public WikiContentHandler(WikiContentHandlerConfig config) throws IOException {
		super(config);

		if (this.getConfig().getConnector().getStandardAnalyzer() == null) {
			logger.error("No standard analyzer set.");
		} else {
			logger.info("Standard Analyzer: "+this.getConfig().getConnector().
				getStandardAnalyzer().getClass().toString());
		}
        try {

            this.getConfig().getConnector().getLuceneAPI().deleteIndex();
            this.getConfig().getConnector().getLuceneAPI().openConnection();
        } catch (Exception ex) {

			logger.error("LuceneContentHandler()", ex);
			System.exit(1);
        }
	}

	/**
	 * Gets the current wiki content handler configuration.
	 * @return WikiContentHandlerConfig
	 */
	@Override
	public WikiContentHandlerConfig getConfig() {
		return (WikiContentHandlerConfig)super.getConfig();
	}

	/**
	 * Implementation of the endElement event of the sax parser.
	 * @param uri
	 * @param localName
	 * @param qName
	 * @throws SAXException
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		super.endElement(uri, localName, qName);

		// if element is title -> only get ist value
		if (localName.equals(titleTagName)) {
			this.titleValue = this.currentValue;
		} else
		// if element is text -> begin analyzing
		if (localName.equals(textTagName)) {

			this.textValue = this.currentValue;
			this.indexCount++;

			// check if max number of to read documents reached
			if (this.indexCount < this.getConfig().getLimitedDocsCount() ||
				this.getConfig().getLimitedDocsCount() == -1) {

				// check if title matches a title exclusion
				boolean useableTitle = true;
				for (String titleExclusion : this.getConfig().getTitleExclusions()) {
					if (this.titleValue.startsWith(titleExclusion)) {
						useableTitle = false;
						break;
					}
				}

				if (useableTitle) {

					Matcher matcher = this.getConfig().getRedirectPattern().matcher(this.textValue);

					// no redirect
					if (!matcher.matches()) {

						// category
						if (this.titleValue.startsWith(this.getConfig().getCategoryTitlePattern())) {

							try {
								this.indexWikipediaCategory(this.titleValue, this.textValue);
							} catch(ApiException ex) {
								throw new SAXException(ex);
							}
						}

						// article or redirect
						else {

							// check if text matches a text exclusion
							boolean useableText = true;
							for (String textExclusion : this.getConfig().getTextExclusions()) {
								if (this.textValue.contains(textExclusion)) {
									useableText = false;
								}
							}

							if (useableText) {

								try {
									this.indexWikipediaArticle(this.titleValue, this.textValue);
								} catch(ApiException ex) {
									throw new SAXException(ex);
								}
							}

							// text not useable, check if it contains alternative URLs
							else {

								matcher = this.getConfig().getAlternativeURIsPattern().
									matcher(this.textValue);
								List<String> alternativeTitles = new ArrayList<String>();

								while (matcher.find()) {
									alternativeTitles.add(matcher.group(1).toString().toLowerCase());
								}

								if (!alternativeTitles.isEmpty()) {
									try {
										// index alternative titles
										this.indexWikipediaDisambiguation(this.titleValue, alternativeTitles);
									} catch(ApiException ex) {
										throw new SAXException(ex);
									}
								}
							}
						}
					}
					// redirect found
					else {

						try {
							// index redirect title
							this.indexWikipediaRedirect(this.titleValue, matcher.group(1).
								toString().toLowerCase());
						} catch(ApiException ex) {
							throw new SAXException(ex);
						}
					}
				}

			}
			// index process done
			else {

				this.endDocument();
				throw new SAXException(new StringBuilder("Limit reached after ").
					append(this.indexCount).append(" entries.").toString());
			}
		}
	}

	/* ---- document write methods ---- */

	private void indexWikipediaCategory(String title, String text) throws ApiException {

		Document document = new Document();
		this.addWikiArticleTypeField(document, WIKI_ARTICLE_TYPE_CATEGORY);
		this.addTitleField(document, title);
		this.addUriField(document, title);
		this.addCategoriesField(document, text);
		this.getConfig().getConnector().getLuceneAPI().addDocument(document);
		logger.info("added Category: "+title.toLowerCase());
	}

	/**
	 * Stores a new document with title and text into the index
	 * @param title
	 * @param text
	 */
	private void indexWikipediaArticle(String title, String text) throws ApiException {

        Document document = new Document();
		this.addWikiArticleTypeField(document, WIKI_ARTICLE_TYPE_ARTICLE);
		this.addTitleField(document, title);
        this.addUriField(document, title);
		this.addContentField(document, text);
		this.addKeywordField(document, text);
		this.addCategoriesField(document, text);
        this.getConfig().getConnector().getLuceneAPI().addDocument(document);
		logger.info("added Article: "+title.toLowerCase());
	}

	/**
	 * Stores a new Document with title and alternative URIs into the index
	 * @param title
	 * @param alternativeTitles
	 */
	private void indexWikipediaDisambiguation(String title, List<String> alternativeTitles) throws ApiException {

        Document document = new Document();
		this.addWikiArticleTypeField(document, WIKI_ARTICLE_TYPE_DISAMBIGUATION);
        this.addTitleField(document, title);
		this.addUriField(document, title);
        this.addAlternativeTitlesField(document, alternativeTitles);
		this.getConfig().getConnector().getLuceneAPI().addDocument(document);
		logger.info("added Disambiguation: "+title.toLowerCase());
	}

	/**
	 * Stores a new Document with title and redirect URI into the index
	 * @param title
	 * @param redirectTitle
	 * @throws ApiException
	 */
	private void indexWikipediaRedirect(String title, String redirectTitle) throws ApiException {

		Document document = new Document();
		this.addWikiArticleTypeField(document, WIKI_ARTICLE_TYPE_REDIRECT);
		this.addTitleField(document, title);
		this.addUriField(document, title);
		this.addRedirectTitleField(document, redirectTitle);
		this.getConfig().getConnector().getLuceneAPI().addDocument(document);
		logger.info("added redirect: "+title.toLowerCase());
	}

	/* ---- /document write methods ---- */

	/**
	 *
	 * @param document
	 * @param text
	 */
	private void addCategoriesField(Document document, String text) {

		Pattern categoryPattern = this.getConfig().getCategoryPattern();
		Set<String> categories = new HashSet<String>();
		Matcher matcher = categoryPattern.matcher(text);
		while(matcher.find()) {
			categories.add(matcher.group(2).toLowerCase().trim());
		}
		document.addField(FIELD_CATEGORIES, new ArrayList<String>(categories), true, false, false);
	}

	/**
	 *
	 * @param document
	 * @param alternativeTitles
	 */
	private void addAlternativeTitlesField(Document document, List<String> alternativeTitles) {
		document.addField(FIELD_ALTERNATIVE_TITLES, alternativeTitles, true, false, false);
	}

	/**
	 *
	 * @param document
	 * @param redirectTitle
	 */
	private void addRedirectTitleField(Document document, String redirectTitle) {
		document.addField(FIELD_REDIRECT_TITLE, redirectTitle, true, false, false);
	}

	/**
	 *
	 * @param document
	 * @param text
	 */
	private void addKeywordField(Document document, String text) {

		List<Pattern> keywordPattern = this.getConfig().getKeywordPattern();
		Set<String> keywords = new HashSet<String>();
		for (Pattern aKeywordPattern : keywordPattern) {
			Matcher matcher = aKeywordPattern.matcher(text);
			while(matcher.find()) {
				keywords.add(matcher.group(2).toLowerCase().trim());
			}
		}
		document.addField(FIELD_KEYWORDS, new ArrayList<String>(keywords), true, false, false);
	}

	/**
	 *
	 * @param document
	 * @param text
	 */
	private void addContentField(Document document, String text) {
		document.addField(FIELD_CONTENT, text, false, true, true);
	}

	/**
	 *
	 * @param document
	 * @param title
	 */
	private void addUriField(Document document, String title) {
		document.addField(FIELD_URI, new StringBuilder().append(this.getConfig().getUriPrefix()).
			append(title).toString(), true, false, false);
	}

	/**
	 *
	 * @param document
	 * @param wikiArticleType
	 */
	private void addWikiArticleTypeField(Document document, String wikiArticleType) {
		document.addField(FIELD_WIKI_ARTICLE_TYPE, wikiArticleType, true, false, false);
	}

	/**
	 *
	 * @param document
	 * @param title
	 */
	private void addTitleField(Document document, String title) {
		document.addField(FIELD_TITLE, title.toLowerCase(), true, false, false);
	}

	/**
	 *
	 * @throws SAXException
	 */
	@Override
	public void endDocument() throws SAXException {

		try {
			// close connection to lucene index if reaching end of input file
			this.getConfig().getConnector().getLuceneAPI().closeConnection();
		} catch (ApiException ex) {
			throw new SAXException("Error closing writer", ex);
		}
	}
}
