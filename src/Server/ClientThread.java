package Server;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ClientThread extends Thread {
	Socket socket;
	String message;
	int numCli;
	static ArrayList<String> idlist = new ArrayList<>();
	static ArrayList<String> chatlist = new ArrayList<>();
	static ArrayList<String> winmessage = new ArrayList<>();

	public ClientThread(Socket socket, int numCli) {
		this.socket = socket;
		this.numCli = numCli;
	}

	public void run() {
		while (!Server.endgame) {
			try {

				try {
					message = ServerUtils.receive(socket);
				} catch (SocketException | EOFException e) {
					break;
				}

				if (message.substring(0, 1).equals("W")) {
					Server.endgame = true;
					winmessage.add(message);
					ServerUtils.broadcast(winmessage);

				} else if (message.substring(0, 1).equals("c")) {

					chatlist.add(null);
					chatlist.set(0, message.substring(0, message.length()));
					ServerUtils.broadcast(chatlist);

				} else if (message.substring(0, 1).equals("b")) {

					Server.bulletsProp.add(message);

					Thread generateBulletCoords = new Thread(() -> {
						try {
							ServerBullet.generateBulletCoords(message);
						} catch (InterruptedException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
					generateBulletCoords.start();

				} else {

					String id = message.substring(message.length() - 5, message.length());

					boolean update = false;
					for (int i = 0; i < Server.tanksProp.size(); i++) {
						if ((Server.tanksProp.get(i).substring(Server.tanksProp.get(i).length() - 5,
								Server.tanksProp.get(i).length())).equals(id)) {
							Server.tanksProp.set(i, message);
							update = true;
							break;
						} else {
							update = false;
						}
					}
					if (!update) {
						Server.tanksProp.add(message);
						idlist.add(id);
					}

					ServerUtils.broadcast(Server.tanksProp);
				}

			} catch (IOException e) {
				break;
			}
		}
	}
}
