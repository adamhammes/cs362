import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import databaseSupport.DatabaseSupport;
import interfaces.BookInterface;
import interfaces.ReviewInterface;

public class getBookTest {
	DatabaseSupport db;
	
	@Before
	public void initialize() {
		db = new DatabaseSupport();
	}

	@Test
	public void testRegularGet() {
		BookInterface book = db.getBook("mobydick");
		
		assertEquals("mobydick", book.getId());
		assertEquals("Moby Dick", book.getTitle());
		
		Collection<ReviewInterface> reviews = book.getReviews();
		assertEquals(2, reviews.size());
	}

}
