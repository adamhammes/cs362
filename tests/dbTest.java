import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import databaseSupport.DatabaseSupport;
import interfaces.BookInterface;
import models.Author;
import models.Book;

public class dbTest {

	DatabaseSupport db;
	
	@Before
	public void setup(){
		db = new DatabaseSupport();
		db.reset();
	}
	
	
	@Test
	public void Book_addBook_Basic(){
		assertEquals(null, db.getBook("nckb"));
		
		Book b = new Book("nckb", "Nick's Book on Testing");
		assertTrue(db.putBook(b));
		
		b = (Book) db.getBook("nckb");
		assertTrue(b.getTitle().equals("Nick's Book on Testing"));
	}
	
	
	@Test
	public void Book_addBook_NewAuthor(){
		assertEquals(null, db.getBook("nckb"));
		
		BookInterface b = new Book("nckb", "Nick's Book on Testing");
		Author a = new Author("nck", "Nicholas Riesen");
		assertTrue(b.addAuthor(a));
		assertTrue(db.putBook(b));
		
		b = db.getBook("nckb");
		assertTrue(b.getTitle().equals("Nick's Book on Testing"));
		
		//Not implemented yet
//		assertTrue(b.getAuthors().get(0).getId().equals("nckb"));
//		assertTrue(b.getAuthors().get(0).getId().equals("Nicholas Riesen"));
	}
	
}
