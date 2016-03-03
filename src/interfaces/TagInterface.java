package interfaces;

import java.util.Collection;

public interface TagInterface {
	public String getName();
	public boolean addBook(BookInterface b);
	public Collection<BookInterface> getBooks();
	public BookInterface getBook(String bookTitle);
	public boolean removeBook(BookInterface book);
}
