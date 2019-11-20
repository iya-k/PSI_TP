package petiteAnnonce.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import petiteAnnonce.message.Reader;
import petiteAnnonce.message.Writer;

/**
 * @author KABA
 *
 */
public class AnnonceThread extends Thread {
	private BufferedReader reader;
	private PrintWriter writer;
	private Socket socket;
	private String userName;
	private Scanner input;
	private String clef;
	private String limit = "!!";
	boolean bye = false;

	public AnnonceThread(Socket socket, String client) {
		this.socket = socket;
		this.userName = client;

		try {
			InputStream input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));
		} catch (IOException ex) {
			System.out.println("Error getting input stream: " + ex.getMessage());
			ex.printStackTrace();
		}

		try {
			OutputStream output = socket.getOutputStream();
			writer = new PrintWriter(output, true);
		} catch (IOException ex) {
			System.out.println("Error getting output stream: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void run() {


		input = new Scanner(System.in);
		System.out.print("\nEnter your name: ");

		userName = input.nextLine();
		if (userName != null || userName != "\0") {
			writer.println(userName);
			writer.flush();

			try {
				String response = reader.readLine();
				System.out.println("\n" + response);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			do {
				String response = "";
				try {

					char choix = '\0';

					System.out.print(" \n\n ======================== \n"
							+ "|| a. Add                ||\n|| b. All Announces      ||\n|| c. My Annonces        ||\n|| "
							+ "d. Send Messages      ||\n|| e. Quit               ||"
							+ "\n ======================== \n\n"
							+ "Votre choix: ");
					choix =  input.next().charAt(0);
					switch (choix) {
					case 'a':
						System.out.println("\n'''''''''''''''' Ajout d'une Annonce");
						add_Announce();

						response = reader.readLine();
						//System.out.println(response);
						break;
					case 'b':
						System.out.println("\n'''''''''''''''' All Announces");

						clef = "all-Ann"+limit;
						writer.println(clef);
						writer.flush();

						response = reader.readLine();
						if(response == null || response == "") {
							
							response = "Not yet any announce";
							break;
						}
						System.out.println("\n'''''''''''''''' My Announces \n");
						
						break;
					case 'c':
						clef = "mes-Ann";
						writer.println(clef);
						writer.flush();

						response = reader.readLine();
						
						if(response == null || response == "") {
							response = "You don't have any announce";
							break;
						}

						System.out.println("\nId-Ann ------- Titre -------- Domaine -------- Prix -------- Description -------- Id-User\n");
						break;
					case 'd':
						System.out.println("'''''''''''''''' Sending Messages ");
						//un package � part qui fait la communication client-serveur, appler ce dernier

						break;
					case 'e':
						//sign_out();
						clef = "bye-bye";
						bye = true;
						writer.println(clef);
						writer.flush();
						
						response = reader.readLine();
						//System.out.println("\n''''''''''''''''" + response);
						socket.close();
						break;

					default:
						System.out.println("''''''''''''''''Choise between [a,e] ");
						writer.println("default");
						writer.flush();
						

						response = reader.readLine();
						
						break;
					}
						
					System.out.println("\n"+response);
					
					if(bye) {
						socket.close();
						break;
					}
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}while (true);

		}
	}

	public void add_Announce() {
		String annonce;
		String titre;
		String domaine;
		String prix;
		String descriptif;
		clef = "add-Ann";
		input = new Scanner(System.in);

		System.out.print("\nTitre: ");
		titre = input.nextLine();
		System.out.print("\nDomaine (voiture, moto, musique, electromenager, telephone, autres): ");
		domaine = input.nextLine();
		System.out.print("\nPrix: ");
		prix = input.nextLine();
		System.out.print("\nDescriptif: ");
		descriptif = input.nextLine();

		annonce = clef+limit+titre+limit+domaine+limit+prix+limit+descriptif;
		writer.println(annonce);
		writer.flush();

	}

}