package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerUDP {

	public static void main(String args[]) throws IOException{
		
		System.out.println("============= UDP Server started =====================");
		
		DatagramSocket serverSocket= new DatagramSocket(9876);
        byte[] receiveData = new byte[1024];
        String msg = "Message recu, aurevoir!!!";
        byte[] sendData = msg.getBytes();
        
        while (true) {
        	DatagramPacket receivePacket= new
        	DatagramPacket(receiveData, receiveData.length);
        	serverSocket.receive(receivePacket);
        	String sentence = new String(receivePacket.getData());
            System.out.println("Reception de:\naddresse: "+receivePacket.getAddress().toString()+"  port: "+receivePacket.getPort());
            System.out.println("Client: "+sentence);
            DatagramPacket env = new DatagramPacket(sendData,sendData.length,InetAddress.getByName("localhost"),receivePacket.getPort());
            serverSocket.send(env);
            
            System.out.println("Server: "+msg);
        }
	}
}
