package models;

import interfaces.ReviewInterface;

public class Review implements ReviewInterface {
	
	private int id = -1;
	private int rating;
	private String review;
	
	public Review(int rating){
		this.rating = rating;
	}

	@Override
	public int getRating() {
		return rating;
	}

	@Override
	public String getReview() {
		return review;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public void setReview(String review) {
		this.review = review;
	}

	@Override
	public void setId() {
		this.id = id;
	}
	
	
}
