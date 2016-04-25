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
		u.addTag("hp1", "fantasy");
		db.putUser(u);
		
		u = (User) db.getUser("nick");
		
		assertTrue(u.getTags().get(0).getName().equals("fantasy"));
	}	
	
	
	@Test
	public void getTag() {
		User user = (User) db.getUser("adam");
		
		System.out.println(user.getBooksWithTag("fantasy").size());
		System.out.println(user.getTags().size());
	}
	
	@Test
	public void test(){
		try {
			int value = Integer.parseInt("5");
			System.out.println(value);

		}
		catch (NumberFormatException e) {
			System.out.println("Exception thrown");
		}
	}
}
