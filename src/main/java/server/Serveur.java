package main.java.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

import utils.Database;


public class Serveur {


	public static void main(String[] str) {
		Database.Creationtable();
		Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
		ServerSocket socket;
		 Database.enregistrement(15, "clous", date);
		// Database.retrait(1);
		//Database.recherche("clous");
		// Database.mise(2, 8);
		//Database.achat(2);
		try {

			socket = new ServerSocket(15000);
			Thread t = new Thread(new Acceptation(socket));
			t.start();


		} catch (IOException e) {

			System.out.println(e.getMessage());
		} 
	}

}

class Acceptation implements Runnable {

	private ServerSocket socketserver;
	private Socket socket;
	private int nbclient = 1;

	public Acceptation(ServerSocket s) {
		socketserver = s;
	}

	public void run() {

		try {
			while (true) {
				socket = socketserver.accept(); // Un client se connecte on
												// l'accepte
				System.out.println("Le client  " + nbclient + " s'est connect� !");
				nbclient++;
				socket.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}