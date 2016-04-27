package ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import interfaces.BookInterface;

import interfaces.ReviewInterface;

import interfaces.SystemInterface;
import interfaces.VersionInterface;
import models.EbookSystem;

public class Controller implements iController{

	SystemInterface system = new EbookSystem();
	
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
				
			case "deleteuser":
				return deleteUser(command);
				
			case "deletebook":
				return deleteBook(command);
				
			case "deleteversion":
				return deleteVersion(command);
				
			case "addauthordescription":
				return addAuthorDescription(command);
				
			case "editauthordescription":
				return editAuthorDescription(command);
				
			case "removeauthordescription":
				return removeAuthorDescription(command);
				
			case "retrieveauthordescription":
				return retrieveAuthorDescription(command);
					
			case "getbookswithauthor":
				return getBooksWithAuthor(command);
			
			case "listversions":
				return listVersions(command);
				
			case "getversion":
				return getVersion(command);
				
			case "addauthor":
				return addAuthor(command);

			case "addauthortobook":
				return addAuthorToBook(command);
				
			case "createseries":
				return createseries(command);

			default:
				return "Invalid Command. Please try again";
		}
	}


	private String createseries(String command) {
		String[] args = command.split("-");
		
		if (args.length != 3) {
			return "Incorrect number of arguments";
		}
		
		boolean result = system.createSeries(args[1], args[2]);
		
		return result ? "Series " + args[2] + " created" :
						"Operation failed";
	}


	private String addAuthorToBook(String command) {
		String[] args = command.split("-");
		
		if (args.length != 3) {
			return "Incorrect number of arguments";
		}
		
		boolean result = system.addAuthorToBook(args[1], args[2]);
		
		return result ? "Author " + args[1] + " added to book " + args[2] :
						"Operation failed";
	}


	@Override
	public String addUser(String input) {
		String[] args = input.split("-");
		
		if (args.length < 2) {
			return "Insufficient number of arguments";
		}
		
		boolean result = system.addUser(args[1]);
		
		return result ? "User " + args[1] + " added" :
						"Operation failed";
	}
 
	@Override
	public String addBook(String input) {
		String[] args = input.split("-");
		
		if (args.length < 4) {
			return "Insufficient number of arguments";
		}
		
		boolean result = system.addBook(args[1], args[2], args[3]);
		
		return result ? "Book " + args[2] + " added" :
						"Operation failed";
	}

	@Override
	public String addVersion(String input) {
		String[] args = input.split("-");
		
		if (args.length < 5) {
			return "Insufficient number of arguments";
		}
		
		boolean result = system.addVersion(args[1], args[2], args[3], args[4]);
		
		return result ? "Version added" : "Operation failed";
	}

	@Override
	public String addTag(String input) {
		String[] args = input.split("-");
		if (args.length < 4)
			return "insufficient number of arguments";
		
		String bid = args[1];
		String tag = args[2];
		String uid = args[3];
				
		if (system.addTag(uid, bid, tag))
			return "Operation succeeded";
		else
			return "Unable to add tag";
	}

	@Override
	public String removeTag(String input) {
		String[] args = input.split("-");
		if (args.length < 4)
			return "insufficient number of arguments";
		
		String bid = args[1];
		String tag = args[2];
		String uid = args[3];
				
		if (system.removeTag(uid, bid, tag))
			return "Operation succeeded";
		else
			return "Unable to remove tag";
	}

	@Override
	public String getBooksWithTag(String input) {
				
		String[] args = input.split("-");
		if (args.length < 3) {
			return "insufficient number of arguments";
		}
		String tag = args[1];
		String uid = args[2];
		
		
		List<BookInterface> books = system.getBooksWithTag(uid, tag);
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
		if(input.split("-").length != 2)
			return "Please enter all required inputs";
		String uid = input.split("-")[1];
		List<BookInterface> l = system.displayAllBooks(uid);
		if(l == null) return "Action not completed. Please try again";
		ArrayList<String> titles = new ArrayList<>();
		String result = "";
		for(BookInterface b : l){
			if(!titles.contains(b.getTitle())){
				result += b.getTitle() + "\n";
				titles.add(b.getTitle());
			}
			
		}
		return result;
	}

	@Override
	public String addRating(String input) {
		String args[] = input.split("-");
		String bid = args[1];
		String rating = args[2];
		String review = args[3];

		try {
			int value = Integer.parseInt(rating.trim());
			if (system.addRating(bid, value, review)) {

				return "Operation succeeded";
			}
			else {
				return "unable to submit review";
			}
		}
		catch (NumberFormatException e) {
			return "Must provide a number for the second argument";
		}
	}

	@Override
	public String displayAllBooksByRating(String input) {
		if(input.split("-").length != 2)
			return "Please enter all required inputs";
		String uid = input.split("-")[1];
		List<BookInterface> l = system.displayAllBooksByRating(uid);
		if(l == null) return "Action not completed. Please try again";
		ArrayList<String> titles = new ArrayList<>();
		String result = "";
		for(BookInterface b : l){
			if(!titles.contains(b.getTitle())){
				result += b.getTitle() + "\n";
				titles.add(b.getTitle());
			}
		}
		return result;
	}

	@Override
	public String searchBySeries(String input) {
		if(input.split("-").length != 2)
			return "Please enter all required inputs";
		
		String sid = input.split("-")[1];
		List<BookInterface> l = system.searchBySeries(sid);
		if(l == null) { 
			return "Action not completed. Please try again";
		}
		
		String result = "";
		for(BookInterface b : l){
			result += b.getTitle() + "\n";
		}
		return result;
	}

	@Override
	public String addBookToSeries(String input) {
		String[] args = input.split("-");
		
		if (args.length != 3) {
			return "Invalid number of arguments";
		}
		
		boolean result = system.addBookToSeries(args[1], args[2]);
		
		return result ? "Book added to series" :
						"Operation failed";
	}

	@Override
	public String removeBookFromSeries(String input) {
		String[] args = input.split("-");
		
		if (args.length < 3) {
			return "Insufficient number of arguments";
		}
		
		boolean result = system.removeBookFromSeries(args[1], args[2]);
		
		return result ? "Book removed from series" :
						"Operation failed";
	}

	@Override
	public String changeRating(String input) {
		String[] args = input.split("-");
		if (args.length < 5)
			return "invalid number of arguments";
		
		String bid = args[1];
		String reviewId = args[2];
		String newRating = args[3];
		String newReview = args[4];
		
		try {
			system.changeRating(bid, Integer.parseInt(reviewId), Integer.parseInt(newRating), newReview);
			return "";
		}
		catch (NumberFormatException e) {
			return "review Id and newRating must be of type int";
		}
	}

	@Override
	public String removeRating(String input) {
		String args[] = input.split("-");
		if (args.length < 3)
			return "Needs more arguments";
		
		String bookId = args[1];
		String reviewId = args[2];
		
		try {
			if (system.removeRating(bookId, Integer.parseInt(reviewId))){
				return "Operation succeeded";
			}
			else {
				return "unable to remove rateing";
			}
		}
		catch (NumberFormatException e){
			return "invalid review id";
		}
	}

	@Override
	public String getReviews(String input) {
		String[] args = input.split("-");
		if (args.length < 2)
			return "Invalid number of arguments. Please provide book id";
		
		String bid = args[1];
		
		List<ReviewInterface> reviews = system.getReviews(bid);
		if (reviews != null) {
			StringBuffer buff = new StringBuffer();
			for (ReviewInterface review : reviews) {
				buff.append(review.toString()).append("\n");
			}
			return buff.toString();
		}
		else {
			return "Unable to get reviews";
		}
	}

	@Override
	public String addDescription(String input) {
		if(input.split("-").length != 3)
			return "Please enter all required inputs";
		String bid = input.split("-")[1];
		String desc = input.split("-")[2];
		
		boolean b = system.addDescription(bid, desc);

		if(b) return "Description added";
		else return "Action not completed. Please try again";
	}

	@Override
	public String editDescription(String input) {
		if(input.split("-").length != 3)
			return "Please enter all required inputs";
		String bid = input.split("-")[1];
		String desc = input.split("-")[2];
		
		boolean b = system.editDescription(bid, desc);

		if(b) return "Description edited";
		else return "Action not completed. Please try again";
	}

	@Override
	public String removeDescription(String input) {
		if(input.split("-").length != 2)
			return "Please enter all required inputs";
		String bid = input.split("-")[1];
		
		boolean b = system.removeDescription(bid);

		if(b) return "Description deleted";
		else return "Action not completed. Please try again";
	}

	@Override
	public String retrieveDescription(String input) {
		if(input.split("-").length != 2)
			return "Please enter all required inputs";
		String bid = input.split("-")[1];
		
		String result = system.retrieveDescription(bid);

		if(result != null) return result;
		else return "Action not completed. Please try again";
	}

	@Override
	public String deleteUser(String input) {
		String[] args = input.split("-");
		
		if (args.length < 2) {
			return "Insufficient number of arguments";
		}
		
		boolean result = system.deleteUser(args[1]);
		
		return result ? "User \"" + args[1] + "\" deleted" :
						"Operation failed";
	}

	@Override
	public String deleteBook(String input) {
		String[] args = input.split("-");
		
		if (args.length < 2) {
			return "Insufficient number of arguments";
		}
		
		boolean result = system.deleteBook(args[1]);
		
		return result ? "Book \"" + args[1] + "\" deleted" :
						"Operation failed";
	}

	@Override
	public String deleteVersion(String input) {
		String[] args = input.split("-");
		
		if (args.length < 4) {
			return "Insufficient number of arguments";
		}
		
		boolean result = system.deleteVersion(args[1], args[2], args[3]);
		
		return result ? "Version deleted" : "Operation failed";
	}

	@Override
	public String addAuthorDescription(String input) {
		if(input.split("-").length != 3)
			return "Please enter all required inputs";
		String aid = input.split("-")[1];
		String desc = input.split("-")[2];
		
		boolean b = system.addAuthorDescription(aid, desc);

		if(b) return "Author description added";
		else return "Action not completed. Please try again";
	}

	@Override
	public String editAuthorDescription(String input) {
		if(input.split("-").length != 3)
			return "Please enter all required inputs";
		String aid = input.split("-")[1];
		String desc = input.split("-")[2];
		
		boolean b = system.editAuthorDescription(aid, desc);


		if(b) return "Author description edited";
		else return "Action not completed. Please try again";
	}

	@Override
	public String removeAuthorDescription(String input) {
		if(input.split("-").length != 2)
			return "Please enter all required inputs";
		String aid = input.split("-")[1];
		
		boolean b = system.removeAuthorDescription(aid);

		if(b) return "Author description deleted";
		else return "Action not completed. Please try again";
	}

	@Override
	public String retrieveAuthorDescription(String input) {
		if(input.split("-").length != 2)
			return "Please enter all required inputs";
		String aid = input.split("-")[1];
		
		String result = system.retrieveAuthorDescription(aid);

		if(result != null) return result;
		else return "Action not completed. Please try again";
	}
	
	
	public String getBooksWithAuthor(String input) {
		if (input.split("-").length != 2)
			return "Please enter all required inputs";
		String authorId = input.split("-")[1];
		
		Collection<BookInterface> books = system.findBooksByAuthor(authorId, null);
		if (books != null) {
			StringBuffer buff = new StringBuffer();
			for (BookInterface book : books) {
				buff.append(book.toString()).append("\n");
			}
			return buff.toString();
		}
		else {
			return "Unable to execute operation";
		}
	}
	
	public String listVersions(String input) {
		String[] args = input.split("-");
		if (args.length < 3)
			return "Please enter all required inputs";

		String bid = args[1];
		String uid = args[2];
		
		Collection<VersionInterface> versions = system.listAllVersions(bid, uid);
		if (versions != null) {
			StringBuffer buff = new StringBuffer();
			for (VersionInterface version : versions) {
				buff.append(version.toString()).append("\n");
			}
			return buff.toString();
		}
		else {
			return "Unable to execute operation"; 
		}
	}


	@Override
	public String getVersion(String input) {
		String[] args = input.split("-");
		if (args.length < 4)
			return "Please enter all required inputs";
		String bookId = args[1];
		String format = args[2];
		String userId = args[3];
		
		VersionInterface version = system.getVersion(bookId, format, userId);
		if (version != null){
			return version.toString();
		}
		else {
			return "Unable to execute operation";  
		}
	}

	@Override
	public String addAuthor(String input) {
		String[] args = input.split("-");
		if(args.length != 3)
			return "Please enter all required inputs";
		String aid = args[1];
		String name = args[2];
		
		boolean b = system.addAuthor(aid, name);
		if(b) return "Author Added";
		else return "Unable to execute operation";
	}
	
}
