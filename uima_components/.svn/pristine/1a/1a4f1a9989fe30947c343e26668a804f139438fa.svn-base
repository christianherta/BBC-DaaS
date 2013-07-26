/** 
 * ParserUtils.java
 * 
 * Copyright (c) 2009, JULIE Lab. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0 
 *
 * Author: beisswanger
 * 
 * Current version: 1.0
 * Since version:   1.0
 *
 * Creation date: 04.06.2009 
 **/

package de.julielab.jules.util;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.wikipedia.parser.NestedList;
import de.tudarmstadt.ukp.wikipedia.parser.NestedListContainer;
import de.tudarmstadt.ukp.wikipedia.parser.NestedListElement;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import de.tudarmstadt.ukp.wikipedia.parser.Section;
import de.tudarmstadt.ukp.wikipedia.parser.SectionContainer;
import de.tudarmstadt.ukp.wikipedia.parser.SectionContent;


/**
 * TODO insert description
 * @author beisswanger
 */
public class ParserUtils {
	
	/**
	 * Returns a list of all sections recursively contained in SectionContainer sc.
	 * @param sc
	 * @return List<Section>
	 */
	private static List<SectionContent> getAllSubSections(SectionContainer sc){		
		List<Section> sectionList = sc.getSubSections();
		List<SectionContent> newSectionList = new ArrayList<SectionContent>();
		int i = 0;
		for (Section section : sectionList){
			//If first element in section list is a section with no title, add title and level of 
			//subsuming SectionContainer to it. If it is a SecitonContent with title or a SectionContainer,
			//create a new SectionContent annotation and add it to the newSectionList that has title and level of 
			//the subsuming SectionContainer. 
			if (i == 0){
				if (section instanceof SectionContent && section.getTitle() == null){
					section.setTitleElement(sc.getTitleElement());
					section.setLevel(sc.getLevel());
				}
				else { //i.e. if (section instanceof SectionContainer || (section instanceof SectionContent && section.getTitle() != null))
					newSectionList.add(new SectionContent(sc.getTitleElement(), sc.getLevel()));					
				} 
			}			
			if (section instanceof SectionContent){
				newSectionList.add((SectionContent) section);
			}
			else {
			    newSectionList.addAll(getAllSubSections((SectionContainer) section));
			}
			i++;
		}
		return newSectionList;
	}
		
	/**
	 * Returns a list of all sections contained in ParsedPage pp. Sections contained
	 * in sections are also considered.
	 * 
	 * @param pp
	 * @return List<Section>
	 */
	public static List<SectionContent> getAllSections(ParsedPage pp){		
		List<SectionContent> sectionList = new ArrayList<SectionContent>();
		for(Section section : pp.getSections()) {
			if (section instanceof SectionContent){
				sectionList.add((SectionContent) section);
			}
			else if (section instanceof SectionContainer) {
			    SectionContainer sc = (SectionContainer) section;
			    sectionList.addAll(getAllSubSections(sc));
			}
		}
		return sectionList;
	}	
	
	/**
	 * Returns a list of all NestedListElement recursively subsumed by nestedList element.
	 * @param nestedList
	 * @return List<NestedListElement>
	 */
	public static List<NestedListElement> getAllNestedListElements(NestedList nestedList){
		List<NestedListElement> listElementList = new ArrayList<NestedListElement>();
		if (nestedList instanceof NestedListElement){
			NestedListElement listElement = (NestedListElement) nestedList;
			listElementList.add(listElement);
		}
		else if (nestedList instanceof NestedListContainer){
			NestedListContainer listContainer = (NestedListContainer) nestedList;
			for (NestedList newNestedList : listContainer.getNestedLists()){
				listElementList.addAll(getAllNestedListElements(newNestedList));
			}
		}
		return listElementList;
	}
}

