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

class GetUser {

	static public UserInterface getUser(Connection conn, String uid) throws SQLException {
		
		User user = null;
		
		//Get User
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account WHERE account_name = ?;");
		stmt.setString(1, uid);
		ResultSet results = stmt.executeQuery();
		
		if (results.next()) {
			user = new User(results.getString("account_name"));
		} else {
			return null;
		}
		
		//Get Books
		stmt = conn.prepareStatement("SELECT book.book_id, book.title FROM account_book join book on account_book.book_id=book.book_id where account_book.account_name=?;");
		stmt.setString(1, uid);
		results = stmt.executeQuery();
		
		while(results.next()){
			Book toAdd = new Book(results.getString("book_id"), results.getString("title"));
			user.userBooks.put(toAdd.getTitle(), toAdd);
		}
		
		//Get Tags
		stmt = conn.prepareStatement("SELECT Book_tag.book_id, tag, title FROM Book_tag inner join book on Book_tag.book_id=book.book_id WHERE account_name = ?;");
		stmt.setString(1, uid);
		results = stmt.executeQuery();
		
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
		
		return user;
	}
	
}
