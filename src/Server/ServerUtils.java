package Server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerUtils {
	public static void broadcast(ArrayList<String> tanksProp) throws IOException {

		for (Socket s : Server.socketyCli) {
			send(s, tanksProp);
		}
	}

	public static void send(Socket s, ArrayList<String> tanksProp) throws IOException {

		synchronized (s) {

			OutputStream os = s.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(os);

			synchronized (out) {
				out.writeObject(tanksProp);
			}
		}
	}

	public static String receive(Socket s) throws IOException {

		String message;
		InputStream is = s.getInputStream();
		DataInputStream ss = new DataInputStream(is);

		message = ss.readUTF();

		return message;
	}
	
	static String[] getTankProps(String s) {
		
		String[] tankprop;
		
		tankprop=s.split(" ");
		
		return tankprop;
		
	}
}
