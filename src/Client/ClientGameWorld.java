package Client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class ClientGameWorld {

	static ArrayList<String> message = new ArrayList<String>();
	static ArrayList<String> messaget = new ArrayList<String>();
	static ArrayList<String> messageb = new ArrayList<String>();
	static ArrayList<String> messagec = new ArrayList<String>();
	static String id;
	static Socket s;
	static boolean dead = false;
	static int frags=0;
	static int fraglimit=5;
	//private static boolean endgame=false;

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException,
			UnsupportedAudioFileException, LineUnavailableException {

		id = ClientUtils.generateId();

		StringBuilder msg = new StringBuilder();

		System.out.println("Klient" + id);

		s = new Socket("127.0.0.1", 6666);

		new Thread(() -> {
			Application.launch(DrawBattlefield.class, args);
		}).start();

		ClientUtils.send(s, Tank.generateTank());

		while (true) {

			message = ClientUtils.receive(s);
			
			if (message.get(0).substring(0, 1).equals("W")) {
				checkIfIWon(message);
				//endgame=true;
			} else if (message.get(0).substring(0, 1).equals("c")) {
				messagec = message;

				msg.append("\n" + messagec.get(0).substring(2, messagec.get(0).length()));
				ClientUtils.playSound("chat.wav");
				Platform.runLater(() -> {
					DrawBattlefield.chatGlobal.setText(msg.toString());
					DrawBattlefield.chatGlobalHolder.setVvalue(1);
				});
			} else if (message.get(0).substring(0, 1).equals("b")) {
				
				messageb = message;
				
				countFrags(messageb);
				if (frags>=fraglimit)
					ClientUtils.send(s, "Wygrywa gracz nr "+id);
				
				Platform.runLater(() -> {
					DrawBattlefield.bulletScene.getChildren().clear();
				});

				for (String t : messageb) {
					DrawBattlefield.drawBullet(t);
				}

			} else {
				messaget = message;
				if (!dead)
					checkIfImDead(message);
				Platform.runLater(() -> {
					DrawBattlefield.gameScene.getChildren().clear();
				});

				for (String t : messaget) {
					DrawBattlefield.drawTank(t);
				}
			}
		}

		//s.close();
	}

	private static void checkIfIWon(ArrayList<String> m) {
		
		Platform.runLater(() -> {
			DrawBattlefield.titleScene.getChildren().clear();
		});
		
		String winnerid = ClientUtils.getProps(m.get(0))[3];
		
		Label winner = new Label();
		winner.setWrapText(true);
		winner.setTextAlignment(TextAlignment.CENTER);
		if (winnerid.equals(id)) {
			dead = true;
			winner.setText("GRATULACJE, WYGRA£EŒ!");
			System.out.println("Wygra³eœ!");
		} else {
			dead = true;
			winner.setText("PRZEGRA£EŒ, WYGRYWA GRACZ NR "+winnerid);
		}
		winner.setFont(new Font("Arial", 100));

		Platform.runLater(() -> {
			DrawBattlefield.titleScene.getChildren().addAll(winner);
		});
	}

	private static void countFrags(ArrayList<String> messageb) {
		
		for (String m : messageb) {
			if (ClientUtils.getProps(m)[4].equals("destr") && ClientUtils.getProps(m)[5].equals(id)) {
				
				frags++;
				
				Platform.runLater(() -> {
					DrawBattlefield.lfrags.setText("FRAGS: "+frags+ "/"+fraglimit);
				});
				
				break;
			}
		}
	}

	private static void checkIfImDead(ArrayList<String> message)
			throws MalformedURLException, UnsupportedAudioFileException, IOException, LineUnavailableException {

		for (String m : message) {
			if (ClientUtils.getProps(m)[4].equals("dead") && ClientUtils.getProps(m)[5].equals(id)) {
	
				System.out.println("Przegra³eœ!");
				dead = true;
				Label wasted = new Label("PRZEGRA£EŒ!");
				wasted.setFont(new Font("Arial", 100));

				Platform.runLater(() -> {
					DrawBattlefield.titleScene.getChildren().addAll(wasted);
				});
				break;
			}
		}
	}
}