package de.bbcdaas.disambiguation.wikipedia.dataexport.lucene;

import de.bbcdaas.common.beans.category.Category;
import de.bbcdaas.common.beans.category.CategoryTree;
import de.bbcdaas.common.beans.document.Document;
import de.bbcdaas.common.beans.document.Field;
import de.bbcdaas.common.dao.api.LuceneAPI;
import de.bbcdaas.common.dao.exceptions.ApiException;
import de.bbcdaas.disambiguation.core.connector.lucene.LuceneConnector;
import de.bbcdaas.disambiguation.core.impl.AbstractDocumentDataExporter;
import de.bbcdaas.disambiguation.core.interfaces.DocumentDataExporter;
import de.bbcdaas.disambiguation.wikipedia.dataimport.lucene.WikiContentHandler;
import de.bbcdaas.disambiguation.wikipedia.engine.lucene.WikiConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.Version;

/**
 *
 * @author Robert Illers
 */
public class WikiCategoryExporter extends AbstractDocumentDataExporter <WikiCategoryExporterConfig> implements DocumentDataExporter {

	/**
	 * Constructor
	 * @param config Configuration
	 */
	public WikiCategoryExporter(WikiCategoryExporterConfig config) {
		super(config);
	}

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		Version luceneVersion = Version.LUCENE_35;
		String indexPath = WikiConstants.LUCENE_INDEX_PATH;
		Analyzer standardAnalyzer = new GermanAnalyzer(luceneVersion);
		String categoryTitleQueryString = "kategorie:*";
		int maxHits = 200000;

		LuceneConnector connector = new LuceneConnector(luceneVersion, indexPath, standardAnalyzer, null, 0);

		WikiCategoryExporterConfig config = new WikiCategoryExporterConfig(connector);
		config.setCategoryTitleQueryString(categoryTitleQueryString);
		config.addRootDirectoryName("sachsystematik");
		config.addRootDirectoryName("räumliche systematik");
		config.addRootDirectoryName("zeitliche systematik");
		config.addRootDirectoryName("abkürzung");
		config.addRootDirectoryName("begriffsklärung");
		config.addRootDirectoryName("liste");
		config.addRootDirectoryName("!Wikipedia");
		config.addCategoryTitleExclude("wikipedia:");
		//config.addParentCategoryTitleExclude("");

		WikiCategoryExporter categoryExporter = new WikiCategoryExporter(config);
		categoryExporter.export(maxHits);
	}

	/**
	 * Exports a category structure out of a list of documents using the documents
	 * titles and parentCategory names.
	 * @param maxHits max number of documents to get, depends on memory and number of existing documents
	 */
    public CategoryTree export(int maxHits) {

		try {
			LuceneAPI api = this.getConfig().getConnector().getLuceneAPI();
			api.openConnection();

			Query luceneQuery = new WildcardQuery(new Term(WikiContentHandler.FIELD_TITLE, this.getConfig().getCategoryTitleQueryString()));
			List<Document> documents = api.searchForDocuments(luceneQuery, maxHits);

			CategoryTree tree = new CategoryTree();
			int insertedCategories = 0;
			int insertedCategoriesOld;
			int rounds = 0;
			int maxRounds = 50;

			// create category tree
			do {

				insertedCategoriesOld = insertedCategories;
				for (Document document : documents) {

					if (!document.getInserted()) {

						Field titleField = document.getFieldByName(WikiContentHandler.FIELD_TITLE);
						String title = titleField.getValue().split(":")[1];

						// filter title
						boolean hasAllowedTitle = true;
						for (String categoryTitleExclude : this.getConfig().getCategoryTitleExcludes()) {
							if (title.equals(categoryTitleExclude)) {
								hasAllowedTitle = false;
								document.setInserted(true);
								break;
							}
						}
						// done filter title

						if (hasAllowedTitle) {

							Field parentCategoriesField = document.getFieldByName(WikiContentHandler.FIELD_CATEGORIES);
							List<String> parentCategoryTitles = new ArrayList<String>();
							boolean hasParentCategory = false;

							if (parentCategoriesField != null) {

								parentCategoryTitles.addAll(Arrays.asList(parentCategoriesField.
									getValue().split(Field.FIELD_VALUE_LIST_SEPARATOR)));

								// filter parent categories
								List<String> toRemove = new ArrayList<String>();
								for (String parentCategoryTitleExclude : this.getConfig().getParentCategoryTitleExcludes()) {
									for (String parentCategoryTitle : parentCategoryTitles) {
										if (parentCategoryTitle.startsWith(parentCategoryTitleExclude)) {
											toRemove.add(parentCategoryTitle);
										}
									}
								}
								for (String toRem : toRemove) {
									parentCategoryTitles.remove(toRem);
								}

								toRemove.clear();
								for (String rootDirectoryName : this.getConfig().getRootDirectoryNames()) {
									for (String parentCategoryTitle : parentCategoryTitles) {
										if (parentCategoryTitle.equals(rootDirectoryName)) {
											toRemove.add(parentCategoryTitle);
										}
									}
								}
								for (String toRem : toRemove) {
									parentCategoryTitles.remove(toRem);
								}

								hasParentCategory = !parentCategoryTitles.isEmpty();
								// done filter parent categories
							}

							// try to insert category into tree under each parent its has
							if (hasParentCategory) {

								List<String> toRemove = new ArrayList<String>();
								for (String parentCategoryTitle : parentCategoryTitles) {

									Category newCategory = new Category();
									newCategory.setName(title);
									newCategory.setParentName(parentCategoryTitle);

									if (tree.insertCategory(newCategory)) {
										toRemove.add(parentCategoryTitle);
									}
								}
								for (String toRem : toRemove) {
									parentCategoryTitles.remove(toRem);
								}
								if (parentCategoryTitles.isEmpty()) {

									// increase insert counter if category is
									// added successfully into all its parents
									insertedCategories++;
									document.setInserted(true);
								}
							}
							// /try to insert category into tree under each parent its has

							else {

								Category newCategory = new Category();
								newCategory.setName(title);
								newCategory.setParentName(null);

								if (tree.insertCategory(newCategory)) {

									// insert insert counter if category is added
									// as root category (no parent)
									insertedCategories++;
									document.setInserted(true);
								}
							}
						}
					}
				}
				rounds++;
				logger.info("Round: "+rounds);
				logger.info("inserted Categories: "+insertedCategories);
			} while(rounds < maxRounds &&
					insertedCategoriesOld != insertedCategories);
			// done create category tree
			logger.info("tree.getNumberOfObjects(): "+tree.getNumberOfObjects());
			api.closeConnection();
			return tree;
		} catch(ApiException e) {
			logger.error(e.getMessage());
		}
		return null;
    }
}
