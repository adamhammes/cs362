package databaseSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import interfaces.BookInterface;
import interfaces.TagInterface;
import interfaces.UserInterface;

class PutUser {

	static public boolean putUser(Connection conn, UserInterface u) throws SQLException {
				
		//put user
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO account (account_name) VALUES (?) "
														+ "ON CONFLICT (account_name) DO NOTHING;");
		stmt.setString(1, u.getName());
		stmt.executeUpdate();
		
		//put all of the users books
		for (BookInterface book: u.getAllBooks()){
			PutBook.putBook(conn, book, null);
			
			stmt = conn.prepareStatement("INSERT INTO account_book (account_name, book_id) VALUES (?, ?) "
											+ "ON CONFLICT (account_name, book_id) DO NOTHING;");
			stmt.setString(1, u.getName());
			stmt.setString(2, book.getId());
			stmt.executeUpdate();
	
		}
		
		putTags(conn, u);
			
		return true;
	}
	

	private static boolean putTags(Connection conn, UserInterface u) throws SQLException{
		//Build prepared statement for removing unneeded tags
		int size = 0;
		for (TagInterface tag : u.getTags()) size+= tag.getBooks().size();
		
		StringBuffer rmsql = new StringBuffer(
				"DELETE FROM book_tag WHERE (account_name, book_id, tag) NOT IN "
			  + "(SELECT account_name, book_id, tag FROM book_tag WHERE account_name != ? OR (");
		
		for (int i = 0; i < size - 1; i++){
			rmsql.append("(book_id = ? AND tag = ?) OR ");
		}
		rmsql.append("(book_id = ? AND tag = ?)));");
		PreparedStatement rmstmt = conn.prepareStatement(rmsql.toString());
		rmstmt.setString(1, u.getName());
		int index = 2;
		
		PreparedStatement mkstmt = conn.prepareStatement("INSERT INTO book_tag (account_name, book_id, tag) VALUES (?, ?, ?) "
				+ "ON CONFLICT (account_name, book_id, tag) DO NOTHING;");
		
		//put all tags for this user
		for (TagInterface tag : u.getTags()){
			for (BookInterface book : tag.getBooks()){
				
				//create and execute sql command for upsert tag
				mkstmt.setString(1, u.getName());
				mkstmt.setString(2, book.getId());
				mkstmt.setString(3, tag.getName());
				System.out.println(mkstmt);
				mkstmt.executeUpdate();
				
				//insert values into remove statement
				rmstmt.setString(index++, book.getId());
				rmstmt.setString(index++, tag.getName());	
			}
		}
		//The constructed rmstmt is good only if least one tag exists
		if (size > 0){
			System.out.println(rmstmt);
			rmstmt.executeUpdate();
		}
		else{
			rmstmt = conn.prepareStatement("DELETE FROM book_tag WHERE account_name = ?;");
			rmstmt.setString(1, u.getName());
			System.out.println(rmstmt);
			rmstmt.executeUpdate();
		}
		return true;
	}
	
	
}
