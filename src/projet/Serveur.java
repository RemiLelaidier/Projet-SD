package projet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class Serveur {
	static Connection connection;
	static ResultSet result;
	static java.sql.Statement statement;

	public static void main(String[] str) {

		ServerSocket socket;
		//enregistrement();
		//retrait(10);
		recherche("tortue");
		try {

			socket = new ServerSocket(15000);
			Thread t = new Thread(new Acceptation(socket));
			t.start();
			connection = connect();
			statement=connection.createStatement();
			result=(ResultSet) statement.executeQuery("select * from encheres");
			while(result.next()) {
				System.out.println(result.getInt("id")+"\t");
				System.out.println(result.getString("vendeur")+"\t");
				System.out.println(result.getDouble("prix")+"\t");
				System.out.println(result.getString("objet_vente")+"\t");
				System.out.println(result.getDate("date")+"\t");
				 System.out.println();
			}
			
		} catch (IOException e) {

			e.printStackTrace();
		} catch (SQLException e) {

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
			String delete = "DELETE FROM encheres WHERE id=" + id;
			connection = connect();
			statement=connection.createStatement();
			statement.executeUpdate(delete);
		} catch (SQLException e) {
			System.out.println(e.getMessage());

		}

	}

	public static void enregistrement() {
		try {
			connection=connect();
			statement=connection.createStatement();
		PreparedStatement preparedstatement = (PreparedStatement) connection.prepareStatement(
				"INSERT INTO encheres(vendeur, prix,objet_vente,date) VALUES (?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS);
		preparedstatement.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
		preparedstatement.setString(3, "Clous");
		preparedstatement.setFloat(2, (float) 10.2);
		preparedstatement.setString(1, "bobby");
		preparedstatement.executeUpdate();

		  }catch(SQLException e){
	            System.out.println(e.getMessage());
	        }
	}
	public static void recherche(String objet_vente) {
		try {
			String recherche="select * from encheres where objet_vente='"+objet_vente+"'";
			connection=connect();
			statement=connection.createStatement();
			result=(ResultSet) statement.executeQuery(recherche);
			result.last();
			int nombre = result.getRow();
	           if(nombre==0){
	               System.out.println("aucun produit ne correspond a votre recherche");
	           }else{
	                System.out.println("produit trouvé");
	               
	                
	           }
					}catch(SQLException e){
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
					System.out.println("Le client  " + nbclient + " s'est connecté !");
					nbclient++;
					socket.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

