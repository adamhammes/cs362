import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import databaseSupport.DatabaseSupport;
import models.EbookSystem;

public class addVersionTest {
	DatabaseSupport db;
	EbookSystem sys;
	
	@Before
	public void initialize() {
		//db = new DatabaseSupport();
		sys = new EbookSystem();
	}
	
	@Test
	public void test() {
		assertTrue(sys.addVersion("adam", "worm", "/path/to/somewhere", "epub"));
		// TODO: actual test :)
	}

}
