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
		removeOldBooks(conn);
		return true;
	}

	
	private void putAuthor(Connection conn) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO author VALUES (?, ?, ?) "
				+ "ON CONFLICT (author_id) DO UPDATE SET author_name = ?, description = ?;");
		stmt.setString(1, author.getId());
		stmt.setString(2, author.getName());
		stmt.setString(3, author.retreveDescription());
		
		stmt.setString(4, author.getName());
		stmt.setString(5, author.retreveDescription());
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
	
	
	private void removeOldBooks(Connection conn) throws SQLException {
		StringBuffer rmSQL = new StringBuffer("DELETE FROM book_author WHERE (book_id, author_id) NOT IN ");
		rmSQL.append("(SELECT * FROM book_author WHERE author_id != ?");
		for (int i = 0; i < author.getBooks().size(); i++)
			rmSQL.append( "OR book_id = ? ");
		rmSQL.append(");");
		
		PreparedStatement rmstmt = conn.prepareStatement(rmSQL.toString());
		rmstmt.setString(1, author.getId());
		
		int i = 2;
		for (BookInterface book : author.getBooks()){
			rmstmt.setString(i++, book.getId());
		}
		
		rmstmt.executeUpdate();
	}
}
