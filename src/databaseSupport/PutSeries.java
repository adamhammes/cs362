package databaseSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import interfaces.BookInterface;
import interfaces.SeriesInterface;

class PutSeries extends Putable{

	private SeriesInterface series;
	
	public PutSeries(SeriesInterface series){
		this.series = series;
	}
	
	
	@Override
	public boolean put(Connection conn) throws SQLException {
		
		putSeries(conn);
		
		putBooks(conn);
		
		return false;
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
				PutBook putBookRequest = new PutBook(book, null);
				putBookRequest.put(conn);
			}
			
			//update joining table
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO book_series VALUES (?, ?) "
					+ "ON CONFLICT DO NOTHING;"); //The joining table might be duplicated
			stmt.setString(1, book.getId());
			stmt.setString(2, series.getId());
			System.out.println(stmt);
			stmt.executeUpdate();
		}
		
	}
}
