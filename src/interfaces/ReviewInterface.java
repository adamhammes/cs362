package interfaces;

public interface ReviewInterface {

	public int getId();
	
	public String getReview();
	
	public int getRating();
	
	public void setRating(int rating);

	public void setReview(String review);
	
	public void update(int newRating, String newReview);

	public void setId(int id);

}
