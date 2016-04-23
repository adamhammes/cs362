import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import databaseSupport.DatabaseSupport;
import models.User;


public class TagTests {
	DatabaseSupport db;
	
	@Before
	public void setup(){
		db = new DatabaseSupport();
		db.reset();
	}
	
	@Test
	public void addTag(){
		User u = (User) db.getUser("nick");
		u.addTag("Harry Potter and The Sorcerer's Stone", "fantacy");
		db.putUser(u);
		
		u = (User) db.getUser("nick");
		
		assertTrue(u.getTags().get(0).getName().equals("fantacy"));
	}	
	
	
	@Test
	public void getTag() {
		User user = (User) db.getUser("adam");
		
		System.out.println(user.getBooksWithTag("fantasy").size());
		System.out.println(user.getTags().size());
	}
}
