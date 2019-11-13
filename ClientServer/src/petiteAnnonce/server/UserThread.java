package petiteAnnonce.server;
 
import java.io.*;
import java.net.*;

import petiteAnnonce.client.Annonce;
import petiteAnnonce.client.User;
 
/**
 * @author KABA
 *
 */
public class UserThread extends Thread {
    private Socket socket;
    private Server server;
    private PrintWriter writer;
 
    public UserThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }
 
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
 
            String userName = reader.readLine();
            System.out.println("ack");
            String serverMessage = null;
            serverMessage = "Welcome " + userName+"!! You are on port "+socket.getPort();
            Server.add_User(userName, socket);
            server.accuse_Reception(serverMessage, this);
 
            String[] clientMessage;
 
            do {
                clientMessage = reader.readLine().split("//");
            	System.out.println("<"+clientMessage[0]+"> received ");
            	serverMessage = null;
                
                switch(clientMessage[0]) {
                	
                case "add-Ann":
                	String idAnnonce = "Ann00" + Server.info.getAnnonce().size() + 1;
                    
                    Annonce a = new Annonce(idAnnonce, clientMessage[1], clientMessage[2], clientMessage[3], clientMessage[4], server.getUserId(userName));
                    Server.info.addAnnonce(a);
                    System.out.println(a.toString());
                    server.accuse_Reception("\n", this);
                	break;
                	
                case "all-Ann":
                	
                	if(Server.info.getAnnonce().size() <= 0) {
                		serverMessage = "Nothing is founding\n";
                		server.accuse_Reception(serverMessage, this);
                		break;
                	}
                	String list = "";
                	for(Annonce announce: Server.info.getAnnonce()) 
                		list += announce.toString();
                		
                	serverMessage = list;
                	System.out.println(serverMessage);
                	server.accuse_Reception(serverMessage, this);
                	break;
                	
                case "quit":
                	serverMessage = "Aurevoir";
                	server.accuse_Reception(serverMessage, this);
                	break;
                	
                default:
                	serverMessage = "Unknown "+clientMessage[0]+" Command";
                	server.accuse_Reception(serverMessage, this);
    				break;
    			
                }
        		
                server.accuse_Reception(serverMessage, this);
 
            } while (!clientMessage[0].equals("quit"));
            
            //User uToRemove = null;
            for(User uToRemove: Server.info.getUsers()) {
            	if(uToRemove.getUserName() == userName) {

                    Server.info.removeUser(uToRemove, this);
            	}
            	
            }
 
            socket.close();
 
        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    /**
     * Sends a message to the User.
     */
    void sendMessage(String message) {
        writer.println(message);
    }
}