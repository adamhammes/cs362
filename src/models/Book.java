package models;

import java.util.ArrayList;

import interfaces.BookInterface;
import interfaces.TagInterface;

public class Book implements BookInterface, Comparable<BookInterface> {
    private String id;
    private String title;
    private ArrayList<Integer> ratings = new ArrayList<Integer>();
    private ArrayList<TagInterface> tags = new ArrayList<TagInterface>();

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
}
