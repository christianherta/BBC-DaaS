/** 
 * WikipediaReader.java
 * 
 * Copyright (c) 2009, JULIE Lab. 
 * All rights reserved. This program is made available under the terms of the 
 * Common Public License v1.0. It incorporates the JWPL parser developed by UKP Lab, 
 * TU Darmstadt. Please contact UKP Lab for license information.
 *
 * Creation date: 01.05.2009 
 * 
 **/
package de.julielab.jules.reader;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.resource.ResourceInitializationException;

import de.julielab.jules.types.Annotation;
import de.julielab.jules.types.Caption;
import de.julielab.jules.types.Header;
import de.julielab.jules.types.ListItem;
import de.julielab.jules.types.TextObject;
import de.julielab.jules.types.Zone;
import de.julielab.jules.types.wikipedia.ArticleText;
import de.julielab.jules.types.wikipedia.Descriptor;
import de.julielab.jules.types.wikipedia.Title;
import de.julielab.jules.util.ParserUtils;
import de.tudarmstadt.ukp.wikipedia.parser.Content;
import de.tudarmstadt.ukp.wikipedia.parser.ContentElement;
import de.tudarmstadt.ukp.wikipedia.parser.DefinitionList;
import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.NestedList;
import de.tudarmstadt.ukp.wikipedia.parser.NestedListContainer;
import de.tudarmstadt.ukp.wikipedia.parser.NestedListElement;
import de.tudarmstadt.ukp.wikipedia.parser.Paragraph;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import de.tudarmstadt.ukp.wikipedia.parser.Section;
import de.tudarmstadt.ukp.wikipedia.parser.SectionContent;
import de.tudarmstadt.ukp.wikipedia.parser.Table;
import de.tudarmstadt.ukp.wikipedia.parser.TableElement;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.FlushTemplates;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;

/**
 * CollectionReader for Wikipedia that reads in Wikipedia articles from a 
 * database. 
 */
public class WikipediaReader extends DBReader {

	private static final Logger LOGGER = Logger.getLogger(WikipediaReader.class);	

	private static final String PARAM_LANGUAGE = "Language";
	private static final String PARAM_IMAGE_IDENTIFIERS = "ImageIdentifiers";
	private static final String PARAM_CATEGORY_IDENTIFIERS = "CategoryIdentifiers";
	private static final String PARAM_SKIP_TABLE_CONTENTS = "SkipTableContents";	
	private static final String PARAM_SKIP_SECTIONS = "SectionsToSkip";		

	private static final String COMPONENT_ID = "WikipediaReader";	
	private static final String SOURCE = "Wikipedia";
	private static final String DISAMBIGUATION_PAGE_TYPE = "disambiguation-page";
	private static final String ARTICLE_PAGE_TYPE = "article";		
	private static final String DEFAULT_LANGUAGE = "en"; 
	private static final String[] DEFAULT_IMAGE_IDENTIFIERS = new String[] {"Image"}; 
	private static final String[] DEFAULT_CATEGORY_IDENTIFIERS = new String[] {"Category"};
	private static final String[] DEFAULT_SKIP_SECTIONS = new String[] {
		"References", 
		"External links", 
		"Further reading", 
		"See also", 
		"Footnotes", 
		"Bibliography", 
		"Literature"
	};
	private boolean DEFAULT_SKIP_TABLE_CONTENTS = true;
	private static DecimalFormat df = new DecimalFormat("00000000"); //TODO: use number of digits of longest pageId in table 'Page' instead 	

	private static String articleTitle;
	private static String language;
	private static List<String> skipSections;	
	private static MediaWikiParser parser;
	private int offset;
	private static StringBuilder articleText;
	private Boolean skipTableContents;
	
	private Pattern namespacePattern = Pattern.compile(":?(Media|Special|Talk|User|User_talk|Wikipedia|Wikipedia_talk|File|" +
			"File_talk|MediaWiki|MediaWiki_talk|Template|Template_talk|Help|Help_talk|Category|Category_talk|Portal|Portal_" +
			"talk|Book|Book_talk):.*");

	/**
	 * This method is called during initialization. Note that the in
	 * super.initialize() a database connection is established to the Wikipedia
	 * database.
	 * 
	 * @throws ResourceInitializationException
	 */
	@Override
	public void initialize() throws ResourceInitializationException {
		super.initialize();
		
		LOGGER.info("initialize() - Initializing Wikipedia Reader...");

		// create MediaWiki parser deleting all templates and HTML tags but
		// keeping text enclosed by HTML tags
		MediaWikiParserFactory pf = new MediaWikiParserFactory();
		pf.setTemplateParserClass(FlushTemplates.class);
		LOGGER.info("Wikipedia parser config info: " + (new FlushTemplates()).configurationInfo());

		pf.setDeleteTags(true); // TODO: check if other tags than <timeline/>
		// mark content that should be deleted...
		LOGGER.info("Wikipedia parser config info: deleting HTML tags");

		// set image identifier and define that image descriptions appear in
		// paragraph text
		String[] images = (String[]) getConfigParameterValue(PARAM_IMAGE_IDENTIFIERS);
		if (images == null || images.length == 0) {
			images = DEFAULT_IMAGE_IDENTIFIERS;
		}
		pf.setImageIdentifers(Arrays.asList(images));
		pf.setShowImageText(true);
		LOGGER.info("Image identifier(s): " + pf.getImageIdentifers());
		LOGGER.info("Showing image text: " + pf.getShowImageText());

		// set category identifiers
		String[] categories = (String[]) getConfigParameterValue(PARAM_CATEGORY_IDENTIFIERS);
		if (categories == null || categories.length == 0) {
			categories = DEFAULT_CATEGORY_IDENTIFIERS;
		}
		pf.setCategoryIdentifers(Arrays.asList(categories));
		LOGGER.info("Category identifier(s): " + pf.getCategoryIdentifers());

		parser = pf.createParser();

		// set titles of sections to skip
		String[] sectionsToSkip = (String[]) getConfigParameterValue(PARAM_SKIP_SECTIONS);
		if (sectionsToSkip == null) {
			sectionsToSkip = DEFAULT_SKIP_SECTIONS;
		}
		skipSections = Arrays.asList(sectionsToSkip);
		LOGGER.info("Skipping sections with title(s): " + skipSections);

		// set skip table contents
		skipTableContents = (Boolean) getConfigParameterValue(PARAM_SKIP_TABLE_CONTENTS);
		if (skipTableContents == null){
			skipTableContents = DEFAULT_SKIP_TABLE_CONTENTS;
		}
		LOGGER.info("Skipping table contents: " + skipTableContents);

		// set language of Wikipedia articles
		language = (String) getConfigParameterValue(PARAM_LANGUAGE);
		if (language == null) {
			language = DEFAULT_LANGUAGE;
		}
		LOGGER.info("Wikipedia language is: " + language);
	}

	@Override
	public boolean hasNext() throws IOException, CollectionException {
		return super.hasNext();
	}

	/*
	 * @see org.apache.uima.collection.CollectionReader#getNext(org.apache.uima.cas.CAS)
	 */
	public void getNext(CAS cas) throws IOException, CollectionException {
		//get next article
		if (hasNext()){
			Article article = articles.get(idIterator.next());
			if(article==null)
				return;
			
			articleTitle = article.getName();
			//String articleID = df.format(article.getId());
			String articleID = Long.toString(article.getId());	
			LOGGER.info("processing doc with id: " + articleID + " name: " + articleTitle);
			try {					
				JCas jcas = cas.getJCas();
				articleText=new StringBuilder();
				offset = 0;

				//add Header annotation
				Header header = new Header(jcas);
				header.setComponentId(COMPONENT_ID);
				if (article.isDisambiguation()) {
					header.setDocType(DISAMBIGUATION_PAGE_TYPE);
				}
				else {
					header.setDocType(ARTICLE_PAGE_TYPE);
				}
				header.setDocId(articleID);
				header.setSource(SOURCE);
				header.setTitle(articleTitle);
				header.setLanguage(language);
				header.setBegin(0);
				header.addToIndexes();						

				//add Title annotation
				addPageTitle(jcas, articleTitle);

				//NOTE: must be done before parsing! otherwise parser removes <ref/> tags without the text in between
				String articleRawText = removeTimelinesAndReferences(article.getText()); 				

				ParsedPage parsedPage = parser.parse(articleRawText);				

				// add paragraph text, list item text etc. to articleText
				handleParsedPage(jcas, parsedPage); 				

				jcas.setDocumentText(articleText.toString());				

				//NOTE: must be called after handleParsedPage because documentText needs to be known
				header.setEnd(jcas.getDocumentText().length()); 

				// add Descriptor incl. info about categories, redirects, incoming & outgoing links (span: whole docText)
				if (addMetaData){	
					//NOTE: must be called after handleParsedPage because documentText needs to be known
					addPageDescriptor(jcas, article); 
				}

				//add ArticleText annotation
				ArticleText text = new ArticleText(jcas);
				text.setBegin(0);
				text.setEnd(jcas.getDocumentText().length());
				text.addToIndexes();				

			} catch (CASException e) {
				LOGGER.error("Exception in getNext()", e);
				throw new CollectionException(e);
			}
			processedDocuments++;
		}
	}

	/**
	 * Remove all references such as "<ref name="first">"Yoneshima H, Nagata E, (1997) usw."<\/ref>". 
	 * Must be done before parsing the text, otherwise the JWPL parser API would remove the tags 
	 * (like all HTML tags) but would not remove the text included in the tags.
	 *
	 * @param pageText
	 * @return
	 */
	private String removeTimelinesAndReferences(String pageText) {
		pageText = pageText.replaceAll("(<timeline>[\\s\\S]*?</timeline>)|(<ref.*?>[\\s\\S]*?</ref>)", ""); 
		return pageText;
	}

	/**
	 * Returns an FSArray with wikipedia.Title objects containing short and longform of titles.
	 * 
	 * @param jcas The CAS the FSArray will be contained in
	 * @return FSArray containing Title objects
	 */
	private FSArray getTitleArray(JCas jcas, Set<String> titles) {
		FSArray array = new FSArray(jcas, titles.size());
		int i = 0;
		for (String title : titles){
			Title titleAnnotation = new Title(jcas);
			titleAnnotation.setFullname(title.replaceAll("_", " "));
			array.set(i, titleAnnotation);
			i++;
		}
		return array;
	}

	/**
	 * Processes a complete ParsedPage by iterating through all sections and handling 
	 * them appropriately.
	 * 
	 * @param jcas
	 * @param parsedPage
	 */
	private void handleParsedPage(JCas jcas, ParsedPage parsedPage) {		
		// TODO: check end offsets of nesting sections
		//get complete list of sections (All converted to SectionContent)
		List<SectionContent> sectionList = ParserUtils.getAllSections(parsedPage);
		boolean keepSection = true;
		int level = 1;
		List<Zone> textObjectList = new ArrayList<Zone>();
		//iterate through sections
		for (Section section : sectionList) {
			int newLevel = section.getLevel();
			if (newLevel >= level || keepSection){
				level = newLevel;
				Content sectionTitle = section.getTitleElement();
				if (sectionTitle != null 
						&& sectionTitle.getText() != null 
						&& skipSections.contains(sectionTitle.getText())){
					keepSection = false;
					continue;
				}
				//start new section annotation
				de.julielab.jules.types.Section sectionAnnotation = new de.julielab.jules.types.Section(jcas);
				sectionAnnotation.setComponentId(COMPONENT_ID);
				sectionAnnotation.setId("" + level);
				// create section title annotation
				de.julielab.jules.types.Title secTitleAnnotation = new de.julielab.jules.types.Title(jcas);
				if (handleAnnotation(sectionTitle, secTitleAnnotation, jcas, textObjectList)){
					sectionAnnotation.setSectionHeading(secTitleAnnotation);
					//take section title start also as section start offset 
					sectionAnnotation.setBegin(secTitleAnnotation.getBegin());
				}
				else {
					//take current offset plus length of next ending as section start offset 
					sectionAnnotation.setBegin(offset + getEndingLength());	
				}
				//prepare new textObjectList that will be populated in handleSectionContent()
				textObjectList = new ArrayList<Zone>();
				//handle section content
				handleSectionContent(section, textObjectList, jcas);	
				if (sectionAnnotation.getBegin() < offset){
					sectionAnnotation.setEnd(offset + getEndingLength() - 1); //offset plus length of trimmed next ending  	
					sectionAnnotation.addToIndexes();
					//if textObjectsList is not empty add it to section annotation 
					if (textObjectList.size() > 0) {
						FSArray textObjects = new FSArray(jcas, textObjectList.size());
						for (int i = 0; i < textObjectList.size(); i++) {
							textObjects.set(i, textObjectList.get(i));
						}
						sectionAnnotation.setTextObjects(textObjects);
					}
				}
			}	
		}
		if (articleText.length() > 0){			
			addEnding(); //add ending after last section has been dealt with 
			articleText.deleteCharAt(articleText.length() - 1); //remove trailing whitespace
		}
	}

	/**
	 * Processes a single Section by iterating through all content elements contained and
	 * handling them appropriately.
	 * 
	 * @param section
	 * @param textObjectList
	 * @param jcas
	 */
	private void handleSectionContent(Section section, List<Zone> textObjectList, JCas jcas) {
		int counter = 0; 
		for (Content content : section.getContentList()) {
			counter++;
			// for sections with title skip first content (it solely contains the title of the section)
			if (counter == 1 && section.getTitle() != null) {
				continue;
			}
			if (content instanceof Paragraph) {
				Paragraph paragraph = (Paragraph) content;
				de.julielab.jules.types.Paragraph paragraphAnnotation = new de.julielab.jules.types.Paragraph(jcas);
				handleAnnotation(paragraph, paragraphAnnotation, jcas, textObjectList);
			}
			// handle tables 
			else if (content instanceof Table) {
				boolean createTableAnnotation = false;
				//prepare  table annotation
				Table table = (Table) content;				
				TextObject tableObject = new TextObject(jcas);
				tableObject.setComponentId(COMPONENT_ID);
				tableObject.setObjectType("table");				
				//prepare table caption annotation
				Caption tableCapAnno = new Caption(jcas);
				//if table caption annotation was successfully created take start offset as start offset of table annotation 
				if (handleAnnotation(table.getTitleElement(), tableCapAnno, jcas, textObjectList)){
					tableObject.setObjectCaption(tableCapAnno);
					tableObject.setBegin(tableCapAnno.getBegin());
					createTableAnnotation = true;
				}				
				//add table body
				//TODO currently all TableElement objects are taken as 'flat' and no extra annotations are created 
				if (!skipTableContents){
					for (Content tableContent : table.getContentList()){
						if (tableContent instanceof TableElement){ //excludes table caption
							StringBuilder text = adaptTextSegment(tableContent.getText()); 
							if (text.length() > 0) {
								//add suitable ending to articleText
								addEnding();
								//add link annotations for internal Wikipedia links occurring in content	
								//ATTENTION: offsets are only set correctly if addLinkAnnotation() is done before articleText.append(text)
								addLinkAnnotations(content, jcas);
								addImageCaptionAnnotations(content, textObjectList, jcas);
								if (capitalize()){
									text.setCharAt(0, Character.toUpperCase(text.charAt(0)));
								}
								if (!createTableAnnotation){
									tableObject.setBegin(offset);
								}
								articleText.append(text); 
								offset += text.length();
								createTableAnnotation = true;
							}
						}
					}						
				}
				if (createTableAnnotation){
					tableObject.setEnd(offset);
					tableObject.addToIndexes();
					//add table to textObjectList
					textObjectList.add(tableObject);	
				}
			}			
			// handle nested lists
			else if (content instanceof NestedList) {
				NestedList nestedList = (NestedList) content;
				List<ListItem> items = new ArrayList<ListItem>();
				List<NestedListItem> nestedListItems = new ArrayList<NestedListItem>();
				getNestedListItems(nestedList, nestedListItems, 0);
				for (NestedListItem item : nestedListItems){					
					ListItem itemAnnotation = new ListItem(jcas);
					itemAnnotation.setLevel(item.getLevel());
					NestedListElement listElement = (NestedListElement) item.getNestedList();
					if (handleAnnotation(listElement, itemAnnotation, jcas, textObjectList)){
						items.add(itemAnnotation);
					}					
				}				
				//if item list is not empty create FSArray, create listAnnotation and add FSArray as listAnnotation.itemList
				if (items.size() > 0) {
					FSArray itemList = new FSArray(jcas, items.size());
					for (int i = 0; i < items.size(); i++) {
						itemList.set(i, items.get(i));
					}
					de.julielab.jules.types.List listAnnotation = new de.julielab.jules.types.List(jcas);
					listAnnotation.setComponentId(COMPONENT_ID);
					listAnnotation.setBegin(items.get(0).getBegin()); //set start offset of listAnnotation to start offset of first item of its itemList
					listAnnotation.setEnd(offset + getEndingLength() - 1); //offset plus length of trimmed next ending 
					listAnnotation.addToIndexes();
					listAnnotation.setItemList(itemList);
				}
			} 
			// handle definition lists
			else if (content instanceof DefinitionList) {				
				DefinitionList defList = (DefinitionList) content;				
				de.julielab.jules.types.List list = new de.julielab.jules.types.List(jcas);
				handleAnnotation(defList.getDefinedTerm(), list, jcas, textObjectList);
				for (ContentElement definition : defList.getDefinitions()){
					ListItem item = new ListItem(jcas);
					handleAnnotation(definition, item, jcas, textObjectList);
				}
			} 
			else {
				LOGGER.warn("handleSectionContent(): could not handle content of type: " + content.getClass());
			}
		}
	}

	/**
	 * Adds list items recursively subsumed by nestedList to List<NestedListItem> nestedListItems.
	 * 
	 * @param nestedList
	 * @param nestedListItems
	 * @param level
	 */
	private void getNestedListItems(NestedList nestedList, List<NestedListItem> nestedListItems, int level) {
		if (nestedList instanceof NestedListElement) {
			NestedListItem item = new NestedListItem();
			item.setLevel(level);
			item.setNestedList(nestedList);
			String text = nestedList.getText();
			if (text != null && !text.isEmpty() && adaptTextSegment(text).length() > 0) {
				nestedListItems.add(item);
			}
		} else if (nestedList instanceof NestedListContainer) {
			NestedListContainer listContainer = (NestedListContainer) nestedList;
			for (NestedList newNestedList : listContainer.getNestedLists()) {
				getNestedListItems(newNestedList, nestedListItems, level + 1);
			}
		}
	}

	/**
	 * Creates TextObject annotations for all Images contained in the given content and 
	 * adds them to the CAS and the textObjectList.
	 * 
	 * @param content
	 * @param textObjectList
	 * @param jcas
	 */
	private void addImageCaptionAnnotations(Content content, List<Zone> textObjectList, JCas jcas) {
		String contentText = content.getText();
		List<Link> links = content.getLinks(Link.type.IMAGE);
		for (Link link : links) {
			String linkText = link.getText();
			if (linkText != null && !linkText.isEmpty()) {
				int linkEnd = offset + adaptTextSegment(contentText.substring(0, link.getPos().getEnd())).length();
				int linkStart = linkEnd - adaptTextSegment(linkText).length();
				Caption imageCaption = new Caption(jcas);
				imageCaption.setComponentId(COMPONENT_ID);
				imageCaption.setBegin(linkStart);
				imageCaption.setEnd(linkEnd);
				imageCaption.addToIndexes();
				TextObject imageObject = new TextObject(jcas);
				imageObject.setComponentId(COMPONENT_ID);
				imageObject.setObjectType("image");
				imageObject.setObjectCaption(imageCaption);
				imageObject.setBegin(linkStart);
				imageObject.setEnd(linkEnd);
				imageObject.addToIndexes();
				textObjectList.add(imageObject);
			}
		}
	}

	/**
	 * Creates link annotations for Wikipedia internal links occurring within the given content 
	 * element. Skips internal links pointing to articles in other namespaces than the article
	 * namespace without prefix. Cuts the section/anchor naming part of links pointing to particular 
	 * sections or sites of a page.
	 * 
	 * @param content
	 * @param jcas
	 */
	private void addLinkAnnotations(Content content, JCas jcas) {
		String contentText = content.getText();
		List<Link> links = content.getLinks(Link.type.INTERNAL);
		for (Link link : links) {
			String linkTarget = approveLinkTarget(link.getTarget());
			if (linkTarget != null){
				String linkText = link.getText();
				if (linkText != null) {
					int originalLinkEnd = link.getPos().getEnd();
					int linkEnd = offset + adaptTextSegment(contentText.substring(0, originalLinkEnd)).length();
					int linkStart = linkEnd - adaptTextSegment(linkText).length();
					//if link text is not empty create link annotation
					if (linkEnd > linkStart){
						de.julielab.jules.types.wikipedia.Link linkAnnotation = new de.julielab.jules.types.wikipedia.Link(jcas);
						linkAnnotation.setComponentId(COMPONENT_ID);
						linkAnnotation.setTarget(linkTarget);
						linkAnnotation.setBegin(linkStart);
						linkAnnotation.setEnd(linkEnd);
						linkAnnotation.addToIndexes();
					}
				}
			}
		}
	}

	/**
	 * If linkTarget is in one of the linkTarget listed in namespacePattern returns null. 
	 * Else, if linkTarget denotes a section, return the title of the page the section belongs
	 * to. Else, return linkTarget as it is.
	 * 
	 * @param linkTarget
	 * @return
	 */	
	private String approveLinkTarget(String linkTarget) {
		String newTarget = null;
		//if linkTarget is not in one of the namespaces listed in namespacePattern
		if (!namespacePattern.matcher(linkTarget).matches()){			
			//links to subsections of the same page get page name as link target
			if (linkTarget.startsWith("#")){
				newTarget = articleTitle;
			}
			//links to sections of a page get page name as link target
			else if (linkTarget.contains("#")){
				newTarget = linkTarget.split("#")[0];
			}
			else {
				newTarget = linkTarget;
			}
		}		
		return newTarget;
	}

	/**
	 * Add the Wikipedia article title to the CAS including disambiguation strings.
	 * 
	 * @param jcas
	 * @param title
	 * @throws CollectionException
	 */
	private void addPageTitle(JCas jcas, String title) throws CollectionException {
		LOGGER.debug("document title is: " + title);
		String cleansedTitle = title.replaceAll("_", " "); //TODO: avoid replaceAll
		StringBuilder text = adaptTextSegment(cleansedTitle);
		if (text.length() > 0) {
			Title titleAnnotation = new Title(jcas);
			titleAnnotation.setComponentId(COMPONENT_ID);
			// titleAnnotation.setName(xxx) TODO add title without disambiguation string making it unique 
			titleAnnotation.setFullname(cleansedTitle);
			titleAnnotation.setBegin(offset);
			articleText.append(text);
			offset += text.length();
			titleAnnotation.setEnd(offset);
			titleAnnotation.addToIndexes(jcas);
		}
		else {
			LOGGER.error("Found Wikipedia page with empty title (was '" + cleansedTitle + "' before processing...)");
			throw new CollectionException();
		}
	}

	/**
	 * Creates a Descriptor annotation comprising a list of redirects, incoming links, outgoing
	 * links, and categories associated with the Wikipedia page represented by the given CAS.
	 * 
	 * @param jcas
	 * @param article
	 */
	private void addPageDescriptor(JCas jcas, Article article) {

		Descriptor wikiDescriptor = new Descriptor(jcas);
		wikiDescriptor.setComponentId(COMPONENT_ID);

		Set<String> redirects = article.getRedirects();
		StringArray redirectArray =	new StringArray(jcas, redirects.size());
		int i = 0;
		for (String redirectString : redirects){
			redirectArray.set(i, redirectString);
			i++;
		}
		wikiDescriptor.setRedirects(redirectArray);

		FSArray categoryArray = getTitleArray(jcas, article.getCategories());
		wikiDescriptor.setCategories(categoryArray);		


		FSArray inLinkArray = getTitleArray(jcas, article.getInLinks());
		wikiDescriptor.setIncomingLinks(inLinkArray);

		FSArray outLinkArray = getTitleArray(jcas, article.getOutLinks());
		wikiDescriptor.setOutgoingLinks(outLinkArray);

		wikiDescriptor.setBegin(0);
		wikiDescriptor.setEnd(jcas.getDocumentText().length());
		wikiDescriptor.addToIndexes();
	}


	/**
	 * Removes empty brackets, '&nbsp', replaces invalid XML characters, replaces duplicate
	 * whitespaces by single whitespaces and trims the textSegment. Returns the modified 
	 * string as StringBuilder.
	 * 
	 * @param text
	 * @return
	 */
	private StringBuilder adaptTextSegment(String text) {

		StringBuilder buffer = new StringBuilder();
		if (text != null){
			text = text.replaceAll(" [\\(\\[\\{\"']\\s*?[\\)\\]\\}\"']", "");
			text = text.replaceAll("(&nbsp)|([\\s]+)", " ");
			buffer = stripInvalidXmlCharacters(text);
			//trim leading end
			if (buffer.length() > 0 && buffer.charAt(0) == ' '){
				buffer.deleteCharAt(0);
			}
			//trim trailing end
			if (buffer.length() > 0 && buffer.charAt(buffer.length() - 1) == ' '){
				buffer.deleteCharAt(buffer.length() - 1);
			}
		}
		return buffer;
	}

	/**
	 * Handles single content. If content is not null and the preprocessed version of the
	 * corresponding text fragment is not empty: <br>
	 * - add an appropriate ending to the text fragment <br>
	 * - add link annotations and image caption annotations <br>
	 * - capitalize the first character of the text fragment depending on the ending of 
	 * the previous text fragment<br>
	 * - add start and end offset to the corresponding annotation, add the annotation to 
	 * the CAS<br>
	 * - return true. <br>
	 * Otherwise return false. 
	 * 
	 * @param content
	 * @param annotation
	 * @param jcas
	 * @param textObjectList
	 * @return
	 */
	private boolean handleAnnotation(Content content, Annotation annotation, JCas jcas, List<Zone> textObjectList) {	
		if (content != null){
			StringBuilder text = adaptTextSegment(content.getText()); 
			if (text.length() > 0) {
				//add suitable ending to articleText
				addEnding();
				//add link annotations for internal Wikipedia links occurring in content	
				//ATTENTION: offsets are only set correctly if addLinkAnnotation() is done before articleText.append(text)
				addLinkAnnotations(content, jcas);
				addImageCaptionAnnotations(content, textObjectList, jcas);
				if (capitalize()){
					text.setCharAt(0, Character.toUpperCase(text.charAt(0)));
				}
				annotation.setBegin(offset);
				articleText.append(text); 
				offset += text.length();
				annotation.setEnd(offset);
				annotation.setComponentId(COMPONENT_ID);
				annotation.addToIndexes(jcas);		
				return true;
			}
		}
		return false;
	}


	/**
	 * If last character of articleText that is no space is one of {.:?!}
	 * return true. Else return false.
	 * @return
	 */
	private boolean capitalize() {	
		if (articleText.length() == 0){
			return true;
		}		
		if (articleText.length() > 1 && articleText.charAt(articleText.length() - 1) == ' '){
			char nextToLastChar = articleText.charAt(articleText.length() - 2);
			if (nextToLastChar == '.'
				|| nextToLastChar == ':'
					|| nextToLastChar == '?'
						|| nextToLastChar == '!')
				return true;
		}
		return false;
	}

	private void addEnding() {
		if (articleText.length() > 0){
			char lastChar = articleText.charAt(articleText.length() - 1);
			// if last character is '.' append a single whitespace
			if (lastChar == '.'){
				articleText.append(" "); //
				offset += 1;
			}
			// if last character is one of {,;:?!} replace it with '.' and append a single whitespace
			else if (lastChar == ',' 
				|| lastChar == ';' 
					|| lastChar == ':' 
						|| lastChar == '?' 
							|| lastChar == '!'){
				articleText.deleteCharAt(articleText.length() - 1);
				articleText.append(". "); //
				offset += 1;
			}
			else {
				articleText.append(". ");
				offset += 2;
			}		
		}
	}

	private int getEndingLength() {
		int endingLength = 0;
		if (articleText.length() > 0){
			char lastChar = articleText.charAt(articleText.length() - 1);
			if (lastChar == '.' 
				|| lastChar == ',' 
					|| lastChar == ';' 
						|| lastChar == ':' 
							|| lastChar == '?' 
								|| lastChar == '!'){ 
				endingLength = 1; //ending is ' '
			}
			else { 
				endingLength = 2; // ending is ', ' or '. '
			}		
		}
		return endingLength;
	}

	/**
	 * Returns the input stripped of invalid XML characters. See 
	 * http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char for valid XML character list.
	 *
	 * @param input
	 * @return
	 */

	private StringBuilder stripInvalidXmlCharacters(String input) {
		char current;
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			current = input.charAt(i);
			//remove 'zero width space'
			if (current == '\u200b') {
				continue;
			}
			if ((current == 0x9) 		//tab '\t'
					|| (current == 0xA) //line feed '\n'
					|| (current == 0xD) //carriage return '\r'
					|| ((current >= 0x20) && (current <= 0xD7FF))
					|| ((current >= 0xE000) && (current <= 0xFFFD))
					|| ((current >= 0x10000) && (current <= 0x10FFFF))) //all but substitution chars
			{
				buffer.append(current);
			}
		}
		return buffer;
	}


	@Override
	public void close() throws IOException {
		LOGGER.info("Parsed " + processedDocuments + " Wikipedia articles.");
	}

}
