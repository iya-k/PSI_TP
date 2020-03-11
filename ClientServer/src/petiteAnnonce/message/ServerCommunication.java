package petiteAnnonce.message;

import java.io.*;
import java.net.*;
import java.util.Scanner;


/**
 * @author KABA
 *
 */
public class ServerCommunication extends Thread {
	
	private static int portEcoute;
	private static String buyer;
	
	public ServerCommunication(String port, String acheteur) {
		portEcoute = Integer.parseInt(port);
		buyer = acheteur;
		
	}
	public ServerCommunication() {
		
	}
	
@SuppressWarnings("resource")
public static void execute() throws IOException{
		
		System.out.println("\n============= Communication Vendeur: "+portEcoute+" =====================\n");
		
		Scanner inFromMe = new Scanner(System.in);
		DatagramSocket serverSocket= new DatagramSocket(portEcoute);
		String msg, sentence;
        
        while(true){
        	byte[] receiveData = new byte[1024];
        	
        	DatagramPacket receivePacket= new DatagramPacket(receiveData, receiveData.length);
        	serverSocket.receive(receivePacket);
        	sentence = new String(receivePacket.getData());
        	int port_dest = receivePacket.getPort();
        	
            System.out.println("\n"+buyer+": "+sentence);
            
            if(sentence.equals("bye"))
				break;
            
            System.out.print("\nMoi: ");
             msg = inFromMe.nextLine();
            
            byte[] sendData = msg.getBytes();
            DatagramPacket env = new DatagramPacket(sendData,sendData.length,receivePacket.getAddress(),port_dest);
            serverSocket.send(env);
            
            if(msg.equals("bye"))
			break;
           
        }
        System.out.println("\nAurevoir");
        
        serverSocket.close();
	}

	  public static void main(String args[]) throws IOException {
	  
	  ServerCommunication.execute();
	  
	  }
	 
}
