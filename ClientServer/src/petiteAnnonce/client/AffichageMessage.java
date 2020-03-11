package petiteAnnonce.client;

import java.io.InputStream;
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
	
	
	public void affichage(String code) {

		switch (code) {
		case "ajout":
			System.out.println("\n'''''''''''''''' Annonce sauvegardee avec succes");
			break;
		case "SuppressionOK":
			System.out.println("");
			break;
		case "0annonce":
			System.out.println("\nNot yet any announce");
			break;
			
		case "0myannonce":
			System.out.println("\nYou don't have any announce");
			break;
		case "suppressionOK":
			System.out.println("\nDeleted with success");
			break;
		case "suppressionKO":
			System.out.println("\nThis announce doesn't exist");
			break;
		case "entete":
			System.out.println("\nId-Ann ------- Titre -------- Domaine -------- Prix -------- Description -------- User\n");

			break;
		case "DestinateurKO":
			System.out.println("Refused connexion with Seller");
			break;

		default:
			System.out.println("Erreur Inconnue");
			break;


		}
	}

	

	public void write(PrintWriter writer, String message) {
		writer.println(message);
		writer.flush();
	}

	public String read(String message) {

		return message;
	}

	public String read(InputStream reader) {

		return null;
	}
	
	public void writeSecure(PrintWriter writer, String message, String key) {
		writer.println(safety.encrypt(message, key));
		//writer.println(message);
		writer.flush();

	}
	public String writeSecure1(String message, String key) {
		return (safety.encrypt(message, key));

	}
	public String readSecure(String message, String key) {

		return safety.decrypt(message, key);
		//return message;

	}

}
