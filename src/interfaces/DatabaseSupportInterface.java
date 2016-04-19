package interfaces;

public interface DatabaseSupportInterface {

	public UserInterface getUser(String uid);
	public boolean putUser(UserInterface u);
	
	public BookInterface getBook(String bid);
	public BookInterface getBook(String bid, String username);
	public boolean putBook(BookInterface book);
	public boolean putBook(BookInterface book, String username);
	
	public SeriesInterface getSeries(String sid);
	public boolean putSeries(SeriesInterface series);
}
