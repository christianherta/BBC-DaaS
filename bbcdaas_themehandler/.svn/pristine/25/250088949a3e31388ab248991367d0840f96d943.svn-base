package de.bbcdaas.themehandler.domains;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 * Primary Key for theme cloud terms.
 * @author Robert Illers
 */
	@Embeddable
	public class ThemeCloudTermPK implements Serializable {
		
		private Integer themeCloudId;
		private Integer themeCloudTermId;

		public ThemeCloudTermPK() {
		}
		
		public ThemeCloudTermPK(Integer themeCloudId, Integer themeCloudTermId) {
			this.themeCloudId = themeCloudId;
			this.themeCloudTermId = themeCloudTermId;
		}
		
		public int getThemeCloudId() {
			return this.themeCloudId;
		}
		
		public int getThemeCloudTermId() {
			return this.themeCloudTermId;
		}

		@Override
		public int hashCode() {
			return this.themeCloudId != null && this.themeCloudTermId != null ?
				themeCloudId.hashCode() + (themeCloudTermId.hashCode() << 1) : 0;
		}

		@Override
		public boolean equals(Object that) {
			return (this == that) || ((that instanceof ThemeCloudTermPK) &&
				this.themeCloudId == ((ThemeCloudTermPK)that).themeCloudId &&
				this.themeCloudTermId == ((ThemeCloudTermPK)that).themeCloudTermId);
		}
	}
