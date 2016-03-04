import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import databaseSupport.DatabaseSupport;
import interfaces.BookInterface;
import models.Book;
import models.Review;

public class putBookTest {
	DatabaseSupport db;
	
	@Before
	public void initialize() {
		db = new DatabaseSupport();
	}
	
	@Test
	public void test() {
		BookInterface book = new Book("hp3", "Harry Potter and the Prisoner of Azkaban");
		book.addReview(new Review(4, 0, "asdf"));
		db.putBook(book);
		
		BookInterface dbBook = db.getBook("hp3");
		
		assertEquals(book.getTitle(), dbBook.getTitle());
		assertEquals(book.getId(), dbBook.getId());
		
		assertEquals(1, dbBook.getReviews().size());
	}

}
