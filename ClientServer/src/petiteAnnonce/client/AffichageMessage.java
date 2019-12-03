package petiteAnnonce.client;

import java.io.PrintWriter;

import petiteAnnonce.securite.AES;

public class AffichageMessage {

	AES safety = new AES();

	public void erreur(String code, Class c) {

		switch (code) {
		case "ajout":
			System.out.println(""+c.getName());
			break;
		case "SuppressionOK":
			System.out.println(""+c.getName());
			break;
		case "I/O":
			System.out.println("Error socket connexion"+c.getName());
			break;
		case "SuppressionKO":
			System.out.println(""+c.getName());
			break;
		case "DestinataireOK":
			System.out.println(""+c.getName());

			break;
		case "DestinateurKO":
			System.out.println("Refused connexion with Seller"+c.getClass());
			break;

		default:
			System.out.println("Erreur Inconnue"+c.getClass());
			break;


		}
	}

	public void write(PrintWriter writer, String message) {
		writer.println(message);
		writer.flush();
	}

	public String read(String message, String key) {

		return safety.decrypt(message, key);
	}

	public void writeSecure(PrintWriter writer, String message, String key) {
		writer.println(safety.encrypt(message, key));
		writer.flush();

	}

	public String readSecure(String message, String key) {

		return safety.decrypt(message, key);

	}

}
