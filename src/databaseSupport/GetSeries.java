package databaseSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import interfaces.SeriesInterface;
import models.Series;

class GetSeries implements Getable{

	private String seriesId;
	private Series series = null;
	
	public GetSeries(String seriesId){
		this.seriesId = seriesId;
	}
	
	@Override
	public Object get(Connection conn) throws SQLException {
		
		getSeries(conn);
		getBooks(conn);
		
		return series;
	}

	
	private void getSeries(Connection conn) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM series WHERE series_id = ?;");
		stmt.setString(1, seriesId);
		ResultSet results = stmt.executeQuery();
		
		if (results.next()){
			series = new Series(results.getString("series_id"), results.getString("series_name"));
		}
	}
	
	private void getBooks(Connection conn) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT book_id FROM book_series WHERE series_id = ? ORDER BY index ASC;");
		stmt.setString(1, seriesId);
		ResultSet results = stmt.executeQuery();
		
		while (results.next()) {
			GetBook getBookRequest = new GetBook(results.getString("book_id"), null);
			series.addBook(getBookRequest.get(conn));
		}
	}
}
