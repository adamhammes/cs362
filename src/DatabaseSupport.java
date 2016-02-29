import java.sql.*;

public class DatabaseSupport {
	Connection connection;

	private static final String USERNAME = "Nicholas";
	private static final String PASSWORD = ""; // no password needed
	private static final String CONN_STRING = "jdbc:postgresql://localhost:5432/System";

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		User user = new User("Anthony");
		DatabaseSupport db = new DatabaseSupport();
		db.putUser(user);
		
		Class.forName("org.postgresql.Driver");

		try (Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);

				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet results = stmt.executeQuery("SELECT * FROM Account");) {

			System.out.println("Connected!");

			while (results.next()) {

				System.out.println(results.getString("account_name"));
			}

		} catch (SQLException e) {
			System.err.println(e);
		}
	}
	
	public User getUser(String uid) throws SQLException {
		User user = null;
		Connection conn = null;
		
		try {
			conn = openConnection();
			
			//Get User
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account WHERE account_name = ?;");
			stmt.setString(1, uid);
			ResultSet results = stmt.executeQuery();
			
			results.next();
			user = new User(results.getString("account_name"));
			
			//Get Books
			stmt = conn.prepareStatement("SELECT book.book_id, book.title FROM account_book join book on account_book.book_id=book.book_id where account_book.account_name=?;");
			stmt.setString(1, uid);
			results = stmt.executeQuery();
			
			while(results.next()){
				Book toAdd = new Book(results.getString("book_id"), results.getString("title"));
				user.userBooks.put(toAdd.getTitle(), toAdd);
			}
			
			//Get Tags
			stmt = conn.prepareStatement("SELECT Book_tag.book_id, tag, title FROM Book_tag inner join book on Book_tag.book_id=book.book_id WHERE account_name = ?;");
			stmt.setString(1, uid);
			results = stmt.executeQuery();
			
			while(results.next()){
				Tag tag = user.userTags.get(results.getString("tag"));
				if (tag == null){
					tag = new Tag(results.getString("tag"));
					user.userTags.put(tag.getName(), tag);
				}
				tag.books.add(user.userBooks.get(results.getString("title")));
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if (conn != null)
				conn.close();
		}
		
		return user;
	}
	
	public boolean putUser(User u) throws SQLException {
		Connection conn = null;

		try {
			conn = openConnection();
			
			//put user
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO account (account_name) VALUES (?) "
															+ "ON CONFLICT (account_name) DO NOTHING;");
			stmt.setString(1, u.getName());
			stmt.executeUpdate();
			
			//put all of the users books
			for (Book book : u.userBooks.values()){
				stmt = conn.prepareStatement("INSERT INTO account_book (account_name, book_id) VALUES (?, ?) "
												+ "ON CONFLICT (account_name, book_id) DO NOTHING;");
				stmt.setString(1, u.getName());
				stmt.setString(2, book.getId());
				stmt.executeUpdate();
			}
			
			//put all tags for this user
			for (Tag tag : u.userTags.values()){
				for (Book book : tag.books){
					stmt = conn.prepareStatement("INSERT INTO book_tag (account_name, book_id, tag) VALUES (?, ?, ?) "
							+ "ON CONFLICT (account_name, book_id, tag) DO NOTHING;");
					stmt.setString(1, u.getName());
					stmt.setString(2, book.getId());
					stmt.setString(3, tag.getName());
					stmt.executeUpdate();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			if (conn != null)
				conn.close();
		}
		return true;
	}
	
	public Connection openConnection() throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		return DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
	}
}
