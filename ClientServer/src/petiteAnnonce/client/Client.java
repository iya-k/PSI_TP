package petiteAnnonce.client;

import java.net.*;
import java.util.Scanner;
import java.io.*;

import petiteAnnonce.message.Communication;
import petiteAnnonce.message.Readers;
import petiteAnnonce.message.Writers;
import petiteAnnonce.securite.AES;
import petiteAnnonce.message.ServerCommunication;
import petiteAnnonce.server.Informations;

/**
 * @author KABA
 *
 */

public class Client {

	private BufferedReader reader;
	private PrintWriter writer;
	private String host;
	private String userName;
	private int port;
	public Scanner input;
	private static Socket socket;;
	private String clef;
	private String limit = "!!";
	String destinateur = "";
	AES safety = new AES();
	String key = "Client";
	

	final static int DEFAULT_PORT_TCP = 1028;
	protected Informations info;

	public Client(String hostname, int port) {

		this.host = hostname;
		this.port = port;

	}

	public void execute() {

		try {

			socket = new Socket(host, port);

			System.out.println("------------- Bienvenue sur la plateforme de vente entre particulier "+socket.getPort()+":"+"---------------\n ");

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
				writer.println(safety.encrypt("Connexion"+limit+userName+limit, key));//print the hello message from server
				//System.out.println(safety.encrypt("Connexion"+limit+userName+limit, key));//print the hello message from server
				writer.flush();

				try {
					String rep = safety.decrypt(reader.readLine(), key);
					String[] welcom = rep.split(limit);
					if(welcom[0].equals("wel-com")) {
						
						System.out.println("\n" +welcom[0]+ welcom[1]);
					
						String  corpus = "";

				do {
					boolean bye = false;
					char choix = '\0';
					BufferedReader saisie = new BufferedReader(new InputStreamReader(System.in));
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
							writer.println(safety.encrypt(clef,key));
							writer.flush();

							break;
						case 'c':
							clef = "mes-Ann";
							writer.println(safety.encrypt(clef,key));
							writer.flush();
							
							break;
						case 'd':
							
							clef = "snd-Msg";
							System.out.print("\nThe destinator:");
							String dest = saisie.readLine();
							//System.out.println(dest);
							//System.out.print("\n[Corpus]:");
							//corpus = saisie.readLine();
							writer.println(clef+limit+dest+limit);
							writer.flush();
							
							break;

						case 'e':
							
							System.out.print("\nId Announce to delete:");
							String annonce_to_delete = saisie.readLine();
							clef = "del-Ann"+limit;
							writer.println(safety.encrypt(clef+annonce_to_delete,key));
							writer.flush();
							break;

						case 'f':
							//sign_out();
							clef = "bye-bye";
							System.out.println("Aurevoir!");
							bye = true;
							writer.println(safety.encrypt(clef,key));
							writer.flush();
							
							break;

						default:
							writer.println(safety.encrypt("default",key));
							writer.flush();

							break;
						}


						if(bye == true)
							break;
						
						rep = safety.decrypt(reader.readLine(), key);
						String[] response = rep.split(limit);
						

						int cpt = 0;

						switch(response[0]) {

						case "ack-add":
							System.out.println("\n'''''''''''''''' Annonce sauvegardee avec succes\n");
							break;

						case "ack-all":
							cpt = Integer.parseInt(response[1]);
							if(cpt <= 0) {
								System.out.println("\nNot yet any announce");
								break;
							}
							System.out.println("\n'''''''''''''''' All Announces \n");
							System.out.println("\nId-Ann ------- Titre -------- Domaine -------- Prix -------- Description -------- User\n");
							
							int i = 0;
							int t = 1;
							while(i < cpt) {
								System.out.println(response[++t]);
								i++;
							}
							
							break;

						case "ack-mes":
							cpt = Integer.parseInt(response[1]);
							if(cpt <= 0) {
								System.out.println("\nYou don't have any announce");
								break;
							}
							System.out.println("\n'''''''''''''''' My Announces \n");
							System.out.println("\nId-Ann ------- Titre -------- Domaine -------- Prix -------- Description -------- User\n");

							i = 0;
							t = 1;
							while(i < cpt) {
								System.out.println(response[++t]);
								i++;
							}
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
							
							/*
							 * 
							 * verifier si le destinaire est connecte, envoyer un message au client pour qu'il envoie son message.
							 * 
							 * pour que ça ne repete pas la meme chose à l'envoie du message, je declare une variable pour que
							 * 
							 * apres verification du dest qu'il ne le refasse pas
							 * 
							 */
							//System.out.println(response[response.length - 1]);
							if(response[1].equals("0")) {
								System.out.println("\n"+response[1]);
								break;
							}
								
							
							//je cree une socket avec mon ip et le port du destinateur
							
							//destinateur = response[2];
							
							//new Writers(socket, userName).start();
							Socket s = new Socket(host, (Integer.parseInt(response[3])-1000));
							System.out.println(response[3]);
							//Communication comClient = new Communication(s,response[2]);
							//comClient.CommunicationClient();
							
							break;
							
						case "cou-cou":
							Communication com = new Communication(socket.getLocalPort() - 1000);
							com.communicationServer();
							break;
								 
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
		writer.println(safety.encrypt(annonce, key));
		writer.flush();

	}

	//une fois la communication établie, il ne passe plus par le serveur donc pas besoin de tester
	

	public static void main(String[] args) throws IOException {

		String hostname;
		int port;

		if (args.length > 1) {
			
			hostname = InetAddress.getByName(args[0]).getHostName();
			port = Integer.parseInt(args[1]);
			
		}else if (args.length == 1) {
			hostname = "localhost";
			port = Integer.parseInt(args[0]);
		}
		else {
			
			hostname = "localhost";
			//hostname = "192.168.137.1";
			port = DEFAULT_PORT_TCP;
			//port = 1027;
		}

		Client client = new Client(hostname, port);
		
		client.execute();
		
	}
}
