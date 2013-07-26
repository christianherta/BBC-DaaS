package de.bbcdaas.themehandler.domains;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

/**
 *
 * @author Robert Illers
 */
@Entity
public class ThemeCloudTerm implements Serializable {
	
	@EmbeddedId
	private ThemeCloudTermPK id;
	private float weighting;
	private int type;
	@ManyToOne
	@MapsId("themeCloudId")
	private ThemeCloud themeCloud;

	protected ThemeCloudTerm() {}
	
	public ThemeCloudTerm(ThemeCloud themeCloud, int id, int type, float weighting) {
		
		this.id = new ThemeCloudTermPK(themeCloud.getId(),id);
		this.themeCloud = themeCloud;
		this.type = type;
		this.weighting = weighting;
	}
	
	/**
	 * 
	 * @return 
	 */
	public ThemeCloudTermPK getId() {
		return id;
	}

	/**
	 * 
	 * @return 
	 */
	public ThemeCloud getThemeCloud() {
		return themeCloud;
	}

	/**
	 * 
	 * @return 
	 */
	public int getType() {
		return type;
	}

	/**
	 * 
	 * @param type 
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 
	 * @return 
	 */
	public float getWeighting() {
		return weighting;
	}

	/**
	 * 
	 * @param weighting 
	 */
	public void setWeighting(float weighting) {
		this.weighting = weighting;
	}
	
	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object that) {
		return (this == that) || ((that instanceof ThemeCloudTerm) &&
			this.getId().equals(((ThemeCloudTerm) that).getId()));
	}
	
	
}
