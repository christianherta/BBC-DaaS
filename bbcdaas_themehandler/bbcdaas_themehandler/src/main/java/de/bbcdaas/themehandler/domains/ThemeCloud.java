package de.bbcdaas.themehandler.domains;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author Robert Illers
 */
@Entity
public class ThemeCloud implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private Integer userId;
	@OneToMany(mappedBy="themeCloud",cascade=CascadeType.ALL)
	private Set<ThemeCloudTerm> terms = new HashSet<ThemeCloudTerm>();

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
	public void setThemeCloudId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return 
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name 
	 */ 
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return 
	 */
	public Set<ThemeCloudTerm> getTerms() {
		return terms;
	}

	/**
	 * 
	 * @param terms 
	 */
	public void setTerms(Set<ThemeCloudTerm> terms) {
		this.terms = terms;
	}

	/**
	 * 
	 * @return 
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * 
	 * @param userId 
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
