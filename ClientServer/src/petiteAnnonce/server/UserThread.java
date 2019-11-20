package petiteAnnonce.server;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import petiteAnnonce.client.Annonce;
import petiteAnnonce.client.Informations;
import petiteAnnonce.client.User;

/**
 * @author KABA
 *
 */
public class UserThread extends Thread {
	private Socket socket;
	private static Informations info;
	private PrintWriter writer;
	private String userName;
	String serverMessage;
	private String limit = "!!";
	private Scanner input;
	private String clef;
	private int cpt_annonce = 0;

	Set<Annonce> my_annonces;

	public UserThread(Socket socket, Informations information) {
		this.socket = socket;
		this.info = information;
		my_annonces = new HashSet<>();
	}

	public void run() {
		try {

			OutputStream output = socket.getOutputStream();
			writer = new PrintWriter(output, true);

			InputStream in = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));




			writer.println("Enter your name: ");
			writer.flush();


			userName = reader.readLine();
			add_User(userName, socket);//add the new connected user


			serverMessage = "wel-com!!Hi " + userName+"! You are on port "+socket.getPort();//send message to user who comes to connect
			accuse_Reception(serverMessage, this);
			System.out.println("---------------------"+userName);

			do {
				//System.out.println(my_annonces.size());

				serverMessage = "";

				String[] clientMessage = reader.readLine().split(limit);
				System.out.println(clientMessage[0]+" has received ");//to see if he really receives from user

				switch(clientMessage[0]) {

				case "add-Ann": 
					clef = "ack-add"+limit; 
					String idAnnonce = "Ann00" + (info.getAnnonce().size() + 1);
					Annonce a = new Annonce(idAnnonce,clientMessage[1], clientMessage[2], clientMessage[3], clientMessage[4],getUserId(userName));
					System.out.println(a.toString()+"\n");//to verify all information
					info.addAnnonce(a);
					my_annonces.add(a);

					break;

				case "all-Ann":
					clef = "ack-all"+limit;
					allAnnounces();
					break;

				case "mes-Ann":
					clef = "ack-mes"+limit;
					System.out.println(serverMessage);
					myAnnounces();
					break;

				case "del-Ann":

					clef = "ack-del"+limit;
					//System.out.println(clientMessage[1]);
					if(delete_Announce(clientMessage[1]))
						serverMessage = "1";
					else
						serverMessage = "0";
					break;

				case "snd-Msg":
					clef = "ack-msg"+limit;
					serverMessage = "[\r\n" +
							"                	//appeler la classe qui fera la communication]: ";
					break;

				case "bye-bye":
					clef = "ack-bye"+limit;
					serverMessage = "Aurevoir";
					break;

				default:
					clef = "other";
					serverMessage = "Unknown "+clientMessage[0]+" Command";
					break;

				}

				if(serverMessage != null)
					accuse_Reception(clef+serverMessage, this);

			} while (true);//!clientMessage[0].equals("bye-bye");

			/*
			 * for(User uToRemove: info.getUsers()) { if(uToRemove.getUserName() ==
			 * userName) {
			 * 
			 * info.removeUser(uToRemove, this); }
			 * 
			 * } socket.close();
			 */

		} catch (IOException ex) {
			System.out.println("Error in UserThread: " + ex.getMessage());
			ex.printStackTrace();
		}
	}


	/**
	 * Delivers a reply to user who asked
	 */
	void accuse_Reception(String message, UserThread excludeUser) {
		for (UserThread aUser : info.getUserThreads()) {
			if (aUser == excludeUser) {
				writer.println(message);
				writer.flush();
			}
		}
	}

	/**
	 * Stores the newly connected client.
	 */

	public static void add_User(String username, Socket clt) throws IOException {
		info.setnUsers(info.getnUsers() + 1);
		String idUser = "User00" + info.getnUsers();
		User user = new User(idUser, username, clt.getPort());

		info.addUser(user);
		System.out.println("nombre d'utilisateur"+info.getnUsers());
	}

	String getUserId(String userName) {
		for(User user: info.getUsers()) {
			if (user.getUserName() == userName) {
				System.out.println(user.getId_User()+": "+userName);
				return user.getId_User();
			}
		}
		return null;
	}


	/*
	 * public void myAnnouncesOld(String user) { cpt_annonce = 0; serverMessage =
	 * ""; String userId = ""; for(User u: info.getUsers()) { if(u.getUserName() ==
	 * user) userId = u.getId_User(); } for(Annonce a: info.getAnnonce()) {
	 * if(userId == a.getIdUser()) { serverMessage += a.toString(); cpt_annonce++; }
	 * 
	 * } serverMessage += info.getAnnonce().size(); }
	 */


	public void myAnnounces() {
		serverMessage = "";

		for(Annonce a: info.getAnnonce()) {
			if(getUserId(userName) == a.getIdUser()) 
				serverMessage += a.toString();

		} 
		serverMessage += my_annonces.size();
	}


	public void allAnnounces() {
		serverMessage = "";

		for(Annonce a: info.getAnnonce()) 
			serverMessage += a.toString();	

		serverMessage += info.getAnnonce().size();
	}

	public boolean delete_Announce(String idAnnounce) {
		
		for(Annonce a: my_annonces) {
			System.out.println(a.getId_Annonce());
			if(a.getId_Annonce().equals(idAnnounce)) {
				my_annonces.remove(a);
				info.getAnnonce().remove(a);
				System.out.println("suppression OK");
				return true;
			}
		}
		System.out.println("suppression impossible");
		return false;
	}


}