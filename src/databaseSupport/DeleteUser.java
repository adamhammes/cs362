package databaseSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DeleteUser implements Deletable {
	String name;
	
	public DeleteUser(String name) {
		this.name = name;
	} 

	@Override
	public boolean delete(Connection conn) throws SQLException {
		PreparedStatement deleteVersions = conn.prepareStatement("DELETE FROM Book_Version WHERE account_name = ?");
		deleteVersions.setString(1, name);
		deleteVersions.executeUpdate();
		
		PreparedStatement deleteTags = conn.prepareStatement("DELETE FROM Book_Tag WHERE account_name = ?");
		deleteTags.setString(1, name);
		deleteTags.executeUpdate();
		
		PreparedStatement deleteBookRelation = conn.prepareStatement("DELETE FROM Account_Book WHERE account_name = ?");
		deleteBookRelation.setString(1, name);
		deleteBookRelation.executeUpdate();
		
		PreparedStatement deleteUser = conn.prepareStatement("DELETE FROM Account WHERE account_name = ?");
		deleteUser.setString(1, name);
		deleteUser.executeUpdate();
		
		return true;
	}

}
