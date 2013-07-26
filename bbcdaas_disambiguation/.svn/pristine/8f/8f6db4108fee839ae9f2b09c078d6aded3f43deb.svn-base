package de.bbcdaas.disambiguation.wikipedia.dataimport.lucene;

import de.bbcdaas.disambiguation.core.configs.AbstractContentHandlerConfig;
import de.bbcdaas.disambiguation.core.connector.lucene.LuceneConnector;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * Configuration for the wiki content handler.
 * @author Robert Illers
 */
public final class WikiContentHandlerConfig extends AbstractContentHandlerConfig<LuceneConnector> {

	private Set<String> titleExclusions = new TreeSet<String>();
	private Set<String> textExclusions = new TreeSet<String>();
	private String categoryTitlePattern;
	private Pattern categoryPattern;
	private Pattern redirectPattern;
	private Pattern alternativeURIsPattern;
	private List<Pattern> keywordPattern = new ArrayList<Pattern>();
	private String uriPrefix;

	/**
	 * Constructor
	 * @param connector
	 */
    public WikiContentHandlerConfig(LuceneConnector connector) {
        super(connector);
    }

	/**
	 * Strings that should not occur in titles of found documents
	 * @return titleExclusions
	 */
	public Set<String> getTitleExclusions() {
		return titleExclusions;
	}

	/**
	 * Strings that should not occur in titles of found documents
	 * @param titleExclusions
	 */
	public void setTitleExclusions(Set<String> titleExclusions) {
		this.titleExclusions = titleExclusions;
	}

	/**
	 * Pattern used in wikipedia documents for redirect links.
	 * @return redirectPattern
	 */
	public Pattern getRedirectPattern() {
		return redirectPattern;
	}

	/**
	 * Pattern used in wikipedia documents for redirect links.
	 * @param regex
	 */
	public void setRedirectPattern(String regex) {
		this.redirectPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	}

	/**
	 *
	 * @return categoryPattern
	 */
	public Pattern getCategoryPattern() {
		return categoryPattern;
	}

	/**
	 *
	 * @param regex
	 */
	public void setCategoryPattern(String regex) {
		this.categoryPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	}

	/**
	 *
	 * @return
	 */
	public String getCategoryTitlePattern() {
		return categoryTitlePattern;
	}

	/**
	 *
	 * @param titlePattern
	 */
	public void setCategoryTitlePattern(String titlePattern) {
		this.categoryTitlePattern = titlePattern;
	}

	/**
	 * Strings that should not occur in the text of found documents
	 * @return textExclusions
	 */
	public Set<String> getTextExclusions() {
		return textExclusions;
	}

	/**
	 * Strings that should not occur in the text of found documents
	 * @param textExclusions
	 */
	public void setTextExclusions(Set<String> textExclusions) {
		this.textExclusions = textExclusions;
	}

	/**
	 * Pattern for links in a wikipedia disambiguation article to other articles.
	 * @return alternativeURIsPattern
	 */
	public Pattern getAlternativeURIsPattern() {
		return alternativeURIsPattern;
	}

	/**
	 * Pattern for links in a wikipedia disambiguation article to other articles.
	 * @param regex
	 */
	public void setAlternativeURIsPattern(String regex) {
		this.alternativeURIsPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	}

	/**
	 * Pattern for Keywords in a documents text.
	 * @return keywordPattern
	 */
	public List<Pattern> getKeywordPattern() {
		return keywordPattern;
	}

	/**
	 * Pattern for Keywords in a documents text
	 * @param regex
	 */
	public void setKeywordPattern(List<String> regex) {
		this.keywordPattern.clear();
		for (String aRegex : regex) {
			this.keywordPattern.add(Pattern.compile(aRegex, Pattern.UNICODE_CASE));
		}
	}

	/**
	 * Prefix used for wikipedia article uris, containing the domain and path
	 * @return uriPrefix
	 */
	public String getUriPrefix() {
		return uriPrefix;
	}

	/**
	 * Prefix used for wikipedia article uris, containing the domain and path
	 * @param uriPrefix
	 */
	public void setUriPrefix(String uriPrefix) {
		this.uriPrefix = uriPrefix;
	}
}
