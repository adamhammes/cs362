import java.sql.SQLException;

import databaseSupport.DatabaseSupport;
import interfaces.BookInterface;
import interfaces.TagInterface;
import models.Book;
import models.User;

public class TagTests {
//	public static void main(String[] args) throws SQLException{
//		EbookSystem sys = new EbookSystem();
//		boolean result = sys.addTag("nick", "Harry Potter and The Sorcerer's Stone", "Im out of ideas");
//
//		System.out.println("result: " + result);
//	}
	
	public static void main(String[] args) throws SQLException{
		DatabaseSupport db = new DatabaseSupport();
		User u = (User) db.getUser("nick");
		
		for (BookInterface b : u.userBooks.values()){
			System.out.println("book: " + ((Book) b).getTitle());
			System.out.print("\tTags: ");
			for (TagInterface t : b.getTags()){
				System.out.print(t.getName() + " ");
			}
			System.out.println();
		}
	}
}
