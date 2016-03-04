package models;

import java.util.ArrayList;

import interfaces.AuthorInterface;
import interfaces.BookInterface;
import interfaces.ReviewInterface;
import interfaces.TagInterface;
import interfaces.VersionInterface;

public class Book implements BookInterface, Comparable<BookInterface> {
    private String id;
    private String title;
    private ReviewInterface review;
    private ArrayList<Integer> ratings = new ArrayList<Integer>();
    private ArrayList<TagInterface> tags = new ArrayList<TagInterface>();
    private ArrayList<VersionInterface> versions = new ArrayList<VersionInterface>();
    private ArrayList<AuthorInterface> authors = new ArrayList<AuthorInterface>();

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
	public boolean addRating(int rating) {
		return ratings.add(rating);
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
		for (Integer i: ratings) {
			total += i;
		}
		double average = total/ratings.size();
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
	public ArrayList<VersionInterface> getVersions() {
		return versions;
	}

	@Override
	public boolean addAuthor(AuthorInterface author){
		return authors.add(author);
	}
	
	@Override
	public ArrayList<AuthorInterface> getAuthors() {
		return authors;
	}
	
}
