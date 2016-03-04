package models;

import java.util.ArrayList;
import java.util.Collection;

import interfaces.BookInterface;
import interfaces.ReviewInterface;
import interfaces.TagInterface;
import interfaces.VersionInterface;

public class Book implements BookInterface, Comparable<BookInterface> {
    private String id;
    private String title;

    private ArrayList<ReviewInterface> reviews = new ArrayList<>();
    private ArrayList<TagInterface> tags = new ArrayList<TagInterface>();
    private ArrayList<VersionInterface> versions = new ArrayList<VersionInterface>();

    public Book(String id, String title) {
        this.id = id;
        this.title = title;
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
	public Collection<VersionInterface> getVersion() {
		return versions;
	}
	
}
