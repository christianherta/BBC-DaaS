package de.bbcdaas.disambiguation.evaluation.core;

import de.bbcdaas.disambiguation.core.configs.AbstractDisambiguationEngineConfig;
import java.util.ArrayList;
import java.util.List;
import org.jboss.logging.Logger;

/**
 * Base class for all evaluators.
 * @author Robert Illers
 */
public abstract class AbstractEvaluator<T extends AbstractDisambiguationEngineConfig> {

	protected Logger logger = Logger.getLogger(this.getClass());
	private List<VarParameter> varParameters = new ArrayList<VarParameter>();
	protected int parameterIndex;
	protected boolean doReset = false;
	protected String xmlOutputPath;
	protected boolean testRun = false;

	/**
	 * Constructor
	 * @param xmlOutputPath
	 */
	public AbstractEvaluator(String xmlOutputPath) {
		this.xmlOutputPath = xmlOutputPath;
	}

	/**
	 * Calculates how many evaluation steps are neccessary for using all possible
	 * combinations of variable parameters.
	 * @return stepAmount
	 */
	public int calculateEvaluationStepAmount() {

		int stepAmount = 1;
		for (VarParameter varParameter : this.varParameters) {

			int varParamStepAmount = 0;
			int type = varParameter.getType();
			switch(type) {
				case VarParameter.PARAMETER_TYPE_ENUM:
					varParamStepAmount = varParameter.getSteps().size();
					break;
				case VarParameter.PARAMETER_TYPE_INTEGER:
					int intStartParameter = Integer.parseInt(varParameter.getStartValue());
					int intEndParameter = Integer.parseInt(varParameter.getEndValue());
					int intStep = Integer.parseInt(varParameter.getSteps().get(0));
					varParamStepAmount = Math.abs(intStartParameter - intEndParameter) / intStep + 1;
					break;
				case VarParameter.PARAMETER_TYPE_FLOAT:
					float floatStartParameter = Float.parseFloat(varParameter.getStartValue());
					float floatEndParameter = Float.parseFloat(varParameter.getEndValue());
					float floatStep = Float.parseFloat(varParameter.getSteps().get(0));
					varParamStepAmount = (int)(Math.abs(floatStartParameter - floatEndParameter) / floatStep) + 1;
					break;
			}
			stepAmount *= varParamStepAmount;
		}
		return stepAmount;
	}

	/**
	 *
	 * @param config
	 */
	protected abstract void  setVarParameterIntoConfig(T config);

	/**
	 *
	 * @param varParameter
	 */
	public final void setVarParameters(List<VarParameter> varParameters) {

		this.varParameters = varParameters;
		this.parameterIndex = this.varParameters.size()-1;
	}

	/**
	 *
	 * @return varParameters
	 */
	protected List<VarParameter> getVarParameters() {
		return this.varParameters;
	}

	/**
	 *
	 * @param name
	 * @return varParameter with given name
	 */
	protected VarParameter getVarParameterByName(String name) {

		for (VarParameter param : this.varParameters) {

			if (param.getName().equals(name)) {
				return param;
			}
		}
		return null;
	}

	/** Tries to apply a step on the current selected parameter. If that does
	 * not work, it tries to apply a step on the following variable parameter
	 * and resets the trailing parameters. If the parameterIndex leaves the
	 * range of parameters, all combinations of variable parameters have been
	 * applied.
	 * Example: 3 variable parameters, step = 1 (for each), startValue = 0 (for each), endvalue = 2 (for each)
	 * 0,0,0
	 * 0,0,1
	 * 0,0,2
	 * 0,1,0
	 * 0,1,1
	 * 0,1,2
	 * 0,2,0
	 * 0,2,1
	 * 0,2,2
	 * 1,0,0
	 * 1,0,1 .... last combination: 2,2,2
	 * More complex example: 3 varParams, step_1: 0.1, step_2: 1, steps_3: Alpha, Beta, Gamma, ... etc
	 * 0.3,7,Alpha
	 * 0.3,7,Beta ... last combination: -0.4,8,Omega
	 * @return false if all combinations of variable parameters have been applied
	 */
	protected boolean applyParameterSteps() {

		if (!this.getVarParameters().isEmpty()) {

			if (!applyStep()) {
				return false;
			}
			if (this.doReset) {
				reset();
			}
			return true;
		}
		return true;
	}

	/**
	 * Recursive part of the method applyParameterSteps().
	 * @return false if all combinations of variable parameters have been applied
	 */
	private boolean applyStep() {

		if (!this.getVarParameters().get(this.parameterIndex).applyStep()) {
			this.parameterIndex--;
			if (this.parameterIndex < 0) {
				return false;
			}
			this.doReset = true;
			return applyStep();
		}
		return true;
	}

	/**
	 * Resets all variable parameters that where applied to their endValue
	 * before the actual value of the current variable parameter.
	 * The current variable parameter is selected by the parameter index.
	 */
	private void reset() {

		while(this.parameterIndex + 1 < this.getVarParameters().size()) {

			this.parameterIndex++;
			this.getVarParameters().get(this.parameterIndex).reset();
		}
		this.doReset = false;
	}

	/**
	 * Starts the evaluation. Needs to be implememented by the evaluator.
	 * @param testRun if true disambiguation will not be executed, only variable parameter change process
	 */
	public abstract <R extends AbstractEvaluationResult> R evaluate();

	public boolean isTestRun() {
		return testRun;
	}

	public void setTestRun(boolean testRun) {
		this.testRun = testRun;
		if (testRun) {
			logger.info("Caution: Evaluator is set to test mode.");
		}
	}
}
