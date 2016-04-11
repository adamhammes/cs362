package databaseSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import interfaces.BookInterface;
import interfaces.ReviewInterface;
import models.Author;
import models.Book;
import models.Review;

/**
 * 
 * @author Nicholas
 *
 */
class GetBook implements Gettable{

	private String bid;
	private String username;
	
	public GetBook(String bid, String username){
		this.bid = bid;
		this.username = username;
	}
	
	/**
	 * Retrives the requested book from the database as well as all of the authors, 
	 * and reviews for the book. If a username is specified, the versions of the book
	 * owned by the user will also be retreived. 
	 * 
	 * If the book does not exist in the database, this method returns null.
	 * 
	 * @param conn connection to the database
	 * @param bid book id of the bookt to retrive
	 * @param username username of whom the book belongs too
	 * @return requested book or null
	 * @throws SQLException
	 */
	public BookInterface get(Connection conn) throws SQLException{

		BookInterface book = null;
		
		book = retrieveBook(conn, bid);
		if (book == null)
			return null;
		
		retrieveRateings(conn, book, bid);
		retrieveAuthors(conn, book, bid);		
		retrieveVersions(conn, book, bid, username);
			
		return book;
	}
	
	
	/**
	 * Retrieves only the book object from the database. Nothing else is populated.
	 * Returns null if the book does not exist
	 * 
	 * @param conn connection to the database
	 * @param bid book id of the book to retreve
	 * @return requested book
	 * @throws SQLException
	 */
	private static Book retrieveBook(Connection conn, String bid) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Book WHERE book_id = ?;");
		stmt.setString(1, bid);
		ResultSet results = stmt.executeQuery();
		
		if (results.next())
			return new Book(results.getString("book_id"), results.getString("title"), results.getString("description"));
		else
			return null;
	}
	

	/**
	 * Retreives ratings from the database for a specific book and attaches them
	 * to the book object. 
	 * 
	 * @param conn connection to the database
	 * @param book book on which to addRateings
	 * @param bid book id of the book
	 * @throws SQLException
	 */
	private static void retrieveRateings(Connection conn, BookInterface book, String bid) throws SQLException {
		PreparedStatement stmt = makeRatingStatement(bid, conn);
		System.out.println(stmt);
		ResultSet results = stmt.executeQuery();
		
		
		while(results.next()){
			ReviewInterface toAdd = new Review(results.getInt(2), results.getInt(3), results.getString(4));
			book.addReview(toAdd);
		}
	}
	
	
	/**
	 * Generates the required SQL prepared statement for retreving Ratings for the provided
	 * book.
	 * 
	 * @param bid
	 * @param conn connection to the database
	 * @return
	 * @throws SQLException
	 */
	private static PreparedStatement makeRatingStatement(String bid, Connection conn) throws SQLException {
		PreparedStatement stmt;
		stmt = conn.prepareStatement(
				"SELECT *"
		      + "FROM book_review WHERE book_id = ?;");
		stmt.setString(1, bid);
		return stmt;
	}
	
	
	/**
	 * 
	 * @param conn connection to the database
	 * @param book
	 * @param bid
	 * @throws SQLException
	 */
	private static void retrieveAuthors(Connection conn, BookInterface book, String bid) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT author.author_id, author_name FROM author JOIN book_author "
				+ "ON author.author_id = book_author.author_id WHERE book_id = ?;");
		stmt.setString(1, book.getId());
		ResultSet results = stmt.executeQuery();
		
		while(results.next()){
			Author author = new Author(results.getString("author_id"), results.getString("author_name"));
			book.addAuthor(author);
		}
	}
	
	
	/**
	 * Retreves all versions of the specified book owned by a user and attaches 
	 * them to the provided book instance. If the username specified is null,
	 * no items will be retreved.
	 * 
	 * @param conn connection to the database
	 * @param book
	 * @param bid
	 * @param username
	 * @throws SQLException
	 */
	private static void retrieveVersions (Connection conn, BookInterface book, String bid, String username) throws SQLException {
		if (username != null){
			PreparedStatement stmt = conn.prepareStatement("SELECT format, location FROM book_version WHERE book_id = ? AND account_name = ?;");
			stmt.setString(1, book.getId());
			stmt.setString(2, username);
			ResultSet results = stmt.executeQuery();
			
			while(results.next()){
	//				Version ver = new Version(results.getString("format"), results.getString("location"));
				book.addVersion(results.getString("location"), results.getString("format"));
			}
		}
	}
}
