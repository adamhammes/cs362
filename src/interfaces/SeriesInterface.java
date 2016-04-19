package interfaces;

import java.util.List;

public interface SeriesInterface {
	boolean setName(String name);
	String getName();
	String getId();

	boolean addBook(BookInterface book);
	List<BookInterface> getBooks();
	boolean removeBook(String bid);
	BookInterface getBook(String bid);
}
