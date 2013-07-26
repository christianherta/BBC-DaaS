package de.bbcdaas.common.beans.document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Representation of a document field.
 * @author Robert Illers
 */
public class Field implements Serializable {
    
	public final static String FIELD_VALUE_LIST_SEPARATOR = ";";
	
    private String name = "";
    private List<String> values = new ArrayList<String>();
	private Map<String, Integer> fieldFrequencyVector = new TreeMap<String, Integer>();
    // storage configuration fields 
    private boolean store = true;
    private boolean analyze = true;
    private boolean termVectors = true;
    
    /**
     * 
     * @param name
     * @param store
     * @param analyze
     * @param termVectors 
     */
    public Field(String name, boolean store, boolean analyze, boolean termVectors) {
        this.name = name;
        this.store = store;
        this.analyze = analyze;
        this.termVectors = termVectors;
    }

    /**
     * 
     * @param value 
     */
    public void setValue(String value) {
        this.values.clear();
        this.values.add(value);
    }

    /**
     * 
     * @param values 
     */
    public void setValues(List<String> values) {
        this.values = values;
    }
    
    /**
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return  value
     */
    public String getValue() {
        
        if (this.values.isEmpty()) {
            return "";
        }
        return values.get(0);
    }
    
    /**
     * 
     * @return  values
     */
    public List<String> getValues() {
        return values;
    }
    
    /**
     * 
     * @return 
     */
    public String getValuesAsSemicolonSeparatedString() {
        
        StringBuilder combinedValue = new StringBuilder();
        boolean first = true;
        for (String value : values) {
            if (first) {
                first = false;
            } else {
                combinedValue.append(FIELD_VALUE_LIST_SEPARATOR);
            }
            combinedValue.append(value);
        }
        return combinedValue.toString();
    }

    /**
     * 
     * @return store
     */
    public boolean shouldBeStored() {
        return store;
    }
    
    /**
     * 
     * @return 
     */
    public boolean shouldBeAnalyzed() {
        return analyze;
    }
    
    /**
     * 
     * @return 
     */
    public boolean shouldCreateTermVectors() {
        return termVectors;
    }

	public Map<String, Integer> getFieldFrequencyVector() {
		return fieldFrequencyVector;
	}

	public void setFieldFrequencyVector(Map<String, Integer> fieldFrequencyVector) {
		this.fieldFrequencyVector = fieldFrequencyVector;
	}
}
