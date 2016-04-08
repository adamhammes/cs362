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
				putBook(book);
				
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
		

		try{
			conn = openConnection();
			
			//Book id and title information
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO book VALUES (?, ?, ?) ON CONFLICT (book_id) DO UPDATE SET title = ?, description = ?;");
			stmt.setString(1, book.getId());
			stmt.setString(2, book.getTitle());
			stmt.setString(3, book.getDescription());
			
			stmt.setString(4, book.getTitle());
			stmt.setString(5, book.getDescription());
			System.out.println(stmt);
			stmt.executeUpdate();
			
			//Version Information
			if (username != null)
				putVersions(conn, book, username);
			
			//Author Information
			putAuthors(conn, book);
			
			//Review Information
			putReviews(conn, book);			
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
		finally{
			try{
				conn.close();
			}
			catch(Exception e2){
				//do nothing
			}
		}
		return true;
	}

	
	
	private void putVersions(Connection conn, BookInterface book, String username) throws SQLException{

		PreparedStatement stmt = conn.prepareStatement("INSERT INTO book_version (book_id, account_name, format, location)"
										+ "VALUES (?, ?, ?, ?) ON CONFLICT (book_id, account_name, format) DO NOTHING");
		
		for (VersionInterface version: book.getVersions()) {
			stmt.setString(1, book.getId());
			stmt.setString(2, username);
			stmt.setString(3, version.getType());
			stmt.setString(4, version.getPath());
			stmt.executeUpdate();
		}
	}
	
	
	
	private void putAuthors(Connection conn, BookInterface book) throws SQLException {
		
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO author VALUES (?, ?) ON CONFLICT (author_id) DO UPDATE SET author_name = ?;");
		PreparedStatement joinstmt = conn.prepareStatement("INSERT INTO book_author VALUES (?, ?) ON CONFLICT (book_id, author_id) DO NOTHING;");
		joinstmt.setString(1, book.getId());
		
		StringBuffer rmSQL = new StringBuffer("DELETE FROM book_author WHERE (book_id, author_id) NOT IN "
				+ "(SELECT * FROM book_author WHERE book_id != ? OR (");
		for (int i = 0; i < book.getAuthors().size() - 1; i++)
			rmSQL.append("(author_id = ?) OR");
		rmSQL.append("(author_id = ?)));");
		PreparedStatement rmstmt = conn.prepareStatement(rmSQL.toString());
		rmstmt.setString(1, book.getId());
		
		int index = 2;
		for (AuthorInterface auth : book.getAuthors()){
			//Create/update Author Entry
			stmt.setString(1, auth.getId());
			stmt.setString(2, auth.getName());
			stmt.setString(3, auth.getName());
			System.out.println(stmt);
			stmt.executeUpdate();
			
			//Create/update Joining table
			joinstmt.setString(2, auth.getId());
			System.out.println(joinstmt);
			joinstmt.executeUpdate();
			
			//Add to remove statement
			rmstmt.setString(index++, auth.getId());
		}
		if (book.getAuthors().size() > 0){
			rmstmt.executeUpdate();
		}
		else{
			rmstmt = conn.prepareStatement("DELETE FROM book_author WHERE book_id =?");
			rmstmt.setString(1, book.getId());
			rmstmt.executeUpdate();
		}
	}
	
	
	
	private void putReviews(Connection conn, BookInterface book) throws SQLException {
		
		PreparedStatement insertstmt = conn.prepareStatement("INSERT INTO book_review (book_id, rating, review) VALUES (?, ?, ?);", new String[]{"review_id"});
		insertstmt.setString(1, book.getId());
		PreparedStatement updatestmt = conn.prepareStatement("UPDATE book_review SET book_id=?, rating=?, review=? WHERE review_id=?;");
		updatestmt.setString(1, book.getId());
		
		//Setup for remove
		StringBuffer rmSQL = new StringBuffer("DELETE FROM book_review WHERE (review_id) NOT IN "
				+ "(SELECT review_id FROM book_review WHERE book_id != ? OR (");
		
		for (int i = 0; i < book.getReviews().size() - 1; i++)
			rmSQL.append("(review_id = ?) OR");
		rmSQL.append("(review_id = ?)));");
		PreparedStatement rmstmt = conn.prepareStatement(rmSQL.toString());
		rmstmt.setString(1, book.getId());
		
		int index = 2;
		for (ReviewInterface rev : book.getReviews()){
			if (rev.getId() == -1){
				insertstmt.setInt(2, rev.getRating());
				insertstmt.setString(3, rev.getReview());
				insertstmt.executeUpdate();
				
				//Retrieve generated key from query so that the -1 doesn't conflict with the remove statement
				insertstmt.getGeneratedKeys().next();
				rev.setId(insertstmt.getGeneratedKeys().getInt("review_id"));
			}
			else{
				updatestmt.setInt(2, rev.getRating());
				updatestmt.setString(3, rev.getReview());
				updatestmt.setInt(4, rev.getId());
				System.out.println(updatestmt);
				updatestmt.executeUpdate();
			}
			rmstmt.setInt(index++, rev.getId());
		}
		System.out.println(rmstmt.toString());
		if (book.getReviews().size() > 0) {
			System.out.println(rmstmt);
			rmstmt.executeUpdate();
		}
		else{
			rmstmt = conn.prepareStatement("DELETE FROM book_review WHERE book_id = ?;");
			rmstmt.setString(1, book.getId());
			rmstmt.executeUpdate();
		}			
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
