import java.sql.SQLException;

import databaseSupport.DatabaseSupport;
import interfaces.BookInterface;
import interfaces.TagInterface;
import models.Book;
import models.User;

public class TagTests {
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
		
		db.putUser(u);
	}
}
