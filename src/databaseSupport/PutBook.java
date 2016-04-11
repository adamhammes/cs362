package databaseSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import interfaces.AuthorInterface;
import interfaces.BookInterface;
import interfaces.ReviewInterface;
import interfaces.VersionInterface;

class PutBook implements Putable{

	private BookInterface book;
	private String username;
	
	public PutBook(BookInterface book, String username) {
		this.book = book;
		this.username = username;
	}
	
	@Override
	public boolean put(Connection conn) throws SQLException {
			
		//Book id and title information
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO book VALUES (?, ?, ?) ON CONFLICT (book_id) DO UPDATE SET title = ?, description = ?;");
		stmt.setString(1, book.getId());
		stmt.setString(2, book.getTitle());
		stmt.setString(3, book.getDescription());
		
		stmt.setString(4, book.getTitle());
		stmt.setString(5, book.getDescription());
		System.out.println(stmt);
		stmt.executeUpdate();
		
		//Version Information
		if (username != null)
			putVersions(conn, book, username);
		
		//Author Information
		putAuthors(conn, book);
		
		//Review Information
		putReviews(conn, book);			

		
		return true;
	}

	static private void putVersions(Connection conn, BookInterface book, String username) throws SQLException{

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
	
	
	
	static private void putAuthors(Connection conn, BookInterface book) throws SQLException {
		
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
			System.out.println(stmt);
			stmt.executeUpdate();
			
			//Create/update Joining table
			joinstmt.setString(2, auth.getId());
			System.out.println(joinstmt);
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
	
	
	
	static private void putReviews(Connection conn, BookInterface book) throws SQLException {
		
		PreparedStatement insertstmt = conn.prepareStatement("INSERT INTO book_review (book_id, rating, review) VALUES (?, ?, ?);", new String[]{"review_id"});
		insertstmt.setString(1, book.getId());
		PreparedStatement updatestmt = conn.prepareStatement("UPDATE book_review SET book_id=?, rating=?, review=? WHERE review_id=?;");
		updatestmt.setString(1, book.getId());
		
		//Setup for remove
		StringBuffer rmSQL = new StringBuffer("DELETE FROM book_review WHERE (review_id) NOT IN "
				+ "(SELECT review_id FROM book_review WHERE book_id != ? OR (");
		
		for (int i = 0; i < book.getReviews().size() - 1; i++)
			rmSQL.append("(review_id = ?) OR");
		rmSQL.append("(review_id = ?)));");
		PreparedStatement rmstmt = conn.prepareStatement(rmSQL.toString());
		rmstmt.setString(1, book.getId());
		
		int index = 2;
		for (ReviewInterface rev : book.getReviews()){
			if (rev.getId() == -1){
				insertstmt.setInt(2, rev.getRating());
				insertstmt.setString(3, rev.getReview());
				insertstmt.executeUpdate();
				
				//Retrieve generated key from query so that the -1 doesn't conflict with the remove statement
				insertstmt.getGeneratedKeys().next();
				rev.setId(insertstmt.getGeneratedKeys().getInt("review_id"));
			}
			else{
				updatestmt.setInt(2, rev.getRating());
				updatestmt.setString(3, rev.getReview());
				updatestmt.setInt(4, rev.getId());
				System.out.println(updatestmt);
				updatestmt.executeUpdate();
			}
			rmstmt.setInt(index++, rev.getId());
		}
		System.out.println(rmstmt.toString());
		if (book.getReviews().size() > 0) {
			System.out.println(rmstmt);
			rmstmt.executeUpdate();
		}
		else{
			rmstmt = conn.prepareStatement("DELETE FROM book_review WHERE book_id = ?;");
			rmstmt.setString(1, book.getId());
			rmstmt.executeUpdate();
		}			
	}

}
