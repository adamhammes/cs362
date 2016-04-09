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

class GetBook {

	public static BookInterface getBook(Connection conn, String bid, String username) throws SQLException{

		BookInterface book = null;
		
		book = retrieveBook(conn, bid);
		if (book == null)
			return null;
		
		retrieveRateings(conn, book, bid);
		retrieveAuthors(conn, book, bid);		
		retrieveVersions(conn, book, bid, username);
			
		return book;
	}
	
	
	private static Book retrieveBook(Connection conn, String bid) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Book WHERE book_id = ?;");
		stmt.setString(1, bid);
		ResultSet results = stmt.executeQuery();
		
		if (results.next())
			return new Book(results.getString("book_id"), results.getString("title"), results.getString("description"));
		else
			return null;
	}
	

	private static void retrieveRateings(Connection conn, BookInterface book, String bid) throws SQLException {
		PreparedStatement stmt = makeRatingStatement(bid, conn);
		System.out.println(stmt);
		ResultSet results = stmt.executeQuery();
		
		
		while(results.next()){
			ReviewInterface toAdd = new Review(results.getInt(2), results.getInt(3), results.getString(4));
			book.addReview(toAdd);
		}
	}
	
	
	private static PreparedStatement makeRatingStatement(String bid, Connection conn) throws SQLException {
		PreparedStatement stmt;
		stmt = conn.prepareStatement(
				"SELECT *"
		      + "FROM book_review WHERE book_id = ?;");
		stmt.setString(1, bid);
		return stmt;
	}
	
	
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
