package petiteAnnonce.client;

import java.net.*;
import java.util.Scanner;
import java.io.*;

import petiteAnnonce.message.ClientCommunication;
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
	private String portEcoute;
	public Scanner input;
	private static Socket socket;;
	private String clef;
	private String limit = "!!";
	String destinateur = "";

	static AffichageMessage verbose = new AffichageMessage();
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

			System.out.println("------------- Bienvenue sur la plateforme de vente entre particulier "+socket.getPort()+" ---------------\n ");

			OutputStream output;
			output = socket.getOutputStream();

			writer = new PrintWriter(output, true);
			InputStream inp = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inp));
			input = new Scanner(System.in);

			String test = "";
			String encore = "Name or Port Alredy exist, Please try again";

			String rep = null;
			do {

				System.out.print("\nEnter your name: ");

				input = new Scanner(System.in);
				userName = input.nextLine();

				System.out.print("\nEnter your Listen port: ");

				input = new Scanner(System.in);
				portEcoute = input.nextLine();

				if ((!userName.equals(null) && !userName.equals("")) && (!portEcoute.equals("") && !portEcoute.equals(null))) {
					verbose.writeSecure(writer,"Connexion"+limit+userName+limit+portEcoute+limit, key);

					rep = verbose.readSecure(reader.readLine(), key);
					test = rep.split(limit)[3];
					
					if(test.equals(encore))
						System.out.println(encore);
				}
			}while(test.equals(encore));

			String[] acceuil = rep.split(limit);


			if(acceuil[0].equals("wel-com")) {

				System.out.println("\n" +acceuil[0]+ " "+ acceuil[2]+ acceuil[3]);

				boolean flag = false;

				do {

					boolean bye = false;
					char choix = '\0';
					BufferedReader saisie = new BufferedReader(new InputStreamReader(System.in));
					try {

						System.out.print(" \n\n ======================== \n"
								+ "|| a. Add                ||\n|| b. All Announces      ||\n|| c. My Annonces        ||\n|| "
								+ "d. Delete An Annouce  ||\\n|| e. Delete An Annouce  ||\n|| f. Quit               ||"
								+ "\n ======================== \n"
								+ "\nVotre choix: ");


						/*
						 * if(flag) { for(int i = 0; i < 2; i++) { try { Thread.sleep(1000); } catch
						 * (InterruptedException e1) {
						 * System.out.println("erreur Thread.sleep dans execute: "+this.getClass().
						 * getName()); e1.printStackTrace(); } } }
						 */
						choix =  input.next().charAt(0);
						switch (choix) {
						case 'a':
							add_Announce();

							break;
						case 'b':
							clef = "all-Ann"+limit;
							verbose.writeSecure(writer,clef,key);

							break;
						case 'c':
							clef = "mes-Ann";
							verbose.writeSecure(writer,clef,key);

							break;

						case 'd':

							System.out.print("\nId Announce to delete:");
							String annonce_to_delete = saisie.readLine();
							clef = "del-Ann"+limit;
							verbose.writeSecure(writer,clef+annonce_to_delete,key);

							break;

						case 'e':

							clef = "snd-Msg";
							System.out.print("\nWhich Announce interest you ?? ");
							String annonce = saisie.readLine();
							verbose.writeSecure(writer,clef+limit+annonce+limit,key);

							break;
						case 'f':
							clef = "bye-bye";
							System.out.println("\nAurevoir!");
							bye = true;
							verbose.writeSecure(writer,clef,key);

							break;

						default:
							verbose.writeSecure(writer,"default",key);

							break;
						}

						if(bye == true)
							break;

						rep = verbose.readSecure(reader.readLine(), key);
						String[] response = rep.split(limit);

						//System.out.println(response[0]);

						int cpt = 0;

						switch(response[0]) {

						case "ack-add":

							System.out.println("\n'''''''''''''''' Annonce sauvegardee avec succes");
							flag = true;
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

							if(response[1].equals("0")) {
								System.out.println("\n"+response[2]);
								break;
							}

							try {
								new ClientCommunication(Integer.parseInt(response[3]),host).main(null);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							};
							//System.out.println(response[3]);

							break;

						case "cou-cou":
							//System.out.println("Bien recu");
							new ServerCommunication(portEcoute).execute();
							break;

						default:
							System.out.println("''''''''''''''''Choise between [a,e] ");
							break;
						}

					} catch (IOException e) {

						e.printStackTrace();
					}
				}while (true);

			}


			socket.close();

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
		verbose.writeSecure(writer,annonce, key);


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
