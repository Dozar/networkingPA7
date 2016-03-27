package pa7;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.ParseException;

public class MovieServer {
	public static void main(String[] args) {
	    System.out.println("Movie Server ...");
	    Socket sSocket = null;
	    
	    try {
	      ServerSocket serverSocket = new ServerSocket(9000);
	      System.out.println("Waiting for connection.....");
	      sSocket = serverSocket.accept();
	      System.out.println("Connected to client");
	      serverSocket.close();
	    } catch (IOException ex) {
	      System.out.println(ex.getMessage());
	    }
	    try {
	      BufferedReader br = new BufferedReader(new InputStreamReader(sSocket.getInputStream()));
	      PrintWriter out = new PrintWriter(sSocket.getOutputStream(), true);

	      String inputLine;

	      int year = 0;
	      int numMovies = 0;

	      while ((inputLine = br.readLine()) != null) {
	        System.out.println("Client request: " + inputLine);
	        String[] request = inputLine.split(",");

	        DecimalFormat decimalFormat = new DecimalFormat("#");
	        try {
	            year = decimalFormat.parse(request[0]).intValue();
	        }
	        catch (ParseException e) {
	            System.out.println("NO NUMBAH");
	        }
	        try {
	            numMovies = decimalFormat.parse(request[1]).intValue(); //me rn
	        }
	        catch (ParseException e) {
	            System.out.println("NO NUMBAHHJHAHAHHAIKDSFHASJDHFAJSDHFKAJSDF");
	        }

	        String movieJsonStr = fetchData(year);
	        
	        Movie[] movies = new Movie[numMovies];
	        movies = parseData(movieJsonStr, numMovies);

	        String outputLine = "";
	        for (Movie movie : movies) {
	          outputLine += movie.toString();
	        }
	        out.println(outputLine);
	      }

	    } catch (IOException ex) {
	      System.out.println(ex.getMessage());
	    }
	}

	public static String fetchData(int year) {
	    HttpURLConnection conn = null;
	    BufferedReader reader = null;
	    String movieJsonStr = null;
	    // Contain the raw JSON response from MovieDatabase API
	    try {
	      // Construct a URL for the MovieDatabase query
	      String sUrl = "http://api.themoviedb.org/3/discover/movie?primary_release_year=" + year +
	      "&sort_by=popularity.desc&api_key=78d7b7955fd40b3e2db8a133e18459a2";
	      URL url = new URL(sUrl);
	      
	      // Setup connection to MovieDatabase
	      conn = (HttpURLConnection) url.openConnection();
	      conn.setRequestMethod("GET");
	      conn.connect();
	      
	      InputStream inputStream = conn.getInputStream();
	      // Read the input stream
	      // Place input stream into a buffered reader
	      reader = new BufferedReader(new InputStreamReader(inputStream));
	      String line;
	      StringBuilder buffer = new StringBuilder();
	      while ((line = reader.readLine()) != null) {
	      buffer.append(line).append("\n");
	      }
	      movieJsonStr = buffer.toString();
	    } catch (IOException e) {
	      System.out.println(e.getMessage());
	    } finally {
	      if (conn != null) {
	        conn.disconnect();
	      }
	      if (reader != null) {
	        try {
	          reader.close();
	        } catch (IOException e) {
	          System.out.println(e.getMessage());
	        }
	      }
	      System.out.println(movieJsonStr);
	    }
	    return movieJsonStr;
	}

	public static Movie[] parseData(String movieJsonStr, int numMovies) {
	    Movie[] movies = new Movie[numMovies];
	    try {
	      JSONObject movieJson = new JSONObject(movieJsonStr);
	      JSONArray movieArray = movieJson.getJSONArray("results");

	      for (int i = 0; i < numMovies; i++) {
	        String title, releaseDate, overview;
	        JSONObject movieObject = (JSONObject) movieArray.get(i);

	        title = movieObject.getString("title");
	        releaseDate = movieObject.getString("release_date");
	        overview = movieObject.getString("overview");

	        movies[i] = new Movie(title, releaseDate, overview);
	      }
	    } catch (JSONException e) {
	      System.out.println(e.getMessage());
	    }
	    return movies;
	}
}
