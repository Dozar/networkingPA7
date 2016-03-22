package pa7;
import java.net.InetAddress;
import java.io.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.net.ServerSocket;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MovieServer{
    
    public static void main(String[] args) 
    {
        System.out.println("Server Name:");
        
        Socket sSocket = null;
        ArrayList<Movie> movies = new ArrayList<Movie>();
        boolean websiteFound = false; 
  
        FileInputStream fin = null;
        
        try
        {
            fin = new FileInputStream("dns.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fin));
            String lineString = "";
            String[] tokens = lineString.split(", ");
            
            while ((lineString = br.readLine()) != null) //Read file  
            {
              tokens = lineString.split(", ");
              movies.add(new Movie(tokens[0], tokens[1]));
            }
        }
        catch (IOException e) 
        {
         e.printStackTrace();
        }
        
        try 
        {    
         ServerSocket serverSocket = new ServerSocket(8000);
         System.out.println("Awaiting Connection");
         sSocket = serverSocket.accept();
         System.out.println("Connected to Client");
         serverSocket.close();
        } 
        catch (IOException ex) 
        {
         System.out.println(ex.getMessage());
        }
  
        try 
        {    
            BufferedReader br = new BufferedReader(new InputStreamReader(sSocket.getInputStream()));
            PrintWriter out = new PrintWriter(sSocket.getOutputStream(), true);
            String input;

            while ((input = br.readLine()) != null) 
            {
                System.out.println("Client request: " + input);

                for (Movie site: movies)
                {
                       if (site.getDomain().equals(input)) 
                       {
                         out.println(site.getIp());
                         websiteFound = true;
                       }
                 }
                if (!websiteFound)
                {
                    InetAddress newSite = InetAddress.getByName(input);
                    out.println(newSite.getHostAddress());
                    movies.add(new Movie(input, newSite.getHostAddress()));

                    FileOutputStream fout = null;
                    PrintStream printOut;
                       
                        try
                        {
                         fout = new FileOutputStream("dns.txt");
                         printOut = new PrintStream(fout);
                         for (Movie site: movies) 
                        {
                             printOut.println(site.getDomain() + ", " + site.getIp());
                        }
                    } 
                    catch (Exception e)
                    {
                        System.err.println ("Error writing to file");
                    }
                }
                websiteFound = false; 
            }
        }
        catch (IOException ex)
        {
         System.out.println(ex.getMessage());
        } 
    }  
}
