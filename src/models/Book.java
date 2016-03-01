package models;

import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import interfaces.BookInterface;
import interfaces.TagInterface;

public class Book implements BookInterface {
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
}
