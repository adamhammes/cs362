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
		
		if (books.contains(book)){
			return true;
		}
		else{
			books.add(book);
			book.addSeries(this);
			return true;
		}
	}
	
	
	@Override
	public boolean removeBook(String bookid){
		
		for (int i = 0; i < books.size(); i++){
			if (books.get(i).getId().equals(bookid)){
				
				BookInterface temp = books.remove(i);
				temp.removeSeries();
				
				return true;
			}
		}
		return false;
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
