package interfaces;

import java.util.List;

public interface DatabaseSupportInterface {

	public UserInterface getUser(String uid);
	public boolean putUser(UserInterface u);
	public boolean addVersion(String uid, String bid, String path, String type);
	public List<String> getBooksWithTag(String uid, String tid);
	public List<BookInterface> getAllBooks(String uid);
	public List<BookInterface> getBooksByRating(String uid);
	public BookInterface getBook(String bid);
	public boolean putBook(BookInterface book);
	public boolean removeTag(String uid, String bookTitle, String tag);
	
}
