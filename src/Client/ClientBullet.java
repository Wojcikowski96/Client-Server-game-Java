package Client;

import java.io.IOException;

public class ClientBullet {

	public static void fire(double x, double y, int[] dir) throws IOException {
		
		String id = ClientUtils.generateId();
		
		String message = "b "+ x + " "+y+" "+ ClientUtils.arrayToString(dir)+" shoot "+ ClientGameWorld.id+" "+id;
		
		ClientUtils.send(ClientGameWorld.s,message);

	}
}
