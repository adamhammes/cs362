import java.sql.SQLException;

public class TagTests {
	public static void main(String[] args) throws SQLException{
			
		EbookSystem sys = new EbookSystem();
		
		boolean result = sys.addTag("adam", "myBook", "myTag");
	
		System.out.println("result: " + result);
	}
}