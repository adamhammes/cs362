package interfaces;

import java.util.ArrayList;

public interface SeriesInterface {

	boolean setName(String name);

	String getName();

	String getId();

	boolean addBook(BookInterface book);

	ArrayList<BookInterface> getBooks();

	boolean removeBook(String bid);

}
