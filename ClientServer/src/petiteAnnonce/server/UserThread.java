package petiteAnnonce.server;
 
import java.io.*;
import java.net.*;

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
 
    public UserThread(Socket socket, Informations information) {
        this.socket = socket;
        this.info = information;
    }
 
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
 
            userName = reader.readLine();
            add_User(userName, socket);//add the new connected user
            
            System.out.println("------------"+userName);
            serverMessage = "Hi " + userName+"!You are on port "+socket.getPort();//send message to user who comes to connect
            accuse_Reception(serverMessage, this);
 
            String[] clientMessage;
 
            do {
                clientMessage = reader.readLine().split("!!");
            	System.out.println(clientMessage[0]+" has received ");//to see if he really receive from user
                
                switch(clientMessage[0]) {
                	
                case "add-Ann":
                	String idAnnonce = "Ann00" + (info.getAnnonce().size() + 1);

                    Annonce a = new Annonce(idAnnonce, clientMessage[1], clientMessage[2], clientMessage[3], clientMessage[4], getUserId());
                    info.addAnnonce(a);
                    
                    System.out.println(a.toString()+"\n");//to verify all information
                    serverMessage = "'''''''''''''''' Annonce sauvegardee avec succes\n";
                	break;
                	
                case "all-Ann":
                	allAnnounces();
                	break;
                	
                case "mes-Ann":
                	System.out.println(serverMessage);
                	myAnnounces(userName);
                	break;
                	
                case "bye-bye":
                	serverMessage = "Aurevoir";
                	break;
                	
                case "snd-msg":
                	
                    serverMessage = "[\r\n" + 
                    		"                	//appeler la classe qui fera la communication]: ";
                    break;
                    
                default:
                	serverMessage = "Unknown "+clientMessage[0]+" Command";
    				break;
    			
                }        		
                accuse_Reception(serverMessage, this);
 
            } while (!clientMessage[0].equals("bye-bye"));
            
            User uToRemove = null;
            for(User u: info.getUsers()) {
            	if(u.getUserName() == userName) {

                    info.removeUser(uToRemove, this);
            	}
            	
            }
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
		info.setnUsers(info.getnUsers() + 1);
		String idUser = "User00" + info.getnUsers();
		User user = new User(idUser, username, clt.getPort());

		info.addUser(user);
		System.out.println("nombre d'utilisateur"+info.getnUsers());
	}
	
	String getUserId() {
    	for(User user: info.getUsers()) {
    		if (user.getUserName() == userName) {
    			System.out.println(user.getId_User()+": "+userName);
        		return user.getId_User();
    		}
    	}
		return null;
    }
	
	
	public void myAnnounces(String user) {
		serverMessage = "";
    	String userId = "";
    	for(User u: info.getUsers()) {
    		if(u.getUserName() == user)
    			userId = u.getId_User();
    	}
    	for(Annonce a: info.getAnnonce()) {
    		if(userId == a.getIdUser())
    			serverMessage += a.toString();
        } 
    }
    
	public void allAnnounces() {
    	serverMessage = "";
    	
    	for(Annonce a: info.getAnnonce()) 
    		serverMessage += a.toString();
    }
	
	
	
}