package de.bbcdaas.common.dao.api;

import de.bbcdaas.common.dao.exceptions.ApiException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.collections.map.CaseInsensitiveMap;
/**
 * API that covers java jdbc to completly separate it from the applications code.
 * @author Robert Illers
 */
public final class JdbcAPI implements PersistenceAPI {

    // if true sql queries will be logged
    private boolean logSqlQueries = false;
	// set to true if database supports transactions
	private boolean transactionsSupported = true;
	// set to true if savepoints should be set (only supported if transactionsSupported == true)
	private boolean useSavepoints = false;
	// if set to true API opens one permanent connection until the connection is forced to close
	// && throws a exception if more than 2 connection levels opened (closeConnection() missing)
	private boolean singleConnection = true;
	// a java jdbc dataSource that contains the connection infos to the database
    private DataSource dataSource = null;
	// the connection object
    private Connection conn = null;
	private Savepoint savepoint = null;
	private PreparedStatement stmt = null;
	// string that stores the last query. if the query does not change between two update or query instructions,
	// no new statement object will be precompiled
	private String query = "";
	// counts how often openConnection is called
	private int openCount = 0;
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

	/*----------------------- public methods ---------------------------------*/

	/**
     * Sets the DataSource Object that contains connection informations to the database
     * @param dataSource
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     *
     * @param logSqlQueries
     */
    public void setLogSqlQueries(boolean logSqlQueries) {
        this.logSqlQueries = logSqlQueries;
    }

	/**
	 * Set to true if database supports transactions (needed for using savepoints/rollbacks and bundles commits)
	 * @param transactionsSupported
	 */
	public void setTransactionsSupported(boolean transactionsSupported) {
		this.transactionsSupported = transactionsSupported;
	}

	/**
	 *
	 * @return
	 */
	public boolean isTransactionsSupported() {
		return transactionsSupported;
	}

	/**
	 * Set to true if Savepoints should be used (only possible if transactionsSupported set to true)
	 * @param useSavepoints
	 */
	public void setUseSavepoints(boolean useSavepoints) {
		this.useSavepoints = useSavepoints;
	}

	/**
	 *
	 * @return
	 */
	public boolean isUseSavepoints() {
		return useSavepoints;
	}

	/**
	 *
	 * @return
	 */
	public boolean isSingleConnection() {
		return singleConnection;
	}

	/**
	 *
	 * @param singleConnection
	 */
	public void setSingleConnection(boolean singleConnection) {
		this.singleConnection = singleConnection;
	}

	/**
	 *
	 * @throws ApiException
	 */
	@Override
	public void openConnection() throws ApiException {

		boolean autoCommit = true;
		if (this.transactionsSupported) {
			autoCommit = false;
		}
		this.openConnection(autoCommit);
	}

	/**
     * Executes an SQL update. ArrayList Arguments are supported but only one list and
	 * only as last or single argument.
     * @param sql
     * @param args
     */
    public void executeUpdate(String sql, Object... args) throws ApiException {

        try {

            prepareStatement(sql, args);
			this.stmt.execute();

        } catch(SQLException e) {
			this.rollback();
			this.closeConnection(true);
			throw new ApiException("executeUpdate", e.getMessage());
        }
    }

    /**
     * Executes a query. ArrayList Arguments are supported but only one list and
	 * only as last or single argument.
     * @param sql
     * @param args
     * @return
     */
    public List<Map<String, Object>> executeQuery(String sql, Object... args) throws ApiException {

        try {

			prepareStatement(sql, args);
			ResultSet rs = this.stmt.executeQuery();
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			try {
				ResultSetMetaData meta = rs.getMetaData();
				while (rs.next()) {
					Map<String, Object> rowMap = new CaseInsensitiveMap();
					for (int i = 1; i <= meta.getColumnCount();i++) {
						switch(meta.getColumnType(i)) {
							case java.sql.Types.BIGINT:
								rowMap.put(meta.getColumnLabel(i),rs.getLong(i));break;
							case java.sql.Types.INTEGER:
								Integer integer = rs.getInt(i);
								rowMap.put(meta.getColumnLabel(i),integer.longValue());break;
							case java.sql.Types.REAL:
								rowMap.put(meta.getColumnLabel(i),rs.getFloat(i));break;
                                                        case java.sql.Types.DOUBLE:
								rowMap.put(meta.getColumnLabel(i),rs.getFloat(i));break;
							case java.sql.Types.VARCHAR:
								rowMap.put(meta.getColumnLabel(i),rs.getString(i));break;
							case java.sql.Types.DATE:
								rowMap.put(meta.getColumnLabel(i), rs.getDate(i));break;
							default:
								String msg = "Type '"+meta.getColumnType(i)+"' of column '"+meta.getColumnLabel(i)+"' not supported";
								throw new ApiException("executeQuery", msg);
						}
					}
					result.add(rowMap);
				}
			} finally {
				rs.close();
			}
			return result;

        } catch(SQLException e) {
			this.rollback();
			this.closeConnection(true);
			throw new ApiException("executeQuery", e.getMessage());
        }
    }

	/**
	 *
	 * @throws ApiException
	 */
	@Override
	public void commitAndCloseConnection() throws ApiException {

		this.closeConnection(false, true);
	}

	/**
	 *
	 */
	@Override
	public void closeConnection() throws ApiException {

		this.closeConnection(false);
	}

	/**
	 *
	 * @param forceClose
	 * @throws ApiException
	 */
	public void closeConnection(boolean forceClose) throws ApiException {

		this.closeConnection(forceClose, false);
	}

	/**
	 * Executes the jdbc commit command and takes cares of error states.
	 */
	@Override
	public void commit() throws ApiException {

		try {
			if (conn != null) {
				if (this.transactionsSupported && !conn.getAutoCommit()) {
					conn.commit();
                    if (logSqlQueries) {
                        System.out.println("COMMIT");
                    }
				}
				if (this.useSavepoints && this.savepoint != null) {
					this.conn.releaseSavepoint(savepoint);
					this.savepoint = null;
				}
			}
		} catch (SQLException e) {
			this.rollback();
			this.closeConnection(true);
			throw new ApiException("commit", e.getMessage());
		}
	}

	/*---------------------- private methods ---------------------------------*/

	/**
	 * Opens a new connection to the selected database.
	 * @param autoCommit
	 */
	private void openConnection(boolean autoCommit) throws ApiException {

		try {
			if (conn == null || conn.isClosed()) {
				if (dataSource == null) {
					String msg = "No datasource set.";
					throw new ApiException("openConnection", msg);
				}
				conn = dataSource.getConnection();
				conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				// set connection counter to 1 to have a permanent connection
				if (singleConnection) {
					this.openCount = 1;
				}
				try {
					if (this.stmt != null) {
						this.stmt.close();
					}
					this.stmt = conn.prepareStatement("SET NAMES 'utf8'");
					this.stmt.executeQuery();
				} catch(SQLException e) {
					// ignore, does not work if not supported by database
				} finally {
					if (this.stmt != null) {
						this.stmt.close();
					}
				}
			}
			if (conn.getAutoCommit() != autoCommit) {
				conn.setAutoCommit(autoCommit);
			}
			if (this.useSavepoints && this.transactionsSupported && !autoCommit && this.savepoint == null) {
				savepoint = conn.setSavepoint();
			}
			// increase connection counter to 2
			this.openCount++;
			if (this.singleConnection && openCount > 2) {
				this.openCount--;
			}
		} catch (SQLException e) {
			this.closeConnection(true);
			throw new ApiException("openConnection", e.getMessage());
		}
	}

	/**
	 * Closes the connection to the database.
	 * @param forceClose
	 */
	private void closeConnection(boolean forceClose, boolean commit) throws ApiException {

		if (forceClose || this.openCount < 0) {
			this.openCount = 0;
		}
		if (!forceClose && this.openCount > 0) {
			this.openCount--;
		}

		try {
			if (!forceClose && commit) {
				this.commit();
			}
			if (this.stmt != null) {
				this.stmt.close();
			}

		} catch(SQLException e) {
			throw new ApiException("closeConnection", e.getMessage());
		}
		this.query = "";
		if (this.openCount == 0) {
			try {
				if (conn != null) {
					if (this.useSavepoints && this.savepoint != null) {
						this.conn.releaseSavepoint(savepoint);
						this.savepoint = null;
					}
						this.conn.close();
					}
			} catch (SQLException e) {
				throw new ApiException("closeConnection", e.getMessage());
			}
		}
	}

	/**
	 * Undoes all changes made in the current transaction and releases any database locks
	 */
	private void rollback() throws ApiException {

		try {
			if (this.useSavepoints && this.transactionsSupported && conn != null && !conn.isClosed() && !conn.getAutoCommit()) {
				if (savepoint != null) {
					this.conn.rollback(this.savepoint);
				} else {
					throw new ApiException("rollback","Savepoint not set!");
				}
			}
		} catch (SQLException e) {
			this.closeConnection(true);
			throw new ApiException("rollback", e.getMessage());
		}
	}

    /**
     * Creates a Prepared Statement Object
     * @param sql
     * @param args
     * @throws SQLException
     */
    private void prepareStatement(String sql, Object... args) throws ApiException {

		try {
			if (conn != null && !conn.isClosed()) {
				// check if new statement has to be created
				if (!sql.equals(this.query)) {
					if (this.stmt != null) {
						this.stmt.close();
					}
					this.stmt = conn.prepareStatement(sql);
                    if (this.logSqlQueries) {
                        System.out.println(sql);
                    }
					// set a timeout jdbc will wait for mySql at max (0 -> infinitive)
					this.stmt.setQueryTimeout(0);
					this.query = sql;
				}
			} else {
				String msg = "connection null or closed";
				throw new ApiException("prepareStatement", msg);
			}

			// parse parameter
			int i = 1;
			List<Object> objects = new ArrayList<Object>();

			// if an arrayList is found in argument list, extract its content to
			// the total list of arguments. Only one list supported and only as last
			// or single argument.
			for (Object arg : args) {
				if (arg instanceof ArrayList) {
					List obj_list = (ArrayList)arg;
					for (Object listObj : obj_list) {
						objects.add(listObj);
					}
				} else {
					objects.add(arg);
				}
			}

			for (Object obj : objects) {

				if (obj instanceof Integer) {
					this.stmt.setInt(i, (Integer)obj);
				} else
				if (obj instanceof String) {
					this.stmt.setString(i, (String)obj);
				} else
				if (obj instanceof Long) {
					this.stmt.setLong(i, (Long)obj);
				} else
				if (obj instanceof Float) {
					this.stmt.setFloat(i, (Float)obj);
				} else
				if (obj instanceof java.sql.Date) {
					this.stmt.setDate(i, (java.sql.Date)obj);
				} else
				if (obj instanceof Boolean) {
					Boolean bool = (Boolean)obj;
					int booleanRepresentative = bool ? 1 : 0;
					this.stmt.setInt(i, booleanRepresentative);
				} else {
					String msg = "Type of argument "+i+" not supported";
					throw new ApiException("prepareStatement", msg);
				}
				i++;
			}
		} catch (SQLException e) {
			this.rollback();
			this.closeConnection(true);
			throw new ApiException("prepareStatement", e.getMessage());
		}
    }

	/**
	 * Returns true if the connection has been opened outside a dao.
	 * @return connectionOpenedExternal
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