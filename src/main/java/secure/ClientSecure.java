package main.java.secure;

import main.java.utils.Folder;

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
	   * Connection to the client
	   */
	  private DataInputStream din;

	  /**
	   * Connection to the client
	   */
	  private DataOutputStream dout;

	  /**
	   * nb aleatoire securise
	   */
	  static private SecureRandom secureRandom;
	  
	  /**
	   * keystore contenant la paire cle public / privee du client (cf: client.privee)
	   */
	  private KeyStore clientKeyStore;
	  
	  
	  /**
	   * A list of visible postings
	   */
	  private Set postings = new HashSet();
	  
	  
	  /**
	   * creation d'un keystore contenant la cle public du serveur (cf: server.public)
	   */
	  private KeyStore serverKeyStore;
	  
	  
	  /**
	   * SocketFactory pour authentifier le serveur distant
	   */
	  private SSLContext sslContext;
	  
	  /**
	   * Passphrase for accessing our authentication keystore
	   */
	  static private final String passphrase = "clientpw";
	  
	  

	  
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
	  
	 
	  
	  //creattion de keystore (cle public serveur) et lecture
	  private void setupServerKeystore() throws GeneralSecurityException, IOException {
		    serverKeyStore = KeyStore.getInstance( "JKS" );
		    serverKeyStore.load( 
		    	new FileInputStream(Folder.getResDirectory() + "server.public" ),
		                        "public".toCharArray() );
		  }

	  	//creattion de keystore (cle public/privee ) et lecture
		  private void setupClientKeyStore() throws GeneralSecurityException, IOException {
		    clientKeyStore = KeyStore.getInstance( "JKS" );
		    clientKeyStore.load( 
		    	new FileInputStream( Folder.getResDirectory() + "client.private" ),
		                       passphrase.toCharArray() );
		  }
	  
	  
		 //connnection au serveur
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
		    } catch( GeneralSecurityException gse ) {
		      gse.printStackTrace();
		    } catch( IOException ie ) {
		      ie.printStackTrace();
		    }
		  }
	  
	  
	  public void run() {
		    try {
		      while (true) {
		        Posting posting = Posting.read( din );
		        postings.add( posting );
		      }
		    } catch( IOException ie ) {
		      ie.printStackTrace();
		    }
		  }
	  
		  
		  private void newPosting( Posting posting ) {
		    postings.add( posting );
		    try {
		      posting.write( dout );
		    } catch( IOException ie ) {
		      ie.printStackTrace();
		    }
		  }
		  
		  static public void main( String args[] ) {
			    if (args.length != 2) {
			      System.err.println( "Usage: java Client [hostname] [port number]" );
			      System.exit( 1 );
			    }

			    String host = args[0];
			    int port = Integer.parseInt( args[1] );

			    System.out.println( "Wait while secure random numbers are initialized...." );
			    secureRandom = new SecureRandom();
			    secureRandom.nextInt();
			    System.out.println( "Done." );

			    new ClientSecure( host, port );
			  }

}
