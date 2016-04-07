package interfaces;

import java.util.ArrayList;
import java.util.Collection;


public interface BookInterface extends Comparable<BookInterface> {
	public String getTitle();
	public String getId();
	
	public boolean addReview(ReviewInterface review);
	public Collection<ReviewInterface> getReviews();
	
	public boolean addVersion(String path, String type);
	public Collection<VersionInterface> getVersions();
	
	public boolean addTag(TagInterface t);
	public boolean removeTag(TagInterface t);
	
	public ArrayList<TagInterface> getTags();
	public Double averageRating();

	public boolean addAuthor(AuthorInterface author);
	public ArrayList<AuthorInterface> getAuthors();
	
	public boolean removeReview(int reviewId);
	public boolean updateReview(int reviewId, int newRateing, String newReview);

}
