package de.bbcdaas.taghandler.writer.statistics;
/**
 * Interface for a writer that creates output files containing data from the
 * databases.
 * @author Robert Illers
 */
public interface StatisticWriter extends Runnable {
    
	/**
	 * starts writing the statistic files using the parameter defined in the
	 * property files.
	 */
	public void writeStatistics();
}
