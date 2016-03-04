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
	public ArrayList<VersionInterface> getVersions();
	public boolean addAuthor(AuthorInterface author);
	public ArrayList<AuthorInterface> getAuthors();
	public boolean addReview(ReviewInterface rev);
	public ArrayList<ReviewInterface> getReviews();
}
