package models;

import java.util.ArrayList;

import interfaces.BookInterface;
import interfaces.SeriesInterface;

public class Series implements SeriesInterface{

	private String seriesIdentifier;
	private String seriesName;
	private ArrayList<BookInterface> books = new ArrayList<BookInterface>();
	
	public Series(String id, String name) {
		seriesIdentifier = id;
		seriesName = name;
	}

	
	@Override
	public boolean addBook(BookInterface book){
		if (book == null) return false;
		
		books.add(book);
		book.addSeries(this);
		return true;
	}
	
	@Override
	public ArrayList<BookInterface> getBooks(){
		return books;
	}
	
	@Override
	public String getId(){
		return seriesIdentifier;
	}
	
	@Override
	public String getName(){
		return seriesName;
	}
	
	@Override
	public boolean setName(String name){
		if (name == null) 
			return false;
		else {
			seriesName = name;
			return true;
		}
	}
	
}
