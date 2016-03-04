import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import databaseSupport.DatabaseSupport;
import interfaces.BookInterface;

public class getBooksWithTagTest {
	DatabaseSupport db;
	
	@Before
	public void initialize() {
		db = new DatabaseSupport();
	}

	@Test
	public void test() {
		List<BookInterface> books = db.getBooksWithTag("adam", "fantasy");
		assertEquals(3, books.size());
	}
}
