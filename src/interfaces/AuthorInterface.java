package interfaces;

import java.util.ArrayList;

public interface AuthorInterface {

	String getId();

	String getName();

	ArrayList<BookInterface> getBooks();

	boolean addBook(BookInterface book);

	void removeBook(String id);

	boolean addDescription(String description);
	boolean deleteDescription();
	String retreveDescription();
	boolean editDescription(String description);

}
