package ui;

import java.util.List;

import interfaces.BookInterface;
import models.EbookSystem;

public class Controller implements iController{

	EbookSystem system = new EbookSystem();
	
	@Override
	public String command(String command) {
		String method = command.split("-")[0].toLowerCase();
		switch (method) {
			case "adduser":
				return addUser(command);
			
			case "addbook":
				return addBook(command);
				
			case "addversion":
				return addVersion(command);
				
			case "addtag":
				return addTag(command);
				
			case "removetag":
				return removeTag(command);
				
			case "getbookswithtag":
				return getBooksWithTag(command);
				
			case "displayallbooks":
				return displayAllBooks(command);
				
			case "addrating":
				return addRating(command);
				
			case "displayallbooksbyrating":
				return displayAllBooksByRating(command);
				
			case "searchbyseries":
				return searchBySeries(command);
				
			case "addbooktoseries":
				return addBookToSeries(command);
				
			case "removebookfromseries":
				return removeBookFromSeries(command);
				
			case "changerating":
				return changeRating(command);
				
			case "removerating":
				return removeRating(command);
				
			case "getreviews":
				return getReviews(command);
				
			case "adddescription":
				return addDescription(command);
				
			case "editdescription":
				return editDescription(command);
				
			case "removedescription":
				return removeDescription(command);
				
			case "retrievedescription":
				return retrieveDescription(command);
						
			default:
				return "Invalid Command. Please try again";
		}
	}

	@Override
	public String addUser(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addBook(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addVersion(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addTag(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String removeTag(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBooksWithTag(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String displayAllBooks(String input) {
		if(input.split("-").length != 2)
			return "Please enter all required inputs";
		String uid = input.split("-")[1];
		List<BookInterface> l = system.displayAllBooks(uid);
		if(l == null) return "Action not completed. Please try again";
		String result = "";
		for(BookInterface b : l){
			result += b.getTitle() + "\n";
		}
		return result;
	}

	@Override
	public String addRating(String input) {
		if(input.split("-").length != 5)
			return "Please enter all required inputs";
		String uid = input.split("-")[1];
		String bid = input.split("-")[2];
		int rating = Integer.parseInt(input.split("-")[3]);
		String review = input.split("-")[4];
		boolean b = system.addRating(uid, bid, rating, review);
		if(b) return "Rating added";
		else return "Action not completed. Please try again";
	}

	@Override
	public String displayAllBooksByRating(String input) {
		if(input.split("-").length != 2)
			return "Please enter all required inputs";
		String uid = input.split("-")[1];
		List<BookInterface> l = system.displayAllBooksByRating(uid);
		if(l == null) return "Action not completed. Please try again";
		String result = "";
		for(BookInterface b : l){
			result += b.getTitle() + "\n";
		}
		return result;
	}

	@Override
	public String searchBySeries(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addBookToSeries(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String removeBookFromSeries(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String changeRating(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String removeRating(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReviews(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addDescription(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String editDescription(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String removeDescription(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String retrieveDescription(String input) {
		// TODO Auto-generated method stub
		return null;
	}
	

	
	
}
