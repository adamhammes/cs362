package models;

import java.util.ArrayList;

import interfaces.AuthorInterface;
import interfaces.BookInterface;

public class Author implements AuthorInterface {
	private String id;
	private String name;
	private String description;
	private ArrayList<BookInterface> books= new ArrayList<BookInterface>();
	
	public Author(String id, String name) {
		this.id = id;
		this.name = name;
		this.description = "";
	}

	public Author(String id, String name, String description) {
		this.id = id;
		this.name = name;
		
		if (description == null)
			this.description = "";
		else
			this.description = description;
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

	@Override
	public void removeBook(String id) {
		
		for (int i = 0; i < books.size(); i++) {
			if (books.get(i).getId().equals(id)){
				BookInterface temp = books.get(i);
				books.remove(i);
				temp.removeAuthor(id);
			}
		}
	}
	
	@Override
	public boolean addDescription(String description) {
		if (description == null)
			this.description = "";
		else
			this.description = description;
		return true;
	}
	
	
	@Override
	public boolean deleteDescription() {
		description = "";
		return true;
	}
	
	
	@Override
	public String retreveDescription() {
		return description;
	}
	
	
	@Override
	public boolean editDescription(String description) {
		if (description == null)
			this.description = "";
		else
			this.description = description;
		return true;
	}
	
}
