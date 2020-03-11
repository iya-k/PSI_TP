package petiteAnnonce.server;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import petiteAnnonce.client.*;


public class Informations {

	private Set<User> users;
	private Set<UserThread> userThreads;
	protected Set<Annonce> annnonces;
	private int nAnnonce;
	public HashMap<String,UserThread> couple;

	public synchronized Set<User> getUsers() {
		return users;
	}

	public synchronized void setUsers(Set<User> users) {
		this.users = users;
	}

	public synchronized Set<UserThread> getUserThreads() {
		return userThreads;
	}

	public synchronized void setUserThreads(Set<UserThread> userThreads) {
		this.userThreads = userThreads;
	}

	public synchronized Set<Annonce> getAnnonce() {
		return annnonces;
	}

	public synchronized void setAnnonce(Set<Annonce> an) {
		this.annnonces = an;
	}

	public synchronized void addAnnonce(Annonce a) {

		annnonces.add(a);
	}

	public synchronized void delAnnonce(Annonce a) {

		annnonces.remove(a);
	}
	
	public synchronized void removeAllAnnonce(Set<Annonce> myAnnouces) {

		annnonces.removeAll(myAnnouces);
	}

	/**
	 * @return the nAnnonce
	 */
	public synchronized int getnAnnonce() {
		return nAnnonce;
	}

	/**
	 * @param nAnnonce the nAnnonce to set
	 */
	public synchronized void setnAnnonce(int nAnnonce) {
		this.nAnnonce = nAnnonce;
	}

	/**
	 * Stores newly connected client.
	 */
	public synchronized void addUser(User user) {
		users.add(user);
	}

	/**
	 * When a client is disconneted, removes the associated user and UserThread
	 */
	public synchronized void removeUser(User user, UserThread aUser) {
		boolean removed = users.remove(user);
		if (removed) {
			userThreads.remove(aUser);
			System.out.println("Bye bye " + user.getUserName());
		}
	}

	/**
	 * Returns true if there are other users connected Ò
	 */
	public synchronized boolean hasUser(String userName) {
		boolean exist = false;
		for (User user : users) {
			if (user.getUserName() == userName)
				exist = true;
		}
		return exist;
	}



	public synchronized UserThread getUserThread(String user) {

		if(couple.containsKey(user)) {
			UserThread aUser = couple.get(user);
			//System.out.println(lecteur+":"+aUser.getName());
			return aUser;
		}
		return null;
	}

	
	  public synchronized void signal(User u, String message) {
		  for(User aUser: users) {
			  if(aUser == u) {
				  u.getPw().println(message);
				  u.getPw().flush();
			  }
				  
		  }
	  }
	 


	public synchronized void accuse_Reception(String message, UserThread recepteur, User u) {
		//System.out.println("envoie "+message+" à "+recepteur);

		for (UserThread aUser : userThreads) {
			if (aUser == recepteur) {
				aUser.send(message, u);
			}
		}
	}
	
	public synchronized String getUserByAnnonce(String annonceId) {

		for (Annonce a : annnonces) {
			if (a.getId_Annonce().equals(annonceId))
				return a.getUser();
		}
		return null;
	}

	public synchronized User getUserByUserName(String userName) {

		for (User user : users) {
			System.out.println(user.getUserName());
			if (user.getUserName().equals(userName))
				return user;
		}
		return null;
	}

	public Informations() {
		users = new HashSet<>();
		userThreads = new HashSet<>();
		annnonces = new HashSet<>();
		nAnnonce = 0;
		couple = new HashMap<>();
	}

}
