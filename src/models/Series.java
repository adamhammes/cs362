package models;

import java.util.ArrayList;

import interfaces.BookInterface;
import interfaces.SeriesInterface;

public class Series implements SeriesInterface{

	private String seriesIdentifier;
	private String seriesName;
	private ArrayList<BookInterface> books;
	
	public Series(String id, String name) {
		seriesIdentifier = id;
		seriesName = name;
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
