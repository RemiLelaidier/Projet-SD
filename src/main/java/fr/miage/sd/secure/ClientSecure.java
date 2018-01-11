package fr.miage.sd.secure;

import fr.miage.sd.utils.Console;
import fr.miage.sd.utils.Folder;

import java.io.*;
import java.security.*;
import java.util.*;

import javax.net.ssl.*;

public class ClientSecure implements Runnable{
	
	
	  public ClientSecure( String host, int port ) {
		    connect(host, port);
		    new Thread( this ).start();
		  }
	  
	  /**
	   * Input stream
	   */
	  private DataInputStream din;

	  /**
	   * Connection to the fr.miage.sd.client
	   */
	  private DataOutputStream dout;

	  /**
	   * nb aleatoire securise
	   */
	  static private SecureRandom secureRandom;
	  
	  /**
	   * keystore contenant la paire cle public / privee du fr.miage.sd.client (cf: fr.miage.sd.client.privee)
	   */
	  private KeyStore clientKeyStore;
	  
	  
	  /**
	   * A list of visible postings
	   */
	  private Set postings = new HashSet();
	  
	  
	  // creation d'un keystore contenant la cle public du serveur (cf: fr.miage.sd.server.public)
	  private KeyStore serverKeyStore;
	  
	  
	  // SocketFactory pour authentifier le serveur distant
	  private SSLContext sslContext;
	  
	  // Passphrase for accessing our authentication keystore
	  static private final String passphrase = "clientpw";

	/**
	 * Run
	 */
	public void run() {
		try {
			while (true) {
				// Show Menu
				Console.printClientMenu();
				// Determine action to perform
				Scanner in = new Scanner(System.in);
				switch (in.nextInt()){
					case 1:
						// TODO Liste des transactions
						break;
					case 2 :
						// TODO Miser sur enchere en cours
						break;
					case 3 :
						// TODO Achat immédiat
						break;
					case 4 :
						// TODO Miner Bloc
						break;
					case 5 :
						System.exit(1);
						break;
					default :
						System.out.println("Erreur");
				}

				Posting posting = Posting.read( din );
				postings.add( posting );
			}
		} catch( IOException ie ) {
			ie.printStackTrace();
		}
	}

	/**
	 * Main
	 * @param args
	 */
	static public void main( String args[] ) {
		if (args.length != 2) {
			System.err.println( "Usage: java Client [hostname] [port number]" );
			System.exit( 1 );
		}

		String host = args[0];
		int port = Integer.parseInt( args[1] );

		System.out.println( "Wait while fr.miage.sd.secure random numbers are initialized...." );
		secureRandom = new SecureRandom();
		secureRandom.nextInt();
		System.out.println( "Done." );

		new ClientSecure( host, port );
	}




	  /**
	   * Un SSLContext contient toutes les informations de clé et de certificat 
	   *  et est utilisé pour créer 
	   * une SSLSocketFactory , qui à son tour crée des sockets sécurisées.
	   * */
	  private void setupSSLContext() throws GeneralSecurityException, IOException {
		  	//pour authentifier au serveur distant
		    TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
		    tmf.init( serverKeyStore );

		    //pour crypter et decrypter des donnees
		    KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
		    kmf.init( clientKeyStore, passphrase.toCharArray() );

		    sslContext = SSLContext.getInstance( "TLS" ); /* TLS := SSL (ancien nom */
		    sslContext.init( kmf.getKeyManagers(),			/*creation de sslContext*/
		                     tmf.getTrustManagers(),
		                     secureRandom );
		  }


	/**
	 * Creation clef publique serveur
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	private void setupServerKeystore() throws GeneralSecurityException, IOException {
		  serverKeyStore = KeyStore.getInstance( "JKS" );
		  serverKeyStore.load(new FileInputStream(Folder.getResDirectory() + "server.public" ),
		                        "public".toCharArray() );
	}

	/**
	 * Creation jeu de clef pour le client
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	private void setupClientKeyStore() throws GeneralSecurityException, IOException {
		  clientKeyStore = KeyStore.getInstance( "JKS" );
		  clientKeyStore.load(new FileInputStream( Folder.getResDirectory() + "client.private" ),
		                       passphrase.toCharArray() );
	}


	/**
	 * Connexion au serveur
	 * @param host
	 * @param port
	 */
	private void connect( String host, int port ) {
		try {
		      setupServerKeystore();
		      setupClientKeyStore();
		      setupSSLContext();

		      SSLSocketFactory sf = sslContext.getSocketFactory();
		      SSLSocket socket = (SSLSocket)sf.createSocket( host, port );

		      InputStream in = socket.getInputStream();
		      OutputStream out = socket.getOutputStream();

		      din = new DataInputStream( in );
		      dout = new DataOutputStream( out );
		} catch( GeneralSecurityException | IOException gse ) {
		      gse.printStackTrace();
		}
	}

	/**
	 * new Posting
	 * @param posting
	 */
	private void newPosting( Posting posting ) {
		postings.add( posting );
		try {
			posting.write( dout );
		} catch( IOException ie ) {
			ie.printStackTrace();
		}
	}
}
