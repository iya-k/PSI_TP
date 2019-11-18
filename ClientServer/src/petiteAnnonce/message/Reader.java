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
		
		System.out.println("------------------ Client started ----------------");

		OutputStream output;
		try {
			output = socket.getOutputStream();

			writer = new PrintWriter(output, true);
			InputStream input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));

			
			
			while (true) {
				//message = "";
				
				String message = reader.readLine();
				System.out.println("\n[Serveur ]: "+message);
				
				System.out.print("\nMoi:");
				message = sc.nextLine();
				writer.println(message);
				
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//writer.close();
        //socket.close();
		
	}


}