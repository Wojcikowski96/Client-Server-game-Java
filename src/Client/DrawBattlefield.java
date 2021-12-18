package Client;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DrawBattlefield extends Application {

	static boolean reloaded = true;
	static int counter;
	static int mapHeight = 760;
	static int mapWidth = 1200;
	static int chatHeight = 85;
	static int chatWriteHeight = 20;
	static boolean canplay = true;
	static Pane gameScene = new Pane();
	static Pane bulletScene = new Pane();
	static StackPane titleScene = new StackPane();
	static int[] dir = { 5, 0, 0, 0 };
	static Text chatGlobal;
	static ScrollPane chatGlobalHolder;
	static Label lfrags = new Label("FRAGS: 0/"+ ClientGameWorld.fraglimit);
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override

	public void start(Stage primaryStage) throws Exception {
		
		lfrags.setTranslateX(-510);
		lfrags.setTranslateY(-360);
		lfrags.setFont(new Font(20)); // set to Label
		lfrags.setStyle("-fx-font-weight: bold");

		primaryStage.setTitle("Shoot or die! Gracz numer " + ClientGameWorld.id);
		primaryStage.setAlwaysOnTop(true);

		VBox root = new VBox();
		
		StackPane universe = new StackPane();

		gameScene.setPrefHeight(mapHeight);
		gameScene.setPrefWidth(mapWidth);
		gameScene.setStyle("-fx-background-color: LIGHTGRAY");

		bulletScene.setPrefHeight(mapHeight);
		bulletScene.setPrefWidth(mapWidth);

		titleScene.setPrefHeight(mapHeight);
		titleScene.setPrefWidth(mapWidth);
		titleScene.setOpacity(0.5);
		titleScene.setAlignment(Pos.CENTER);
		titleScene.getChildren().addAll(lfrags);

		TextField chatWriting = new TextField("Type message: ");
		chatGlobal = new Text();

		chatWriting.setPrefHeight(chatWriteHeight);
		chatWriting.setPrefWidth(mapWidth);

		Pane chatHolder = new Pane();
		chatGlobalHolder = new ScrollPane();
		chatGlobalHolder.setPrefHeight(chatHeight);
		chatHolder.getChildren().addAll(chatWriting);

		chatWriting.setDisable(true);

		chatGlobalHolder.setContent(chatGlobal);

		// Creating the mouse event handler
		EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				chatWriting.setDisable(false);
				chatWriting.setText("");
			}
		};
		// Registering the event filter
		chatHolder.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);

		EventHandler<MouseEvent> e2 = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				chatWriting.setDisable(true);
				chatWriting.setText("Type message: ");
			}
		};
		// Registering the event filter
		universe.addEventHandler(MouseEvent.MOUSE_CLICKED, e2);

		chatWriting.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {

			if (key.getCode() == KeyCode.ENTER) {
				try {
					ClientUtils.send(ClientGameWorld.s,
							"c Gracz nr " + ClientGameWorld.id + " m�wi: " + chatWriting.getText());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				chatWriting.setDisable(true);
				chatWriting.setText("Type message:");
			}

		});

		universe.getChildren().addAll(gameScene, bulletScene, titleScene);

		// setup scene
		Scene scene = new Scene(root, mapWidth, mapHeight + chatHeight + chatWriteHeight);

		primaryStage.setScene(scene);

		root.getChildren().addAll(universe, chatGlobalHolder, chatHolder);

		scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
			if (!ClientGameWorld.dead) {
				try {

					if (((key.getCode() == KeyCode.UP) || (key.getCode() == KeyCode.DOWN)
							|| (key.getCode() == KeyCode.RIGHT) || (key.getCode() == KeyCode.LEFT)) && (canplay)) {
						ClientUtils.playSound("moveTank.wav");
						canplay = false;
					}
					if (key.getCode() == KeyCode.SPACE && reloaded) {
						reloaded = false;
						ClientUtils.playSound("fire.wav");
						double[] barrelendpoint = Tank.getBarrelEndPoint(ClientGameWorld.messaget);
						ClientBullet.fire(barrelendpoint[0], barrelendpoint[1], dir);
						Reload();
					}
					if (key.getCode() == KeyCode.UP) {
						Arrays.fill(dir, 0);
						dir[0] = 5;
						Tank.moveTank(ClientGameWorld.messaget, dir);
					}
					if (key.getCode() == KeyCode.DOWN) {
						Arrays.fill(dir, 0);
						dir[1] = 5;
						Tank.moveTank(ClientGameWorld.messaget, dir);
					}
					if (key.getCode() == KeyCode.RIGHT) {
						Arrays.fill(dir, 0);
						dir[2] = 5;
						Tank.moveTank(ClientGameWorld.messaget, dir);
					}
					if (key.getCode() == KeyCode.LEFT) {
						Arrays.fill(dir, 0);
						dir[3] = 5;
						Tank.moveTank(ClientGameWorld.messaget, dir);
					}
				} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		scene.addEventHandler(KeyEvent.KEY_RELEASED, (key) -> {
			if (!ClientGameWorld.dead) {
				try {
					if ((key.getCode() == KeyCode.UP) || (key.getCode() == KeyCode.DOWN)
							|| (key.getCode() == KeyCode.RIGHT) || (key.getCode() == KeyCode.LEFT)) {
						ClientUtils.playSound("stopTank.wav");
						canplay = true;
					}
				} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// show the stage
		primaryStage.show();

	}

	private void Reload() {
		Timer timer = new Timer();
		counter = 3;
		Label reload = new Label("RELOADING...");
		
		reload.setTranslateX(-510);
		reload.setTranslateY(360);
		reload.setFont(new Font(20)); // set to Label
		reload.setStyle("-fx-font-weight: bold");
		
		Platform.runLater(() -> {
			DrawBattlefield.titleScene.getChildren().addAll(reload);
		});
		
		TimerTask task = new TimerTask() {
			public void run() {
				counter--;
				if (counter == 0) {
					reloaded = true;
					Platform.runLater(() -> {
						DrawBattlefield.titleScene.getChildren().remove(reload);
					});
					timer.cancel();
				} else {

				}
			}
		};
		timer.scheduleAtFixedRate(task, 0, 1000);
	}

	public static void drawTank(String message) {
		
		String[] parts;
		parts = message.split(" ");

		double x = Double.parseDouble(parts[0]);
		double y = Double.parseDouble(parts[1]);
		Color c = Color.valueOf(parts[2]);
		String kierunek = parts[3];
		String status = parts[4];

		Tank czolg = new Tank(x, y, c, status, kierunek);
	}

	public static void drawBullet(String m) throws NumberFormatException, MalformedURLException,
			UnsupportedAudioFileException, IOException, LineUnavailableException {

		String[] parts;
		parts = m.split(" ");

		if (parts[4].equals("shoot")) {
			drawFlash(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
		}
		else if (parts[4].equals("activ")) {
			drawCircle(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
		} else if (parts[4].equals("explo")) {
			drawExplosion(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
		} else {
			// Do nothing
		}
	}

	private static void drawCircle(double x, double y) {
		int rad = 5;
		Circle pocisk = new Circle(x, y, rad);
		pocisk.setFill(Color.BLACK);
		Platform.runLater(() -> {
			DrawBattlefield.bulletScene.getChildren().addAll(pocisk);
		});
	}

	public static void drawFlash(double x, double y) {

		Platform.runLater(() -> {
			try {
				// clientUtils.playSound("boom.wav");
				Image image = new Image(new FileInputStream("pics/shootFlash.png"));
				ImageView iv = new ImageView(image);
				iv.setX(x - 10);
				iv.setY(y - 10);
				DrawBattlefield.bulletScene.getChildren().addAll(iv);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	private static void drawExplosion(double x, double y)
			throws MalformedURLException, UnsupportedAudioFileException, IOException, LineUnavailableException {

		Platform.runLater(() -> {
			try {
				ClientUtils.playSound("boom.wav");
				Image image = new Image(new FileInputStream("pics/Boom.png"));
				ImageView iv = new ImageView(image);
				iv.setX(x - 30);
				iv.setY(y - 30);
				DrawBattlefield.bulletScene.getChildren().addAll(iv);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
}