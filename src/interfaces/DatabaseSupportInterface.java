package interfaces;

import java.util.List;

public interface DatabaseSupportInterface {

	public UserInterface getUser(String uid);
	public boolean putUser(UserInterface u);
	
	public BookInterface getBook(String bid);
	public BookInterface getBook(String bid, String username);
	public boolean putBook(BookInterface book);
	public boolean putBook(BookInterface book, String username);
	
//	public boolean addVersion(String uid, String bid, String path, String type);
	public List<BookInterface> getBooksWithTag(String uid, String tid);
	public List<BookInterface> getAllBooks(String uid);
	public List<BookInterface> getBooksByRating(String uid);
	
	public boolean removeTag(String uid, String bookTitle, String tag);
	
	
	
}
