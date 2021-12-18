package Client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Tank {

	double posX, posY;
	Color c;
	String status;
	static int dim = 40;
	Rectangle r;
	Circle kolo;
	Line lufa;
	static int counter;
	ImageView iv = null;

	public Tank(double posX, double posY, Color c, String status, String kier) {

		this.posX = posX;
		this.posY = posY;
		this.c = c;

		double x, y, xk, yk;

		// Ostateczne wspolrzedne czolgu, zmodyfikowane
		x = Math.min(Math.max(posX, 0), DrawBattlefield.mapWidth);
		y = Math.min(Math.max(posY, 0), DrawBattlefield.mapHeight);

		// Wspolrzedne konca lufy
		xk = x + dim / 2;
		yk = y + dim / 2 - dim * 3 / 4;

		r = new Rectangle(x, y, dim, dim);
		r.setFill(c);

		kolo = new Circle(x + dim / 2, y + dim / 2, dim / 3);
		kolo.setFill(Color.BLACK);

		if (kier.equals("5000")) {
			xk = x + dim / 2;
			yk = y + dim / 2 - dim * 3 / 4;
		}

		if (kier.equals("0500")) {
			xk = x + dim / 2;
			yk = y + dim / 2 + dim * 3 / 4;
		}

		if (kier.equals("0050")) {
			xk = x + dim / 2 + dim * 3 / 4;
			yk = y + dim / 2;
		}

		if (kier.equals("0005")) {
			xk = x + dim / 2 - dim * 3 / 4;
			yk = y + dim / 2;
		}

		lufa = new Line(x + dim / 2, y + dim / 2, xk, yk);
		lufa.setStrokeWidth(10);

		Platform.runLater(() -> {
			if (status.equals("aliv")) {
				DrawBattlefield.gameScene.getChildren().addAll(r);
				DrawBattlefield.gameScene.getChildren().addAll(kolo);
				DrawBattlefield.gameScene.getChildren().addAll(lufa);
			} else {
				drawWreck(x, y);
			}
		});
	}

	private void drawWreck(double x, double y) {

		DrawBattlefield.gameScene.getChildren().addAll(lufa);

		Platform.runLater(() -> {
			DrawBattlefield.gameScene.getChildren().remove(iv);
			Image image;
			try {
				image = new Image(new FileInputStream("pics/Wreck.png"));
				iv = new ImageView(image);
				iv.setX(x);
				iv.setY(y);
				DrawBattlefield.gameScene.getChildren().addAll(iv);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}

	public static void moveTank(ArrayList<String> tanksProp, int[] dir)
			throws IOException, UnsupportedAudioFileException, LineUnavailableException {

		String nt = null;
		double[] xy;

		for (String t : tanksProp) {
			if ((t.substring(t.length() - 5, t.length())).equals(ClientGameWorld.id)) {

				String[] tankprop;
				tankprop = t.split(" ");

				playWallCollisionSound(tankprop[0], tankprop[1]);

				xy = collisionDetectT(tanksProp, dir,
						Math.min(Math.max(Double.parseDouble(tankprop[0]) + dir[2] - dir[3], 0),
								DrawBattlefield.mapWidth - dim),
						Math.min(Math.max(Double.parseDouble(tankprop[1]) - dir[0] + dir[1], 0),
								DrawBattlefield.mapHeight - dim));

				nt = xy[0] + " " + xy[1] + " " + tankprop[2] + " " + arrayToString(dir) + " aliv " + ClientGameWorld.id;
				break;
			}
		}

		ClientUtils.send(ClientGameWorld.s, nt);
	}

	private static void playWallCollisionSound(String x, String y)
			throws MalformedURLException, UnsupportedAudioFileException, IOException, LineUnavailableException {
		if ((Double.parseDouble(x) <= 0) || (Double.parseDouble(x) >= DrawBattlefield.mapWidth - dim)
				|| (Double.parseDouble(y) <= 0) || (Double.parseDouble(y) >= DrawBattlefield.mapHeight - dim))
			ClientUtils.playSound("crash.wav");
	}

	private static String arrayToString(int[] array) {

		StringBuilder string = new StringBuilder();

		for (int i = 0; i < array.length; i++) {
			string.append(array[i]);
		}
		return string.toString();
	}

	public static String generateTank() {

		double x = Math.min(DrawBattlefield.mapWidth * (Math.random()), DrawBattlefield.mapWidth - dim);
		double y = Math.min(DrawBattlefield.mapHeight * (Math.random()), DrawBattlefield.mapHeight - dim);
		String kierunekLufy = "5000";
		Color c = new Color(Math.random(), Math.random(), Math.random(), (Math.random() * 0.5) + 0.5);

		String color = c.toString();
		String tankProp = x + " " + y + " " + color + " " + kierunekLufy + " aliv " + ClientGameWorld.id;

		return tankProp;

	}

	public static double[] collisionDetectT(ArrayList<String> tanksProp, int[] dir, double x, double y)
			throws MalformedURLException, UnsupportedAudioFileException, IOException, LineUnavailableException {

		double nx = x;
		double ny = y;
		// Od lewej

		for (String t : tanksProp) {
			
			String[] tankProp;
			tankProp = t.split(" ");

			if (!t.substring(t.length() - 5, t.length()).equals(ClientGameWorld.id)) {

				if ((x > Double.parseDouble(tankProp[0]) - 40) && (x < Double.parseDouble(tankProp[0]) + 40)
						&& (y > Double.parseDouble(tankProp[1]) - 40) && (y < Double.parseDouble(tankProp[1]) + 40)) {
					ClientUtils.playSound("crash.wav");
					if ((dir[2] > 0) || (dir[3] > 0)) {
						nx = Double.parseDouble(tankProp[0]) - 8 * dir[2] + 8 * dir[3];
					}
					if ((dir[0] > 0) || (dir[1] > 0)) {
						ny = Double.parseDouble(tankProp[1]) + 8 * dir[0] - 8 * dir[1];
					}
				}
			}
		}

		double[] nxy = { nx, ny };
		return nxy;

	}

	public static double[] getBarrelEndPoint(ArrayList<String> messaget) {

		double xk = 0, yk = 0;

		for (String t : messaget) {
			String[] tankProp;
			tankProp = t.split(" ");
			if (t.substring(t.length() - 5, t.length()).equals(ClientGameWorld.id)) {
				if (tankProp[3].equals("5000")) {
					xk = Double.parseDouble(tankProp[0]) + dim / 2;
					yk = Double.parseDouble(tankProp[1]) + dim / 2 - dim * 3 / 4;
				} else if (tankProp[3].equals("0500")) {
					xk = Double.parseDouble(tankProp[0]) + dim / 2;
					yk = Double.parseDouble(tankProp[1]) + dim / 2 + dim * 3 / 4;
				} else if (tankProp[3].equals("0050")) {
					xk = Double.parseDouble(tankProp[0]) + dim / 2 + dim * 3 / 4;
					yk = Double.parseDouble(tankProp[1]) + dim / 2;
				} else if (tankProp[3].equals("0005")) {
					xk = Double.parseDouble(tankProp[0]) + dim / 2 - dim * 3 / 4;
					yk = Double.parseDouble(tankProp[1]) + dim / 2;
				}
				break;
			}
		}

		double[] xkyk = { xk, yk };

		return xkyk;

	}

}
