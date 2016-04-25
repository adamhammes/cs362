package models;

import interfaces.BookInterface;
import interfaces.TagInterface;
import interfaces.UserInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
     * @param bid the title of the book to tag
     * @param tagName what to tag the book with
     * @return successfully completed operation
     */
	public boolean addTag(String bid, String tagName) {
		TagInterface tag = getTag(tagName);
		BookInterface book = getBookById(bid);

		if (book != null){
			tag.addBook(book);
			book.addTag(tag);
			return true;
		}
		else{
			return false;
		}
	}
	
	
	@Override
	public boolean removeTag(String bid, String tag) {
		if (!userTags.containsKey(tag)) {
			return false;
		}
		
		TagInterface t = userTags.get(tag);
		if (t == null)
			return false;
		
		BookInterface book = t.getBook(bid);
		if (book == null)
			return false;
		
		if (book.removeTag(t) == false)
			return false;
		
		if (t.removeBook(book) == false)
			return false;
			
		if (t.getBooks().size() == 0)
			return userTags.remove(tag) != null;
		
		return true;
	}
	
	
	@Override
	public Collection<BookInterface> getBooksWithTag(String tag) {
		return getTag(tag).getBooks();
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
	 * Retrieves the users book specified by the book id. If the user does not own 
	 * a book by this id, then null will be returned.
	 * 
	 * @param bid title of the book to retrieve
	 * @return requested book
	 */
	public BookInterface getBookByTitle(String bookTitle){
		return userBooks.get(bookTitle);
	}
	
	public BookInterface getBookById(String id) {
		for (BookInterface b: getAllBooks()) {
			if (b.getId().equals(id)) {
				return b;
			}
		}
		return null;
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
	public List<BookInterface> getAllBooksByRating(){
		ArrayList<BookInterface> a = new ArrayList<>(userBooks.values());
		Comparator<BookInterface> c = new Comparator<BookInterface>() {
			
			@Override
			public int compare(BookInterface o1, BookInterface o2) {
				if(o1.averageRating() - o2.averageRating() > 0)
					return 1;
				else if(o1.averageRating() - o2.averageRating() == 0)
					return 0;
				else
					return -1;
			}
		};
		
		Collections.sort(a, c);
		return a;
	}


	@Override
	public ArrayList<TagInterface> getTags() {
		return new ArrayList<>(userTags.values());
	}


	@Override
	public boolean addVersion(String bid, String path, String type) {

		BookInterface book = getBookById(bid);
		if (book == null)
			return false;
		
		return book.addVersion(path, type);
	}


}
