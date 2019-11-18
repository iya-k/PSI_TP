package petiteAnnonce.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Service extends Thread{
	private static Socket socket;
	private  PrintWriter writer;
	private  BufferedReader reader;

	final Scanner sc = new Scanner(System.in);
	
	public  Service(Socket clt){

		this.socket = clt;
		
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
	}
	
	public void run() {
	try {
		while (true) {
			String message;
				message = sc.nextLine();
				writer.println(message);
						message = reader.readLine();
							System.out.println("[ Client ]: "+message);
							//message = reader.readLine();
						
		}
		//writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
		

	}

}
