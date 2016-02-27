import java.sql.SQLException;

public class EbookSystem {

	
	boolean addTag(String uid, String bookTitle, String tag) throws SQLException{
		
		DatabaseSupport db = new DatabaseSupport();
		User user = db.getUser(uid);
		
		if (user != null){
			user.addTag(bookTitle, tag);
		
			return db.putUser(user);
		}
		
		return false;	
	}
}
