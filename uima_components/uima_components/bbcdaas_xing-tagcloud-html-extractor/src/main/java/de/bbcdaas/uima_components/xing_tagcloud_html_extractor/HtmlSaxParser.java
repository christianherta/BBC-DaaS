package de.bbcdaas.uima_components.xing_tagcloud_html_extractor;

import org.apache.xerces.parsers.AbstractSAXParser;
import org.cyberneko.html.HTMLConfiguration;

public class HtmlSaxParser extends AbstractSAXParser {
	
	public final static String FEATURE_AUGMENTATIONS = "http://cyberneko.org/html/features/augmentations";

    public HtmlSaxParser() {
        super(new HTMLConfiguration());
    }
    
}
