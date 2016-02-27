import java.sql.SQLException;

public class TagTests {
	public static void main(String[] args) throws SQLException{
			
		EbookSystem sys = new EbookSystem();
		
		boolean result = sys.addTag("nick", "Harry Potter and The Sorcerer's Stone", "More Awesome");
	
		System.out.println("result: " + result);
	}
	
	
//	public static void main(String[] args) throws SQLException{
//		DatabaseSupport db = new DatabaseSupport();
//		
//		User user = new User("anna");
//		db.putUser(user);
//	}
	
}
