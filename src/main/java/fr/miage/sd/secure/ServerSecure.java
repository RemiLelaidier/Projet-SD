package fr.miage.sd.secure;


import fr.miage.sd.utils.Database;
import fr.miage.sd.utils.Folder;
import projetSd.ConnectionProcessor;
import projetSd.Posting;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;
import javax.net.ssl.*;

public class ServerSecure implements Runnable {
	
	  /**
	   * The port we will listen on
	   */
	  private int port;
	  
	  /**
	   * A list of visible postings
	   */
	  private Set postings = new HashSet();
	  
	  private Set connections = new HashSet();

	  /**
	   * A source of fr.miage.sd.secure random numbers
	   */
	  static private SecureRandom secureRandom;
	  
	  /**
	   * KeyStore contenant cle public fr.miage.sd.client(cf: fr.miage.sd.client.public)
	   */
	  private KeyStore clientKeyStore;
	  
	  /**
	   * KeyStore fcontenant cle public serveur avec son certificat(cf: fr.miage.sd.server.private)
	   */
	  private KeyStore serverKeyStore;
	  
	  /**
	   * Passphrase for accessing our authentication keystore
	   */
	  static private final String passphrase = "serverpw";
	  
	  /**
	   * Used to generate a SocketFactory
	   */
	  private SSLContext sslContext;
	  
	  
	  /**
	   * Create a Server that listens on the given port.
	   * Start the background listening thread
	   */
	  public ServerSecure( int port ) {
	    this.port = port;

	    new Thread( this ).start();
	  }
	  
	  private void setupSSLContext() throws GeneralSecurityException, IOException {
		  //authentification du fr.miage.sd.client distant
		    TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
		    tmf.init( clientKeyStore );

		    //decryptage des donnees
		    KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
		    kmf.init( serverKeyStore, passphrase.toCharArray() );

		    sslContext = SSLContext.getInstance( "TLS" );
		    sslContext.init( kmf.getKeyManagers(),
		                     tmf.getTrustManagers(),
		                     secureRandom );
		  }

	  
	  
	  
	  private void setupClientKeyStore() throws GeneralSecurityException, IOException {
		    clientKeyStore = KeyStore.getInstance( "JKS" );
		    clientKeyStore.load( 
		    	new FileInputStream(Folder.getResDirectory() + "client.public" ),
		                       "public".toCharArray() );
	  }

	 private void setupServerKeystore() throws GeneralSecurityException, IOException {
		    serverKeyStore = KeyStore.getInstance( "JKS" );
		    serverKeyStore.load( 
		    	new FileInputStream( Folder.getResDirectory() + "server.private" ),
		                        passphrase.toCharArray() );
	 }
		  
		  
	  /**
	   * Background thread: accept new connections
	   */
	  public void run() {
	    try {
	      setupClientKeyStore();
	      setupServerKeystore();
	      setupSSLContext();

	      SSLServerSocketFactory sf = sslContext.getServerSocketFactory();
	      SSLServerSocket ss = (SSLServerSocket)sf.createServerSocket( port );

	      // Require client authorization
	      ss.setNeedClientAuth( true );

	      System.out.println( "Listening on port "+port+"..." );
	      while (true) {
	        Socket socket = ss.accept();
	        
           BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
   		InputStreamReader isr = new InputStreamReader(socket.getInputStream());
   		
   		/* On transforme cette file en file avec tampon */
   		BufferedReader flux_entrant = new BufferedReader(isr);
   		
   		// On récupère la file de sortie
   		PrintWriter ma_sortie = new PrintWriter(socket.getOutputStream(), true);
   		System.out.println("Sortie attachée");
   		
   		String clientName = socket.getRemoteSocketAddress().toString();
   		System.out.format("Prêt à servir le Client %s\n", clientName);

   		String message_lu = new String();

   		int temp = 0;
   		while  ((message_lu = flux_entrant.readLine()) != null) {
   			temp = Integer.parseInt(message_lu);
   			switch(temp) {
   			case 1:
   				Database.listetransactions();
   				break;
   			case 2:
   				Database.listeencheres();
   				break;
   			case 3:
   				Database.mise(id, prix);
   				break;
   			case 4:
   				Database.achat(id);
   				break;

   			default:
   				break;
   			
   			}
   		}
	      }
	    } catch( GeneralSecurityException gse ) {
	      gse.printStackTrace();
	    } catch( IOException ie ) {
	      ie.printStackTrace();
	    }
	  }
	  
	  
	  /**
	   * Remove a connection that has been closed from our set
	   * of open connections
	   */
	  void removeConnection( ConnectionProcessor cp ) {
	    connections.remove( cp );
	  }

	  /**
	   * Return an iteration over open connections
	   */
	  Iterator getConnections() {
	    return connections.iterator();
	  }

	  /**
	   * Add a posting to the list of postings
	   */
	  void addPosting( Posting posting ) {
	    postings.add( posting );
	System.out.println( "list is "+postings.size() );
	  }

	  /**
	   * Return an iteration over visible postings
	   */
	  Iterator getPostings() {
	    return postings.iterator();
	  }
	  

	  
	  static public void main( String args[] ) { 
		  Database.connect();
		  Database.Creationtable();
		  if (args.length != 1) {
		      System.err.println( "Usage: java Server [port number]" );
		      System.exit( 1 );
		  }
		  int port = Integer.parseInt( args[0] );

		  System.out.println( "Wait while fr.miage.sd.secure random numbers are initialized...." );
		  secureRandom = new SecureRandom();
		  secureRandom.nextInt();
		  System.out.println( "Done." );

		  new ServerSecure( port );
	  }
}
