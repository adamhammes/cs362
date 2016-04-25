package databaseSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import interfaces.BookInterface;
import interfaces.ReviewInterface;
import interfaces.SeriesInterface;
import models.Author;
import models.Book;
import models.Review;

/**
 * This object encapsulates a get user request making it easy for the
 * db to execute.
 */
class GetBook extends Getable{

	private String bid;
	private String username;
	
	private BookInterface book = null;
	
	/**
	 * Creates a new instance of a GetBook request. The request can be
	 * executed by calling the get method.
	 * 
	 * @param bid book id of the book to retrieve
	 * @param username username of the owner of this book, or null if 
	 * 				not available
	 */
	public GetBook(String bid, String username){
		this.bid = bid;
		this.username = username;
	}
	
	
	/**
	 * Retrieves the requested book from the database as well as all of the authors, 
	 * and reviews for the book. If a username is specified, the versions of the book
	 * owned by the user will also be retrieved. 
	 * 
	 * If the book does not exist in the database, this method returns null.
	 * 
	 * @param conn connection to the database
	 * @return requested book or null
	 * @throws SQLException
	 */
	@Override
	public BookInterface get(Connection conn) throws SQLException{
	
		retrieveBook(conn);
		if (book == null)
			return null;
		
		retrieveRateings(conn);
		retrieveAuthors(conn);		
		retrieveVersions(conn);
		
		retrieveSeries(conn);
		return book;
	}
	
	
	/**
	 * Retrieves only the book object from the database. Nothing else is populated.
	 * Returns null if the book does not exist
	 * 
	 * @param conn connection to the database
	 * @return requested book
	 * @throws SQLException
	 */
	private void retrieveBook(Connection conn) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Book WHERE book_id = ?;");
		stmt.setString(1, bid);
		ResultSet results = stmt.executeQuery();
		
		if (results.next()) {
			book = new Book(results.getString("book_id"), results.getString("title"), results.getString("description"));
			alreadyPopulatedBooks.put(bid, book); //Add book to list of books already retrieved
		}
	}
	

	/**
	 * Retrieves ratings from the database for a specific book and attaches them
	 * to the book object. 
	 * 
	 * @param conn connection to the database
	 * @throws SQLException
	 */
	private void retrieveRateings(Connection conn) throws SQLException {
		PreparedStatement stmt = makeRatingStatement(conn);
		ResultSet results = stmt.executeQuery();
		
		
		while(results.next()){
			ReviewInterface toAdd = new Review(results.getInt(2), results.getInt(3), results.getString(4));
			book.addReview(toAdd);
		}
	}
	
	
	/**
	 * Generates the required SQL prepared statement for retrieving Ratings for the provided
	 * book.
	 * 
	 * @param conn connection to the database
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement makeRatingStatement(Connection conn) throws SQLException {
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
	 * @throws SQLException
	 */
	private void retrieveAuthors(Connection conn) throws SQLException {		
		PreparedStatement stmt = conn.prepareStatement("SELECT author_id FROM book_author WHERE book_id = ?");
		stmt.setString(1, book.getId());
		ResultSet results = stmt.executeQuery();
		
		while (results.next()) {
			String authorId = results.getString("author_id");
			
			if (!alreadyPopulatedAuthors.contains(authorId)){
				book.addAuthor(new GetAuthor(authorId, username).get(conn));
			}
		}
	}
	
	
	/**
	 * Retrieves all versions of the specified book owned by a user and attaches 
	 * them to the provided book instance. If the username specified is null,
	 * no items will be retrieved.
	 * 
	 * @param conn connection to the database
	 * @throws SQLException
	 */
	private void retrieveVersions (Connection conn) throws SQLException {
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
	
	private void retrieveSeries(Connection conn) throws SQLException{
		PreparedStatement stmt = conn.prepareStatement("SELECT series_id FROM book_series WHERE book_id = ?;");
		stmt.setString(1, bid);
		ResultSet results = stmt.executeQuery();
		
		if (results.next()){
			String seriesId = results.getString("series_id");
			
			//If the series has not already been created, create it and attach this book to it
			if(!alreadyPopulatedSeries.contains(seriesId)) {
				GetSeries getSeriesRequest = new GetSeries(seriesId, username);
				SeriesInterface series = (SeriesInterface) getSeriesRequest.get(conn);
				series.addBook(book);
			}
		}
	}
}
