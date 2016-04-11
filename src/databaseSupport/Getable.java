package databaseSupport;

import java.sql.Connection;
import java.sql.SQLException;

interface Getable {
	public Object get(Connection conn) throws SQLException;
}
