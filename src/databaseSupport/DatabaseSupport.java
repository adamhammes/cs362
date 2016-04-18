package databaseSupport;

import java.io.File;
import java.sql.*;
import java.util.Scanner;

import interfaces.*;

/**
 * DatabaseSupport provides an easy way to retrive and store books and users in
 * the database. Objects such as reviews, tags, authors, and versions can be stored
 * if they are attached to a user or a book.
 */
public class DatabaseSupport implements DatabaseSupportInterface {
	
	private static final String USERNAME = "postgres";
	private static final String PASSWORD = "a"; // no password needed
	private static final String CONN_STRING = "jdbc:postgresql://localhost:5432/System";

	
	/**
	 * Opens a connection to the postgres database and returns the connection.
	 * 
	 * @return connection to database
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private Connection openConnection() throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		return DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
	}

	
	/**
	 * Helper function to encapsulate the try catch code that is common to all 
	 * get methods. The get parameter contains the information and type of
	 * object to retrieve from the database
	 * 
	 * @param getable get object containing all information for the db request
	 * @return requested object or null
	 */
	private Object get(Getable get) {	
		Connection conn = null;
		
		try {
			conn = openConnection();
			get.clear(); //Clears what has already been loaded
			return get.get(conn);
		} catch (SQLException | ClassNotFoundException e) {
			return null;
		}
		finally{
			try {
				conn.close();
			} catch (Exception e2) {
				// do nothing
				e2.printStackTrace();
			}
		}
	}

	
	/**
	 * Helper function to encapsulate the try catch code that is common to all 
	 * put methods. The put parameter contains the information required to 
	 * execute the put request. 
	 * 
	 * @param put putable object containing all information for the db request
	 * @return true on successfully updated, false on failure.
	 */
	private boolean put(Putable put) {
		Connection conn = null;
		
		try {
			conn = openConnection();
			put.clear();//Clear what has already been stored
			return put.put(conn);
		}
		catch(SQLException | ClassNotFoundException e) {
			return false;
		}
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				//Nothing here?
			}
		}
	}

	
	/**
	 * Requests the the user identified by the userid from the database, and populate
	 * all books and tags owned by this user. If the user does not exist in the database
	 * this method returns null.
	 * 
	 * @param uid 	userid to uniquely identify this user
	 * @return requested user or null
	 */
	@Override
	public UserInterface getUser(String uid) {
		if (uid == null || uid.equals("")) return null;
		
		return (UserInterface) get(new GetUser(uid));
	}
	
	
	/**
	 * Commits this user and all of the books and tags that they own to the database.
	 * If no user by this id exists, a new entry into the database will be created for
	 * them. If the user exists, all changed fields will be updated.
	 * 
	 * @param user The user to commit to the database
	 * @return true on successfully added to database, false on failure to add to database
	 */
	@Override
	public boolean putUser(UserInterface user) {
		if (user == null || user.getName() == null || user.getName().equals("")) return false;
		
		return put(new PutUser(user));
	}
	
	
	/**
	 * Requests a book from the database and populates any rateings and authors that belong 
	 * to the book. If no book by this identifier is found, this method returns null.
	 * 
	 * @param bid the book identifier of the requested book
	 * @return the requested book or null
	 */
	@Override
	public BookInterface getBook(String bid){ return getBook(bid, null); }
	
	
	/**
	 * Requests a book from the database and populates any ratings, authors and versions that
	 * belong to the book. If no book by this identifier is found, this method returns null.
	 * 
	 * @param bid the book identifier of the requested book
	 * @param username the username of the owner of the book (used for retrieving versions)
	 * @return the requested book or null
	 */
	@Override
	public BookInterface getBook(String bid, String username) {
		if (bid == null || bid.equals("")) return null;
		
		return (BookInterface) get(new GetBook(bid, username));
	}
	
	
	/**
	 * Commits a book to the database as well as any authors and reviews for the book.
	 * If the book already exists in the database, the entry will be updated. If there
	 * is no entry, one will be created.
	 * 
	 * @param book the book to commit to the database
	 * @return true of the operation was successful, false if the operation failed
	 */

	@Override
	public boolean putBook(BookInterface book){ return putBook(book, null); }
	
	
	/**
	 * Commits a book to the database as well as any authors, reviews, and versions for the
	 *  book. If the book already exists in the database, the entry will be updated. If there
	 * is no entry, one will be created.
	 * 
	 * @param book the book to commit to the database
	 * @param username the username of the owner of the book (used for storing versions)
	 * @return true of the operation was successful, false if the operation failed
	 */
	@Override
	public boolean putBook(BookInterface book, String username) {
		if (book == null || book.getId() == null || book.getId().equals("")) return false;
		
		return put(new PutBook(book, username));
	}
	

	public SeriesInterface getSeries(String seriesId) {
		if (seriesId == null || seriesId.equals("")) return null;
		
		return (SeriesInterface) get(new GetSeries(seriesId));
	}
	

	public boolean putSeries(SeriesInterface series){
		if (series == null || series.getId() == null || series.getId().equals("")) return false;
		
		return put(new PutSeries(series));
	}
	
	/**
	 * Resets the database to a known starting state to make testing easier. This method will run the 
	 * database/reset.sql script.
	 */
	public void reset(){
		Connection conn = null;
		Scanner resetSQL = null;
		try{
			conn = openConnection();
			resetSQL = new Scanner(new File("database/reset.sql"));
			resetSQL.useDelimiter(";");
			Statement stmt = conn.createStatement();
			
			while(resetSQL.hasNext()){
				stmt.execute(resetSQL.next());
			}
			
		}
		catch(Exception e){
			System.err.println("Failed to reset database");
			e.printStackTrace();
		}
		finally{
			try{
				if (conn != null) conn.close();
				if (resetSQL != null) resetSQL.close();
			}
			catch(Exception e){
				System.err.println("Failed to close resorces");
			}
		}
	}
}
