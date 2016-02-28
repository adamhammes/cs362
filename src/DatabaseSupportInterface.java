import java.util.List;

public interface DatabaseSupportInterface {

	public User getUser(String uid);
	public boolean putUser(User u);
	public boolean addVersion(String uid, String bid, String path, String type);
	public List<String> getBooksWithTag(String uid, String tid);
	public List<Book> getAllBooks(String uid);
	public List<Book> getBooksByRating(String uid);
	public Book getBook(String bid);
	public boolean putBook(Book book);
	public boolean removeTag(String uid, String bookTitle, String tag);
	
}
