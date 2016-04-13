import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import interfaces.BookInterface;
import models.EbookSystem;

public class getBooksWithTagTest {
//	DatabaseSupport db;
	EbookSystem sys;
	
	@Before
	public void initialize() {
//		db = new DatabaseSupport();
		sys = new EbookSystem();
	}

	@Test
	public void test() {
		Collection<BookInterface> books = sys.getBooksWithTag("adam", "fantasy");
		assertEquals(3, books.size());
	}
}
