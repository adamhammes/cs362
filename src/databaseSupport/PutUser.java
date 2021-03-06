package databaseSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import interfaces.BookInterface;
import interfaces.TagInterface;
import interfaces.UserInterface;

/**
 * Encapsulates a put user request allowing the database class to handle
 * put requests more easily.
 */
class PutUser extends Putable{

	private UserInterface user;
	
	/**
	 * Creates a new instance of a PutUser Request for the provided user.
	 * 
	 * @param user user to commit to the database
	 */
	public PutUser(UserInterface user){
		this.user = user;
	}
	
	
	/**
	 * Commits the provided user and everything owned by the user object to the
	 * database on the provided connection.
	 * 
	 * @param conn database connection
	 * @return true on success, false on failure
	 * @throws SQLException
	 */
	@Override
	public boolean put(Connection conn) throws SQLException {
				
		//put user
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO account (account_name) VALUES (?) "
														+ "ON CONFLICT (account_name) DO NOTHING;");
		stmt.setString(1, user.getName());
		stmt.executeUpdate();
		
		//put all of the users books
		for (BookInterface book: user.getAllBooks()){
//			PutBook.putBook(conn, book, null);
			new PutBook(book, user.getName()).put(conn);
			
			
			stmt = conn.prepareStatement("INSERT INTO account_book (account_name, book_id) VALUES (?, ?) "
											+ "ON CONFLICT (account_name, book_id) DO NOTHING;");
			stmt.setString(1, user.getName());
			stmt.setString(2, book.getId());
			stmt.executeUpdate();
	
		}
		
		putTags(conn);
			
		return true;
	}
	

	/**
	 * Commits the tags belonging to this user to the database using the provided connection. Any 
	 * tag that is in the database, but does not belong to the user object will be removed from 
	 * the database.
	 * 
	 * @param conn database connection
	 * @return true on success, false on failure
	 * @throws SQLException
	 */
	private boolean putTags(Connection conn) throws SQLException{
		//Build prepared statement for removing unneeded tags
		int size = 0;
		for (TagInterface tag : user.getTags()) size+= tag.getBooks().size();
		
		StringBuffer rmsql = new StringBuffer(
				"DELETE FROM book_tag WHERE (account_name, book_id, tag) NOT IN "
			  + "(SELECT account_name, book_id, tag FROM book_tag WHERE account_name != ? OR (");
		
		for (int i = 0; i < size - 1; i++){
			rmsql.append("(book_id = ? AND tag = ?) OR ");
		}
		rmsql.append("(book_id = ? AND tag = ?)));");
		PreparedStatement rmstmt = conn.prepareStatement(rmsql.toString());
		rmstmt.setString(1, user.getName());
		int index = 2;
		
		PreparedStatement mkstmt = conn.prepareStatement("INSERT INTO book_tag (account_name, book_id, tag) VALUES (?, ?, ?) "
				+ "ON CONFLICT (account_name, book_id, tag) DO NOTHING;");
		
		//put all tags for this user
		for (TagInterface tag : user.getTags()){
			for (BookInterface book : tag.getBooks()){
				
				//create and execute sql command for upsert tag
				mkstmt.setString(1, user.getName());
				mkstmt.setString(2, book.getId());
				mkstmt.setString(3, tag.getName());
				mkstmt.executeUpdate();
				
				//insert values into remove statement
				rmstmt.setString(index++, book.getId());
				rmstmt.setString(index++, tag.getName());	
			}
		}
		//The constructed rmstmt is good only if least one tag exists
		if (size > 0){
			rmstmt.executeUpdate();
		}
		else{
			rmstmt = conn.prepareStatement("DELETE FROM book_tag WHERE account_name = ?;");
			rmstmt.setString(1, user.getName());
			rmstmt.executeUpdate();
		}
		return true;
	}
	
}
