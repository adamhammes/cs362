import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import databaseSupport.DatabaseSupport;

public class addVersionTest {
	DatabaseSupport db;
	
	@Before
	public void initialize() {
		db = new DatabaseSupport();
	}
	
	@Test
	public void test() {
		assertTrue(db.addVersion("adam", "Worm", "/path/to/somewhere", "epub"));
		// TODO: actual test :)
	}

}
