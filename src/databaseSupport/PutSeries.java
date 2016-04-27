package databaseSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import interfaces.BookInterface;
import interfaces.SeriesInterface;

class PutSeries extends Putable{

	private SeriesInterface series;
	private String username;
	
	public PutSeries(SeriesInterface series, String username){
		this.series = series;
		this.username = username;
	}
	
	
	@Override
	public boolean put(Connection conn) throws SQLException {
		
		putSeries(conn);
		
		putBooks(conn);
		
		removeOldBooks(conn);
		
		return true;
	}

	
	private void putSeries(Connection conn) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO series VALUES (?, ?) "
				+ "ON CONFLICT (series_id) DO UPDATE SET series_name = ?;");
		stmt.setString(1, series.getId());
		stmt.setString(2, series.getName());
		stmt.setString(3, series.getName());
		
		stmt.executeUpdate();
		alreadyStoredSeries.add(series.getId());
	}
	
	private void putBooks(Connection conn) throws SQLException {
		
		for (BookInterface book : series.getBooks()){
			//book must be in place before joining table can be updated
			if (!alreadyStoredBooks.contains(book.getId())){
				PutBook putBookRequest = new PutBook(book, username);
				putBookRequest.put(conn);
			}
			
			//update joining table
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO book_series VALUES (?, ?) "
					+ "ON CONFLICT DO NOTHING;"); //The joining table might be duplicated
			stmt.setString(1, book.getId());
			stmt.setString(2, series.getId());
			stmt.executeUpdate();
		}
		
	}
	
	private void removeOldBooks(Connection conn) throws SQLException {
		StringBuffer rmSQL = new StringBuffer("DELETE FROM book_series WHERE (book_id, series_id) NOT IN ");
		rmSQL.append("(SELECT book_id, series_id FROM book_series WHERE series_id != ? ");
		
		//Build Query String
		for (int i = 0; i < series.getBooks().size(); i++)
			rmSQL.append(" OR book_id = ?");
		rmSQL.append(");");
		
		PreparedStatement rmstmt = conn.prepareStatement(rmSQL.toString());
		rmstmt.setString(1, series.getId());
		
		int i = 2;
		for (BookInterface book : series.getBooks()){
			rmstmt.setString(i++, book.getId());
		}
		System.out.println(rmstmt);
		rmstmt.executeUpdate();
					
	}
}
