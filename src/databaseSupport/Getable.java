package databaseSupport;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import interfaces.BookInterface;

/**
 * Encapsulates all information relevant to a get request to the database.
 *
 * Currently implemented by GetUser and GetBook
 */
abstract class Getable {
	
//	protected static Set<String> alreadyPopulatedBooks = new HashSet<String>();
	protected static Map<String, BookInterface> alreadyPopulatedBooks = new HashMap<String, BookInterface>();
	protected static Set<String> alreadyPopulatedSeries = new HashSet<String>();
	protected static Set<String> alreadyPopulatedAuthors = new HashSet<String>();
	
	/**
	 * Execute the get request on the provided database connection.
	 * returns the requested object or null if the requested object
	 * does not exist in the database.
	 * 
	 * @param conn database connection
	 * @return requested object
	 * @throws SQLException
	 */
	abstract public Object get(Connection conn) throws SQLException;
	
	
	public void clear(){
//		alreadyPopulatedBooks = new HashSet<String>();
		alreadyPopulatedBooks = new HashMap<String, BookInterface>();
		alreadyPopulatedSeries = new HashSet<String>();
		alreadyPopulatedAuthors = new HashSet<String>();
	}
}
