import java.io.*;
import java.net.*;
import java.util.Scanner;

// Client class
public class ListeningClient
{
	public static void main(String[] args) throws IOException
	{
        Scanner scn = null;
        DataInputStream dis = null;
		try
		{
			scn = new Scanner(System.in);
			
			// getting localhost ip
			InetAddress ip = InetAddress.getByName("localhost");
	
			// establish the connection with server port
			Socket s = new Socket(ip, 7000);
	
			// obtaining input stream
			dis = new DataInputStream(s.getInputStream());
	
			// the following loop performs the exchange of
			// information between client and client handler
			while ( true )
			{
				System.out.println(dis.readUTF());
				
				// printing data
				String received = dis.readUTF();
				System.out.println(received);
			}

		} catch( Exception e ){
			e.printStackTrace();
		} finally{
            // closing resources
			scn.close();
			dis.close();
        }
	}
}
