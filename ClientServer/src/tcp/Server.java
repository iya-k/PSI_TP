package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

	static  int PORT_TCP = 1027;

	//definir une socket et attendre que quelqu'un nous parle
	public static void main(String[] arg) throws IOException{

		// server is listening on port 1027 
		ServerSocket sockServer = new ServerSocket(PORT_TCP); 

		AtomicInteger nb_clients = new AtomicInteger(0);

		System.out.println("------------------ Server started ----------------\n");

		// running infinite loop for getting 
		// client request 
		while (true)  
		{ 
			Socket clientSocket = null; 

			try 
			{ 
				// socket object to receive incoming client requests 
				clientSocket = sockServer.accept();   

				// create a new thread object 
				Thread t = new ServiceThread(clientSocket, nb_clients); 

				// Invoking the start() method 
				t.start(); 

			} 
			catch (Exception e){ 
				clientSocket.close(); 
				e.printStackTrace(); 
			} 
		}

	}
}

