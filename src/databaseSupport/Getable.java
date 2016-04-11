package databaseSupport;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Encapsulates all information relevant to a get request to the database.
 *
 * Currently implemented by GetUser and GetBook
 */
interface Getable {
	
	/**
	 * Execute the get request on the provided database connection.
	 * returns the requested object or null if the requested object
	 * does not exist in the database.
	 * 
	 * @param conn database connection
	 * @return requested object
	 * @throws SQLException
	 */
	public Object get(Connection conn) throws SQLException;
}
