import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import databaseSupport.DatabaseSupport;
import interfaces.AuthorInterface;
import interfaces.BookInterface;
import interfaces.ReviewInterface;
import interfaces.UserInterface;
import interfaces.VersionInterface;
import models.Author;
import models.Book;
import models.Review;

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
		
		Book b = new Book("nckb", "Nick's Book on Testing", "This is a pretty Awesome Book");
		assertTrue(db.putBook(b));
		
		b = (Book) db.getBook("nckb");
		assertEquals("Nick's Book on Testing", b.getTitle());
		assertEquals("This is a pretty Awesome Book", b.getDescription());
	}
	
	@Test
	public void Book_updateBook_Basic(){
		Book b = (Book) db.getBook("hp1");
		assertNull(b.getDescription());
		
		b.setTitle("Harry Potter 1");
		b.setDescription("You're a wizard, Harry!");
		assertTrue(db.putBook(b));
		
		b = null;
		b = (Book) db.getBook("hp1");
		
		assertEquals("Harry Potter 1", b.getTitle());
		assertEquals("You're a wizard, Harry!", b.getDescription());
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
	

	@Test
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
	
	
	@Test
	public void Book_updateBook_AddVersion(){
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
	public void Book_updateBook_AddReview(){
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
	public void Book_updateBook_RemoveReview(){
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
	public void User_updateUser_AddBook(){
		UserInterface u = db.getUser("nick");
		assertNotNull(u);
		
		assertTrue(u.addBook("nckb", "Nick's Book on Testing"));
		assertTrue(db.putUser(u));
		
		BookInterface b = db.getBook("nckb");
		assertNotNull(b);
		
		assertEquals(b.getId(), "nckb");
		assertEquals(b.getTitle(), "Nick's Book on Testing");
	}
	
	
	//////////////////////////////////// Null Value Tests/////////////////////////////////////////////
	
	@Test
	public void Book_getBook_Null(){
		assertNull(db.getBook(null));
	}
	
	@Test
	public void Book_getBook_EmptyString(){
		assertNull(db.getBook(""));
	}
	
	@Test
	public void Book_putBook_Null(){
		assertFalse(db.putBook(null));
	}
	
	@Test
	public void User_getUser_Null(){
		assertNull(db.getUser(null));
	}
	
	@Test
	public void User_getUser_EmptyString(){
		assertNull(db.getUser(""));
	}
	
	@Test
	public void User_putUser_Null(){
		assertFalse(db.putUser(null));
	}
}
