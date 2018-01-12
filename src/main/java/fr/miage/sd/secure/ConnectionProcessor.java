package fr.miage.sd.secure;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ConnectionProcessor implements Runnable {

	  /**
	   * The Server we are running inside
	   */
	  private ServerSecure server;

	  /**
	   * The socket connected to our fr.miage.sd.client
	   */
	  private Socket socket;

	  /**
	   * Connection to the fr.miage.sd.client
	   */
	  private DataInputStream din;

	  /**
	   * Connection to the fr.miage.sd.client
	   */
	  private DataOutputStream dout;
	  
	  
	  /**
	   * Create a new ConnectionProcessor
	   */
	  public ConnectionProcessor( ServerSecure server, Socket socket ) {
	    this.server = server;
	    this.socket = socket;
	    new Thread( this ).start();
	  }


	  /**
	   * Send all the postings we were too late for, so
	   * we have the whole history of the session
	   */
	  private void sendExistingPostings() throws IOException {
	    for (Iterator it=server.getPostings(); it.hasNext();) {
	      Posting posting = (Posting)it.next();
	      System.out.println( "hist "+posting );
	      sendPosting( posting );
	    }
	  }

	  /**
	   * Deal with an incoming posting
	   */
	  private void processPosting( Posting posting ) throws IOException {
	    sendToOtherClients( posting );
	    server.addPosting( posting );
	  }

	  /**
	   * Send a posting to all clients but this one
	   */
	  private void sendToOtherClients( Posting posting ) throws IOException {
	    for (Iterator it=server.getConnections(); it.hasNext();) {
	      ConnectionProcessor cp = (ConnectionProcessor)it.next();

	      if (cp==this) {
			System.out.println( "forward "+this+" "+cp+" (skip)" );
	        continue;
	      }

			System.out.println( "forward "+this+" "+cp );
	      cp.sendPosting( posting );
	    }
	  }

	  /**
	   * Send a new posting to this fr.miage.sd.client
	   */
	  private void sendPosting( Posting posting ) throws IOException {
	    posting.write( dout );
	  }

	  /**
	   * Background thread: read new text strings from the fr.miage.sd.client,
	   * and forward them to all other clients
	   */
	  public void run() {
	    try {
	      InputStream in = socket.getInputStream();
	      OutputStream out = socket.getOutputStream();

	      din = new DataInputStream( in );
	      dout = new DataOutputStream( out );

	      sendExistingPostings();

	      while (true) {
	        Posting posting = Posting.read( din );

	        processPosting( posting );
	      }
	      

	    } catch( IOException ie ) {
	      try {
	        socket.close();
	      } catch( IOException ie2 ) {
	        System.out.println( "Error closing socket "+socket );
	      }

	      server.removeConnection( this );

	      System.out.println( "Closed connection from socket "+socket );
	    }
	  }
	  
	  
	  
}
