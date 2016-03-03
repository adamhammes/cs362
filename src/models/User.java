package models;

import interfaces.BookInterface;
import interfaces.TagInterface;
import interfaces.UserInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User implements UserInterface {
    private String name;
    
    //Needs to be public for databaseSupport
    public HashMap<String, BookInterface> userBooks;
    public HashMap<String, TagInterface> userTags;

    public User(String name)  {
        this.name = name;
        
        userTags = new HashMap<String, TagInterface>();
        userBooks = new HashMap<String, BookInterface>();
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
		TagInterface tag = getTag(tagName);
		BookInterface book = getBook(bookTitle);
		
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
	private TagInterface getTag(String tagname){
		TagInterface tag = userTags.get(tagname);
		
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
	private BookInterface getBook(String bookTitle){
		return userBooks.get(bookTitle);
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


	@Override
	public boolean addBook(String bid, String title) {
		userBooks.put(bid, new Book(bid, title));
		return true;
	}


	@Override
	public List<BookInterface> getAllBooks() {
		return new ArrayList<BookInterface>(userBooks.values());
	}


	@Override
	public boolean removeTag(String bookTitle, String tag) {
		if (!userTags.containsKey(tag)) {
			return false;
		}
		userTags.remove(tag);
		return true;
	}


	@Override
	public ArrayList<TagInterface> getTags() {
		return new ArrayList<>(userTags.values());
	}


	@Override
	public boolean addVersion(String bid, String path, String type) {
		BookInterface book = getBook(bid);
		return book.addVersion(path, type);
	}
}
