package databaseTests;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import databaseSupport.DatabaseSupport;
import interfaces.BookInterface;
import interfaces.SystemInterface;
import interfaces.VersionInterface;
import models.EbookSystem;

public class nicks_iteration_3_tests {

	SystemInterface sys = new EbookSystem();
	
	@Before
	public void setup(){
		new DatabaseSupport().reset();
	}
	
	@Test
	public void ListBookVersionsTest() {
		Collection<VersionInterface> versions = sys.listAllVersions("hp2", "nick");
		assertNotNull(versions);
		assertEquals(2, versions.size());
		
		System.out.println("Versions for hp2 owned by nick");
		for (VersionInterface version : versions) {
			System.out.println(version);
		}	
	}
	
	@Test
	public void noVersionsAvalable() {
		Collection<VersionInterface> versions = sys.listAllVersions("hp2", "adam");
		assertNotNull(versions);
		assertEquals(0, versions.size());
	}

	@Test
	public void listAuthorsBooks() {
		Collection<BookInterface> books = sys.listBookByAuthor("jkr", "nick");
		
		assertNotNull(books);
		assertEquals(1, books.size());
		
		System.out.println("Books Writen By jkr");
		for (BookInterface book : books) {
			System.out.println(book);
		}
		
	}
	
	@Test
	public void AuthorHasNoBooks() {
		Collection<BookInterface> books = sys.listBookByAuthor("hm", "nick");
		assertNotNull(books);
		assertEquals(0, books.size());
	}
	
	
	@Test
	public void getVersion() {
		VersionInterface version = sys.getVersionId("hp2", "tex", "nick");
		assertNotNull(version);
		assertEquals("tex", version.getType());
		assertEquals("Who_Converted_HP2_TO_Latex.tex", version.getPath());
	}
	
	
	@Test
	public void noVersionAvalable() {
		VersionInterface version = sys.getVersionId("hp2", "ebook", "nick");
		assertNull(version);
	}
}
