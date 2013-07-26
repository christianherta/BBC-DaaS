package de.bbcdaas.themehandler.domains;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Robert Illers
 */
@Entity
public class Term implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String termValue;

	/**
	 * 
	 * @return 
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id 
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return 
	 */
	public String getTermValue() {
		return termValue;
	}

	/**
	 * 
	 * @param termValue 
	 */
	public void setTermValue(String termValue) {
		this.termValue = termValue;
	}
}
