package databaseSupport;

import java.sql.Connection;
import java.sql.SQLException;

interface Putable {
	public boolean put(Connection conn) throws SQLException;
}
