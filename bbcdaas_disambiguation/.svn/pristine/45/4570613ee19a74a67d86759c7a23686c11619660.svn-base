package de.bbcdaas.disambiguation.evaluation.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;

/**
 * Base class for all evaluation results produced by an implementation of AbstractEvaluator.
 * @author Robert Illers
 */
public abstract class AbstractEvaluationResult {
	
	// common configuration
	private List<VarParameter> varParameters = new ArrayList<VarParameter>();
	private List<Parameter> constParameters = new ArrayList<Parameter>();
	protected String xmlOutputPath;
	
	/**
	 * Constructor needed for unmarshalling
	 */
	public AbstractEvaluationResult() {}
	
	/**
	 * Constructor
	 * @param xmlOutputPath abolute path to location where this object should be marshalled to
	 */
	public AbstractEvaluationResult(String xmlOutputPath) {
		this.xmlOutputPath = xmlOutputPath;
	}
	
	/**
	 * 
	 * @return 
	 */
	@XmlElement(name = "variableParameter")
	public List<VarParameter> getVarParameters() {
		return varParameters;
	}

	/**
	 * 
	 * @param varParameters 
	 */
	public void setVarParameters(List<VarParameter> varParameters) {
		this.varParameters = varParameters;
	}

	/**
	 * 
	 * @return 
	 */
	@XmlElement(name = "constantParameter")
	public List<Parameter> getConstParameters() {
		return constParameters;
	}

	/**
	 * 
	 * @param constParameters 
	 */
	public void setConstParameters(List<Parameter> constParameters) {
		this.constParameters = constParameters;
	}
	
	/**
	 * 
	 */
	public abstract void writeResultToXmlFile() throws JAXBException, IOException;
}
