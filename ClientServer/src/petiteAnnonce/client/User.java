package petiteAnnonce.client;
 
import java.util.Scanner;
 
/**
 * @author KABA
 *
 */

public class User {
	
	public String getId_User() {
		return _id_User;
	}
	public void setId_User(String id_User) {
		_id_User = id_User;
	}

	public static int getPort() {
		return _port;
	}
	public static void setPort(int port) {
		_port = port;
	}

	/**
	 * @return the userName
	 */
	public static String getUserName() {
		return _userName;
	}

	/**
	 * @param username the userName to set
	 */
	public void setUserName(String username) {
		_userName = username;
	}


	private static int _port;
    private static String _userName;
    private String _id_User;
    public Scanner input;
    
    /**
	 * @param id_User
	 * @param userName
	 * @param port
	 */
	public User(String id_User, String username, int port) {
		_id_User = id_User;
		_userName = username;
		_port = port;
	}
    
}
