package pa7;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MovieClient {
    
	 public static void main(String[] args) {
	 
		 System.out.println("Movie Client");
		 
		 try {
			 System.out.println("Waiting for connection.....");
			 InetAddress localAddress = InetAddress.getLocalHost();
			 
			 try {
				 Socket clientSocket = new Socket(localAddress, 6666);
				 BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				 
				 System.out.println("Connected to server");
				 System.out.print("Enter movie year and number of movies:  ");
				
				 Scanner scanner = new Scanner(System.in);
				 String inputLine = scanner.nextLine();
				 out.println(inputLine);
				 System.out.println("Server response: \n");
				 String response = br.readLine();
		
				 String[] resultStrs = response.split(",");
				
				 for (String s : resultStrs) {
					 System.out.println(s);
				 }
				 
			 } catch (IOException ex) {
				 System.out.println(ex.getMessage());
			 }
		 } catch (IOException ex) {
			 System.out.println(ex.getMessage());
		 }
	 }
}