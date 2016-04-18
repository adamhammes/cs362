package databaseTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import databaseSupport.DatabaseSupport;
import interfaces.AuthorInterface;
import interfaces.BookInterface;
import interfaces.ReviewInterface;
import interfaces.SeriesInterface;
import interfaces.VersionInterface;
import models.Author;
import models.Book;
import models.Review;

public class BookDbTests {

	DatabaseSupport db;
	
	@Before
	public void setup(){
		db = new DatabaseSupport();
		db.reset();
	}
	
	@Test
	public void addBook_Basic(){
		assertEquals(null, db.getBook("nckb"));
		
		Book b = new Book("nckb", "Nick's Book on Testing", "This is a pretty Awesome Book");
		assertTrue(db.putBook(b));
		
		b = (Book) db.getBook("nckb");
		assertEquals("Nick's Book on Testing", b.getTitle());
		assertEquals("This is a pretty Awesome Book", b.retrieveDescription());
	}
	
	
	@Test
	public void addBook_NoBid(){
		Book b = new Book(null, "Test Book");
		
		assertFalse(db.putBook(b));
	}
	
	@Test
	public void addBook_EmptyBid(){
		Book b = new Book("", "Test Book");
		
		assertFalse(db.putBook(b));
	}
	
	@Test
	public void addBook_NoTitle(){
		Book b = new Book("abc", null);
		assertFalse(db.putBook(b));
	}
	
	@Test
	public void addBook_EmptyTitle(){
		//Group decided to alow empty titles
		Book b = new Book("abc", "");
		assertTrue(db.putBook(b));
	}
	
	@Test
	public void addBook_NewAuthor(){
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
	public void updateBook_Basic(){
		Book b = (Book) db.getBook("hp1");
		assertNull(b.retrieveDescription());
		
		b.setTitle("Harry Potter 1");
		b.editDescription("You're a wizard, Harry!");
		assertTrue(db.putBook(b));
		
		b = null;
		b = (Book) db.getBook("hp1");
		
		assertEquals("Harry Potter 1", b.getTitle());
		assertEquals("You're a wizard, Harry!", b.retrieveDescription());
	}
	
	
	@Test
	public void updateBook_NewAuthor(){
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
	

	@Test
	public void updateBook_RemoveAuthor(){
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
	
	
	@Test
	public void updateBook_AddVersion(){
		BookInterface b = db.getBook("hp1");
		assertNotNull(b);
		assertTrue(b.getVersions().isEmpty());
		
		assertTrue(b.addVersion("/books/fantacy/Harry_Potter/hp-1", ".pdf"));
		assertTrue(db.putBook(b, "nick"));
		
		b = db.getBook("hp1", "nick");
		assertNotNull(b);
		VersionInterface v = b.getVersions().iterator().next();  //getBook does not populate versions yet
		
		assertEquals(v.getPath(), "/books/fantacy/Harry_Potter/hp-1");
		assertEquals(v.getType(), ".pdf");
	}
	
	
	@Test
	public void updateBook_AddReview(){
		BookInterface b = db.getBook("hp1");
		assertNotNull(b);
		assertTrue(b.getReviews().isEmpty());
		
		assertTrue(b.addReview(new Review(-1, 3, "meh")));
		assertTrue(db.putBook(b));
		
		b = db.getBook("hp1");
		ReviewInterface r = b.getReviews().iterator().next();
		assertEquals(r.getRating(), 3);
		assertEquals(r.getReview(), "meh");
	}
	
	
	@Test
	public void updateBook_RemoveReview(){
		BookInterface b = db.getBook("mobydick");
		assertNotNull(b);
		assertFalse(b.getReviews().isEmpty());
		int numOfReviews = b.getReviews().size();
		
		//Remove 
		b.getReviews().remove(b.getReviews().iterator().next());
		assertTrue(db.putBook(b));
		
		b = db.getBook("mobydick");
		assertNotNull(b);
		assertEquals(b.getReviews().size(), numOfReviews - 1);
	}
	
	
	@Test
	public void getBook_Series_and_Series_Books(){
		BookInterface book = db.getBook("hp1");
		
		SeriesInterface series = book.getSeries();
		assertNotNull(series);
		assertEquals(series.getId(), "hp");
		assertEquals(series.getName(), "Harry Potter");
		
		for (BookInterface b : series.getBooks()){
			
			if (b.getId().equals("hp1")) {
				assertEquals(book, b); //both books should point to the same object
			}
			else if (b.getId().equals("hp2")) {
				assertEquals(b.getTitle(), "Harry Potter and the Chamber of Secrets");
			}
			else {
				//This will need to be changed if more harry potter books are added
				fail();
			}
		}
	}
	
	
	@Test
	public void getBook_Null(){
		assertNull(db.getBook(null));
	}
	
	
	@Test
	public void getBook_EmptyString(){
		assertNull(db.getBook(""));
	}
	
	
	@Test
	public void putBook_Null(){
		assertFalse(db.putBook(null));
	}

	
	@Test
	public void putBook_null_id(){
		Book book = new Book(null, "some title");
		
		assertFalse(db.putBook(book));
	}
	
	
	@Test
	public void putBook_empty_id(){
		Book book = new Book("", "some title");
		
		assertFalse(db.putBook(book));
	}
}
