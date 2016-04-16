package databaseTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import databaseSupport.DatabaseSupport;
import interfaces.BookInterface;
import interfaces.SeriesInterface;

public class SeriesDbTests {

	DatabaseSupport db;
	
	@Before
	public void setup(){
		db = new DatabaseSupport();
		db.reset();
	}
	
	
	@Test
	public void getSeries_basic(){
		SeriesInterface series = db.getSeries("hp");
		
		assertNotNull(series);
		assertEquals(series.getId(), "hp");
		assertEquals(series.getName(), "Harry Potter");
	}
	
	
	@Test
	public void getSeries_books(){
		SeriesInterface series = db.getSeries("hp");
		
		assertNotNull(series);
		assertEquals(series.getBooks().size(), 2);
		
		BookInterface hp1 = series.getBooks().get(0);
		assertNotNull(hp1);
		assertEquals(hp1.getId(), "hp1");
		assertEquals(hp1.getTitle(), "Harry Potter and The Sorcerer's Stone");
		
		BookInterface hp2 = series.getBooks().get(1);
		assertNotNull(hp2);
		assertEquals(hp2.getId(), "hp2");
		assertEquals(hp2.getTitle(), "Harry Potter and the Chamber of Secrets");
	}
	
	
}
