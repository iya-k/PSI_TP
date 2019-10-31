package tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ServiceThread extends Thread{

	protected Socket client ; 
	AtomicInteger nb_clt;

	//Definir une socket qui demande de se connecter et reçoit des messages

	public  ServiceThread(Socket clt, AtomicInteger nb){

		this.client = clt;
		nb_clt = nb;
	}

	public void run() {
		

		String request = "message", reply = "reponse";
		PrintWriter out;
		BufferedReader in;		  

		System.out.println("Nouvelle connexion de: " + client);
		nb_clt.incrementAndGet();

		try {
			out = new PrintWriter(client.getOutputStream()); 
			in = new BufferedReader(new InputStreamReader( client.getInputStream()));

			//out.println(reply);

			while (true) {

				out.println(nb_clt +" Client(s) connecte(s)");
				out.flush();
				//if(in.ready()){
					request = in.readLine();	// traiter request pour fournir reply 
					if(request.equals("quit") || request.equals("QUIT")){
						nb_clt.decrementAndGet();
						break;
					}
				//}
				Thread.sleep(3000);
			}
			out.close();
			in.close();
			client.close();

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

}