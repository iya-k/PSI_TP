package petiteAnnonce.client;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import petiteAnnonce.server.UserThread;

public class Informations {

	private Set<User> users;
	private Set<UserThread> userThreads;
	protected Set<Annonce> an;
	private int nUsers;
	public Scanner input;

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<UserThread> getUserThreads() {
		return userThreads;
	}

	public void setUserThreads(Set<UserThread> userThreads) {
		this.userThreads = userThreads;
	}

	public Set<Annonce> getAnnonce() {
		return an;
	}

	public void setAnnonce(Set<Annonce> an) {
		this.an = an;
	}

	public void addAnnonce(Annonce a) {

		an.add(a);
	}

	public void delAnnonce(Annonce a) {

		an.remove(a);
	}

	/**
	 * @return the nUsers
	 */
	public int getnUsers() {
		return nUsers;
	}

	/**
	 * @param nUsers the nUsers to set
	 */
	public void setnUsers(int nUsers) {
		this.nUsers = nUsers;
	}

	/**
	 * Stores newly connected client.
	 */
	public void addUser(User user) {

		users.add(user);
	}

	/**
	 * When a client is disconneted, removes the associated user and UserThread
	 */
	public void removeUser(User u, UserThread aUser) {
		boolean removed = users.remove(u);
		if (removed) {
			userThreads.remove(aUser);
			System.out.println("The user " + u + " quitted");
		}
	}

	/**
	 * Delivers a announce from one user to others (broadcasting)
	 *//*
		 * void broadcast(Annonce a, UserThread excludeUser) { System.out.
		 * println("===================== Liste des annonces ========================");
		 * for (UserThread aUser : userThreads) { if (aUser == excludeUser) {
		 * System.out.println(a.toString()); } }
		 * 
		 * }
		 */

	/**
	 * Returns true if there are other users connected (not count the currently
	 * connected user)
	 */
	public boolean hasUser(String userName) {
		boolean exist = false;
		for (User user : users) {
			if (user.getUserName() == userName)
				exist = true;
		}
		return exist;
	}

	public Informations() {
		users = new HashSet<>();
		userThreads = new HashSet<>();
		an = new HashSet<>();
		nUsers = 0;
		input = new Scanner(System.in);
	}

}
