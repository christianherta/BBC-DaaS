package de.bbcdaas.common.dao.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.bbcdaas.common.dao.exceptions.ApiException;
import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lightcouch.CouchDbClient;
/**
 * Covers the LightChouch API to completly separate it from the applications code
 * LightChouch is a Java Persistence API for ChouchDB (http://www.lightcouch.org/)
 * @author Robert illers
 */
public final class LightChouchAPI implements PersistenceAPI {

	private CouchDbClient dbClient = null;
	private GsonBuilder gsonBuilder;
	private Gson gson;
	private boolean connectionOpenedExternal = false;
	private boolean enableLogs = true;

	@Override
	public boolean isEnableLogs() {
		return enableLogs;
	}

	@Override
	public void setEnableLogs(boolean enableLogs) {
		this.enableLogs = enableLogs;
	}

	/**
	 *
	 */
	public LightChouchAPI() {
		this.openConnection(new StringBuilder().append("properties").append(File.pathSeparator).append("chouchdb.properties").toString());
	}

	/**
	 *
	 * @param properties
	 */
	public void initGson(String propertiesFile) {

		this.gsonBuilder = new GsonBuilder();
		Properties properties = new Properties();
		try {
			BufferedInputStream stream;
			stream = new BufferedInputStream(new FileInputStream(propertiesFile));
			properties.load(stream);
			stream.close();
			// init gsonBuilder here
		} catch(IOException ex) {
			Logger.getLogger(LightChouchAPI.class.getName()).log(Level.SEVERE, null, ex);
		}
		this.gson = gsonBuilder.create();
		if (dbClient != null) {
			this.dbClient.setGsonBuilder(gsonBuilder);
		}
	}

	/**
	 *
	 * @param properties
	 */
	public final void openConnection(String propertiesFile) {

		this.closeConnection();
		this.dbClient = new CouchDbClient(propertiesFile);
		this.initGson("gson.properties");
	}

	/**
	 * Closes the connection to the light chouch server.
	 */
	@Override
	public void closeConnection() {

		if (this.dbClient != null) {
			this.dbClient.shutdown();
			this.dbClient = null;
		}
	}

	/**
	 *
	 * @return
	 */
	public Gson getGson() {

		return this.gson;
	}

	/**
	 *
	 * @param object
	 * @return
	 */
	public String storeDocument(Object object) {

		return dbClient.save(object).getId();
	}

	/**
	 *
	 * @param object
	 */
	public void batchStoreDocument(Object object) {

		dbClient.batch(object);
	}

	/**
	 *
	 * @param object
	 * @return
	 */
	public String updateDocument(Object object) {

		return dbClient.update(object).getId();
	}

	/**
	 *
	 * @param clazz
	 * @param id
	 * @return
	 */
	public Object getDocumentAsObject(Class clazz, String id) {

		return dbClient.find(clazz, id);
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public String getDocumentFromStream(String id) {

		InputStream in = dbClient.find(id);
		final char[] buffer = new char[0x10000];
		StringBuilder out = new StringBuilder();
		try {
			Reader isr = new InputStreamReader(in, "UTF-8");
			int read;
			do {
				read = isr.read(buffer, 0, buffer.length);
				if (read > 0) {
					out.append(buffer, 0, read);
				}
			} while (read >= 0);
			in.close();
		} catch (IOException ioex) {
			Logger.getLogger(LightChouchAPI.class.getName()).log(Level.SEVERE, null, ioex);
		}
		return out.toString();
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public boolean documentExists(String id) {

		return dbClient.contains(id);
	}

	/**
	 *
	 * @param object
	 */
	public void removeDocument(Object object) {

		dbClient.remove(object);
	}

	/**
	 *
	 * @param id
	 * @param rev
	 */
	public void removeDocument(String id, String rev) {

		dbClient.remove(id, rev);
	}

	/**
	 *
	 * @return
	 */
	public String getServerDBVersion() {

		return dbClient.context().serverVersion();
	}

	/**
	 *
	 * @return
	 */
	public List<String> getAllServerDBNames() {

		return dbClient.context().getAllDbs();
	}

	/**
	 *
	 * @return
	 */
	public long getNumberOfDocuments() {

		return dbClient.context().info().getDocCount();
	}

	@Override
	public void openConnection() throws ApiException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void commit() throws ApiException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void commitAndCloseConnection() throws ApiException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 *
	 * @return
	 */
	@Override
	public boolean isConnectionOpenedExternal() {
		return connectionOpenedExternal;
	}

	/**
	 *
	 * @param connectionOpenedExternal
	 */
	@Override
	public void setConnectionOpenedExternal(boolean connectionOpenedExternal) {
		this.connectionOpenedExternal = connectionOpenedExternal;
	}
}
