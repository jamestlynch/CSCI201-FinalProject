/**
 * ==========================================================================================================================
 * @fileName 			MapSQLStatementModel.java
 * @createdBy 			JamesLynch
 * @teamMembers 		Joseph Lin (josephml), Sarah Loui (sloui), James Lynch (jamestly), Ivana Wang (ivanawan)
 * @date 				Apr 8, 2014 3:01:12 PM
 * @professor			Jeffrey Miller
 * @project   			CSCI201 Final Project
 * @projectDescription	Map Transportation Network for Spring 2014 CSCI201 ("Principles of Software Development") at USC
 * @version				0.10
 * @versionCreated		0.10
 * ==========================================================================================================================
 *
 * ==========================================================================================================================
 */

package map.sql;

import java.sql.*;

public class MapSQLStatementModel implements Statement {

	// =========================================================================
	//   MEMBER VARIABLES
	// =========================================================================
	
	// =========================================================================
	//   CONSTRUCTORS
	// =========================================================================
	
	// =========================================================================
	//   MAP MODEL UPDATES
	// =========================================================================
	
	// =========================================================================
	//   JAVA.SQL STATEMENT IMPLMENTATION
	// =========================================================================
	
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int executeUpdate(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void close() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public int getMaxFieldSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setMaxFieldSize(int max) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public int getMaxRows() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setMaxRows(int max) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setEscapeProcessing(boolean enable) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public int getQueryTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setQueryTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void cancel() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setCursorName(String name) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public boolean execute(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public ResultSet getResultSet() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getUpdateCount() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean getMoreResults() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setFetchDirection(int direction) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setFetchSize(int rows) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getResultSetConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getResultSetType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void addBatch(String sql) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void clearBatch() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public int[] executeBatch() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getMoreResults(int current) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setPoolable(boolean poolable) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
