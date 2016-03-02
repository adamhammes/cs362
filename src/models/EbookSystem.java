package models;

import java.sql.SQLException;

import databaseSupport.DatabaseSupport;


public class EbookSystem {
	public boolean addTag(String uid, String bookTitle, String tag) throws SQLException{
		DatabaseSupport db = new DatabaseSupport();
		User user = db.getUser(uid);
		
		if (user != null){
			boolean result = user.addTag(bookTitle, tag);
			if (result == false) {
				return false;
			}
			
			System.out.println("results prior to database :" + result);
			return db.putUser(user);
		}
		return false;	
	}
	
	public List<Book> getAllBooks(String uid){
		DatabaseSupport db = new DatabaseSupport();
		User user = db.getUser(uid);
		
		if(user != null)
			return user.getAllBooks();
		else
			return null;
		
	}
}
