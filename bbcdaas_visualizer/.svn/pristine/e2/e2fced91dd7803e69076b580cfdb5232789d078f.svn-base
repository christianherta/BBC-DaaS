package de.bbcdaas.visualizer.beans;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.ThemeCloud;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert Illers
 */
public class ThemeCloudSessionBean implements Serializable {
	
	private int themeCloudCreatorActive = 1;
	private int themeCloudViewerActive = 0;
	private int syntagCloudParameterVisible = 0;
	private List<Term> syntagTerms = new ArrayList<Term>();
	private String themeCloudName;
	private List<Term> themeCloud = new ArrayList<Term>();
	private List<WhiteGrayRedThemeCloudColumn> coloredThemeCloud = new ArrayList<WhiteGrayRedThemeCloudColumn>();
	private Float minSyntag = null;
	private Float syntagmaticEntityTermFactor = null;
	private Float a = null;
	private Float b = null;
	private List<ThemeCloud> themeClouds = new ArrayList<ThemeCloud>();

	/**
	 * 
	 * @return 
	 */
	public List<Term> getSyntagTerms() {
		return syntagTerms;
	}

	/**
	 * 
	 * @param syntagTerms 
	 */
	public void setSyntagTerms(List<Term> syntagTerms) {
		this.syntagTerms = syntagTerms;
	}

	/**
	 * 
	 * @return 
	 */
	public List<Term> getThemeCloud() {
		return themeCloud;
	}

	public List<Term> getWhiteThemeCloud() {
		List<Term> whiteThemeCloud = new ArrayList<Term>();
		for (Term term : this.themeCloud) {
			if (term.getRating() == 0) {
				whiteThemeCloud.add(term);
			}
		}
		return whiteThemeCloud;
	}
	
	/**
	 * 
	 * @param themeCloud 
	 */
	public void setThemeCloud(List<Term> themeCloud) {
		this.themeCloud = themeCloud;
	}

	/**
	 * 
	 * @return 
	 */
	public String getThemeCloudName() {
		return themeCloudName;
	}

	/**
	 * 
	 * @param themeCloudName 
	 */
	public void setThemeCloudName(String themeCloudName) {
		this.themeCloudName = themeCloudName;
	}

	public Float getA() {
		return a;
	}

	public void setA(Float a) {
		this.a = a;
	}

	public Float getB() {
		return b;
	}

	public void setB(Float b) {
		this.b = b;
	}

	public Float getMinSyntag() {
		return minSyntag;
	}

	public void setMinSyntag(Float minSyntag) {
		this.minSyntag = minSyntag;
	}

	public Float getSyntagmaticEntityTermFactor() {
		return syntagmaticEntityTermFactor;
	}

	public void setSyntagmaticEntityTermFactor(Float syntagmaticEntityTermFactor) {
		this.syntagmaticEntityTermFactor = syntagmaticEntityTermFactor;
	}

	public List<WhiteGrayRedThemeCloudColumn> getColoredThemeCloud() {
		return coloredThemeCloud;
	}

	public void setColoredThemeCloud(List<WhiteGrayRedThemeCloudColumn> coloredThemeCloud) {
		this.coloredThemeCloud = coloredThemeCloud;
	}

	public int getThemeCloudCreatorActive() {
		return themeCloudCreatorActive;
	}

	public void setThemeCloudCreatorActive(int themeCloudCreatorActive) {
		this.themeCloudCreatorActive = themeCloudCreatorActive;
	}

	public int getThemeCloudViewerActive() {
		return themeCloudViewerActive;
	}

	public void setThemeCloudViewerActive(int themeCloudViewerActive) {
		this.themeCloudViewerActive = themeCloudViewerActive;
	}

	public int getSyntagCloudParameterVisible() {
		return syntagCloudParameterVisible;
	}

	public void setSyntagCloudParameterVisible(int syntagCloudParameterVisible) {
		this.syntagCloudParameterVisible = syntagCloudParameterVisible;
	}

	public List<ThemeCloud> getThemeClouds() {
		return themeClouds;
	}

	public void setThemeClouds(List<ThemeCloud> themeClouds) {
		this.themeClouds = themeClouds;
	}
	
	public void rebuildColoredThemeCloud() {
		
		this.coloredThemeCloud.clear();
		List<Term> whiteTerms = new ArrayList<Term>();
		List<Term> grayTerms = new ArrayList<Term>();
		List<Term> redTerms = new ArrayList<Term>();
		for (Term term : this.themeCloud) {
			switch(term.getRating()) {
				case 0:
					whiteTerms.add(term);
					break;
				case 1:
					grayTerms.add(term);
					break;
				case 2:
					redTerms.add(term);
					break;
			}
		}
		int maxTermSize = whiteTerms.size();
		if (grayTerms.size() > maxTermSize) {
			maxTermSize = grayTerms.size();
		} else if (redTerms.size() > maxTermSize) {
			maxTermSize = redTerms.size();
		} 
		for (int i=0; i < maxTermSize;i++) {
			this.coloredThemeCloud.add(new WhiteGrayRedThemeCloudColumn());
		}
		for (int i = 0; i < whiteTerms.size();i++) {
			this.coloredThemeCloud.get(i).whiteTerm = whiteTerms.get(i);
		}
		for (int i = 0; i < grayTerms.size();i++) {
			this.coloredThemeCloud.get(i).grayTerm = grayTerms.get(i);
		}
		for (int i = 0; i < redTerms.size();i++) {
			this.coloredThemeCloud.get(i).redTerm = redTerms.get(i);
		}
	}
	
	/**
	 * inner class representing a row of the theme cloud column
	 */
	public class WhiteGrayRedThemeCloudColumn {
		
		private Term whiteTerm = new Term();
		private Term grayTerm = new Term();
		private Term redTerm = new Term();

		public Term getGrayTerm() {
			return grayTerm;
		}

		public Term getRedTerm() {
			return redTerm;
		}

		public Term getWhiteTerm() {
			return whiteTerm;
		}
	}
}
