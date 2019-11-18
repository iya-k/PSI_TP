package petiteAnnonce.client;
 
import java.net.*;
import java.util.Scanner;


import java.io.*;
 
/**
 * @author KABA
 *
 */

public class Client {
	
    private String host;
    private int port;
    private String userName;
    public Scanner input;

	final static int DEFAULT_PORT_TCP = 1028;
	protected Informations info;
 
    public Client(String hostname, int port) {
        this.host = hostname;
        this.port = port;
    }
 
    public void execute() {
    	
        try {
            Socket socket = new Socket(host, port);
 
            System.out.println("------------- client starting!!!--------------- ");
 
            	new AnnonceThread(socket, userName).start();
            
 
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
 
    }
 
 
    public static void main(String[] args) throws IOException {
    	
    	String hostname;
    	int port;
    	
		if (args.length <= 1) {
			hostname = "localhost";
			port = DEFAULT_PORT_TCP;
		} else {
			hostname = InetAddress.getByName(args[0]).getHostName();
			port = Integer.parseInt(args[1]);
		}

        Client client = new Client(hostname, port);
        client.execute();
    }
}
