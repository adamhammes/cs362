package databaseSupport;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import models.*;
import interfaces.*;

public class DatabaseSupport implements DatabaseSupportInterface {
	Connection connection;

	private static final String USERNAME = "postgres";
	private static final String PASSWORD = "a"; // no password needed
	private static final String CONN_STRING = "jdbc:postgresql://localhost:5432/System";

	
	public UserInterface getUser(String uid) {
		User user = null;
		Connection conn = null;
		
		try {
			conn = openConnection();
			
			//Get User
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account WHERE account_name = ?;");
			stmt.setString(1, uid);
			ResultSet results = stmt.executeQuery();
			
			if (results.next()) {
				user = new User(results.getString("account_name"));
			} else {
				return null;
			}
			
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
	public List<BookInterface> getBooksWithTag(String uid, String tid) {
		UserInterface user = getUser(uid);
		if (null == user) {
			return null;
		}
		
		return new ArrayList<>(user.getBooksWithTag(tid));
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
	public BookInterface getBook(String bid){return getBook(bid, null);}
	
	public BookInterface getBook(String bid, String username) {
		Connection conn = null;
		BookInterface book = null;
		try {
			conn = openConnection();
			
			//get book
			PreparedStatement stmt = makeBookStatement(bid, conn);
			ResultSet results = stmt.executeQuery();
			if (results.next())
				book = new Book(results.getString("book_id"), results.getString("title"), results.getString("description"));
			else
				return null;
			
			// get ratings
			stmt = makeRatingStatement(bid, conn);
			System.out.println(stmt);
			results = stmt.executeQuery();
			
			
			while(results.next()){
				ReviewInterface toAdd = new Review(results.getInt(2), results.getInt(3), results.getString(4));
				book.addReview(toAdd);
			}
			
			//Get Authors
			stmt = conn.prepareStatement("SELECT author.author_id, author_name FROM author JOIN book_author "
					+ "ON author.author_id = book_author.author_id WHERE book_id = ?;");
			stmt.setString(1, book.getId());
			results = stmt.executeQuery();
			
			while(results.next()){
				Author author = new Author(results.getString("author_id"), results.getString("author_name"));
				book.addAuthor(author);
			}
			
			//Get Versions
			if (username != null){
				stmt = conn.prepareStatement("SELECT format, location FROM book_version WHERE book_id = ? AND account_name = ?;");
				stmt.setString(1, book.getId());
				stmt.setString(2, username);
				results = stmt.executeQuery();
				
				while(results.next()){
//					Version ver = new Version(results.getString("format"), results.getString("location"));
					book.addVersion(results.getString("location"), results.getString("format"));
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			try {
				conn.close();
			} catch (Exception e2) {
				// do nothing
			}
		}
		return book;
	}

	private PreparedStatement makeRatingStatement(String bid, Connection conn) throws SQLException {
		PreparedStatement stmt;
		stmt = conn.prepareStatement(
				"SELECT *"
		      + "FROM book_review WHERE book_id = ?;");
		stmt.setString(1, bid);
		return stmt;
	}

	private PreparedStatement makeBookStatement(String bid, Connection conn) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Book WHERE book_id = ?;");
		stmt.setString(1, bid);
		return stmt;
	}

	@Override
	public boolean putBook(BookInterface book){ return putBook(book, null); }
	
	public boolean putBook(BookInterface book, String username) {
		
		Connection conn = null;
		
		try {
			conn = openConnection();
			PutBook.putBook(conn, book, username);
		}
		catch(SQLException | ClassNotFoundException e) {
			return false;
		}
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				//Nothing here?
			}
		}
		return true;
	}
	
	
	@Override
	public boolean putUser(UserInterface user) {
		
		Connection conn = null;
		
		try {
			conn = openConnection();
			PutUser.putUser(conn, user);
		}
		catch(SQLException | ClassNotFoundException e) {
			return false;
		}
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				//Nothing here?
			}
		}
		return true;
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
	
	
	public void reset(){
		Connection conn = null;
		Scanner resetSQL = null;
		try{
			conn = openConnection();
			resetSQL = new Scanner(new File("database/reset.sql"));
			resetSQL.useDelimiter(";");
			Statement stmt = conn.createStatement();
			
			while(resetSQL.hasNext()){
				stmt.execute(resetSQL.next());
			}
			
		}
		catch(Exception e){
			System.err.println("Failed to reset database");
			e.printStackTrace();
		}
		finally{
			try{
				if (conn != null) conn.close();
				if (resetSQL != null) resetSQL.close();
			}
			catch(Exception e){
				System.err.println("Failed to close resorces");
			}
		}
	}
}
