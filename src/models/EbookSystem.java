package models;

import interfaces.BookInterface;
import interfaces.ReviewInterface;
import interfaces.UserInterface;

import java.sql.SQLException;
import java.util.Collection;
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
	
	
	
	public Collection<BookInterface> getBookWithTag(String uid, String tag){
		DatabaseSupport db = new DatabaseSupport();
		UserInterface user = db.getUser(uid);
		return user.getBooksWithTag(tag);
	}
	
	
	public List<BookInterface> getAllBooks(String uid){
		DatabaseSupport db = new DatabaseSupport();
		UserInterface user = db.getUser(uid);
		
		if(user != null)
			return user.getAllBooks();
		else
			return null;
		
	}



	public boolean removeReview(String bookId, int reviewId) {
		DatabaseSupport db = new DatabaseSupport();
		BookInterface book = db.getBook(bookId);
		
		if (book == null) return false;
		if (book.removeReview(reviewId) == false) return false;
		
		return db.putBook(book);
	}



	public boolean updateReview(String bookId, int reviewId, int newRateing, String newReview) {
		DatabaseSupport db = new DatabaseSupport();
		BookInterface book = db.getBook(bookId);
		
		if (book == null) return false;
		if (book.updateReview(reviewId, newRateing, newReview) == false) return false;
		
		return db.putBook(book);
	}



	public Collection<ReviewInterface> getReviews(String bookId) {
		DatabaseSupport db = new DatabaseSupport();
		BookInterface book = db.getBook(bookId);
		return book.getReviews();
	}


	
	public boolean addVersion(String uid, String bid, String path, String type) {
		DatabaseSupport db = new DatabaseSupport();
		
		UserInterface user = db.getUser(uid);
		if (null == user){
			return false;
		}
		
		if (!user.addVersion(bid, path, type))
			return false;///!!!!!! This doesn't commit changes to the db
		
		return db.putUser(user);
	}	
	
	
}
