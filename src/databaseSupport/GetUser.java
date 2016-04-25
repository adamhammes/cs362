package databaseSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import interfaces.BookInterface;
import interfaces.TagInterface;
import interfaces.UserInterface;
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
//<<<<<<< HEAD
//				Book toAdd = new Book(results.getString("book_id"), results.getString("title"));
//
//
//				PreparedStatement versionQuery = conn.prepareStatement("SELECT * FROM Book_Version WHERE book_id = ? AND account_name = ?;");
//				versionQuery.setString(1, toAdd.getId());
//				versionQuery.setString(2, user.getName());
//
//				ResultSet versionResults = versionQuery.executeQuery();
//				while (versionResults.next()) {
//					toAdd.addVersion(versionResults.getString("location"), versionResults.getString("format"));
//				}
//				
//				user.userBooks.put(toAdd.getTitle(), toAdd);
//=======
				//Book toAdd = new Book(results.getString("book_id"), results.getString("title"));
				//user.userBooks.put(toAdd.getId(), toAdd);
				String bid = results.getString("book_id");
				
//				BookInterface BookInterface book;
				if (!alreadyPopulatedBooks.containsKey(bid)) {
					BookInterface book = new GetBook(bid, uid).get(conn);
					user.userBooks.put(bid, book);
				}
				else {
					user.userBooks.put(bid, alreadyPopulatedBooks.get(bid));
				}
//>>>>>>> refs/remotes/origin/nicks_iteration_3
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
				System.out.println("new tag created: " + tag.getName());
			}
			BookInterface book = user.userBooks.get(results.getString("book_id"));
			tag.addBook(book);
			book.addTag(tag);
			System.out.println("book: " + book.getTitle() + " Tag: " + tag.getName());
		}
	}	
}
