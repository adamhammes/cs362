import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class User {
    private String name;
    
    HashMap<String, Book> userBooks;
    HashMap<String, Tag> userTags;
    

    public User(String name)  {
        this.name = name;
        
        userTags = new HashMap<String, Tag>();
        userBooks = new HashMap<String, Book>();
    }

    
    public String getName() {
        return name;
    }

    
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
	
	
	private Tag getTag(String tagname){
		Tag tag = userTags.get(tagname);
		
		if (tag == null){
			tag = new Tag(tagname);
			userTags.put(tagname, tag);
		}
		return tag;
		
	}
	
	
	private Book getBook(String bookTitle){
		
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
		System.out.println();
		
		System.out.println("Tag fantasy: " + this.userTags.get("fantasy").books.get(0).getTitle());
	}
}
