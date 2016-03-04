package models;

import interfaces.ReviewInterface;

public class Review implements ReviewInterface {
	private int id;
	private int rating;
	private String review;
	
	public Review(int id, int rating, String review){
		this.id = id;
		this.rating = rating;
		this.review = review;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getReview() {
		return review;
	}

	@Override
	public int getRating() {
		return rating;
	}
	
	
}
