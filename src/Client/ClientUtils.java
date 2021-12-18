package Client;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ClientUtils {

	public static String generateId() {
		return Integer.toString(10000 + new Random().nextInt(90000));
	}

	public static void send(Socket s, String message) throws IOException {
		OutputStream os = s.getOutputStream();
		DataOutputStream ss = new DataOutputStream(os);
		ss.writeUTF(message);
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String> receive(Socket s) throws IOException, ClassNotFoundException {

		InputStream is = s.getInputStream();
		ObjectInputStream ss = new ObjectInputStream(is);

		return (ArrayList<String>) ss.readObject();
	}
	
	static void playSound(String soundFile) throws MalformedURLException, UnsupportedAudioFileException, IOException, LineUnavailableException {
	    File f = new File("sounds/" + soundFile);
	    AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());  
	    Clip clip = AudioSystem.getClip();
	    clip.open(audioIn);
	    clip.start();
	}
	
	static String arrayToString(int[] dir) {
		
		StringBuilder string = new StringBuilder();

		for (int i = 0; i < dir.length; i++) {

			string.append(dir[i]);

		}

		return string.toString();
	}
	
	static String[] getProps(String s) {
		
		String[] prop;
		
		prop=s.split(" ");
		
		return prop;
		
	}
}