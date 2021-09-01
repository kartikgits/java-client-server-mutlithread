// Java program for Sending Client
import java.net.*;
import java.io.*;
  
public class SendingClient
{
    // initialize socket and input output streams
    private Socket socket            = null;
    private DataInputStream  input   = null;
    private DataOutputStream out     = null;
  
    // constructor to put ip address and port
    public SendingClient(String address, int port)
    {
        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");
  
            // takes input from terminal
            input  = new DataInputStream(System.in);
  
            // sends output to the socket
            out    = new DataOutputStream(socket.getOutputStream());
        }
        catch(UnknownHostException unknownHostException)
        {
            unknownHostException.printStackTrace();
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
  
        // string to read message from input
        String line = "";
  
        // keep reading until "Over" is input
        while (!line.equals("Over"))
        {
            try
            {
                line = input.readLine();
                out.writeUTF(line);
            }
            catch(IOException ioException)
            {
                ioException.printStackTrace();
            }
        }
  
        // close the connection
        try
        {
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
  
    public static void main(String args[])
    {
        SendingClient sendingClient = new SendingClient("127.0.0.1", 7000);
    }
}