package de.bbcdaas.uima_components.cpe;

import org.apache.log4j.Logger;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.collection.StatusCallbackListener;

/**
 *
 * @author Robert Illers
 */
public class StatusCallbackListenerImpl implements StatusCallbackListener {

	private static Logger logger = Logger.getLogger(StatusCallbackListenerImpl.class);
    private volatile boolean finished = false;

	/**
	 *
	 * @return
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Called when the processing of a Document is completed. <br/>
     * The process status can be looked at and corresponding actions taken.
	 * @param cas CAS corresponding to the completed processing
	 * @param eps EntityProcessStatus that holds the status of all the events for aEntity
	 */
	@Override
	public void entityProcessComplete(CAS cas, EntityProcessStatus eps) {
		if (eps.getExceptions().size() > 0) {
			logger.error("entityProcessComplete(): ",eps.getExceptions().get(0));
		}
	}

	/**
	 * Called when the initialization is completed.
	 * @see org.apache.uima.collection.processing.StatusCallbackListener#initializationComplete()
	 */
	@Override
	public void initializationComplete() {
		logger.info("Collection Processing managers initialization is complete.");
	}

	/**
	 * Called when the batchProcessing is completed.
	 * @see org.apache.uima.collection.processing.StatusCallbackListener#batchProcessComplete()
	 */
	@Override
	public void batchProcessComplete() {
		logger.info("Batch processing is complete.");
	}

	/**
	 * Called when the collection processing is completed.
	 * @see org.apache.uima.collection.processing.StatusCallbackListener#collectionProcessComplete()
	 */
	@Override
	public void collectionProcessComplete() {
		this.finished = true;
		logger.info("Collection processing is complete.");
	}

	/**
	 * Called when the CPM is paused.
	 * @see org.apache.uima.collection.processing.StatusCallbackListener#paused()
	 */
	@Override
	public void paused() {
		logger.info("Collection processing manager paused.");
	}

	/**
	 * Called when the CPM is resumed after a pause.
	 * @see org.apache.uima.collection.processing.StatusCallbackListener#resumed()
	 */
	@Override
	public void resumed() {
		logger.info("Collection processing manager resumed.");
	}

	/**
	 * Called when the CPM is stopped abruptly due to errors.
	 * @see org.apache.uima.collection.processing.StatusCallbackListener#aborted()
	 */
	@Override
	public void aborted() {
		logger.info("Collection processing manager stopped.");
	}
}
