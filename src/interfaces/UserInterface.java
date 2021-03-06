package interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface UserInterface {
	public String getName();
	public boolean addTag(String bookTitle, String tag);
	public boolean addBook(String bid, String title);
	public List<BookInterface> getAllBooks();
	public List<BookInterface> getAllBooksByRating();
	public boolean removeTag(String bookTitle, String tag);
	public ArrayList<TagInterface> getTags();
	public boolean addVersion(String bid, String path, String type);
	public Collection<BookInterface> getBooksWithTag(String tag);
	public BookInterface getBookByTitle(String title);
	public BookInterface getBookById(String bid);
}
