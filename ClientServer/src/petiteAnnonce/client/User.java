package petiteAnnonce.client;
/**
 * @author KABA
 *
 */

public class User {
	
	
	protected String id_User;
	protected String username;
	protected int port;

	/**
	 * @param id_User
	 * @param username
	 * @param port
	 */
	public User(String id_User, String username, int port) {
		this.id_User = id_User;
		this.username = username;
		this.port = port;
	}
	
	public String getId_User() {
		return id_User;
	}
	public void setId_User(String id_User) {
		this.id_User = id_User;
	}

	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the username
	 */
	public String getUserName() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUserName(String username) {
		this.username = username;
	}
}
