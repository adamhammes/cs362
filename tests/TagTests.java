import java.sql.SQLException;
import java.util.Collection;

import databaseSupport.DatabaseSupport;
import interfaces.BookInterface;
import interfaces.TagInterface;
import models.Book;
import models.EbookSystem;
import models.User;

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
		EbookSystem system = new EbookSystem();
//		System.out.println(system.addTag("nick", "Worm", "awesome"));
//		System.out.println(system.removeTag("nick", "Worm", "awesome"));
		
		Collection<BookInterface> books = system.getBookWithTag("nick", "awesome");
		for (BookInterface book : books){
			System.out.println(book.getTitle());
		}
	}
}
