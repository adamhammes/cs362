package databaseSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import interfaces.AuthorInterface;
import interfaces.BookInterface;

class PutAuthor extends Putable {

	private AuthorInterface author;
	
	public PutAuthor(AuthorInterface author){
		this.author = author;
	}
	
	@Override
	public boolean put(Connection conn) throws SQLException {
		putAuthor(conn);
		putBooks(conn);
		return false;
	}

	
	private void putAuthor(Connection conn) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO author VALUES (?, ?) "
				+ "ON CONFLICT (author_id) DO UPDATE SET author_name = ?;");
		stmt.setString(1, author.getId());
		stmt.setString(2, author.getName());
		stmt.setString(3, author.getName());
		stmt.executeUpdate();
		alreadyStoredAuthors.add(author.getId());
	}
	
	
	private void putBooks(Connection conn) throws SQLException {
		for (BookInterface book : author.getBooks()){
			if (book != null && !alreadyStoredBooks.contains(book)){
				new PutBook(book, null).put(conn);
			}
		}
	}
}
