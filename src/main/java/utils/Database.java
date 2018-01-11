package main.java.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Database {
	
	static Connection connection;
	static ResultSet result;
	static ResultSet result2;
	static java.sql.Statement statement;
	
	/* méthode pour se connecter à la base de donnée
	 * atttention, il est importante de garder ?useSSL=true&serverTimezone=UTC
	 * en paramètre lors de la connexion sécurisé sinon il n'est pas possible de se connecter
	 */

	public static Connection connect() {

		String url = "jdbc:mysql://localhost/projet_sd?useSSL=true&serverTimezone=UTC";
		String login = "root";
		String passwd = "";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = (Connection) DriverManager.getConnection(url, login, passwd);
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void Creationtable() {
		try {
		String sql = "CREATE TABLE IF NOT EXISTS transactions (\n"
					+ "	id integer PRIMARY KEY auto_increment,\n"
					+ "	acheteur varchar (25),\n" + "	prix double,\n" 
					+ "	objet varchar (25),\n"
					+ "	date timestamp, \n" + "	type integer " + ");";

			String sql2 = "CREATE TABLE IF NOT EXISTS historique_vente (\n"
					+ "	id_ve integer PRIMARY KEY auto_increment,\n" 
					+ "	acheteur varchar (25),\n" + "	prix double,\n"
					+ "	objet varchar (25),\n" + "	date_mise_en_vente timestamp \n" + ");";
			
			String sql3 = "CREATE TABLE IF NOT EXISTS bloc (\n"
					+ "	id integer PRIMARY KEY auto_increment,\n" + "	prev_bloc integer,\n" + "	date timestamp,\n"
					+ "	transaction1 varchar (25),\n" + "	transaction2 varchar (25),\n"
					+ "	transaction3 varchar (25),\n" + "	transaction4 varchar (25),\n"
					+ "	transaction5 varchar (25)\n" + ");";
			
			String sql4 = "CREATE TABLE IF NOT EXISTS personne (\n"
					+ "	id integer PRIMARY KEY auto_increment,\n" + "	nom varchar (25),\n" + "prenom varchar (25),\n"
					+ "	mail varchar (25) \n" + ");";
			
			String sql5 = "CREATE TABLE IF NOT EXISTS objet (\n"
					+ "	id integer PRIMARY KEY auto_increment,\n" + "	nom varchar (25),\n" + "	prix double,\n"
					+ "	possede_par varchar (25)\n" + ");";
			
			connection = connect();
			statement = connection.createStatement();
			statement.execute(sql);
		statement.execute(sql2);
			statement.execute(sql3);
			statement.execute(sql4);
			statement.execute(sql5);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
	public static void retrait(int id) {

		try {
			String delete = "DELETE FROM transactions WHERE id =" + id;
			connection = connect();
			statement = connection.createStatement();
			statement.executeUpdate(delete);
		} catch (SQLException e) {
			System.out.println(e.getMessage());

		}

	}

	public static void enregistrement(double prix, String objet, Timestamp date) {
		try {
			if (prix < 0) {
				System.out.println("veuillez entrer une mise positive");
			} else {
				connection = connect();
				statement = connection.createStatement();
				PreparedStatement preparedstatement = (PreparedStatement) connection.prepareStatement(
						"INSERT INTO transactions(prix,objet,date) VALUES (?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
				preparedstatement.setTimestamp(3, date);
				preparedstatement.setString(2, objet);
				preparedstatement.setFloat(1, (float) prix);
				preparedstatement.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void recherche(String objet_vente) {
		try {

			connection = connect();
			statement = connection.createStatement();
			result = (ResultSet) statement
					.executeQuery("select * from transactions where objet='" + objet_vente + "'");
			ResultSetMetaData resultMeta = result.getMetaData();
			result.last();
			int nombre = result.getRow();
			if (nombre == 0) {
				System.out.println("aucun produit ne correspond a votre recherche");
			}
			System.out.println("il existe " + nombre + " objets correpondant a votre recherche");
			System.out.println("voici la liste correspondant a votre recherche\n");
			for (int i = 1; i <= resultMeta.getColumnCount(); i++)
				System.out.print("\t" + resultMeta.getColumnName(i).toUpperCase() + "\t ||");
			System.out.println("\n");
			result.first();
			for (int i = 1; i <= resultMeta.getColumnCount(); i++)
				System.out.print("\t" + result.getObject(i).toString() + "\t --");

			System.out.println("\n");
			while (result.next()) {
				for (int i = 1; i <= resultMeta.getColumnCount(); i++)
					System.out.print("\t" + result.getObject(i).toString() + "\t --");

				System.out.println("\n");

			}
			System.out.println("\n\n");

			result.close();
			statement.close();
		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}
	}

	public static void mise(int id, double prix) {
		try {

			if (prix < 0) {
				System.out.println("veuillez entrer une mise positive");
			} else {
				java.sql.PreparedStatement prep = connection
						.prepareStatement("update transactions set prix = prix + ? where id_ec = ?");
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
		try {

			connection = connect();
			statement = connection.createStatement();
			PreparedStatement preparedstatement = (PreparedStatement) connection.prepareStatement(

					"INSERT INTO historique_vente(id,acheteur,prix,objet,date_mise_en_vente) select id, "
					+ "acheteur, prix, objet, date from transactions where id="+ id);
			preparedstatement.executeUpdate();
			PreparedStatement preparedstatement2 = (PreparedStatement) connection
					.prepareStatement("delete from transactions where id=" + id);
			preparedstatement2.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
