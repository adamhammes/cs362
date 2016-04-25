package databaseTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import databaseSupport.DatabaseSupport;
import interfaces.BookInterface;
import interfaces.UserInterface;
import models.EbookSystem;
import models.User;

public class UserDbTests {

	DatabaseSupport db;
	EbookSystem system;
	
	@Before
	public void setup(){
		system = new EbookSystem();
		db = new DatabaseSupport();
		db.reset();
	}
	
	
	@Test
	public void updateUser_AddBook(){
		UserInterface u = db.getUser("nick");
		assertNotNull(u);
		
		assertTrue(u.addBook("nckb", "Nick's Book on Testing"));
		assertTrue(db.putUser(u));
		
		BookInterface b = db.getBook("nckb");
		assertNotNull(b);
		
		assertEquals(b.getId(), "nckb");
		assertEquals(b.getTitle(), "Nick's Book on Testing");
	}
	
	
	//////////////////////////////////// Null Value get Tests/////////////////////////////////////////////
	
	
	@Test
	public void getUser_Null(){
		assertNull(db.getUser(null));
	}
	
	@Test
	public void getUser_EmptyString(){
		assertNull(db.getUser(""));
	}
	
	
	//////////////////////////////////// Null Value put Tests/////////////////////////////////////////////

	@Test
	public void putUser_Null(){
		assertFalse(db.putUser(null));
	}
	
	@Test
	public void putUser_null_id(){
		User user = new User(null);
		
		assertFalse(db.putUser(user));
	}
	
	@Test
	public void putUser_empty_id(){
		User user = new User("");
		
		assertFalse(db.putUser(user));
	}
	
	@Test
	public void deleteUser_basic() {
		assertNotNull(db.getUser("adam"));
		
		EbookSystem system = new EbookSystem();
		system.deleteUser("adam");
		
		assertNull(db.getUser("adam"));
	}
	
	@Test
	public void deleteUser_noUser() {
		system.deleteUser("asdfasdfasdfasdf");
		// just making sure no exception gets thrown
	}
	
}
