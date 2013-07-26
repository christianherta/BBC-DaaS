package de.bbcdaas.common.util;
/**
 * Helper class used to count the execution time of lines of code.
 * @author Robert Illers
 */
public final class PerformanceUtils {

	private final Long startTime;
	private Long stopTime;
	private Boolean timerRunning;

	/**
	 * Constructor. Sets a timestamp that is used to calulate the time from 
	 * instanciating this class until @see #getRunningTimer is called.
	 */
	public PerformanceUtils() {

		startTime = System.currentTimeMillis();
		stopTime = Long.valueOf(0);
		timerRunning = false;

	}

	/**
	 * Sets a timestamp. Use @see #getTimeAndContinue or @see #getTimeAndStop
	 * to calculate the running time using this timestamp.
	 */
	public void startTimer() {

		stopTime = System.currentTimeMillis();
		timerRunning = true;
	}

	/**
	 * Returns the time from last call of @see #startTimer until calling this methods.
	 * @return time in milliseconds
	 */
	public long getTimeAndContinue() {
	
		return timerRunning ? System.currentTimeMillis() - stopTime : 0;
	}

	/**
	 * Returns the time from last call of @see #startTimer until calling this methods
	 * and resets the timestamp.
	 * @return time in milliseconds 
	 */
	public long getTimeAndStop() {

		long time = timerRunning ? System.currentTimeMillis() - stopTime : 0;
		if (timerRunning) {
			timerRunning = false;
			stopTime = Long.valueOf(0);
		}
		return time;
	}

	/**
	 * Returns if the timer was started by @see #startTimer .
	 * @return true if timer was started
	 */
	public boolean isTimerRunning() {

		return timerRunning;
	}

	/**
	 * Returns the time from instanciating this class until the call of this method.
	 * @return 
	 */
	public long getRunningTimer() {

		return System.currentTimeMillis() - startTime;
	}
}