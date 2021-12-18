package Server;

import java.io.IOException;

public class ServerBullet {

	static int mapHeight = 760;

	static int mapWidth = 1200;

	static String activity;

	public static void generateBulletCoords(String message) throws InterruptedException, IOException {

		activity = " activ ";

		long endLoop = 0;
		long startingValue = 0;

		String[] bulletprop;
		bulletprop = message.split(" ");

		long xstart = Math.round(Double.parseDouble(bulletprop[1]));
		long ystart = Math.round(Double.parseDouble(bulletprop[2]));

		if (bulletprop[3].equals("5000")) {

			endLoop = 0;
			startingValue = ystart;

			for (long y = startingValue; y >= endLoop; y--) {

				if (y <= startingValue && y >= startingValue - 1) {
					activity = " shoot ";
				} else if (y <= endLoop + 1 && y > endLoop) {
					activity = " explo ";
				} else if (y == endLoop) {
					activity = " wallh ";
				} else {
					activity = " activ ";
				}

				if (HitDetect.hitTank(Server.tanksProp, Server.bulletsProp)) {
					break;
				}

				String bulletcoord = "b " + bulletprop[1] + " " + y + " " + bulletprop[3] + activity + bulletprop[5]
						+ " " + bulletprop[6];

				updateBulletsArray(bulletcoord, bulletprop[6]);

				if (activity.equals(" activ ")) {
					Thread.sleep(1);
				} else
					Thread.sleep(200);

				ServerUtils.broadcast(Server.bulletsProp);
			}

			removeBullet(bulletprop[6]);

		} else if (bulletprop[3].equals("0500")) {

			endLoop = mapHeight;
			startingValue = ystart;

			for (long y = startingValue; y <= endLoop; y++) {

				if (y >= startingValue && y <= startingValue + 1) {
					activity = " shoot ";
				} else if (y >= endLoop - 1 && y < endLoop) {
					activity = " explo ";
				} else if (y == endLoop) {
					activity = " wallh ";
				} else {
					activity = " activ ";
				}

				if (HitDetect.hitTank(Server.tanksProp, Server.bulletsProp)) {
					break;
				}

				String bulletcoord = "b " + bulletprop[1] + " " + y + " " + bulletprop[3] + activity + bulletprop[5]
						+ " " + bulletprop[6];

				updateBulletsArray(bulletcoord, bulletprop[6]);

				if (activity.equals(" activ ")) {
					Thread.sleep(1);
				} else
					Thread.sleep(200);

				ServerUtils.broadcast(Server.bulletsProp);

			}

			removeBullet(bulletprop[6]);

		} else if (bulletprop[3].equals("0050")) {

			endLoop = mapWidth;
			startingValue = xstart;

			for (long x = startingValue; x <= endLoop; x++) {

				if (x >= startingValue && x <= startingValue + 1) {
					activity = " shoot ";
				} else if (x >= endLoop - 1 && x < endLoop) {
					activity = " explo ";
				} else if (x == endLoop) {
					activity = " wallh ";
				} else {
					activity = " activ ";
				}

				if (HitDetect.hitTank(Server.tanksProp, Server.bulletsProp)) {
					break;
				}

				String bulletcoord = "b " + x + " " + bulletprop[2] + " " + bulletprop[3] + activity + bulletprop[5]
						+ " " + bulletprop[6];

				updateBulletsArray(bulletcoord, bulletprop[6]);

				if (activity.equals(" activ ")) {
					Thread.sleep(1);
				} else
					Thread.sleep(200);

				ServerUtils.broadcast(Server.bulletsProp);
			}

			removeBullet(bulletprop[6]);

		} else if (bulletprop[3].equals("0005")) {

			endLoop = 0;
			startingValue = xstart;

			for (long x = startingValue; x >= endLoop; x--) {

				if (x <= startingValue && x >= startingValue - 1) {
					activity = " shoot ";
				} else if (x <= endLoop + 1 && x > endLoop) {
					activity = " explo ";
				} else if (x == endLoop) {
					activity = " wallh ";
				} else {
					activity = " activ ";
				}

				if (HitDetect.hitTank(Server.tanksProp, Server.bulletsProp)) {
					break;
				}

				String bulletcoord = "b " + x + " " + bulletprop[2] + " " + bulletprop[3] + activity + bulletprop[5]
						+ " " + bulletprop[6];

				updateBulletsArray(bulletcoord, bulletprop[6]);

				if (activity.equals(" activ ")) {
					Thread.sleep(1);
				} else
					Thread.sleep(200);

				ServerUtils.broadcast(Server.bulletsProp);
			}

			removeBullet(bulletprop[6]);
		}
	}

	private static void updateBulletsArray(String bulletcoord, String id) {

		for (int i = 0; i < Server.bulletsProp.size(); i++) {
			String[] bullet;
			bullet = Server.bulletsProp.get(i).split(" ");
			if (bullet[6].equals(id)) {
				synchronized (Server.bulletsProp) {
					Server.bulletsProp.set(i, bulletcoord);
				}
			}
		}
	}

	private static void removeBullet(String bid) {

		for (int i = 0; i < Server.bulletsProp.size(); i++) {
			String[] bullet;
			bullet = Server.bulletsProp.get(i).split(" ");
			if (bullet[6].equals(bid)) {
				synchronized (Server.bulletsProp) {
					Server.bulletsProp.remove(i);
				}
			}
		}
	}
}
