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
	
	public ServerCommunication(String port) {
		portEcoute = Integer.parseInt(port);
		
	}
	public ServerCommunication() {
		
	}
	
@SuppressWarnings("resource")
public static void execute() throws IOException{
		
		System.out.println("\n============= Communication Vendeur =====================\n");
		
		//int port_dest = 0;
		
		Scanner inFromMe = new Scanner(System.in);
		DatagramSocket serverSocket= new DatagramSocket(portEcoute);
		String msg, sentence;
		//boolean flag = true;
        
        while(true){
        	byte[] receiveData = new byte[1024];
        	
        	DatagramPacket receivePacket= new DatagramPacket(receiveData, receiveData.length);
        	serverSocket.receive(receivePacket);
        	sentence = new String(receivePacket.getData());
        	int port_dest = receivePacket.getPort();
        	
            System.out.println("\nClient: "+sentence);
            
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
        System.out.println("Aurevoir");
        
        serverSocket.close();
	}

	
	  public static void main(String args[]) throws IOException {
	  
	  ServerCommunication.execute();
	  
	  }
	 
}
