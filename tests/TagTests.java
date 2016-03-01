import java.sql.SQLException;

import models.EbookSystem;

public class TagTests {
	public static void main(String[] args) throws SQLException{
		EbookSystem sys = new EbookSystem();
		boolean result = sys.addTag("nick", "Harry Potter and The Sorcerer's Stone", "My Book");

		System.out.println("result: " + result);
	}
}
