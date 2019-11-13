package petiteAnnonce.server;

import java.io.*;
import java.net.*;
import petiteAnnonce.client.*;

public class Server {

	final static int PORT_TCP = 1028;
	private static int port;
	static Informations info;
	ServerSocket serverSocket;

	public Server(int port) {
		Server.port = port;
		info = new Informations();
	}

	public void execute() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {

			System.out.println("--------------- Server is listening on port " + PORT_TCP + " --------------\n");

			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("New user connected");

				UserThread newUser = new UserThread(socket, this);
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

	/**
	 * Delivers a reply to user who asked
	 */
	void accuse_Reception(String message, UserThread excludeUser) {
		for (UserThread aUser : info.getUserThreads()) {
			if (aUser == excludeUser) {
				aUser.sendMessage(message);
			}
		}
	}

	/**
	 * Stores the newly connected client.
	 */

	public static void add_User(String username, Socket clt) throws IOException {
		info.setnUsers(info.getnUsers() + 1);
		String idUser = "User00" + info.getUsers();
		User user = new User(idUser, username, clt.getPort());

		info.addUser(user);
	}
	
	String getUserId(String userName) {
    	for(User user: info.getUsers()) {
    		if (info.hasUser(userName))
        		return user.getId_User();
    	}
		return null;
    }
}