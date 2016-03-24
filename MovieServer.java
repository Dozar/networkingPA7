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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieServer {
  public static void main(String[] args) {
    System.out.println("Movie Server ...");
    Socket sSocket = null;
    try {
      ServerSocket serverSocket = new ServerSocket(6000);
      System.out.println("Waiting for connection.....");
      sSocket = serverSocket.accept();
      System.out.println("Connected to client");
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(sSocket.getInputStream()));
      PrintWriter out = new PrintWriter(sSocket.getOutputStream(), true);

      String inputLine;
      String outputLine = "";
      int year = 0;
      int numMovies = 0;

      while ((inputLine = br.readLine()) != null) {
        System.out.println("Client request: " + inputLine);
        String[] request = inputLine.split(",");

        year = request[0]
        numMovies = request[1];

        String movieJsonStr = fetchData(year);
        String[] resultStrs = new String[numMovies];
        resultStrs = parseData(movieJsonStr, numMovies);
        for (String s : resultStrs) {
          outputLine += s + ",";
        }
        System.out.println(outputLine);
        //testing purpose
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
  //Contain the raw JSON response from MovieDatabase API
  try {
    // Construct a URL for the MovieDatabase query
    String sUrl = "http://api.themoviedb.org/3/discover/movie?primary_release_year=" + year +
    "&sort_by=vote_average.desc& api_key=78d7b7955fd40b3e2db8a133e18459a2";
    URL url = new URL(sUrl);
    //Setup connection to MovieDatabase
    conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.connect();
    InputStream inputStream = conn.getInputStream();
    // Read the input stream
    //Place input stream into a buffered reader
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
    //Create forecast data from buffer
    if (reader != null) {
      try {
        reader.close();
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
    System.out.println(forecastJsonStr);
  }
  return movieJsonStr;
  }

public static String[] parseData(String movieJsonStr, int numMovies) {
  String[] resultStrs = new String[numMovies];
  try {
    JSONObject movieJson = new JSONObject(movieJsonStr);
    JSONArray movieArray = movieJson.getJSONArray("list");
    // OpenWeatherMAP returns daily forecasts based upon the local time of the city that is
    // being asked for, which means that we need to know the GMT offset to translate this
    // data properly.The final format: "Day, description, hi/low"
    for (int i = 0; i < movieArray.length(); i++) {
      String description;
      String highAndLow;
      //create a Gregorian Calendar, which is in current date
      GregorianCalendar gc = new GregorianCalendar();
      //add ith day to current date of calendar
      gc.add(GregorianCalendar.DATE, i);
      //get that date, format it, and "save" it on variable day
      Date time = gc.getTime();
      //The format of day that we want: Wed Jul 01
      SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
      String day = shortenedDateFormat.format(time);
      // Get the JSON object representing the day
      JSONObject dayForecast = (JSONObject) weatherArray.get(i);
      // description is in a child array called "weather", which is 1 element long.
      JSONObject weatherObject = dayForecast.getJSONArray("weather").getJSONObject(0);
      description = weatherObject.getString("main");
      // Temperatures are in a child object called "temp".
      JSONObject temperatureObject = dayForecast.getJSONObject("temp");
      double high = temperatureObject.getDouble("max");
      double low = temperatureObject.getDouble("min");
      highAndLow = Math.round(high) + "/" + Math.round(low);
      resultStrs[i] = day + " - " + description + " - " + highAndLow;
    }
  } catch (JSONException e) {
    System.out.println(e.getMessage());
  }
  return resultStrs;
  }
}
