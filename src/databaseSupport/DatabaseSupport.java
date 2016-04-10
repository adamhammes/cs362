package databaseSupport;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
		
		UserInterface user = null;
		Connection conn = null;
		
		try {
			conn = openConnection();
			
			user = GetUser.getUser(conn, uid);
		} catch (SQLException | ClassNotFoundException e) {
			return null;
		}
		finally{
			try {
				conn.close();
			} catch (Exception e2) {
				// do nothing
			}
		}
		
		return user;
	}
	
	
	/**
	 * Commits this user and all of the books and tags that they own to the database.
	 * If no user by this id exists, a new entry into the database will be created for
	 * them. If the user exists, all changed feilds will be updated.
	 * 
	 * @param user The user to commit to the database
	 * @return true on successfuly added to database, false on failure to add to database
	 */
	@Override
	public boolean putUser(UserInterface user) {
		if (user == null) return false;
		
		Connection conn = null;
		
		try {
			conn = openConnection();
			PutUser.putUser(conn, user);
		}
		catch(SQLException | ClassNotFoundException e) {
			return false;
		}
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				//Nothing here?
			}
		}
		return true;
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
	 * Requests a book from the database and populates any rateings, authors and versions that
	 * belong to the book. If no book by this identifier is found, this method returns null.
	 * 
	 * @param bid the book identifier of the requested book
	 * @param username the username of the owner of the book (used for retreving versions)
	 * @return the requested book or null
	 */
	@Override
	public BookInterface getBook(String bid, String username) {
		if (bid == null || bid.equals("")) return null;
		
		Connection conn = null;
		BookInterface book = null;
		try {
			conn = openConnection();
			
			book = GetBook.getBook(conn, bid,  username);
		} catch (SQLException | ClassNotFoundException e) {
			return null;
		} finally {
			try {
				conn.close();
			} catch (Exception e2) {
				// do nothing
			}
		}
		return book;
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
		if (book == null) return false;
		
		Connection conn = null;
		
		try {
			conn = openConnection();
			PutBook.putBook(conn, book, username);
		}
		catch(SQLException | ClassNotFoundException e) {
			return false;
		}
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				//Nothing here?
			}
		}
		return true;
	}
	
	

	@Override
	public boolean addVersion(String uid, String bid, String path, String type) {
		UserInterface user = getUser(uid);
		if (null == user){
			return false;
		}
		
		return user.addVersion(bid, path, type); ///!!!!!! This doesn't commit changes to the db
	}

	
	@Override
	public List<BookInterface> getBooksWithTag(String uid, String tid) {
		UserInterface user = getUser(uid);
		if (null == user) {
			return null;
		}
		
		return new ArrayList<>(user.getBooksWithTag(tid));
	}

	
	@Override
	public List<BookInterface> getAllBooks(String uid) {
		UserInterface user = getUser(uid);
		if (null == user) {
			return null;
		}
		return user.getAllBooks();
	}

	
	@Override
	public List<BookInterface> getBooksByRating(String uid) {
		UserInterface user = getUser(uid);
		if (null == user) {
			return null;
		}
		List<BookInterface> books = user.getAllBooks();
		Collections.sort(books);
		Collections.reverse(books);
		return books;
	}

	
	@Override
	public boolean removeTag(String uid, String bookTitle, String tag) {
		UserInterface user = getUser(uid);
		if (null == user){
			return false;
		}
		
		boolean toReturn = user.removeTag(bookTitle, tag);
		return toReturn && putUser(user);
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
