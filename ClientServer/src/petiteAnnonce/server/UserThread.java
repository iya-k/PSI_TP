package petiteAnnonce.server;


/**
 * @author KABA
 *
 */

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

import petiteAnnonce.client.*;

public class UserThread extends Thread {
	private Socket socket;
	private static Informations info;
	private PrintWriter writer;
	BufferedReader reader;
	private String userName;
	private String serverMessage;
	private String limit = "!!";
	private String clef;
	private static User user;
	boolean add_announce = false;
	int port_dest ;
	String host_dest ;
	String destName = "";
	static AffichageMessage verbose = new AffichageMessage();
	String key = "annonce";

	Set<Annonce> my_annonces;

	public UserThread(Socket socket, Informations information) {
		this.socket = socket;
		UserThread.info = information;
		my_annonces = new HashSet<>();
	}

	public void run() {
		try {
			System.out.println(this);
			OutputStream output = socket.getOutputStream();
			writer = new PrintWriter(output, true);

			InputStream in = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in));

			String rep = verbose.readSecure(reader.readLine(), key);
			String bienvenue = "";

			int ajout = 0;
			do {
				ajout = add_User(rep.split(limit)[1], Integer.parseInt(rep.split(limit)[2]));//add the new connected user
				if(ajout == 0) {
					info.accuse_Reception("wel-com!!"+ajout+limit+rep.split(limit)[1]+limit+"Name or Port Alredy exist, Please try again"+limit, this, user);
					
				}
				else {
					bienvenue = ", you are on port "+socket.getPort()+" IP: "+InetAddress.getByName(null).getHostAddress();//send message to user who comes to connect

					info.accuse_Reception("wel-com!!"+ajout+limit+rep.split(limit)[1]+limit+bienvenue+limit, this, user);
					userName = rep.split(limit)[1];
				}

			}while(ajout == 0);

			info.couple.put(userName, this);

			System.out.println("Welcome "+userName);

			key = rep.split(limit)[2];
			//System.out.println(key);
			
			rep = verbose.readSecure(reader.readLine(), key);
			
			String[] clientMessage;

			do {

				serverMessage = "";
				clientMessage = rep.split(limit);

				System.out.println("\n---------------------"+userName+" - "+this+",\tcommande: "+clientMessage[0]+"\n");

				switch(clientMessage[0]) {

				case "add-Ann": 
					clef = "ack-add"+limit; 
					info.setnAnnonce(info.getnAnnonce() + 1);
					String idAnnonce = "Ann00" + info.getnAnnonce();
					Annonce a = new Annonce(idAnnonce,clientMessage[1], clientMessage[2], clientMessage[3], clientMessage[4],userName);
					System.out.println(a.toString()+"\n");//to verify all informations
					info.addAnnonce(a);
					my_annonces.add(a);
					add_announce = true;

					break;

				case "all-Ann":
					clef = "ack-all"+limit;
					allAnnounces();

					break;

				case "mes-Ann":
					clef = "ack-spe"+limit;
					myAnnounces();
					break;

				case "del-Ann":

					clef = "ack-del"+limit;
					if(delete_Announce(clientMessage[1]))
						serverMessage = "1";
					else
						serverMessage = "0";
					break;

				case "snd-Msg":
					clef = "ack-msg"+limit;

					String announceToBuy = clientMessage[1];
					destName = info.getUserByAnnonce(announceToBuy);

					User destUser = info.getUserByUserName(destName);

					int exist = 0;
					if(destName == userName) {
						System.out.println("Vous ne pouvez pas vous envoyer un message");
						break;
					}

					

					if(destUser == null) {
						
						serverMessage = exist+limit+destName+" n'existe pas"+limit;
						System.out.println(serverMessage);

						break;
					}

					exist = 1;
					
					serverMessage = exist+limit+destName+limit+destUser.getPortEcoute()+limit+destUser.getAdresseIp()+limit;
					System.out.println(serverMessage);

					info.accuse_Reception("cou-cou"+limit+userName+limit, info.couple.get(destName), destUser);
					System.out.println("\n coucou à: "+destUser.getUserName());


					break;

				case "bye-bye":
					serverMessage = null;
					break;
					
				default:
					clef = "other"+limit;
					serverMessage = "Unknown "+clientMessage[0]+" Command"+limit;
					break;
				}

				if(serverMessage != null)
					info.accuse_Reception(clef+serverMessage, this, user);
				else
					continue;

				rep = verbose.readSecure(reader.readLine(), key);
				
				user.setOutput(writer);
				user.setInput(reader);


			} while (!clientMessage[0].equals("bye-bye"));
			
			info.removeUser(user, this);
			info.annnonces.removeAll(my_annonces);
			
			socket.close();

		} catch (IOException ex) {
			System.out.println("Error in UserThread: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/*
	 * 
	 * Send a message.
	 * 
	 */

	public synchronized void send(String message, User u) {

		verbose.writeSecure(u.getPw(), message, key);
	}

	/*
	 * 
	 * Stores the newly connected client.
	 * 
	 */

	public synchronized int add_User(String username, int portE) throws IOException {
		System.out.println(username+": "+portE);

		user = new User(username, InetAddress.getByName(null).getHostAddress(), socket.getPort(), portE, reader, writer);

		for(User u: info.getUsers()) {
			System.out.println(u.getUserName());

			if((u.getUserName().equals(username)) || (u.getPortEcoute() == portE))
				return 0;
		}

		info.addUser(user);
		return 1;
	}
	/*
	 * 
	 * get client's announces.
	 * 
	 */
	public synchronized void myAnnounces() {
		serverMessage = my_annonces.size()+limit;
		int compteur = 0;
		for(Annonce a: info.getAnnonce()) {
			if(userName == a.getUser()) {
				serverMessage += ++compteur+" - "+a.toString();
			}
		} 
	}
	/*
	 * 
	 * get all announces.
	 * 
	 */
	public synchronized void allAnnounces() {
		serverMessage = info.getAnnonce().size()+limit;

		int compteur = 0;
		for(Annonce a: info.getAnnonce()) { 
			serverMessage += ++compteur+" - "+a.toString();	
			System.out.println("\n"+a.toString());

		}
	}
	/*
	 * 
	 * delete an announce.
	 * 
	 */
	public synchronized boolean delete_Announce(String idAnnounce) {

		for(Annonce a: my_annonces) {
			System.out.println(a.getId_Annonce());
			if(a.getId_Annonce().equals(idAnnounce)) {
				my_annonces.remove(a);
				info.getAnnonce().remove(a);
				System.out.println(idAnnounce+"\nDeleted succesful!");
				return true;
			}
		}
		System.out.println("\n"+idAnnounce+" Doesn't existe or it's not yours!");
		return false;
	}

}