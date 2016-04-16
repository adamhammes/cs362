package databaseSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import interfaces.BookInterface;
import interfaces.TagInterface;
import interfaces.UserInterface;
import models.Book;
import models.Tag;
import models.User;

class GetUser extends Getable{

	private String uid;
	
	private User user = null;
	
	
	/**
	 * Creates a new instance of a GetUser request. The request can be 
	 * Executed by calling the get method.
	 * 
	 * @param uid user id of the user to retrieve
	 */
	public GetUser(String uid){
		this.uid = uid;
	}
	
	
	/**
	 * Retrieves the requested user from the database along with all books,
	 * versions, and tags owned by the user.
	 * 
	 * If the user does not exist in the database, this method returns null.
	 * 
	 * @param conn connection to the database
	 * @return requested user or null
	 */
	@Override
	public UserInterface get(Connection conn) throws SQLException {
		
		user = retreiveUser(conn);
		retreiveBooks(conn);
		retreiveTags(conn); //requires that retreiveBooks was executed first

		return user;
	}
	
	
	/**
	 * Retrieves User object from the database. All books/tags owned by the user
	 * are un-populated.
	 * 
	 * @param conn connection to the database
	 * @return user without any owned objects
	 * @throws SQLException
	 */
	private User retreiveUser(Connection conn) throws SQLException{
		//Get User
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account WHERE account_name = ?;");
		stmt.setString(1, uid);
		ResultSet results = stmt.executeQuery();
		
		if (results.next()) {
			return new User(results.getString("account_name"));
		} else {
			return null;
		}	
	}
	
	
	/**
	 * Retrieves all books owned by the specified user id and attaches them to the provided user.
	 * 
	 * @param conn database connection
	 * @throws SQLException
	 */
	private void retreiveBooks(Connection conn) throws SQLException{
			PreparedStatement stmt = conn.prepareStatement("SELECT book.book_id, book.title FROM account_book join book on account_book.book_id=book.book_id where account_book.account_name=?;");
			stmt.setString(1, uid);
			ResultSet results = stmt.executeQuery();
			
			while(results.next()){
				Book toAdd = new Book(results.getString("book_id"), results.getString("title"));
				user.userBooks.put(toAdd.getTitle(), toAdd);
			}
	}
	
	
	/**
	 * Retrieves all tags owned by the user and links them with the books owned by the user.
	 * retreiveBooks MUST be executed first for this method to work properly.
	 * 
	 * @param conn database connection
	 * @throws SQLException
	 */
	private void retreiveTags(Connection conn) throws SQLException{
		PreparedStatement stmt = conn.prepareStatement("SELECT Book_tag.book_id, tag, title FROM Book_tag inner join book on Book_tag.book_id=book.book_id WHERE account_name = ?;");
		stmt.setString(1, uid);
		ResultSet results = stmt.executeQuery();
		
		while(results.next()){
			TagInterface tag = user.userTags.get(results.getString("tag"));
			if (tag == null){
				tag = new Tag(results.getString("tag"));
				user.userTags.put(tag.getName(), tag);
			}
			BookInterface book = user.userBooks.get(results.getString("title"));
			tag.addBook(book);
			book.addTag(tag);
		}
	}	
}
