package interfaces;

import java.util.Collection;
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
	public boolean addRating(String bid, int rating, String review);
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
	
	// Nick Iteration 3
	public Collection<VersionInterface> listAllVersions(String bid, String userId);
	public VersionInterface getVersion(String bookid, String format, String userId);
	public Collection<BookInterface> findBooksByAuthor(String authorId, String userId);

	// Adam Iteration 3
	public boolean deleteUser(String uid);
	public boolean deleteBook(String bid);
	public boolean deleteVersion(String uid, String bid, String format);
	
	//Steven Iteration 3
	public boolean addAuthor(String aid, String name);
	public boolean addAuthorDescription(String aid, String desc);
	public boolean editAuthorDescription(String aid, String desc);
	public boolean removeAuthorDescription(String aid);
	public String retrieveAuthorDescription(String aid);
	
	// for demo
	public boolean addAuthor(String aid, String name);
	public boolean addAuthorToBook(String aid, String bid);
	public boolean createSeries(String sid, String name);
	
}
