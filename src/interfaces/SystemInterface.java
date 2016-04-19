package interfaces;

import java.util.List;

public interface SystemInterface {
	public boolean addTag(String uid, String bookTitle, String tag);
	public boolean addBook(String uid, String bid, String title);
	public boolean addUser(String uid, String name);
	public boolean addVersion(String uid, String bid, String path, String type);
	
	public List<String> getBooksWithTag(String uid, String tid);
	public List<BookInterface> displayAllBooks(String uid);
	public List<BookInterface> displayAllBooksByRating(String uid);
	
	public boolean addRating(String uid, String bid, int rating);
	public boolean removeTag(String uid, String bookTitle, String tag);
	
	public boolean addDescription(String bid, String desc);
	public boolean editDescription(String bid, String desc);
	public boolean removeDescription(String bid);
	public String retrieveDescription(String bid);
}
