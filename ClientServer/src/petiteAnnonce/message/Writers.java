package petiteAnnonce.message;

import java.io.*;
import java.net.*;
import java.util.Scanner;


/**
 * @author KABA
 *
 */
public class Writers extends Thread {

	private static  PrintWriter writer;
	private String destinateur;

	final String clef = "snd-Msg";
	final String limit = "!!";
	final static Scanner sc = new Scanner(System.in);

	public Writers(PrintWriter wr, String dest) { 

		writer = wr;
		destinateur = dest;
	}

	public void run() {

		String  bye = "";

		do{ 

			String msg ;

			System.out.print("\n[Moi]:");
			msg = sc.nextLine();
			writer.println(destinateur+limit+msg+limit);



		}while (!bye.equals("bye"));
		
		writer.close();
	} 
}
