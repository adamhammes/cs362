package databaseSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import interfaces.AuthorInterface;
import interfaces.BookInterface;
import models.Author;

class GetAuthor extends Getable{

	private String authorId;
	private String username;
	private AuthorInterface author;
	
	
	public GetAuthor(String authorId, String username){
		this.authorId = authorId;
		this.username = username;
	}
	
	@Override
	public AuthorInterface get(Connection conn) throws SQLException {
		getAuthor(conn);
		getBooks(conn);
		
		return author;
	}

	private void getAuthor(Connection conn) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM author WHERE author_id = ?;");
		stmt.setString(1, authorId);
		ResultSet results = stmt.executeQuery();
		
		if(results.next()) {
			author = new Author(results.getString("author_id"), 
					results.getString("author_name"), 
					results.getString("description"));
			
			alreadyPopulatedAuthors.add(authorId);
		}
	}
	
	
	private void getBooks(Connection conn) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT book_id FROM book_author WHERE author_id = ?;");
		stmt.setString(1, authorId);
		ResultSet results = stmt.executeQuery();
		
		while (results.next()) {
			String bookid = results.getString("book_id");
			
			if (!alreadyPopulatedAuthors.contains(bookid)){
				author.addBook((BookInterface) (new GetBook(bookid, username)).get(conn));
			}
		}
	}
	
}
