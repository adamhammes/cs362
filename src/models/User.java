import java.util.ArrayList;

public class User {
    private String name;
    private ArrayList<Tag> userTags;
    private ArrayList<Book> userBooks;

    public User(String name)  {
        this.name = name;
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
		for (Tag tag : userTags){
			if (tag.getName().equals(tagname))
				return tag;
		}
		
		Tag newTag = new Tag(tagname);
		userTags.add(newTag);
		
		return newTag;
	}
	
	private Book getBook(String bookTitle){
		
		for (Book book : userBooks){
			if(book.getTitle().equals(bookTitle))
				return book;
		}
		
		return null;
	}
}
