package models;

import java.util.HashMap;
import java.util.List;

public class User {
    private String name;
    
    //Needs to be public for databaseSupport
    public HashMap<String, Book> userBooks;
    public HashMap<String, Tag> userTags;

    public User(String name)  {
        this.name = name;
        
        userTags = new HashMap<String, Tag>();
        userBooks = new HashMap<String, Book>();
    }

    
    public String getName() {
        return name;
    }

    
    /**
     * Adds tags a book owned by the user. If the tag does not exist a tag will
     * be created.
     * 
     * @param bookTitle the title of the book to tag
     * @param tagName what to tag the book with
     * @return successfully completed operation
     */
	public boolean addTag(String bookTitle, String tagName) {
		
		Tag tag = getTag(tagName);
		Book book = getBook(bookTitle);
		
		if (book != null){
			tag.addBook(book);
			book.addTag(tag);
			return true;
		}
		else{
			return false;
		}
	}
	
	
	/**
	 * Retrieves the users tag specified by the tagname. If no tag by that name exists, 
	 * one will be created.
	 * 
	 * @param tagname name of the tag to retrieve
	 * @return requested tag
	 */
	private Tag getTag(String tagname){
		Tag tag = userTags.get(tagname);
		
		if (tag == null){
			tag = new Tag(tagname);
			userTags.put(tagname, tag);
		}
		return tag;
	}
	
	
	/**
	 * Retrieves teh users book specified by the book title. If the user does not own 
	 * a book by this title, then null will be returned.
	 * 
	 * @param bookTitle title of the book to retrieve
	 * @return requested book
	 */
	private Book getBook(String bookTitle){
		return userBooks.get(bookTitle);
	}
	
	
	public List<Book> getAllBooks(){
		if(userBooks.isEmpty())
			return null;
		else
			return (List<Book>) userBooks;
	}
	
	
	public void printUser(){
		System.out.println("name: " + name);
		System.out.print("books: ");
		for (String book : userBooks.keySet()){
			System.out.print(book + " ");
		}
		System.out.println();
		
		System.out.print("tags: ");
		for (String tag : userTags.keySet()){
			System.out.print(tag + " ");
		}
	
	}
}
