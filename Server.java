// Java program for the Server
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.io.*;
import java.net.*;

public class Server
{
	public static void main(String[] args) throws IOException {
        int client = 0;
		ArrayList<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();
		// server is listening on port 6999
		ServerSocket ss = null;
		Socket sendingClientSocket = null;

		try {
			ss = new ServerSocket(7000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// running infinite loop for getting
		// client request
		while (client < 6) {
			Socket s = null;
			
			try {
				// socket object to receive incoming client requests
				
				s = ss.accept();

				System.out.println("Client" + client +" is connected : " + s);


				if(client++ == 0){
					sendingClientSocket = s;
				}else{
					// obtaining input and out streams
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					ClientHandler cHandler= new ClientHandler(s, dos);
					clientHandlers.add(cHandler);
				}
			}
			catch (Exception e){
				e.printStackTrace();
				break;
			}
		}

		while(true){
			try {
				ExecutorService executor1 = Executors.newFixedThreadPool(3); //creating a pool of 3 threads, 2 pools 
				Future<Integer> task = executor1.submit(new SendingClientHandler(new DataInputStream(sendingClientSocket.getInputStream()), sendingClientSocket));
				Integer n = task.get();

				ExecutorService executor2 = Executors.newFixedThreadPool(3);
				
				if(n == -1) {
					break;
				}
	
				for (int i = 0; i < clientHandlers.size(); i+=2) {
					ClientHandler cHandler1 = clientHandlers.get(i);
					
					//Set sending number
					cHandler1.setSendingNumber((n % 10) + i);

					ClientHandler cHandler2 = null;
					if (i+1 < clientHandlers.size()) {
						cHandler2 = clientHandlers.get(i+1);
						cHandler2.setSendingNumber((n % 10) + i + 1);
					}

					Runnable worker = cHandler1;
					executor1.execute(worker); //calling execute method of ExecutorService
					worker = cHandler2;
					executor2.execute(worker);
				}  
				executor1.shutdown();
				while (!executor1.isTerminated()) {   }
				executor2.shutdown();
				while (!executor2.isTerminated()) {   }
		
				System.out.println("Finished all threads");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			ss.close();
		}catch(IOException e){
			e.printStackTrace();
		}catch(NullPointerException e){
			e.printStackTrace();
		}
	}
}

// Create two classes, one for sending client, other for listeningClients

// Sending Client class
class SendingClientHandler implements Callable<Integer> {
	private DataInputStream dis;
	private Socket s;

	public SendingClientHandler(DataInputStream dis, Socket s) {
		this.dis = dis;
		this.s = s;
	}

	@Override
	public Integer call() throws Exception {
		System.out.println("Called run() from Sending Client!");
		String received;
		Integer n = null;

		while (true) {
			try {
				
				// receive the data from sending client
				received = dis.readUTF();

				try{
					n = Integer.valueOf(received);
					break;
				} catch(NumberFormatException numberFormatException){
					numberFormatException.printStackTrace();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return n;
	}
}

// For Listening Clients
class ClientHandler extends Thread
{
	final DataOutputStream dos;
	final Socket s;
	private Integer n;

	// Constructor
	public ClientHandler(Socket s, DataOutputStream dos) {
		this.s = s;
		this.dos = dos;
	}

	public void setSendingNumber(int n) {
		this.n = n;
	}

	@Override
	public void run() {
		try{
			this.dos.writeUTF(String.valueOf(n));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
