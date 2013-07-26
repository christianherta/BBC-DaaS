package de.bbcdaas.disambiguation.evaluation.core;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Configuration for a variable parameter.
 * @author Robert Illers
 */
@XmlRootElement(name = "VariableParameter")
public class VarParameter extends Parameter {

	private String startValue;
	private String endValue;
	private List<String> steps = new ArrayList<String>();
	private int enumIndex = 0;
	// int
	private int intStartValue;
	private int intEndValue;
	private int intStep;
	private Integer intActualValue;
	// float
	private float floatStartValue;
	private float floatEndValue;
	private float floatStep;
	private Float floatActualValue;

	/**
	 * Constructor for unmarshalling
	 */
	public VarParameter() {}
	
	/**
	 * Typeless constructor, needs to be called by typed constructors.
	 * @param name name of the parameter
	 * @param type parameter type
	 */
	private VarParameter(String name, int type) {
		super(name, "", type);		
	}
	
	/**
	 * Constructor for the parameter types: enum
	 * @param name
	 * @param type
	 * @param steps
	 * @throws InvalidParameterException 
	 */
	public VarParameter(String name, int type, List<String> steps) throws InvalidParameterException {
	
		this(name, type);
		
		if (type == PARAMETER_TYPE_ENUM) {
			
		} else {
			throw new InvalidParameterException("type not supported by this constructor");
		}
		
		switch(this.getType()) {
			case PARAMETER_TYPE_ENUM:
				if (steps != null && !steps.isEmpty()) {
					this.steps = steps;
				} else {
					throw new InvalidParameterException("parameter steps is null or empty");
				}
				break;
		}
	}
	
	/**
	 * Constructor for the parameter types: int, float
	 * @param name name of the parameter
	 * @param type parameter type, has to be supported
	 * @param startValue initial value of the parameter
	 * @param endValue value that has to be reached by applying steps
	 * @param step the step that should be applied
	 * @throws InvalidParameterException
	 */
	public VarParameter(String name, int type, String startValue,
					 String endValue, String step) throws InvalidParameterException {

		this(name, type);
		
		if (type == PARAMETER_TYPE_FLOAT ||
			type == PARAMETER_TYPE_INTEGER) {

			this.startValue = startValue;
			this.endValue = endValue;
			this.steps.add(step);
		} else {
			throw new InvalidParameterException("type not supported by this constructor");
		}

		switch(this.getType()) {
			case PARAMETER_TYPE_FLOAT:
				this.floatStartValue = Float.parseFloat(this.startValue);
				this.floatEndValue = Float.parseFloat(this.endValue);
				this.floatStep = Float.parseFloat(this.steps.get(0));
				this.floatActualValue = this.floatStartValue;
				break;
			case PARAMETER_TYPE_INTEGER:
				this.intStartValue = Integer.parseInt(this.startValue);
				this.intEndValue = Integer.parseInt(this.endValue);
				this.intStep = Integer.parseInt(this.steps.get(0));
				this.intActualValue = this.intStartValue;
				break;
		}
	}

	/**
	 * Applies a step on the actual value of the parameter
	 * @return false if endValue reached
	 */
	public boolean applyStep() {

		boolean applied = true;
		switch(this.getType()) {
			case PARAMETER_TYPE_INTEGER:
				if (this.intStartValue < this.intEndValue) {
					int newValue = this.intActualValue + this.intStep;
					if (newValue > this.intEndValue) {
						applied = false;
					} else {
						this.intActualValue = newValue;
					}
				} else {
					int newValue = this.intActualValue - this.intStep;
					if (newValue < this.intEndValue) {
						applied = false;
					} else {
						this.intActualValue = newValue;
					}
				}
				break;
			case PARAMETER_TYPE_FLOAT:
				if (this.floatStartValue < this.floatEndValue) {
					double newValue = this.floatActualValue + this.floatStep;
					if ((float)(((int)(newValue*100))/100.0) > this.floatEndValue) {
						applied = false;
					} else {
						this.floatActualValue = (float)newValue;
					}
				} else {
					double newValue = this.floatActualValue + this.floatStep;
					if ((float)(((int)(newValue*100))/100.0) < this.floatEndValue) {
						applied = false;
					} else {
						this.floatActualValue = (float)newValue;
					}
				}
				break;
			case PARAMETER_TYPE_ENUM:
				if (this.steps.size()-1 > this.enumIndex) {
					this.enumIndex++;
				} else {
					applied = false;
				}
				break;
		}
		return applied;
	}

	/**
	 * Sets the actual value of the parameter to the start value.
	 */
	public void reset() {

		switch(this.getType()) {
			case PARAMETER_TYPE_INTEGER:
				this.intActualValue = this.intStartValue;
				break;
			case PARAMETER_TYPE_FLOAT:
				this.floatActualValue = this.floatStartValue;
				break;
			case PARAMETER_TYPE_ENUM:
				this.enumIndex = 0;
				break;
		}
	}

	/**
	 * 
	 * @return 
	 */
	@XmlElement(name = "startValue")
	public String getStartValue() {
		return startValue;
	}

	/**
	 * 
	 * @return 
	 */
	@XmlElement(name = "endValue")
	public String getEndValue() {
		return endValue;
	}
	
	/**
	 * 
	 * @return 
	 */
	@XmlElement(name = "step")
	public List<String> getSteps() {
		return steps;
	}

	/**
	 * The actual value as string
	 * @return actualValue
	 */
	@Override
	@XmlTransient
	public String getValue() {

		String actualValue = null;
		switch(this.getType()) {
			case PARAMETER_TYPE_INTEGER:
				actualValue = this.intActualValue.toString();
				break;
			case PARAMETER_TYPE_FLOAT:
				actualValue = this.floatActualValue.toString();
				break;
			case PARAMETER_TYPE_ENUM:
				actualValue = this.steps.get(this.enumIndex);
				break;
		}
		return actualValue;
	}
}
