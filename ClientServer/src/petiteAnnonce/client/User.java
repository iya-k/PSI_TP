package petiteAnnonce.client;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * @author KABA
 *
 */

public class User {
	
	
	protected String adress_ip;
	protected String username;
	protected int port;
	protected int portEcoute;
	private BufferedReader br;
	private PrintWriter pw;

	/**
	 * @param adresse_ip
	 * @param username
	 * @param port
	 * @param portEcoute
	 */
	public User(String username, String adresse, int port, int portE, BufferedReader br, PrintWriter pw) {
		
		this.adress_ip = adresse;
		this.username = username;
		this.port = port;
		portEcoute = portE;
		this.br = br;
        this.pw = pw;
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

	public int getPortEcoute() {
		return portEcoute;
	}
	public void setPortEcoute(int portE) {
		this.portEcoute = portE;
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
	

	public void setOutput(PrintWriter pw) {
        this.pw = pw;
    }

    public void setInput(BufferedReader br) {
        this.br = br;
    }

	public BufferedReader getBr() {
		return br;
	}

	public PrintWriter getPw() {
		return pw;
	}
	
}
