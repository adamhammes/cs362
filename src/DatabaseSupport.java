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
		
		
		// Class.forName("com.mysql.jdbc.Driver");
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
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account WHERE account_name = ?;");
			stmt.setString(1, uid);
			ResultSet results = stmt.executeQuery();
			
			results.next();
			user = new User(results.getString("account_name"));
			
			
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
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO Account Values (?);");
			stmt.setString(1, u.getName());
			stmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
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
