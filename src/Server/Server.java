package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class Server {
	static int lastCliNum;
	static ArrayList<Socket> socketyCli = new ArrayList<Socket>();
	static ArrayList<String> tanksProp = new ArrayList<String>();
	static ArrayList<String> bulletsProp = new ArrayList<String>();
	static boolean endgame = false;
	static ServerSocket s;
	
	public static void main(String args[]) throws UnknownHostException, IOException {

		s = new ServerSocket(6666);
		int numCli = 0;
		
		while (!endgame) {
			try {
			Socket cliSock = s.accept();
			new ClientThread(cliSock, numCli++).start();
			lastCliNum = numCli;
			socketyCli.add(cliSock);
			} catch (SocketException e) {
				break;
			}
		}
		System.out.println("Koniec gry, wy��czam serwer");
	}
	
}
