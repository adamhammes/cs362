

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import databaseSupport.DatabaseSupport;
import interfaces.ReviewInterface;
import models.EbookSystem;

public class RatingAndReviewTest {

	private DatabaseSupport db;
	
	@Before
	public void setup(){
		db = new DatabaseSupport();
		db.reset();
	}
	
	
	@Test
	public void RemoveReview(){
		EbookSystem sys = new EbookSystem();
		boolean result = sys.removeReview("mobydick", 1);
		
		assertTrue(result);
		
		Collection<ReviewInterface> reviews= sys.getReviews("mobydick");
		assertNotNull(reviews);
		for (ReviewInterface review : reviews){		
			assertNotEquals(review.getId(), 1);
		}
	}
	
	
	@Test
	public void UpdateReview(){
		EbookSystem sys = new EbookSystem();
		boolean result = sys.updateReview("mobydick", 1, 5, "Not as boaring as I thought");
		
		assertTrue(result);
		
		Collection<ReviewInterface> reviews= sys.getReviews("mobydick");
		assertNotNull(reviews);
		for (ReviewInterface review : reviews){
			if (review.getId() == 1){
				assertEquals(review.getRating(), 5);
				assertEquals(review.getReview(), "Not as boaring as I thought");
				return;
			}
		}
		
		fail();
	}
	
	
	@Test
	public void getReiveiws(){
		EbookSystem sys = new EbookSystem();
		Collection<ReviewInterface> reviews= sys.getReviews("mobydick");
		assertNotNull(reviews);
		
		//Handdled by Controller?
		for (ReviewInterface review : reviews){
			System.out.println(review.getRating() + "\t" + review.getReview());
		}
	}
}
