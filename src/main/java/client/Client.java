package main.java.client;

import java.io.IOException;
import java.net.*;

public class Client {

	public static void main(String[] str){
		
		Socket socket;
		try {
		socket = new Socket("localhost",15000);
		socket.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
