package models;

import interfaces.BookInterface;
import interfaces.UserInterface;

import java.sql.SQLException;
import java.util.List;

import databaseSupport.DatabaseSupport;


public class EbookSystem {
	
	
	public boolean addTag(String uid, String bookTitle, String tag) throws SQLException{
		DatabaseSupport db = new DatabaseSupport();
		UserInterface user = db.getUser(uid);
		
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
	
	
	
	public boolean removeTag(String uid, String bookTitle, String tag){
		DatabaseSupport db = new DatabaseSupport();
		UserInterface user = db.getUser(uid);
		
		if (user != null){
			if (user.removeTag(bookTitle, tag)) //if tag was successfully removed
				return db.putUser(user); //if user was successfully updated
		}
		return false;
	}
	
	
	
	public List<BookInterface> findBookWithTag(String uid, String tag){
		return null;
	}
	
	
	public List<BookInterface> getAllBooks(String uid){
		DatabaseSupport db = new DatabaseSupport();
		UserInterface user = db.getUser(uid);
		
		if(user != null)
			return user.getAllBooks();
		else
			return null;
		
	}
}
