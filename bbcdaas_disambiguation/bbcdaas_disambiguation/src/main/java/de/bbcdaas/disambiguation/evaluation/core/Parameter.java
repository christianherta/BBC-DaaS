package de.bbcdaas.disambiguation.evaluation.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * CLass representing a parameter. Can be extended to variable parameter.
 * @author Robert Illers
 */
@XmlRootElement
public class Parameter {
	
	public static final int PARAMETER_TYPE_INTEGER = 1;
	public static final int PARAMETER_TYPE_FLOAT = 2;
	public static final int PARAMETER_TYPE_ENUM = 3;
	
	private final String name;
	private String value;
	private List<String> enumValue = new ArrayList<String>();
	// integer/float/enum
	private final int type;
	private List<Map<String, String>> parameterDependencies = new ArrayList<Map<String, String>>();

	/**
	 * Constructor for unmarshalling
	 */
	public Parameter() {
		
		this.name = null;
		this.type = 0;
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param type 
	 */
	private Parameter(String name, int type) {
		
		this.name = name;
		this.type = type;
		
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param value
	 * @param type 
	 */
	public Parameter(String name, String value, int type) {
		
		this(name, type);
		
		if (this.type == PARAMETER_TYPE_FLOAT ||
			this.type == PARAMETER_TYPE_INTEGER) {
			this.value = value;
		} else
		if (this.type == PARAMETER_TYPE_ENUM) {
			
		}
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param value
	 * @param type 
	 */
	public Parameter(String name, List<String> value, int type) {
		
		this(name, type);
		
		if (this.type == PARAMETER_TYPE_ENUM) {
			this.enumValue = value;
		}
	}
	
	/**
	 * 
	 * @return 
	 */
	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return 
	 */
	@XmlTransient
	public String getValue() {
		
		if (this.type == PARAMETER_TYPE_FLOAT ||
			this.type == PARAMETER_TYPE_INTEGER) {
			return this.value;
		} else
		if (this.type == PARAMETER_TYPE_ENUM) {	
			return this.enumValue.toString();
		}
		return null;
	}

	/**
	 * 
	 * @return 
	 */
	@XmlElement(name = "type")
	public int getType() {
		return type;
	}

	/**
	 * 
	 * @return 
	 */
	@XmlTransient 
	public List<Map<String, String>> getParameterDependencies() {
		return parameterDependencies;
	}

	/**
	 * 
	 * @param parameterDependencies 
	 */
	public void setParameterDependencies(List<Map<String, String>> parameterDependencies) {
		this.parameterDependencies = parameterDependencies;
	}
}
