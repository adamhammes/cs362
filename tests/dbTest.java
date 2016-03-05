import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import databaseSupport.DatabaseSupport;
import interfaces.AuthorInterface;
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
		assertNull(db.getBook("nckb"));
		
		BookInterface b = new Book("nckb", "Nick's Book on Testing");
		Author a = new Author("nck", "Nicholas Riesen");
		assertTrue(b.addAuthor(a));
		assertTrue(db.putBook(b));
		
		b = db.getBook("nckb");
		assertTrue(b.getTitle().equals("Nick's Book on Testing"));
		
		assertEquals(b.getAuthors().get(0).getId(), "nck");
		assertEquals(b.getAuthors().get(0).getName(), "Nicholas Riesen");
	}
	
	
	@Test
	public void Book_updateBook_NewAuthor(){
		BookInterface b = db.getBook("hp1");
		assertNotNull(b);
		assertEquals(0, b.getAuthors().size());
		
		AuthorInterface a = new Author("jkr", "J.K. Rowling");
		assertTrue(b.addAuthor(a));
		assertTrue(db.putBook(b));
		
		b = db.getBook("hp1");
		assertNotNull(b);
		assertEquals(b.getAuthors().get(0).getId(), "jkr");
		assertEquals(b.getAuthors().get(0).getName(), "J.K. Rowling");
	}
	

	@Test //Not implemented yet
	public void Book_updateBook_RemoveAuthor(){
		BookInterface b = db.getBook("hp2");
		assertNotNull(b);
		assertEquals(1, b.getAuthors().size());
		
		assertEquals(b.getAuthors().get(0).getId(), "jkr");
		System.out.println(b.getAuthors().get(0).getName());
		assertEquals(b.getAuthors().get(0).getName(), "J.K. Rowling");
		
		b.getAuthors().remove(0);
		assertTrue(db.putBook(b));
		
		b = db.getBook("hp2");
		assertNotNull(b);
		assertEquals(b.getAuthors().size(), 0); 
	}
	
}
