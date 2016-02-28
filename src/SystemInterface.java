import java.util.List;

public interface SystemInterface {

	public boolean addTag(String uid, String bookTitle, String tag);
	public boolean addBook(String uid, String bid, String title);
	public boolean addUser(String uid, String name);
	public boolean addVersion(String uid, String bid, String path, String type);
	public List<String> getBooksWithTag(String uid, String tid);
	public List<Book> displayAllBooks(String uid);
	public List<Book> displayAllBooksByRating(String uid);
	public boolean addRating(String uid, String bid, int rating);
	public boolean removeTag(String uid, String bookTitle, String tag);
	
	
}
