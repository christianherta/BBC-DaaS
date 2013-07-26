package de.bbcdaas.disambiguation.wikipedia.dataexport.lucene;

import de.bbcdaas.disambiguation.core.configs.AbstractDataExporterConfig;
import de.bbcdaas.disambiguation.core.connector.lucene.LuceneConnector;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert Illers
 */
public class WikiCategoryExporterConfig extends AbstractDataExporterConfig<LuceneConnector> {

	private String categoryTitleQueryString;
	private List<String> rootDirectoryNames = new ArrayList<String>();
	private List<String> categoryTitleExcludes = new ArrayList<String>();
	private List<String> parentCategoryTitleExcludes = new ArrayList<String>();

	/**
	 * Constructor
	 * @param connector lucene connector
	 */
	public WikiCategoryExporterConfig(LuceneConnector connector) {
        super(connector);
    }

	/**
	 *
	 * @return categoryTitleQueryString
	 */
	public String getCategoryTitleQueryString() {
		return categoryTitleQueryString;
	}

	/**
	 *
	 * @param categoryTitleQueryString
	 */
	public void setCategoryTitleQueryString(String categoryTitleQueryString) {
		this.categoryTitleQueryString = categoryTitleQueryString;
	}

	/**
	 *
	 * @param categoryTitleExclude
	 */
	public void addCategoryTitleExclude(String categoryTitleExclude) {
		this.categoryTitleExcludes.add(categoryTitleExclude);
	}

	/**
	 *
	 * @return categoryTitleExcludes
	 */
	public List<String> getCategoryTitleExcludes() {
		return categoryTitleExcludes;
	}

	/**
	 *
	 * @param parentCategoryTitleExclude
	 */
	public void addParentCategoryTitleExclude(String parentCategoryTitleExclude) {
		this.parentCategoryTitleExcludes.add(parentCategoryTitleExclude);
	}

	/**
	 *
	 * @return parentCategoryTitleExcludes
	 */
	public List<String> getParentCategoryTitleExcludes() {
		return parentCategoryTitleExcludes;
	}

	/**
	 *
	 * @param rootDirectoryName
	 */
	public void addRootDirectoryName(String rootDirectoryName) {
		this.rootDirectoryNames.add(rootDirectoryName);
	}

	/**
	 *
	 * @return rootDirectoryNames
	 */
	public List<String> getRootDirectoryNames() {
		return rootDirectoryNames;
	}
}
