package petiteAnnonce.message;
 
import java.io.*;
import java.net.*;
import java.util.Scanner;
 
/**
 * @author KABA
 *
 */
public class Reader{
    private static Socket socket;
    private String userName;
    
	/*
	 * public Reader(Socket socket, String client) { this.socket = socket;
	 * this.userName = client;
	 * 
	 * }
	 */
    
    public static void main(String[] args) throws IOException {


		final PrintWriter writer;
		final BufferedReader reader;
		final Scanner sc = new Scanner(System.in);
		
		socket = new Socket("localhost",2000);
		
		
		try {
			OutputStream output = socket.getOutputStream();
			writer = new PrintWriter(output, true);
			InputStream input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));

			Thread send = new Thread(new Runnable() {
				String message;
				@Override
				public void run() {
					message = sc.nextLine();
					writer.println(message);
					writer.flush();
				}
			});
			send.start();


			Thread receive = new Thread(new Runnable() {
				String message;
				@Override
				public void run() {
					try {
						message = reader.readLine();
						while(message != null) {
							System.out.println("[ Serveur ]: "+message);
							message = reader.readLine();
						}
						System.out.println("Aurevoir");
						writer.close();
						socket.close();
					} catch (IOException e) {
						System.out.println("Error reader");
						e.printStackTrace();
					}
					
				}
			});
			receive.start();

		} catch (IOException ex) {
			System.out.println("Error getting output/input stream: " + ex.getMessage());
			ex.printStackTrace();
		}

		
    }
}