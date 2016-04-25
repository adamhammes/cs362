package databaseSupport;

import java.sql.Connection;
import java.sql.SQLException;

public interface Deletable {
	boolean delete(Connection conn) throws SQLException;
}
