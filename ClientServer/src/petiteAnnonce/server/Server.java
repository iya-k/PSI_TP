package petiteAnnonce.server;

import java.io.*;
import java.net.*;
import java.util.*;

import petiteAnnonce.client.*;

public class Server {

	final static int PORT_TCP = 1028;
	private static int port;
	static Informations info;
	ServerSocket serverSocket;

	public Server(int port) {
		this.port = port;
		info = new Informations();
	}

	public void execute() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {

			System.out.println("--------------- Server is listening on port " + PORT_TCP + " --------------\n");

			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("What's your name?? ");

				UserThread newUser = new UserThread(socket, info);
				info.getUserThreads().add(newUser);
				newUser.start();
			}

		} catch (IOException ex) {
			System.out.println("Error in the server: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			port = PORT_TCP;
		} else {
			port = Integer.parseInt(args[0]);
		}

		Server server = new Server(port);
		server.execute();
	}

	
}