/** 
 * Article.java
 * 
 * Copyright (c) 2009, JULIE Lab. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0 
 *
 * Creation date: 13.11.2009 
 **/

package de.julielab.jules.reader;

import java.util.Set;

public class Article {
	
	private int id;
	private String name;
	private String text;
	private boolean isDisambiguation;
	private Set<String> redirects;
	private Set<String> categories;
	private Set<String> inLinks;
	private Set<String> outLinks;
	

	public Article(){		
	}
	
	public Article(int id, String name, String text, boolean isDisambiguation) {
		super();
		this.id = id;
		this.name = name;
		this.text = text;
		this.isDisambiguation = isDisambiguation;
	}
	
	public Article(int id, String name, String text, boolean isDisambiguation, 
			Set<String> redirects, Set<String> categories, Set<String> inLinks, Set<String> outLinks) {
		super();
		this.id = id;
		this.name = name;
		this.text = text;
		this.isDisambiguation = isDisambiguation;
		this.redirects = redirects;
		this.categories = categories;
		this.inLinks = inLinks;
		this.outLinks = outLinks;
	}
	
	public Set<String> getCategories() {
		return categories;
	}

	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}

	public Set<String> getInLinks() {
		return inLinks;
	}

	public void setInLinks(Set<String> inLinks) {
		this.inLinks = inLinks;
	}

	public Set<String> getOutLinks() {
		return outLinks;
	}

	public void setOutLinks(Set<String> outLinks) {
		this.outLinks = outLinks;
	}

	public boolean isDisambiguation() {
		return isDisambiguation;
	}

	public void setDisambiguation(boolean isDisambiguation) {
		this.isDisambiguation = isDisambiguation;
	}

	public Set<String> getRedirects() {
		return redirects;
	}

	public void setRedirects(Set<String> redirects) {
		this.redirects = redirects;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String fileName) {
		this.name = fileName;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}


}

