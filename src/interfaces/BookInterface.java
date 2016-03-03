package interfaces;

import java.util.ArrayList;


public interface BookInterface extends Comparable<BookInterface> {
	public String getTitle();
	public String getId();
	public boolean addRating(int rating);
	public boolean addTag(TagInterface t);
	public ArrayList<TagInterface> getTags();
	public Double averageRating();
	public boolean addVersion(String path, String type);
	public boolean removeTag(TagInterface t);
}
