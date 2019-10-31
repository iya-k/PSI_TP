package tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	String servhost;
	int portserveur;
	static  int DEFAULT_PORT_TCP = 1027;

	public static void main(String[] args){

		String servhost = "localhost";

		try {
			servhost = args[0];
		} catch (Exception ex) {

		}

		try {
			DEFAULT_PORT_TCP = Integer.parseInt(args[1]);
		} catch (Exception ex) {

		}

		System.out.println("------------- client starting!!!--------------- ");
		Scanner sc = new Scanner(System.in);
		boolean session= false;
		String msg = null;

		while (true) {
			try {
				Socket sock = new Socket(servhost, DEFAULT_PORT_TCP);
				BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				PrintWriter out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));

				session= true;
				msg = in.readLine() ;

				System.out.println("Server: "+msg);

				while(session) {
					System.out.println("QUIT pour terminer");
					String message = sc.nextLine();
					out.print(message+"\n");
					out.flush();
					
					  msg = in.readLine() ; 
					  if(message.equals("QUIT") || message.equals("quit")){ 
						  //session = false; 
						  System.out.println("Aurevoir"); break; }
					 
				}
				in.close();
				out.close();
				sock.close();
				break;

			}catch (Exception e) {
			}

		}

	}

}
