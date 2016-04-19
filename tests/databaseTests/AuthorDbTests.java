package databaseTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import databaseSupport.DatabaseSupport;
import interfaces.AuthorInterface;
import interfaces.BookInterface;

public class AuthorDbTests {

	DatabaseSupport db = new DatabaseSupport();
	
	@Before
	public void setup(){
		db.reset();
	}
	
	@Test
	public void getAuthor_basic() {
		AuthorInterface author = db.getAuthor("jkr");
		assertNotNull(author);
		assertNotNull(author.getBooks());
		assertEquals(author.getBooks().size(), 1);
		
		BookInterface book = author.getBooks().get(0);
		assertEquals("hp2", book.getId());
	}
}
