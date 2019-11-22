package petiteAnnonce.server;

import java.io.*;
import java.net.*;
import java.util.HashSet;
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
	private String serverMessage;
	private String limit = "!!";
	private String clef;
	private static User user;

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




			//writer.println("Enter your name: ");
			//writer.flush();


			userName = reader.readLine().split(limit)[1];
			add_User(userName, socket);//add the new connected user
			
			System.out.println("Welcome "+userName);

			serverMessage = "wel-com!!"+userName+"! You are on port "+socket.getPort()+" IP: "+InetAddress.getByName(null).getHostAddress();//send message to user who comes to connect
			accuse_Reception(serverMessage, this);

			String[] clientMessage;
			
			do {
				//System.out.println(my_annonces.size());
				

				serverMessage = "";

				clientMessage = reader.readLine().split(limit);

				System.out.println("\n---------------------"+userName+", commande: "+clientMessage[0]+"\n");
				
				switch(clientMessage[0]) {

				case "add-Ann": 
					clef = "ack-add"+limit; 
					info.setnAnnonce(info.getnAnnonce() + 1);
					String idAnnonce = "Ann00" + info.getnAnnonce();
					Annonce a = new Annonce(idAnnonce,clientMessage[1], clientMessage[2], clientMessage[3], clientMessage[4],userName);
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
					//clef = "ack-bye"+limit;
					serverMessage = null;
					break;

				default:
					clef = "other";
					serverMessage = "Unknown "+clientMessage[0]+" Command";
					break;

				}

				if(serverMessage != null)
					accuse_Reception(clef+serverMessage, this);

			} while (!clientMessage[0].equals("bye-bye"));
			info.removeUser(user, this);
			socket.close();

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
	
		user = new User(username, InetAddress.getByName(null).getHostAddress(), clt.getPort());

		info.addUser(user);
		//System.out.println("\nnombre d'utilisateur "+info.getUsers().size());
	}

	public void myAnnounces() {
		serverMessage = "";

		for(Annonce a: info.getAnnonce()) {
			if(userName == a.getUser()) 
				serverMessage += a.toString();

		} 
		serverMessage += my_annonces.size();
	}

	public void allAnnounces() {
		serverMessage = "";

		for(Annonce a: info.getAnnonce()) { 
			serverMessage += a.toString();	
			System.out.println("\n"+a.toString());
		}
		serverMessage += info.getAnnonce().size();
	}

	public boolean delete_Announce(String idAnnounce) {
		
		for(Annonce a: my_annonces) {
			System.out.println(a.getId_Annonce());
			if(a.getId_Annonce().equals(idAnnounce)) {
				my_annonces.remove(a);
				info.getAnnonce().remove(a);
				System.out.println("\nsuppression OK");
				return true;
			}
		}
		System.out.println("\nsuppression impossible");
		return false;
	}

}