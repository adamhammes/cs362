import java.util.ArrayList;

public class Tag {
    private String name;

    ArrayList<Book> books;
    
    public Tag(String name) {
        this.name = name;
        books = new ArrayList<Book>();
    }

    public String getName() {
        return name;
    }

	public void addBook(Book book) {
		// TODO Auto-generated method stub
		
	}
}
