package databaseSupport;

import java.sql.*;
import java.util.Collections;
import java.util.List;

import models.*;
import interfaces.*;

public class DatabaseSupport implements DatabaseSupportInterface {
	Connection connection;

	private static final String USERNAME = "Nicholas";
	private static final String PASSWORD = ""; // no password needed
	private static final String CONN_STRING = "jdbc:postgresql://localhost:5432/System";

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		User user = new User("Anthony");
		DatabaseSupport db = new DatabaseSupport();
		db.putUser(user);
		
		Class.forName("org.postgresql.Driver");

		try (Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);

				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet results = stmt.executeQuery("SELECT * FROM Account");) {

			System.out.println("Connected!");

			while (results.next()) {

				System.out.println(results.getString("account_name"));
			}

		} catch (SQLException e) {
			System.err.println(e);
		}
	}
	
	public UserInterface getUser(String uid) {
		User user = null;
		Connection conn = null;
		
		try {
			conn = openConnection();
			
			//Get User
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account WHERE account_name = ?;");
			stmt.setString(1, uid);
			ResultSet results = stmt.executeQuery();
			
			results.next();
			user = new User(results.getString("account_name"));
			
			//Get Books
			stmt = conn.prepareStatement("SELECT book.book_id, book.title FROM account_book join book on account_book.book_id=book.book_id where account_book.account_name=?;");
			stmt.setString(1, uid);
			results = stmt.executeQuery();
			
			while(results.next()){
				Book toAdd = new Book(results.getString("book_id"), results.getString("title"));
				user.userBooks.put(toAdd.getTitle(), toAdd);
			}
			
			//Get Tags
			stmt = conn.prepareStatement("SELECT Book_tag.book_id, tag, title FROM Book_tag inner join book on Book_tag.book_id=book.book_id WHERE account_name = ?;");
			stmt.setString(1, uid);
			results = stmt.executeQuery();
			
			while(results.next()){
				TagInterface tag = user.userTags.get(results.getString("tag"));
				if (tag == null){
					tag = new Tag(results.getString("tag"));
					user.userTags.put(tag.getName(), tag);
				}
				BookInterface book = user.userBooks.get(results.getString("title"));
				tag.addBook(book);
				book.addTag(tag);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally{
			try {
				conn.close();
			} catch (Exception e2) {
				// do nothing
			}
		}
		
		return user;
	}
	
	public boolean putUser(UserInterface u) {
		Connection conn = null;

		try {
			conn = openConnection();
			
			//put user
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO account (account_name) VALUES (?) "
															+ "ON CONFLICT (account_name) DO NOTHING;");
			stmt.setString(1, u.getName());
			stmt.executeUpdate();
			
			//put all of the users books
			for (BookInterface book: u.getAllBooks()){
				stmt = conn.prepareStatement("INSERT INTO account_book (account_name, book_id) VALUES (?, ?) "
												+ "ON CONFLICT (account_name, book_id) DO NOTHING;");
				stmt.setString(1, u.getName());
				stmt.setString(2, book.getId());
				stmt.executeUpdate();
			}
			
			putTags(conn, u);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			try {
				conn.close();
			} catch (Exception e2) {
				// do nothing
			}
		}
		return true;
	}
	

	private boolean putTags(Connection conn, UserInterface u) throws SQLException{
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
	
	public Connection openConnection() throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		return DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
	}

	@Override
	public boolean addVersion(String uid, String bid, String path, String type) {
		UserInterface user = getUser(uid);
		if (null == user){
			return false;
		}
		
		return user.addVersion(bid, path, type);
	}

	@Override
	public List<String> getBooksWithTag(String uid, String tid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BookInterface> getAllBooks(String uid) {
		UserInterface user = getUser(uid);
		if (null == user) {
			return null;
		}
		return user.getAllBooks();
	}

	@Override
	public List<BookInterface> getBooksByRating(String uid) {
		UserInterface user = getUser(uid);
		if (null == user) {
			return null;
		}
		List<BookInterface> books = user.getAllBooks();
		Collections.sort(books);
		Collections.reverse(books);
		return books;
	}

	@Override
	public BookInterface getBook(String bid) {
		return null;
	}

	@Override
	public boolean putBook(BookInterface book) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeTag(String uid, String bookTitle, String tag) {
		UserInterface user = getUser(uid);
		if (null == user){
			return false;
		}
		
		boolean toReturn = user.removeTag(bookTitle, tag);
		return toReturn && putUser(user);
	}
}
