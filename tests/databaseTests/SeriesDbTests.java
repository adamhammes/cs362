package databaseTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import databaseSupport.DatabaseSupport;
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
	
}
