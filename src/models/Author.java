package models;

import java.util.ArrayList;

import interfaces.AuthorInterface;
import interfaces.BookInterface;

public class Author implements AuthorInterface {
	private String id;
	private String name;
	private ArrayList<BookInterface> books= new ArrayList<BookInterface>();
	
	public Author(String id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public ArrayList<BookInterface> getBooks() {
		return books;
	}
	
	@Override
	public boolean addBook(BookInterface book) {
		
		if (!books.contains(book)){
			books.add(book);
			book.addAuthor(this);
		}
		return true;
	}
}
