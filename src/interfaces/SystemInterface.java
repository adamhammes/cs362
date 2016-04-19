package interfaces;

import java.util.List;

public interface SystemInterface {
	// Adam Iteration 1
	public boolean addUser(String name);
	public boolean addBook(String uid, String bid, String title);
	public boolean addVersion(String uid, String bid, String path, String type);
	
	// Nick Iteration 1
	public boolean addTag(String uid, String bookTitle, String tag);
	public boolean removeTag(String uid, String bookTitle, String tag);
	public List<BookInterface> getBooksWithTag(String uid, String tid);

	// Steve Iteration 1
	public List<BookInterface> displayAllBooks(String uid);
	public boolean addRating(String uid, String bid, int rating, String review);
	public List<BookInterface> displayAllBooksByRating(String uid);
	
	// Adam Iteration 2
	public List<BookInterface> searchBySeries(String sid);
	public boolean addBookToSeries(String bid, String sid);
	public boolean removeBookFromSeries(String bid, String sid);
	
	
	// Nick Iteration 2
	public boolean changeRating(String bid, int reviewId, int newRating, String newReview);
	public boolean removeRating(String bid, int reviewId);
	public List<ReviewInterface> getReviews(String bid);
	
	// Steve Iteration 2
	public boolean addDescription(String bid, String desc);
	public boolean editDescription(String bid, String desc);
	public boolean removeDescription(String bid);
	public String retrieveDescription(String bid);
}
