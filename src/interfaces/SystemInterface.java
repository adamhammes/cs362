package interfaces;

import java.util.List;

public interface SystemInterface {
	public boolean addTag(String uid, String bookTitle, String tag);
	public boolean addBook(String uid, String bid, String title);
	public boolean addUser(String name);
	public boolean addVersion(String uid, String bid, String path, String type);
	
	public List<BookInterface> getBooksWithTag(String uid, String tid);
	public List<BookInterface> displayAllBooks(String uid);
	public List<BookInterface> displayAllBooksByRating(String uid);
	
	public boolean addRating(String uid, String bid, int rating);
	public boolean removeTag(String uid, String bookTitle, String tag);
	
	public boolean addDescription(String bid, String desc);
	public boolean editDescription(String bid, String desc);
	public boolean removeDescription(String bid);
	public String retrieveDescription(String bid);
	
	public boolean addBookToSeries(String bid, String sid);
	public List<BookInterface> searchBySeries(String sid);
	
	public boolean changeRating(String bid, int reviewId, int newRating, String newReview);
	public boolean removeRating(String bid, int reviewId);
	public List<ReviewInterface> getReviews(String bid);
}
