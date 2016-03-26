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
				 Socket clientSocket = new Socket(localAddress, 6000);
				 BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

				 System.out.println("Connected to server");
				 Scanner scanner = new Scanner(System.in);

				 while (true) {
					 System.out.print("Enter movie year and number of movies:  ");
					 String inputLine = scanner.nextLine();
						if ("quit".equalsIgnoreCase(inputLine)) {
							break;
						}
						out.println(inputLine);
						String response = br.readLine();	//Loop until NULL encountered, response +=
						System.out.println("Server response: " + response);
				}

			 } catch (IOException ex) {
				 System.out.println(ex.getMessage());
			 }
		 } catch (IOException ex) {
			 System.out.println(ex.getMessage());
		 }
	 }
}
