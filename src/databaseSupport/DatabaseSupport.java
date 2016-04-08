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

	
	private Connection openConnection() throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		return DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
	}

	public UserInterface getUser(String uid) {
		UserInterface user = null;
		Connection conn = null;
		
		try {
			conn = openConnection();
			
			user = GetUser.getUser(conn, uid);
		} catch (SQLException | ClassNotFoundException e) {
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
			
			book = GetBook.getBook(conn, bid,  username);
		} catch (SQLException | ClassNotFoundException e) {
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
