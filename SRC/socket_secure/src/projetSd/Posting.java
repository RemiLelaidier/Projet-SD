package projetSd;

import java.io.*;
public class Posting {

	  private String text;

	  
	  private Posting() {
	  }

	  public Posting( String text) {
	    this.text = text;
	  }
	  static Posting read( DataInputStream din ) throws IOException {
	    Posting posting = new Posting();
	    posting.text = din.readUTF();
	    int r = din.readInt();
	    
	System.out.println( "read "+posting );
	    return posting;
	  }

	  public void write( DataOutputStream dout ) throws IOException {
	    dout.writeUTF( text );

	System.out.println( "wrote "+this );
	    dout.flush();
	  }
	  
	  
	  public String toString() {
		    return text;
		  }
}
