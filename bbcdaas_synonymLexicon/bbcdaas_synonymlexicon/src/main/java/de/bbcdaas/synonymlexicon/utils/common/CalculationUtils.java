package de.bbcdaas.synonymlexicon.utils.common;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import org.apache.log4j.Logger;
/**
 *
 * @author Frithjof Schulte
 * @author Robert Illers
 */
public class CalculationUtils {
	
	private static Logger logger = Logger.getLogger(CalculationUtils.class);

	/**
	 *
	 * @param value
	 * @param decimalFormat
	 * @return
	 */
	public static Double roundValue(Double value, String decimalFormat) {

		try {
			
			DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
			symbols.setDecimalSeparator('.');
			DecimalFormat df = new DecimalFormat(decimalFormat, symbols);
			String formated = df.format(value);
			return Double.valueOf(formated);
		} catch(Exception ex) {
			
			logger.error("Error: Double value == "+value+" could not be converted to decimal format '"+decimalFormat+"'.", ex);
			return value;
		} 
	}
}