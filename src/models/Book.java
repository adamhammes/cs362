package models;

import java.util.ArrayList;
import java.util.Collection;

import interfaces.AuthorInterface;
import interfaces.BookInterface;
import interfaces.ReviewInterface;
import interfaces.SeriesInterface;
import interfaces.TagInterface;
import interfaces.VersionInterface;

public class Book implements BookInterface, Comparable<BookInterface> {
    private String id;
    private String title;
    private String description;
    
    private ArrayList<TagInterface> tags = new ArrayList<TagInterface>();
    private ArrayList<VersionInterface> versions = new ArrayList<VersionInterface>();
    private ArrayList<AuthorInterface> authors = new ArrayList<AuthorInterface>();
    private ArrayList<ReviewInterface> reviews = new ArrayList<ReviewInterface>();

    private SeriesInterface series = null;
    
    public Book(String id, String title) {
        this.id = id;
        this.title = title;
        this.description = null;
    }

    public Book(String id, String title, String description){
    	this.id = id;
    	this.title = title;
    	this.description = description;
    }
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    
	@Override
	public boolean addTag(TagInterface t) {
		return tags.add(t);
	}

	@Override
	public ArrayList<TagInterface> getTags() {
		return tags;
	}
	


	@Override
	public int compareTo(BookInterface o) {
		return averageRating().compareTo(o.averageRating());
	}

	@Override
	public Double averageRating() {
		double total = 0.;
		for (ReviewInterface r: reviews) {
			total += r.getRating();
		}
		double average = total/reviews.size();
		return new Double(average);
	}
	
	@Override
	public boolean addVersion(String path, String type) {
		return this.versions.add(new Version(path, type));
	}

	@Override
	public boolean removeTag(TagInterface t) {
		return tags.remove(t);
	}

	@Override
	public boolean addReview(ReviewInterface review) {
		return reviews.add(review);
	}

	@Override
	public Collection<ReviewInterface> getReviews() {
		return reviews;
	}

	@Override
	public Collection<VersionInterface> getVersions() {
		return versions;
	}

	@Override
	public boolean addAuthor(AuthorInterface author){
		
		if (!authors.contains(author)){
			authors.add(author);
			author.addBook(this);
		}
		return true;
	}
	
	@Override
	public ArrayList<AuthorInterface> getAuthors() {
		return authors;
	}

	@Override
	public boolean removeReview(int reviewId) {
		for (int i = 0; i < reviews.size(); i++){
			if (reviews.get(i).getId() == reviewId){
				reviews.remove(i);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean updateReview(int reviewId, int newRateing, String newReview) {
		for (ReviewInterface review : reviews){
			if (review.getId() == reviewId){
				review.setRating(newRateing);
				review.setReview(newReview);
				return true;
			}
		}
		return false;
	}

	@Override
	public void addSeries(SeriesInterface series) {
		this.series = series;
		series.addBook(this);
	}
	
	@Override
	public boolean addDescription(String desc){
		this.description = desc; //TODO: check if description exists already??
		return true;
	}
	
	@Override
	public boolean editDescription(String desc){
		this.description = desc;
		return true;
	}
	
	@Override
	public boolean removeDescription(){
		this.description = new String();
		return true;
	}
	
	@Override
	public String retrieveDescription(){
		return this.description;
	}
	
	@Override
	public SeriesInterface getSeries(){
		return series;
	}
	
	@Override
	public void removeSeries(){
		SeriesInterface temp = series;
		series = null;
		
		if (temp != null) temp.removeBook(id);
	}

	
	@Override
	public boolean equals(Object other){
		return id.equals(((Book) other).id);
	}
}
