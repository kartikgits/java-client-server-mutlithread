// Java program for the Server

import java.io.*;
import java.net.*;

public class Server
{
	public static void main(String[] args) throws IOException
	{
        int client = 0;
		// server is listening on port 6999
		ServerSocket ss = null;

		try {
			ss = new ServerSocket(7000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// running infinite loop for getting
		// client request
		while (ss != null)
		{
			Socket s = null;
			
			try
			{
				// socket object to receive incoming client requests
				s = ss.accept();
				
				System.out.println("Client" + client +" is connected : " + s);
				
				// obtaining input and out streams
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				
				System.out.println("Assigning new thread for this client");

				// create a new thread object
				Thread t = new ClientHandler(s, dos);

				if(client++ == 0){
					ClientHandler.setClientListener(new DataInputStream(s.getInputStream()));
				}

				// Invoking the start() method
				t.start();
				
			}
			catch (Exception e){
				s.close();
				e.printStackTrace();
			}
		}

		try{
			ss.close();
		}catch(IOException e){
			e.printStackTrace();
		}catch(NullPointerException e){
			e.printStackTrace();
		}
	}
}

// ClientHandler class
class ClientHandler extends Thread
{
	static DataInputStream dis;
	static int clientNumberCounter = 0;
	final int clientNumber;
	final DataOutputStream dos;
	final Socket s;
	

	// Constructor
	public ClientHandler(Socket s, DataOutputStream dos)
	{
		this.s = s;
		this.dos = dos;
		this.clientNumber = clientNumberCounter++;
	}

	public static void setClientListener(DataInputStream dataInputStream){
		dis = dataInputStream;
	}

	@Override
	public void run()
	{
		System.out.println("Called run()!");
		String received;

		while (true)
		{
			try {
				dos.writeUTF("You're Connected.");
				
				// receive the data from sending client
				received = dis.readUTF();

				Integer n = null;

				try{
					n = Integer.valueOf(received);
					System.out.println(n);
				} catch(NumberFormatException numberFormatException){
					numberFormatException.printStackTrace();
				}

				if(n != null)
				{
					// dos.writeUTF(toreturn);
					switch(this.clientNumber){
						case 1: this.dos.writeUTF(String.valueOf((n % 10) + 0));
								break;
						case 2: this.dos.writeUTF(String.valueOf((n % 10) + 1));
								break;
						case 3: this.dos.writeUTF(String.valueOf((n % 10) + 2));
								break;
						case 4: this.dos.writeUTF(String.valueOf((n % 10) + 3));
								break;
						case 5: this.dos.writeUTF(String.valueOf((n % 10) + 4));
								break;
						default: break;
					}
				}else{
					System.out.println("inside else for closing");
					this.s.close();
					break;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try
		{
			// closing resources
			dis.close();
			this.dos.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
