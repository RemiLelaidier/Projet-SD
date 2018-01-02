package projet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;


public class Serveur {
	static Connection connection;
	static ResultSet result;
	static java.sql.Statement statement;
	

	public static void main(String[] str) {
		//Creationtable();
		Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
		ServerSocket socket;
		enregistrement("cl1", 15, "marteau", date);
		// retrait(1);
		//recherche("clous");
		// retrait(5)
		//mise(25, -8);
		try {

			socket = new ServerSocket(15000);
			Thread t = new Thread(new Acceptation(socket));
			t.start();

			connection = connect();
			statement = connection.createStatement();
			result = (ResultSet) statement.executeQuery("select * from encheres");
			ResultSetMetaData resultMeta = result.getMetaData();
			for (int i = 1; i <= resultMeta.getColumnCount(); i++)
				System.out.print("\t" + resultMeta.getColumnName(i).toUpperCase() + "\t ||");
			System.out.println("\n");

			while (result.next()) {
				for (int i = 1; i <= resultMeta.getColumnCount(); i++)
					System.out.print("\t" + result.getObject(i).toString() + "\t --");

				System.out.println("\n");

			}
			result.close();
			statement.close();

		} catch (IOException e) {

			System.out.println(e.getMessage());
		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}
	}
	
	public static void Creationtable() {
		try {
	      String sql = "CREATE TABLE IF NOT EXISTS encheres (\n"
	                + "	id_ec integer PRIMARY KEY auto_increment,\n"
	                + "	client varchar (25),\n"
	                + "	prix double,\n"
	                + "	objet_vente varchar (25),\n"
	                + "	date timestamp \n"
	                + ");";
	      String sql2 = "CREATE TABLE IF NOT EXISTS ventes (\n"
	                + "	id_cl integer PRIMARY KEY auto_increment,\n"
	                + "	client varchar (25),\n"
	                + "	objet_achat varchar (25),\n"
	                + "	date timestamp \n"
	                + ");";
	      connection = connect();
			statement = connection.createStatement();
			statement.execute(sql);
			statement.execute(sql2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Connection connect() {

		String url = "jdbc:mysql://localhost/projet_sd";
		String login = "root";
		String passwd = "";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = (Connection) DriverManager.getConnection(url, login, passwd);
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void retrait(int id) {

		try {
			String delete = "DELETE FROM encheres WHERE id_ec=" + id;
			connection = connect();
			statement = connection.createStatement();
			statement.executeUpdate(delete);
		} catch (SQLException e) {
			System.out.println(e.getMessage());

		}

	}

	public static void enregistrement(String client, double prix, String objet, Timestamp date) {
		try {
			if(prix<0) {
				System.out.println("veuillez entrer une mise positive");	
				} else{
			connection = connect();
			statement = connection.createStatement();
			PreparedStatement preparedstatement = (PreparedStatement) connection.prepareStatement(
					"INSERT INTO encheres(client, prix,objet_vente,date) VALUES (?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			preparedstatement.setTimestamp(4, date);
			preparedstatement.setString(3, objet);
			preparedstatement.setFloat(2, (float) prix);
			preparedstatement.setString(1, client);
			preparedstatement.executeUpdate();
				}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void recherche(String objet_vente) {
		try {
			String recherche = "select * from encheres where objet_vente='" + objet_vente + "'";
			connection = connect();
			statement = connection.createStatement();
			result = (ResultSet) statement.executeQuery(recherche);
			result.last();
			int nombre = result.getRow();
			if (nombre == 0) {
				System.out.println("aucun produit ne correspond a votre recherche");
			} else {
				System.out.println("il existe " + nombre + " objets correpondant � votre recherche");

			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void mise(int id, double prix) {
		try {
			if(prix<0) {
			System.out.println("veuillez entrer une mise positive");	
			} else{
			java.sql.PreparedStatement prep = connection
					.prepareStatement("update encheres set prix = prix + ? where id = ?");
			prep.setDouble(1, +prix);
			prep.setInt(2, +id);
			prep.executeUpdate();
			prep.close();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}
	public static void achat(int id) {
		
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