package petiteAnnonce.client;

import java.net.*;
import java.util.Scanner;


import java.io.*;

/**
 * @author KABA
 *
 */

public class Client {

	private BufferedReader reader;
	private PrintWriter writer;
	private String host;
	String userName;
	private int port;
	public Scanner input;
	private Socket socket;;
	private String clef;
	private String limit = "!!";
	

	final static int DEFAULT_PORT_TCP = 1028;
	protected Informations info;

	public Client(String hostname, int port) {

		this.host = hostname;
		this.port = port;

	}

	public void execute() {


		try {

			socket = new Socket(host, port);

			System.out.println("------------- client starting!!!---------------\n ");

			OutputStream output;
			output = socket.getOutputStream();

			writer = new PrintWriter(output, true);
			InputStream inp = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inp));

			//String receive = reader.readLine();
			//System.out.print(receive);

			System.out.print("Enter your name: ");
			
			input = new Scanner(System.in);
			userName = input.nextLine();

			if (userName != null || userName != "\0") {
				writer.println("Connexion!!"+userName+limit);//print the hello message from server
				writer.flush();

				try {
					String[] welcom = reader.readLine().split(limit);
					if(welcom[0].equals("wel-com")) {
						
						System.out.println("\n" +welcom[0]+ welcom[1]);
					
				

				do {
					boolean bye = false;
					char choix = '\0';

					try {

						System.out.print(" \n\n ======================== \n"
								+ "|| a. Add                ||\n|| b. All Announces      ||\n|| c. My Annonces        ||\n|| "
								+ "d. Send Messages      ||\n|| e. Delete An Annouce  ||\n|| f. Quit               ||"
								+ "\n ======================== \n\n"
								+ "Votre choix: ");

						choix =  input.next().charAt(0);
						switch (choix) {
						case 'a':
							add_Announce();

							break;
						case 'b':
							clef = "all-Ann"+limit;
							writer.println(clef);
							writer.flush();

							break;
						case 'c':
							clef = "mes-Ann";
							writer.println(clef);
							writer.flush();

							break;
						case 'd':
							clef = "snd-Msg";
							writer.println(clef);
							writer.flush();
							break;

						case 'e':
							BufferedReader saisie = new BufferedReader(new InputStreamReader(System.in));
							System.out.print("\nId Announce to delete:");
							String annonce_to_delete = saisie.readLine();
							clef = "del-Ann"+limit;
							writer.println(clef+annonce_to_delete);
							writer.flush();
							break;

						case 'f':
							//sign_out();
							clef = "bye-bye";
							System.out.println("Aurevoir!");
							bye = true;
							writer.println(clef);
							writer.flush();
							
							break;

						default:
							writer.println("default");
							writer.flush();

							break;
						}


						if(bye == true)
							break;
						
						String[] response = reader.readLine().split(limit);
						//System.out.println("\n"+response);

						int cpt = 0;

						switch(response[0]) {

						case "ack-add":
							System.out.println("\n'''''''''''''''' Annonce sauvegardee avec succes\n");
							break;

						case "ack-all":
							cpt = Integer.parseInt(response[response.length - 1]);
							if(cpt <= 0) {
								System.out.println("\nNot yet any announce");
								break;
							}
							System.out.println("\n'''''''''''''''' All Announces \n");
							System.out.println("\nId-Ann ------- Titre -------- Domaine -------- Prix -------- Description -------- User\n");

							for(int i = 1; i <= cpt; i++)
								System.out.println(response[i]);
							break;

						case "ack-mes":
							cpt = Integer.parseInt(response[response.length - 1]);
							if(cpt <= 0) {
								System.out.println("\nYou don't have any announce");
								break;
							}
							System.out.println("\n'''''''''''''''' My Announces \n");
							System.out.println("\nId-Ann ------- Titre -------- Domaine -------- Prix -------- Description -------- User\n");

							for(int i = 1; i <= cpt; i++)
								System.out.println(response[i]);

							break;

						case "ack-del":
							String result = response[1];
							//System.out.println(result);
							if(result.equals("1")) {
								System.out.println("\nDeleted with success");
								break;
							}
							System.out.println("\nThis announce doesn't exist");
							break;

						case "ack-msg":

							break;
							
						/*
						 * case "ack-bye": bye = true; break;
						 */
						default:
							System.out.println("''''''''''''''''Choise between [a,e] ");
							break;
						}

					} catch (IOException e) {

						e.printStackTrace();
					}
				}while (true);
				
				//socket.close();
				}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}

		} catch (IOException e) {

			System.out.println("Echec reader");
			e.printStackTrace();
		}



		//new AnnonceThread(socket).start();


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


	public static void main(String[] args) throws IOException {

		String hostname;
		int port;

		if (args.length <= 1) {
			hostname = "localhost";
			hostname = "192.168.137.1";
			//port = DEFAULT_PORT_TCP;
			port = 1027;
		} else {
			hostname = InetAddress.getByName(args[0]).getHostName();
			port = Integer.parseInt(args[1]);
		}

		Client client = new Client(hostname, port);
		client.execute();
	}
}
