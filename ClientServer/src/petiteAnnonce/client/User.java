package petiteAnnonce.client;
/**
 * @author KABA
 *
 */

public class User {
	
	
	protected String adress_ip;
	protected String username;
	protected int port;

	/**
	 * @param adresse_ip
	 * @param username
	 * @param port
	 */
	public User(String username, String adresse, int port) {
		this.adress_ip = adresse;
		this.username = username;
		this.port = port;
	}
	
	public String getAdresseIp() {
		return adress_ip;
	}
	public void setAdresseIp(String adresse) {
		this.adress_ip = adresse;
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
