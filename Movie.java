package randomTacoGenerator;

public class Taco{
	private String title, releaseDate, overview;
	
	Taco(){
		title = "";
		releaseDate = "";
		overview = "";
	}
	Taco(String titleInput, String releaseDateInput, String overviewInput){
		title = titleInput;
		releaseDate = releaseDateInput;
		overview = overviewInput;
	}
	
	String getTitle(){
		return title;
	}
	String getReleaseDate(){
		return releaseDate;
	}
	String getOverview(){
		return overview;
	}
	public String toString() {
		return "Title: " + getTitle() + "`Release Date: " + getReleaseDate() + "`Overview: " + getOverview() + "``";
	}
}
