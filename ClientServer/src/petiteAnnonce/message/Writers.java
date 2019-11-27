package petiteAnnonce.message;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import tcp.ServiceThread;

/**
 * @author KABA
 *
 */
public class Writer extends Thread {

	private static ServerSocket serverSocket;
	private static Socket socket;
	private static  PrintWriter writer;
	private static  BufferedReader reader;

	final static Scanner sc = new Scanner(System.in);

	/*
	 * public Writer(int port) { try { serverSocket = new ServerSocket(port);
	 * socket = serveurSocket.accept(); } catch (IOException e) {
	 * System.out.println("Erreur port"); e.printStackTrace(); } }
	 */

	public static void main(String[] args) throws IOException {

		final String userName;

		final String clef;
		final String limit = "//";
		System.out.println("------------------ Server started ----------------\n");

		try {

			serverSocket = new ServerSocket(2000);

			while (true)  
			{ 
				socket = null; 

				try 
				{ 

					// socket object to receive incoming client requests 
					socket = serverSocket.accept();  

					OutputStream output;
					try {
						output = socket.getOutputStream();

						writer = new PrintWriter(output, true);
						InputStream input = socket.getInputStream();
						reader = new BufferedReader(new InputStreamReader(input));

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					// create a new thread object 
					//Thread t = new Service(socket); 


					Thread t= new Thread(new Runnable() {
						String msg ;
						@Override
						public void run() {
							try {
								writer.println("Bienvenue");
								writer.flush();
								//tant que le client est connecté
								while(true){

									msg = reader.readLine();
									System.out.println("\n[Client] : "+msg);
									//msg = reader.readLine();
									System.out.print("\nMoi:");
									msg = sc.nextLine();
									writer.println(msg);
								}

							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});

					// Invoking the start() method 
					t.start(); 
					//writer.close();
					//socket.close();
					//serverSocket.close();

				} 
				catch (Exception e){ 
					//socket.close(); 
					e.printStackTrace(); 
				} 
			}


		} catch (IOException ex) {
			System.out.println("Error getting output/input stream: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

}
