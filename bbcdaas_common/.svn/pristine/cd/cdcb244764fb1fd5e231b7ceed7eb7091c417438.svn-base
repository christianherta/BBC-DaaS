package de.bbcdaas.common.dao.impl.chouchdb;

import com.google.gson.JsonElement;
import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.common.dao.DocumentDao;
import de.bbcdaas.common.dao.base.ChouchDBBaseDao;
/**
 * DAO that stores and retrieves documents from a chouchDB.
 * @author Robert Illers
 */
public class DocumentDaoImpl extends ChouchDBBaseDao implements DocumentDao {
	
	/**
	 * 
	 * @param entity
	 * @return 
	 */
	@Override
	public String storeEntity(Entity entity) {
		
		JsonElement jsonElement = this.getChouchDB().getGson().toJsonTree(entity);
		logger.debug(this.getChouchDB().getGson().toJson(jsonElement));
		return this.getChouchDB().storeDocument(jsonElement.getAsJsonObject());
	}

        /**
         * 
         */
	@Override
	public void commit() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
