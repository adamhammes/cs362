package ui;

import java.util.List;

import interfaces.BookInterface;
import models.EbookSystem;

public class Controller implements iController{

	EbookSystem sys = new EbookSystem();
	
	@Override
	public String command(String command) {
		String method = command.split(" ")[0].toLowerCase();
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
		String[] args = input.split(" ");
		if (args.length < 4)
			return "insufficient number of arguments";
		
		String bid = args[1];
		String tag = args[2];
		String uid = args[3];
				
		if (sys.addTag(uid, bid, tag))
			return "";
		else
			return "Unable to add tag";
	}

	@Override
	public String removeTag(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBooksWithTag(String input) {
				
		String[] args = input.split(" ");
		if (args.length < 3) {
			return "insufficient number of arguments";
		}
		String tag = args[1];
		String uid = args[2];
		
		
		List<BookInterface> books = sys.getBooksWithTag(uid, tag);
		if (books != null) {
			StringBuffer responce = new StringBuffer();
			
			for (BookInterface book : books) {
				responce.append(book.toString()).append("\n");
			}
			return responce.toString();
		}
		else {
			return "unable to complete operation";
		}
	}

	@Override
	public String displayAllBooks(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addRating(String input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String displayAllBooksByRating(String input) {
		// TODO Auto-generated method stub
		return null;
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
