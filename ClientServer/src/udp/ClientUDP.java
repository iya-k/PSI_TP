package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientUDP {

	public static void main(String args[]) throws Exception {
		
		System.out.print("Client:  ");
		Scanner inFromUser = new Scanner(System.in);
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");
		byte[] sendData = new byte[1024];
		String sentence = inFromUser.nextLine();
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new 
				DatagramPacket(sendData, sendData.length, IPAddress, 9876);
		clientSocket.send(sendPacket);
		
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new
		DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		String msg = new String(receivePacket.getData());
		System.out.println("Serveur: "+msg);
	}
}
