package projet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class Serveur {

	public static void main(String[] zero) {

		ServerSocket socket;
		try {
			socket = new ServerSocket(15000);
			Thread t = new Thread(new Acceptatition(socket));
			t.start();
			System.out.println("commençons !");
			enregistrement("Jean", "Dupont", "Saulcy");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public static void enregistrement(String prenom, String nom, String adresse) {
		String url = "jdbc:mysql://localhost/projet_sd";
		String login = "root";
		String passwd = "";
		java.sql.Connection co = null;
		java.sql.Statement st = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			co = DriverManager.getConnection(url, login, passwd);
			st = co.createStatement();
			String sql = "INSERT INTO client "+" VALUES ('test2','test2','test2')";
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				co.close();
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

class Acceptatition implements Runnable {

	private ServerSocket socketserver;
	private Socket socket;
	private int nbclient = 1;

	public Acceptatition(ServerSocket s) {
		socketserver = s;
	}

	public void run() {

	        try {
	        	while(true){
			  socket = socketserver.accept(); // Un client se connecte on l'accepte
	                  System.out.println("Le client  "+nbclient+ " s'est connecté !");
	                  nbclient++;
	                  socket.close();
	        	}
	        
	        } catch (IOException e) {
				e.printStackTrace();
			}
		}
}
