package models;

import java.util.ArrayList;

public class Tag {
    private String name;

    //needs to be public for databaseSupport
    public ArrayList<Book> books;
    
    public Tag(String name) {
        this.name = name;
        books = new ArrayList<Book>();
    }

    public String getName() {
        return name;
    }

    
    /**
     * Add book to this tag. 
     * 
     * @param book book to tag
     */
	public void addBook(Book book) {
		books.add(book);
	}
}
