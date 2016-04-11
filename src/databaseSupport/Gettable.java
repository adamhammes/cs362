package databaseSupport;

import java.sql.Connection;
import java.sql.SQLException;

interface Gettable {
	public Object get(Connection conn) throws SQLException;
}
