package de.bbcdaas.common.dao.api;

import de.bbcdaas.common.dao.exceptions.ApiException;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Wrapper API for hibernate.
 * @author Robert Illers
 */
public final class HibernateAPI implements PersistenceAPI {

	private static SessionFactory sessionFactory = null;
	private Transaction transaction = null;
	private Session session = null;
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
	public HibernateAPI() {

		try {

			Configuration configuration = new Configuration().configure();
			sessionFactory = configuration.buildSessionFactory(new ServiceRegistryBuilder().
				applySettings(configuration.getProperties()).buildServiceRegistry());
		}
		catch (HibernateException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 *
	 */
	@Override
	public void openConnection() throws ApiException {

		if (session == null || !session.isOpen()) {
			try {
				session = sessionFactory.openSession();
			} catch(HibernateException ex) {
				throw new ApiException("openConnection()", ex.getMessage());
			}
		}
		if (transaction == null) {
			transaction = session.beginTransaction();
		}
	}

	/**
	 *
	 * @param clazz
	 * @param id
	 * @return
	 */
	public Object get(Class clazz, Integer id) {

		return session.get(clazz, id);
	}

	/**
	 *
	 * @param table
	 * @return
	 */
	public List<Object> getAll(String table) {

		return session.createQuery(new StringBuilder("FROM ").
			append(table).toString()).list();
	}

	/**
	 *
	 * @param object
	 */
	public void save(Object object) {

		session.save(object);
	}

	/**
	 *
	 * @param object
	 */
	public void update(Object object) {

		session.update(object);
	}

	/**
	 *
	 * @param clazz
	 * @param ID
	 */
	public void delete(Class clazz, int ID) {

		Object object = session.get(clazz, ID);
		session.delete(object);
	}

	/**
	 *
	 * @param object
	 */
	public void delete(Object object) {

		session.delete(object);
	}

	/**
	 *
	 */
	@Override
	public void closeConnection() throws ApiException {

		if (session != null && session.isOpen()) {

			session.close();
			transaction = null;
			session = null;
		}
		throw new ApiException("closeConnection()", "hibernate session is not opened or null.");
	}

	/**
	 *
	 */
	@Override
	public void commitAndCloseConnection() throws ApiException {

		if (session != null && session.isOpen() && transaction != null) {

			try {
				transaction.commit();
			} catch(Exception e) {
				transaction.rollback();
				throw new ApiException("commitAndCloseConnection()", e.getMessage());
			} finally {
				session.close();
			}
			transaction = null;
			session = null;
		}
		throw new ApiException("commitAndCloseConnection()", "hibernate session is not opened or null.");
	}

	/**
	 *
	 */
	@Override
	public void commit() throws ApiException {

		if (session != null && session.isOpen() && transaction != null) {

			try {
				transaction.commit();
			} catch(Exception e) {
				transaction.rollback();
				session.close();
				transaction = null;
				session = null;
				throw new ApiException("commit()", e.getMessage());
			}
			session.clear();
			transaction = null;
		}
		throw new ApiException("commit()", "hibernate session is not opened or null.");
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