package databaseSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteBook implements Deletable {
	String bid;

	public DeleteBook(String bid) {
		this.bid = bid;
	}
	@Override
	public boolean delete(Connection conn) throws SQLException {
		PreparedStatement deleteVersions = conn.prepareStatement("DELETE FROM Book_Version WHERE book_id = ?");
		deleteVersions.setString(1, bid);
		deleteVersions.executeUpdate();
		
		PreparedStatement deleteReview = conn.prepareStatement("DELETE FROM Book_Review WHERE book_id = ?");
		deleteReview.setString(1, bid);
		deleteReview.executeUpdate();

		PreparedStatement deleteAuthor = conn.prepareStatement("DELETE FROM Book_Author WHERE book_id = ?");
		deleteAuthor.setString(1, bid);
		deleteAuthor.executeUpdate();
		
		PreparedStatement deleteSeries = conn.prepareStatement("DELETE FROM Book_Series WHERE book_id = ?");
		deleteSeries.setString(1, bid);
		deleteSeries.executeUpdate();
		
		PreparedStatement deleteTags = conn.prepareStatement("DELETE FROM Book_Tag WHERE book_id = ?");
		deleteTags.setString(1, bid);
		deleteTags.executeUpdate();
		
		PreparedStatement deleteUserRelation = conn.prepareStatement("DELETE FROM Account_Book WHERE book_id = ?");
		deleteUserRelation.setString(1, bid);
		deleteUserRelation.executeUpdate();
		
		PreparedStatement deleteBook = conn.prepareStatement("DELETE FROM Book WHERE book_id = ?");
		deleteBook.setString(1, bid);
		deleteBook.executeUpdate();

		return true;
	}

}
