package petiteAnnonce.client;

import java.net.*;
import java.util.Scanner;

import petiteAnnonce.message.ClientCommunication;
import petiteAnnonce.message.ServerCommunication;
import petiteAnnonce.server.Informations;

import java.io.*;

/**
 * @author KABA
 *
 */

public class Client {

	private BufferedReader reader, saisie;
	public Scanner input;
	private PrintWriter writer;
	private String host;
	private int port;
	private String userName;
	private String portEcoute;
	private static Socket socket;;
	private String clef;
	private String limit = "!!";
	private String toSend = "";
	private String mdp;
	private String key = "annonce";
	private static AffichageMessage verbose = new AffichageMessage();
	

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

			String test = "1";

			String rep = null;
			do {

				System.out.print("\nEnter your name: ");

				input = new Scanner(System.in);
				userName = input.nextLine();
				
				System.out.print("\nEnter your password: ");

				input = new Scanner(System.in);
				mdp = input.nextLine();

				System.out.print("\nEnter your Listen port: ");

				input = new Scanner(System.in);
				//portEcoute = input.nextInt();
				portEcoute = input.nextLine();
				//System.out.println(userName+" "+ mdp+ " "+ portEcoute);
				if ((!userName.equals(null) && !userName.equals("")) && (!portEcoute.equals("") && !portEcoute.equals(null))) {
					
					verbose.writeSecure(writer, "Connexion"+limit+userName+limit+mdp+limit+portEcoute+limit, key);

					//System.out.println(/*writer,*/"Connexion"+limit+userName+limit+mdp+limit+portEcoute+limit);

					rep = verbose.readSecure(reader.readLine(), key);
					test = rep.split(limit)[1];
					
					if(test.equals("0"))
						System.out.println(rep.split(limit)[1]);
				}
				System.out.println(/*writer,*/"Connexion"+limit+userName+limit+mdp+limit+portEcoute+limit);

				System.out.println(rep.split(limit)[1]);
			}while(test.equals("0"));
			
			key = mdp;
			//System.out.println(key);

			String[] accueil = rep.split(limit);

			if(accueil[0].equals("wel-com")) {

				//System.out.println("\n" +accueil[0]+ " "+ accueil[2]+ accueil[3]);

				do {

					boolean chat = false;
					
					boolean bye = false;
					char choix = '\0';
					saisie = new BufferedReader(new InputStreamReader(System.in));
					try {

						System.out.print(" \n\n ========= MENU ========= \n"
								+ "|| a. Add                ||\n|| b. All Announces      ||\n|| c. My Annonces        ||\n|| "
								+ "d. Delete an Annouce  ||\n|| e. Send a Message     ||\n|| f. Answer Messages    ||\n|| "
								+ "g. Quit               ||\n ======================== \n"
								+ "\nVotre choix: ");

						choix =  input.next().charAt(0);
						switch (choix) {
						case 'a':
							add_Announce();

							break;
						case 'b':
							clef = "all-Ann";
							toSend = clef+limit;

							break;
						case 'c':
							clef = "mes-Ann";
							toSend = clef+limit;
							System.out.println(socket.getLocalPort());

							break;

						case 'd':

							clef = "del-Ann";
							System.out.print("\nId Announce to delete:");
							String annonce_to_delete = saisie.readLine();
							toSend = clef+limit+annonce_to_delete+limit;

							break;

						case 'e':

							clef = "snd-Msg";
							System.out.print("\nWhich Announce interest you ?? ");
							String annonce = saisie.readLine();
							toSend = clef+limit+annonce+limit;

							break;
							
						case 'f':
							toSend = "";
							chat = true;
							break;
							
						case 'g':
							clef = "bye-bye";
							toSend = clef+limit;
							System.out.println("\nAurevoir!");
							bye = true;

							break;

						default:
							clef = "default";
							toSend = clef+limit;

							break;
						}
						
						if(!toSend.equalsIgnoreCase(""))//pour severCommunication
							//System.out.println(toSend);
							verbose.writeSecure(writer, toSend, key);
						
						if(bye == true)
							break;
						
						String[] response = verbose.readSecure(reader.readLine(), key).split(limit);;
						String serverResponse;
						int cpt = 0;
						
						if(!toSend.equalsIgnoreCase(""))//pour severCommunication
							serverResponse = response[0];

						else 
							serverResponse = "cou-cou";

						switch(serverResponse) {

						case "ack-add":
							verbose.affichage("ajout");
							break;

						case "ack-all":
							cpt = Integer.parseInt(response[1]);
							if(cpt <= 0) {
								verbose.affichage("0annonce");
								break;
							}
							System.out.println("\n'''''''''''''''' All Announces \n");
							verbose.affichage("entete");
							
							int i = 0;
							int t = 1;
							while(i < cpt) {
								System.out.println(response[++t]);
								i++;
							}

							break;

						case "ack-spe":
							cpt = Integer.parseInt(response[1]);
							if(cpt <= 0) {
								verbose.affichage("0myannonce");
								break;
							}
							System.out.println("\n'''''''''''''''' My Announces \n");
							verbose.affichage("entete");
							i = 0;
							t = 1;
							while(i < cpt) {
								System.out.println(response[++t]);
								i++;
							}
							break;

						case "ack-del":
							String result = response[1];
							if(result.equals("1")) {
								verbose.affichage("suppressionOK");
								break;
							}
							verbose.affichage("suppressionKO");
							break;

						case "ack-msg":

							if(response[1].equals("0")) {
								System.out.println("\n"+response[2]);
								break;
							}

							try {
								new ClientCommunication(Integer.parseInt(response[3]),host, response[2]).execute();
							} catch (Exception e) {
								e.printStackTrace();
							};

							break;

						case "cou-cou":
							if(chat)
								new ServerCommunication(portEcoute, response[1]).execute();
							chat = false;
							break;

						default:
							System.out.println("\n''''''''''''''''Choise between [a,e] ");
							System.out.println(response[0]);
							break;
						}

					} catch (IOException e) {
						saisie.close();
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

		toSend = clef+limit+titre+limit+domaine+limit+prix+limit+descriptif;
	}

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
