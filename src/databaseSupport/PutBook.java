package databaseSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import interfaces.AuthorInterface;
import interfaces.BookInterface;
import interfaces.ReviewInterface;
import interfaces.VersionInterface;

/**
 * Encapsulates the putBook operation, so that it may be more easily used by the 
 * DatabaseSupport class.
 * 
 */
class PutBook extends Putable{

	private BookInterface book;
	private String username;
	
	/**
	 * Creates new instance of the PutBook request. 
	 * 
	 * @param book book to be committed
	 * @param username username of the user making the commit
	 */
	public PutBook(BookInterface book, String username) {
		this.book = book;
		this.username = username;
	}
	
	
	/**
	 * Execute the putUser operation on the provided database connection. This operation
	 * commits the book and anything the book owns to the database.
	 * 
	 * @param conn Database Connection
	 * @return true on success false on failure
	 */
	@Override
	public boolean put(Connection conn) throws SQLException {
				
		putBook(conn);
		
		//Version Information
		if (username != null) putVersions(conn);
		
		//Author Information
		putAuthors(conn);
		
		//Review Information
		putReviews(conn);			

		putSeries(conn);
		return true;
	}

	
	/**
	 * Commits ONLY the book to the database. Does not commit anything the book owns
	 * such as authors, reviews, tags, etc.
	 * 
	 * @param conn Database connection
	 * @throws SQLException
	 */
	private void putBook(Connection conn) throws SQLException {
		//Book id and title information
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO book VALUES (?, ?, ?) ON CONFLICT (book_id) DO UPDATE SET title = ?, description = ?;");
		stmt.setString(1, book.getId());
		stmt.setString(2, book.getTitle());
		stmt.setString(3, book.retrieveDescription());
		
		stmt.setString(4, book.getTitle());
		stmt.setString(5, book.retrieveDescription());
		stmt.executeUpdate();
		alreadyStoredBooks.add(book.getId());
	}
	
	
	/**
	 * Commit the book versions attached to this book to the database, and link then to
	 * the provided user. Any versions in the database assosiated with this user that
	 * are in the database but not attached to the book object will be removed from
	 * the database.
	 * 
	 * @param conn Database connection
	 * @throws SQLException
	 */
	private void putVersions(Connection conn) throws SQLException{

		PreparedStatement stmt = conn.prepareStatement("INSERT INTO book_version (book_id, account_name, format, location)"
										+ "VALUES (?, ?, ?, ?) ON CONFLICT (book_id, account_name, format) DO NOTHING");
		
		for (VersionInterface version: book.getVersions()) {
			stmt.setString(1, book.getId());
			stmt.setString(2, username);
			stmt.setString(3, version.getType());
			stmt.setString(4, version.getPath());
			stmt.executeUpdate();
		}
	}
	
	
	/**
	 * Commit the attached authors attached to the book to the database. Any authors
	 * in the database but not attached to the provided book object will be removed
	 * from the database.
	 * 
	 * @param conn Database connection
	 * @throws SQLException
	 */
	private void putAuthors(Connection conn) throws SQLException {
		
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO author VALUES (?, ?) ON CONFLICT (author_id) DO UPDATE SET author_name = ?;");
		PreparedStatement joinstmt = conn.prepareStatement("INSERT INTO book_author VALUES (?, ?) ON CONFLICT (book_id, author_id) DO NOTHING;");
		joinstmt.setString(1, book.getId());
		
		StringBuffer rmSQL = new StringBuffer("DELETE FROM book_author WHERE (book_id, author_id) NOT IN "
				+ "(SELECT * FROM book_author WHERE book_id != ? OR (");
		for (int i = 0; i < book.getAuthors().size() - 1; i++)
			rmSQL.append("(author_id = ?) OR");
		rmSQL.append("(author_id = ?)));");
		PreparedStatement rmstmt = conn.prepareStatement(rmSQL.toString());
		rmstmt.setString(1, book.getId());
		
		int index = 2;
		for (AuthorInterface auth : book.getAuthors()){
			//Create/update Author Entry
			stmt.setString(1, auth.getId());
			stmt.setString(2, auth.getName());
			stmt.setString(3, auth.getName());
			stmt.executeUpdate();
			
			//Create/update Joining table
			joinstmt.setString(2, auth.getId());
			joinstmt.executeUpdate();
			
			//Add to remove statement
			rmstmt.setString(index++, auth.getId());
		}
		if (book.getAuthors().size() > 0){
			rmstmt.executeUpdate();
		}
		else{
			rmstmt = conn.prepareStatement("DELETE FROM book_author WHERE book_id =?");
			rmstmt.setString(1, book.getId());
			rmstmt.executeUpdate();
		}
	}
	
	
	/**
	 * Commits all reviews belonging to the provided book to the database. If the
	 * database has reviews for this book that the book object doesn't, they will
	 * be removed from the database.
	 * 
	 * @param conn Database Connection
	 * @throws SQLException
	 */
	private void putReviews(Connection conn) throws SQLException {
		
		PreparedStatement insertstmt = conn.prepareStatement("INSERT INTO book_review (book_id, review_id, rating, review) VALUES (?, ?, ?, ?) "
															+ " ON CONFLICT (review_id) DO NOTHING;");
		insertstmt.setString(1, book.getId());
		
		//Setup for remove
		StringBuffer rmSQL = new StringBuffer("DELETE FROM book_review WHERE (review_id) NOT IN "
				+ "(SELECT review_id FROM book_review WHERE book_id != ?");
		
		for (int i = 0; i < book.getReviews().size(); i++)
			rmSQL.append("OR review_id = ?");
		rmSQL.append(");");

		PreparedStatement rmstmt = conn.prepareStatement(rmSQL.toString());
		rmstmt.setString(1, book.getId());

		int index = 2;
		for (ReviewInterface rev : book.getReviews()){
			insertstmt.setString(1, book.getId());
			insertstmt.setInt(2, rev.getId());
			insertstmt.setInt(3, rev.getRating());
			insertstmt.setString(4, rev.getReview());

			insertstmt.executeUpdate();
			
			rmstmt.setInt(index++, rev.getId());
		}
		if (book.getReviews().size() > 0) {
			rmstmt.executeUpdate();
		}
		else{
			rmstmt = conn.prepareStatement("DELETE FROM book_review WHERE book_id = ?;");
			rmstmt.setString(1, book.getId());
			rmstmt.executeUpdate();
		}			
	}

	
	private void putSeries(Connection conn) throws SQLException {
		if (book.getSeries() != null && !alreadyStoredSeries.contains(book.getSeries().getId())){
			PutSeries putSeriesRequest = new PutSeries(book.getSeries(), username);
			putSeriesRequest.put(conn);
		}
		else if (book.getSeries() == null) {
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM book_series WHERE book_id = ?;");
			stmt.setString(1, book.getId());
			stmt.executeUpdate();
		}
	}
	
}
