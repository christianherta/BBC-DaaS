package de.bbcdaas.common.beans.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Bean that represents an entity.
 * @author Christian Herta
 * @author Robert Illers
 */
public final class Entity implements Serializable {

	private int id;
	private String name;
    List<TermCloudField> fields = new ArrayList<TermCloudField>();
	
	/**
	 * constructor
	 */
	public Entity() {};
	
    /**
     * constructor
     * @param entityName
     * @param fields 
     */
    public Entity(String entityName, List<TermCloudField> fields) {
        this.name = entityName;
        this.fields.addAll(fields);
    }
    
    /**
     * constructor
     * @param entityID
     * @param entityName
     * @param fields 
     */
    public Entity(int entityID, String entityName, List<TermCloudField> fields) {
        this(entityName, fields);
        this.id = entityID;
    }
	
	/**
     * 
     * @return 
     */
	public int getID() {
		return id;
	}
	
	/**
	 * 
	 * @param id 
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return String 
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
     * @param fields 
     */
    public void setFields(List<TermCloudField> fields) {
        this.fields = fields;
    }
    
    /**
     * 
     * @param field 
     */
    public void addField(TermCloudField field) {
        this.fields.add(field);
    }
    
    /**
     * 
     * @return 
     */
    public List<TermCloudField> getFields() {
        return fields;
    }
    
    /**
     * 
     * @param id
     * @return 
     */
    public TermCloudField getField(int id) {
        for (TermCloudField field : fields) {
            if (field.getID() == id) {
                return field;
            }
        }
        return null;
    }	
    
    /**
     * Removes all attached fields from the entity.
     */
    public void clearFields() {
        this.fields.clear();
    }
}