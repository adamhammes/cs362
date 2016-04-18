package databaseSupport;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Encapsulates all information required for a put request.
 * 
 * Currently implemented by PutUser and PutBook.
 */
abstract class Putable {
	
	protected static Set<String> alreadyStoredBooks = new HashSet<String>();
	protected static Set<String> alreadyStoredSeries = new HashSet<String>();
	protected static Set<String> alreadyStoredAuthors = new HashSet<String>();
	
	/**
	 * Execute the put request on the provided database connection.
	 * 
	 * @param conn database connection
	 * @return true on successful completion, false on failure
	 * @throws SQLException
	 */
	abstract public boolean put(Connection conn) throws SQLException;
	
	
	
	public void clear(){
		alreadyStoredBooks = new HashSet<String>();
		alreadyStoredSeries = new HashSet<String>();
		alreadyStoredAuthors = new HashSet<String>();
	}
}
