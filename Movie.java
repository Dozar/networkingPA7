package pa7;

public class Movie {
	private String title, releaseDate, overview;

    Movie(){
        title = "";
        releaseDate = "";
        overview = "";
    }
    Movie(String titleInput, String releaseDateInput, String overviewInput){
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
        return "Title: " + getTitle() + "`Release Date: " + getReleaseDate() + "`Overview: " + getOverview() + "`";
    }
}
