package de.bbcdaas.visualizer.beans;

import de.bbcdaas.common.beans.entity.Entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert Illers
 */
public class SyntagCloudSessionBean implements Serializable {
	
	private List<Entity> entities = new ArrayList<Entity>();
	// selected entity
	private int entityIndex = 0;
	private boolean syntagCloudsCalculatedForCurrentRandomEntity = false;
	private Float minSyntag = null;
	private Float syntagmaticEntityTermFactor = null;
	private String term1Value = null;
	private boolean term1Valid = true;
	private String term2Value = null;
	private boolean term2Valid = true;
	private String term3Value = null;
	private boolean term3Valid = true;
	private Float a = null;
	private Float b = null;
	
	/**
	 * 
	 */
	public void resetForGetRandomEntity() {
		
		entities.clear();
		this.entityIndex = 0;
		syntagCloudsCalculatedForCurrentRandomEntity = false;
		minSyntag = null;
		syntagmaticEntityTermFactor = null;
		term1Value = null;
		term2Value = null;
		term3Value = null;
		term1Valid = true;
		term2Valid = true;
		term3Valid = true;
	}
	
	/**
	 * 
	 */
	public void resetForSearchForEntity() {
		
		entities.clear();
		this.entityIndex = 0;
		syntagCloudsCalculatedForCurrentRandomEntity = false;
	}
	
	/**
	 * 
	 * @return 
	 */
	public int getEntityIndex() {
		return entityIndex;
	}

	/**
	 * 
	 * @param entityIndex 
	 */
	public void setEntityIndex(int entityIndex) {
		this.entityIndex = entityIndex;
	}
	
	/**
	 * 
	 * @return 
	 */
	public boolean increaseEntityIndex() {
		
		if (entityIndex+1 < this.entities.size()) {
			this.entityIndex++;
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return 
	 */
	public boolean decreaseEntityIndex() {
		
		if (entityIndex > 0) {
			this.entityIndex--;
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return 
	 */
	public boolean isTerm1Valid() {
		return term1Valid;
	}

	/**
	 * 
	 * @param term1Valid 
	 */
	public void setTerm1Valid(boolean term1Valid) {
		this.term1Valid = term1Valid;
	}

	/**
	 * 
	 * @return 
	 */
	public boolean isTerm2Valid() {
		return term2Valid;
	}

	/**
	 * 
	 * @param term2Valid 
	 */
	public void setTerm2Valid(boolean term2Valid) {
		this.term2Valid = term2Valid;
	}

	/**
	 * 
	 * @return 
	 */
	public boolean isTerm3Valid() {
		return term3Valid;
	}

	/**
	 * 
	 * @param term3Valid 
	 */
	public void setTerm3Valid(boolean term3Valid) {
		this.term3Valid = term3Valid;
	}
	
	/**
	 * 
	 * @return 
	 */
	public String getTerm1Value() {
		return term1Value;
	}

	/**
	 * 
	 * @param term1Value 
	 */
	public void setTerm1Value(String term1Value) {
		this.term1Value = term1Value;
	}

	/**
	 * 
	 * @return 
	 */
	public String getTerm2Value() {
		return term2Value;
	}

	/**
	 * 
	 * @param term2Value 
	 */
	public void setTerm2Value(String term2Value) {
		this.term2Value = term2Value;
	}

	/**
	 * 
	 * @return 
	 */
	public String getTerm3Value() {
		return term3Value;
	}

	/**
	 * 
	 * @param term3Value 
	 */
	public void setTerm3Value(String term3Value) {
		this.term3Value = term3Value;
	}
	
	/**
	 * 
	 * @return 
	 */
	public Float getMinSyntag() {
		return minSyntag;
	}

	/**
	 * 
	 * @param minSyntag 
	 */
	public void setMinSyntag(Float minSyntag) {
		this.minSyntag = minSyntag;
	}

	/**
	 * 
	 * @return 
	 */
	public Float getSyntagmaticEntityTermFactor() {
		return syntagmaticEntityTermFactor;
	}

	/**
	 * 
	 * @param syntagmaticEntityTermFactor 
	 */
	public void setSyntagmaticEntityTermFactor(Float syntagmaticEntityTermFactor) {
		this.syntagmaticEntityTermFactor = syntagmaticEntityTermFactor;
	}

	/**
	 * 
	 * @return 
	 */
	public Boolean getSyntagCloudsCalculatedForCurrentRandomEntity() {
		return syntagCloudsCalculatedForCurrentRandomEntity;
	}

	/**
	 * 
	 * @param syntagCloudsCalculatedForCurrentRandomEntity 
	 */
	public void setSyntagCloudsCalculatedForCurrentRandomEntity(Boolean syntagCloudsCalculatedForCurrentRandomEntity) {
		this.syntagCloudsCalculatedForCurrentRandomEntity = syntagCloudsCalculatedForCurrentRandomEntity;
	}
	
	/**
	 * 
	 * @return 
	 */
	public List<Entity> getEntities() {
		return entities;
	}
	
	/**
	 * 
	 * @param entities
	 */
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	
	/**
	 * 
	 * @param entity 
	 */
	public void addEntity(Entity entity) {
		this.entities.add(entity);
	} 
	
	/**
	 * 
	 * @return 
	 */
	public int getNumberOfFoundEntities() {
		return this.entities.size();
	}
	
	/**
	 * 
	 * @return 
	 */
	public Entity getSelectedEntity() {
		return this.entities.get(entityIndex);
	}
	
	/**
	 * 
	 * @param entity 
	 */
	public void updateSelectedEntity(Entity entity) {
		this.entities.set(entityIndex, entity);
	}
	
	/**
	 * 
	 * @return 
	 */
	public Float getA() {
		return a;
	}

	/**
	 * 
	 * @param a 
	 */
	public void setA(Float a) {
		this.a = a;
	}

	/**
	 * 
	 * @return 
	 */
	public Float getB() {
		return b;
	}

	/**
	 * 
	 * @param b 
	 */
	public void setB(Float b) {
		this.b = b;
	}
}
