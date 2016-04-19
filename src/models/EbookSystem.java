package models;

import interfaces.BookInterface;
import interfaces.ReviewInterface;
import interfaces.SeriesInterface;
import interfaces.SystemInterface;
import interfaces.UserInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import databaseSupport.DatabaseSupport;

public class EbookSystem implements SystemInterface {

	public boolean addTag(String uid, String bookTitle, String tag) {
		DatabaseSupport db = new DatabaseSupport();
		UserInterface user = db.getUser(uid);

		if (user != null) {
			boolean result = user.addTag(bookTitle, tag);
			if (result == false) {
				return false;
			}

			return db.putUser(user);
		}
		return false;
	}

	public boolean removeTag(String uid, String bookTitle, String tag) {
		DatabaseSupport db = new DatabaseSupport();
		UserInterface user = db.getUser(uid);

		if (user != null) {
			if (user.removeTag(bookTitle, tag)) // if tag was successfully
												// removed
				return db.putUser(user); // if user was successfully updated
		}
		return false;
	}

	public List<BookInterface> getAllBooks(String uid) {
		DatabaseSupport db = new DatabaseSupport();
		UserInterface user = db.getUser(uid);

		if (user != null)
			return user.getAllBooks();
		else
			return null;

	}

	public boolean removeReview(String bookId, int reviewId) {
		DatabaseSupport db = new DatabaseSupport();
		BookInterface book = db.getBook(bookId);

		if (book == null)
			return false;
		if (book.removeRating(reviewId) == false)
			return false;

		return db.putBook(book);
	}

	public boolean updateReview(String bookId, int reviewId, int newRateing, String newReview) {
		DatabaseSupport db = new DatabaseSupport();
		BookInterface book = db.getBook(bookId);

		if (book == null)
			return false;
		if (book.updateReview(reviewId, newRateing, newReview) == false)
			return false;

		return db.putBook(book);
	}

	public Collection<ReviewInterface> getReviews(String bookId) {
		DatabaseSupport db = new DatabaseSupport();
		BookInterface book = db.getBook(bookId);
		return book.getReviews();
	}

	public boolean addVersion(String uid, String bid, String path, String type) {
		DatabaseSupport db = new DatabaseSupport();

		UserInterface user = db.getUser(uid);
		if (null == user) {
			return false;
		}

		if (!user.addVersion(bid, path, type))
			return false;/// !!!!!! This doesn't commit changes to the db

		return db.putUser(user);
	}

	public List<BookInterface> getBooksWithTag(String uid, String tid) {
		DatabaseSupport db = new DatabaseSupport();

		UserInterface user = db.getUser(uid);
		if (null == user) {
			return null;
		}

		return new ArrayList<>(user.getBooksWithTag(tid));
	}

	public List<BookInterface> getBooksByRating(String uid) {
		DatabaseSupport db = new DatabaseSupport();

		UserInterface user = db.getUser(uid);
		if (null == user) {
			return null;
		}
		List<BookInterface> books = user.getAllBooks();
		Collections.sort(books);
		Collections.reverse(books);
		return books;
	}

	public boolean addDescription(String bid, String desc) {
		DatabaseSupport db = new DatabaseSupport();
		BookInterface b = db.getBook(bid);

		if (b == null)
			return false;

		b.addDescription(desc);
		return (db.putBook(b));

	}

	public boolean editDescription(String bid, String desc) {
		DatabaseSupport db = new DatabaseSupport();
		BookInterface b = db.getBook(bid);

		if (b == null)
			return false;

		b.editDescription(desc);
		return (db.putBook(b));

	}

	public boolean removeDescription(String bid) {
		DatabaseSupport db = new DatabaseSupport();
		BookInterface b = db.getBook(bid);

		if (b == null)
			return false;

		b.removeDescription();
		return (db.putBook(b));

	}

	public String retrieveDescription(String bid) {
		DatabaseSupport db = new DatabaseSupport();
		BookInterface b = db.getBook(bid);

		if (b == null)
			return null;

		return b.retrieveDescription();
	}

	@Override
	public boolean addBook(String uid, String bid, String title) {
		DatabaseSupport db = new DatabaseSupport();
		
		UserInterface user = db.getUser(uid);
		return user.addBook(bid, title);
	}

	@Override
	public boolean addUser(String name) {
		DatabaseSupport db = new DatabaseSupport();
		UserInterface user = new User(name);
		return db.putUser(user);
	}

	@Override
	public List<BookInterface> displayAllBooks(String uid) {
		DatabaseSupport db = new DatabaseSupport();
		UserInterface user = db.getUser(uid);
		return user.getAllBooks();
	}

	@Override
	public List<BookInterface> displayAllBooksByRating(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addRating(String uid, String bid, int rating) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addBookToSeries(String bid, String sid) {
		DatabaseSupport db = new DatabaseSupport();
		
		SeriesInterface series = db.getSeries(sid);
		if (null == series) {
			return false;
		}
		
		BookInterface book = db.getBook(bid);
		return series.addBook(book);
	}

	@Override
	public List<BookInterface> searchBySeries(String sid) {
		DatabaseSupport db = new DatabaseSupport();
		SeriesInterface series = db.getSeries(sid);
		
		return series == null ? null : series.getBooks();
	}

	@Override
	public boolean changeRating(String bid, int reviewId, int newRating, String newReview) {
		DatabaseSupport db = new DatabaseSupport();
		BookInterface book = db.getBook(bid);
		
		return book == null ? false : book.updateReview(reviewId, newRating, newReview);
	}

	@Override
	public boolean removeRating(String bid, int reviewId) {
		DatabaseSupport db = new DatabaseSupport();
		
		BookInterface book = db.getBook(bid);
		
		if (null == book) {
			return false;
		}
		
		if (!book.removeRating(reviewId)) {
			return true;
		}
		return db.putBook(book);
	}
}
