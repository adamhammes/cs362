package databaseSupport;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Encapsulates all information required for a put request.
 * 
 * Currently implemented by PutUser and PutBook.
 */
interface Putable {
	
	/**
	 * Execute the put request on the provided database connection.
	 * 
	 * @param conn database connection
	 * @return true on successful completion, false on failure
	 * @throws SQLException
	 */
	public boolean put(Connection conn) throws SQLException;
}
