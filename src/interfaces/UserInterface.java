package interfaces;

import java.util.ArrayList;
import java.util.List;

public interface UserInterface {
	public String getName();
	public boolean addTag(String bookTitle, String tag);
	public boolean addBook(String bid, String title);
	public List<BookInterface> getAllBooks();
	public boolean removeTag(String bookTitle, String tag);
	public ArrayList<TagInterface> getTags();
}
