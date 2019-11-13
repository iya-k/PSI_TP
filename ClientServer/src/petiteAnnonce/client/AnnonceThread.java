package petiteAnnonce.client;
 
import java.io.*;
import java.net.*;
import java.util.Scanner;
 
/**
 * @author KABA
 *
 */
public class AnnonceThread extends Thread {
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private String userName;
    private Scanner input;
	//Informations info;
	private String clef;
	private String limit = "//";
 
    public AnnonceThread(Socket socket, String client) {
        this.socket = socket;
        this.userName = client;
 
        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public void run() {


        input = new Scanner(System.in);
    	System.out.print("\nHello! Your name: ");
		
		userName = input.nextLine();
		if (userName != null || userName != "\0") {
		
		//System.out.print("[" + userName + "]: ");
		writer.println(userName);
		
		try {
			String response = reader.readLine();
			System.out.println("\n" + response);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
       
        do {
        	String response;
			try {
				
			char choix = '\0';

			System.out.print(" \n\n ======================== \n"
					+ "|| a. Add                ||\n|| b. All Announces      ||\n|| c. My Annonces        ||\n|| "
					+ "d. Send Messages      ||\n|| e. Quit               ||"
					+ "\n ======================== \n\n"
					+ "Votre choix: ");
			choix =  input.next().charAt(0);
			switch (choix) {
			case 'a':
				System.out.println("\n''''''''''''''''''''''''''' Add an Announce");
				add_Announce();
				System.out.println("\n''''''''''''''''''''''''''' Saved with success");
				response = reader.readLine();
				System.out.println("\n'''''''''''''''''''''''''''" + response);
				break;
			case 'b':
				System.out.println("\n''''''''''''''''''''''''''' All Announces");
				all_Announces();
				response = reader.readLine();
				System.out.println("\n\t" + response);	
				System.out.println("\n'''''''''''''''''''''''''''FIN DES ANNONCES ");
				break;
			case 'c':
				System.out.println("\n''''''''''''''''''''''''''' My Announces ");
				//my_Announces();
				break;
			case 'd':
				System.out.println("''''''''''''''''''''''''''' Sending Messages ");
				//send_Message();
				break;
			case 'e':
				//sign_out();
				clef = "quit";
				writer.println(clef);
				response = reader.readLine();
				System.out.println("\n'''''''''''''''''''''''''''" + response);
				socket.close();
				break;

			default:
				System.out.println("'''''''''''''''''''''''''''Choise between [a,e] ");
				writer.println("default");
				break;
			}
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }while (true);
        
		}
    }
    
    public void add_Announce() {
    	String annonce;
    	String titre;
    	String domaine;
    	String prix;
    	String descriptif;
    	clef = "add-Ann";
    	input = new Scanner(System.in);
    	
    	System.out.print("\nTitre: ");
    	titre = input.nextLine();
    	System.out.print("\nDomaine (voiture, moto, musique, �lectrom�nager, t�l�phone, autres): ");
    	domaine = input.nextLine();
    	System.out.print("\nPrix: ");
    	prix = input.nextLine();
    	System.out.print("\nDescriptif: ");
    	descriptif = input.nextLine();
    	
    	annonce = clef+limit+titre+limit+domaine+limit+prix+limit+descriptif;
		writer.println(annonce);
    	
    }
    
    public void all_Announces() {
    	/*
    	 * void printUsers() { if (server.hasUsers()) {
    	 * writer.println("Connected users: " + server.getUserNames()); } else {
    	 * writer.println("No other users connected"); } }
    	 */
    	
    	clef = "all-Ann"+limit;
    	
		writer.println(clef);
    	
    }
}