import java.sql.SQLException;
import java.util.Collection;

import databaseSupport.DatabaseSupport;
import interfaces.BookInterface;
import interfaces.TagInterface;
import models.*;


public class TagTests {
//	public static void main(String[] args) throws SQLException{
//		DatabaseSupport db = new DatabaseSupport();
//		User u = (User) db.getUser("nick");
//		
//		for (BookInterface b : u.userBooks.values()){
//			System.out.println("book: " + ((Book) b).getTitle());
//			System.out.print("\tTags: ");
//			for (TagInterface t : b.getTags()){
//				System.out.print(t.getName() + " ");
//			}
//			System.out.println();
//		}
//		
//		db.putUser(u);
//	}
	
	public static void main(String[] args) throws SQLException{
//		EbookSystem system = new EbookSystem();
//		System.out.println(system.addTag("nick", "Worm", "awesome"));
//		System.out.println(system.removeTag("nick", "Worm", "awesome"));
		
//		Collection<BookInterface> books = system.getBookWithTag("nick", "awesome");
//		for (BookInterface book : books){
//			System.out.println(book.getTitle());
//		}
		
		DatabaseSupport db = new DatabaseSupport();
		Book b = new Book("bk2", "An alright book");
		b.addVersion("books/awesome/thatOtherBook.pdf", ".pdf");
		Author a = new Author("amriesen", "Anthony Riesen");
		b.addAuthor(a);
		
		Review r = new Review(2);
		r.setReview("This book was ok.");
		
		b.addReview(r);
		
		db.putBook(b);
	}
	
	
}
