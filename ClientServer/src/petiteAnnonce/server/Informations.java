package petiteAnnonce.client;

import java.util.HashSet;
import java.util.Set;
import petiteAnnonce.server.UserThread;

class Message{
	
	protected String intitule;
	protected String idMsg;
	protected String _src;
	protected String _dest;
	
	public Message(String id, String corps, String src, String dest) {
		idMsg = id;
		intitule = corps;
		_src = src;
		_dest = dest;
	}
}

public class Informations {

	private Set<User> users;
	private Set<UserThread> userThreads;
	protected Set<Annonce> an;
	private int nAnnonce;
	//public Scanner input;
	public Set<Message> msg;

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
	 * @return the nAnnonce
	 */
	public int getnAnnonce() {
		return nAnnonce;
	}

	/**
	 * @param nAnnonce the nAnnonce to set
	 */
	public void setnAnnonce(int nAnnonce) {
		this.nAnnonce = nAnnonce;
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
			System.out.println("Bye bye " + u.getUserName());
		}
	}

	/**
	 * Returns true if there are other users connected (not count the currently
	 * connected user)
	 */
	public boolean hasUser(String userName) {
		boolean exist = false;
		for (User user : users) {
			if (user.username == userName)
				exist = true;
		}
		return exist;
	}

	
	public Informations() {
		users = new HashSet<>();
		userThreads = new HashSet<>();
		an = new HashSet<>();
		nAnnonce = 0;
		msg = new HashSet<>();
	}

}
