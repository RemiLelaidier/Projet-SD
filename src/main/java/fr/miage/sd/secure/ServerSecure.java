package fr.miage.sd.secure;


import fr.miage.sd.utils.Database;
import fr.miage.sd.utils.Folder;

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

	      // Require fr.miage.sd.client authorization
	      ss.setNeedClientAuth( true );

	      System.out.println( "Listening on port "+port+"..." );
	      while (true) {
	        Socket socket = ss.accept();
	        System.out.println( "Got connection from "+socket );
	        
	       
	      }
	    } catch( GeneralSecurityException gse ) {
	      gse.printStackTrace();
	    } catch( IOException ie ) {
	      ie.printStackTrace();
	    }
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
