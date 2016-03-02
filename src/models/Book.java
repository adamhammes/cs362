package models;

public class Book {
    private String id;
    private String title;
    private Review review;

    public Book(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

	public void addTag(Tag tag) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean addRating(int rating){
		
		this.review = new Review(rating);
		
		return true;
		
	}
}
