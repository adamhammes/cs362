import java.util.List;

public interface UserInterface {
	
	public boolean addTag(String bookTitle, String tag);
	public boolean addBook(String bid, String title);
	public List<Book> getAllBooks();
	public boolean removeTag(String bookTitle, String tag);
	
}
