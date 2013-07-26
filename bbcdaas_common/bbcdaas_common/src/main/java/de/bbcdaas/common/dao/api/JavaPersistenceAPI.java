package de.bbcdaas.common.dao.api;

import de.bbcdaas.common.dao.exceptions.ApiException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * Wrapper API for JPA.
 * @author Robert Illers
 */
public final class JavaPersistenceAPI implements PersistenceAPI {

	private EntityManagerFactory entityManagerFactory = null;
	private EntityManager entityManager = null;
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
	 * @param persistenceUnitName
	 */
	public JavaPersistenceAPI(String persistenceUnitName) {
		this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
	}

	/**
	 *
	 */
	@Override
	public void openConnection() throws ApiException {

		if (!connectionOpenedExternal) {
			if (this.entityManager == null || !this.entityManager.isOpen()) {
				this.entityManager = entityManagerFactory.createEntityManager();
			}
			this.entityManager.getTransaction().begin();
		}
	}

	/**
	 *
	 * @throws ApiException
	 */
	@Override
	public void commit() throws ApiException {

		this.entityManager.getTransaction().commit();
		this.entityManager.clear();
		this.entityManager.getTransaction().begin();
	}

	/**
	 *
	 */
	@Override
	public void closeConnection() throws ApiException {

		if (!connectionOpenedExternal) {
			this.entityManager.close();
		}
	}

	/**
	 *
	 * @param forceClose
	 * @throws ApiException
	 */
	public void closeConnection(boolean forceClose) throws ApiException {

		if (forceClose) {
			this.entityManager.close();
			this.connectionOpenedExternal = false;
		} else {
			this.closeConnection();
		}
	}

	/**
	 *
	 * @throws ApiException
	 */
	@Override
	public void commitAndCloseConnection() throws ApiException {

		this.commit();
		this.closeConnection();
	}

	/**
	 *
	 * @param object
	 */
	public void persist(Object object) {
		this.entityManager.persist(object);
	}

	/**
	 *
	 * @param object
	 */
	public void remove(Object object) {
		this.entityManager.remove(object);
	}

	/**
	 *
	 * @param object
	 */
	public void refresh(Object object) {
		this.entityManager.refresh(object);
	}

	/**
	 *
	 * @param clazz
	 * @param primaryKey
	 * @return
	 */
	public <T> T find(Class<T> clazz, Object primaryKey) {
		return (T)this.entityManager.find(clazz, primaryKey);
	}

	/**
	 *
	 * @param table
	 * @return
	 */
	public <T> List<T> findAll(Class<T> clazz) {

		TypedQuery<T> q = this.entityManager.createQuery(new StringBuilder("SELECT e FROM ").
			append(clazz.getSimpleName()).append(" e").toString(), clazz);
		return q.getResultList();
	}

	/**
	 *
	 * @param <T>
	 * @param clazz
	 * @param whereClause
	 * @return
	 */
	public <T> List<T> findAllByConditions(Class<T> clazz, String... conditions) {

		StringBuilder query = new StringBuilder("SELECT e FROM ").
			append(clazz.getSimpleName()).append(" e WHERE ");
		int i = 0;
		for (String condition : conditions) {
			if (i != 0) {
				query.append(" AND ");
			}
			query.append("e.").append(condition);
			i++;
		}
		TypedQuery<T> q = this.entityManager.createQuery(query.toString(), clazz);
		return q.getResultList();
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