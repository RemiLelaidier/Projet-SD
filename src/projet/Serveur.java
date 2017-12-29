package projet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Serveur {

	public static void main(String[] str) {

		ServerSocket socket;
		try {
			socket = new ServerSocket(15000);
			Thread t = new Thread(new Acceptation(socket));
			t.start();
			System.out.println("commençons !");
			enregistrement("Jean");

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public static void enregistrement(String personne) {
		String url = "jdbc:mysql://localhost/projet_sd";
		String login = "root";
		String passwd = "";

		java.sql.Connection co = null;
		java.sql.Statement st = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			co = DriverManager.getConnection(url, login, passwd);
			st = co.createStatement();
			String sql = "INSERT INTO encheres VALUES ('test2',10.2,'test',now())";
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
				System.out.println("Le client  " + nbclient + " s'est connecté !");
				nbclient++;
				socket.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
