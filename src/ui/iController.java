package ui;

import java.util.List;

import interfaces.BookInterface;
import interfaces.ReviewInterface;

public interface iController {

	
	public String command(String command);
	
	// Adam Iteration 1
	public String addUser(String input);
	public String addBook(String input);
	public String addVersion(String input);
	
	// Nick Iteration 1
	public String addTag(String input);
	public String removeTag(String input);
	public String getBooksWithTag(String input);

	// Steve Iteration 1
	public String displayAllBooks(String input);
	public String addRating(String input);
	public String displayAllBooksByRating(String input);
	
	// Adam Iteration 2
	public String searchBySeries(String input);
	public String addBookToSeries(String input);
	public String removeBookFromSeries(String input);
	
	
	// Nick Iteration 2
	public String changeRating(String input);
	public String removeRating(String input);
	public String getReviews(String input);
	
	// Steve Iteration 2
	public String addDescription(String input);
	public String editDescription(String input);
	public String removeDescription(String input);
	public String retrieveDescription(String input);
	
	// Adam Iteration 3
	public String deleteUser(String input);
	public String deleteBook(String input);
	public String deleteVersion(String input);
	
	//Steven Iteration 3
	public String addAuthorDescription(String input);
	public String editAuthorDescription(String input);
	public String removeAuthorDescription(String input);
	public String retrieveAuthorDescription(String input);
	
}
