package models;

import interfaces.BookInterface;
import interfaces.TagInterface;

import java.util.ArrayList;

public class Tag implements TagInterface {
    private String name;

    //needs to be public for databaseSupport
    public ArrayList<BookInterface> books;
    
    public Tag(String name) {
        this.name = name;
        books = new ArrayList<BookInterface>();
    }

    public String getName() {
        return name;
    }

    
    /**
     * Add book to this tag. 
     * 
     * @param book book to tag
     */
	public boolean addBook(BookInterface book) {
		books.add(book);
		return true;
	}
}
