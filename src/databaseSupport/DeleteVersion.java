package databaseSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteVersion implements Deletable {
	String bid;
	String uid;
	String format;

	public DeleteVersion(String uid, String bid, String format) {
		this.bid = bid;
		this.uid = uid;
		this.format = format;
	}

	@Override
	public boolean delete(Connection conn) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("DELETE FROM Book_Version WHERE "
				+ "book_id = ? AND "
				+ "account_name = ? AND "
				+ "format = ?");
		stmt.setString(1, bid);
		stmt.setString(2, uid);
		stmt.setString(3, format);
		stmt.executeUpdate();
		
		return true;
	}

}
