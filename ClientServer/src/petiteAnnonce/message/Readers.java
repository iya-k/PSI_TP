package petiteAnnonce.message;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import petiteAnnonce.client.AffichageMessage;
import petiteAnnonce.securite.UserSecure;

/**
 * @author KABA
 *
 */
public class Readers extends Thread{

	private static Socket socket;
	private UserSecure user;
	private  BufferedReader reader;
	private static  PrintWriter writer;

	static AffichageMessage verbose = new AffichageMessage();
	String clef = "lus-lus";
	String limit = "!!";
	boolean bye = false;


	public Readers(UserSecure aUser) { 

		reader = aUser.getBr();
		writer = aUser.getPw();
		user = aUser;
		//message = msg;
	}
	
	public void run() {
		
		try {
			String message;
			do{

				 message = reader.readLine();
				System.out.println("\n["+user.getUserName()+"]: "+message+"\n");
				verbose.write(writer,clef+limit);

			}while (!message.equals("bye")) ;

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}