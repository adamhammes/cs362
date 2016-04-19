package databaseTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import databaseSupport.DatabaseSupport;
import interfaces.AuthorInterface;
import interfaces.BookInterface;
import models.Book;

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
	
	@Test
	public void author_putBook() {
		AuthorInterface author = db.getAuthor("jkr");
		assertNotNull(author);
		author.addBook(new Book("hp3", "The Prisoner of Azkaban"));
		db.putAuthor(author);
		
		author = db.getAuthor("jkr");
		assertNotNull(author);
		assertNotNull(author.getBooks());
		assertEquals(author.getBooks().size(), 2);
		
		for (BookInterface book : author.getBooks()){
			if (book.getId().equals("hp3")){
				assertEquals("The Prisoner of Azkaban", book.getTitle());
				return;
			}
		}
		fail();
	}
	
	@Test
	public void author_removeBook(){
		AuthorInterface author = db.getAuthor("jkr");
		assertNotNull(author);
		assertNotNull(author.getBooks());
		assertEquals(1, author.getBooks().size());
		
		BookInterface book = author.getBooks().get(0);
		author.removeBook(book.getId());
		
		db.putAuthor(author);
		
		author = db.getAuthor("jkr");
		assertNotNull(author);
		assertNotNull(author.getBooks());
		assertEquals(0, author.getBooks().size());
	}
}
