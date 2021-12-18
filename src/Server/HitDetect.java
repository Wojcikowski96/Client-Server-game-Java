package Server;

import java.io.IOException;
import java.util.ArrayList;

public class HitDetect {

	public static boolean hitTank(ArrayList<String> tanksProp, ArrayList<String> bulletsProp) throws IOException {

		boolean checkIfHit = false;
		outerloop:
		for (int i = 0; i < bulletsProp.size(); i++) {
			for (int j = 0; j < tanksProp.size(); j++) {

				String partstank[];
				String partsbullet[];
				partstank = tanksProp.get(j).split(" ");
				partsbullet = bulletsProp.get(i).split(" ");
				Double xc = Double.parseDouble(partstank[0]);
				Double yc = Double.parseDouble(partstank[1]);
				Double xp = Double.parseDouble(partsbullet[1]);
				Double yp = Double.parseDouble(partsbullet[2]);

				if (((xp > xc - 1) && (xp < xc + 41) && (yp > yc - 1) && (yp < yc + 41))
						&& !((xp > xc) && (xp < xc + 40) && (yp > yc) && (yp < yc + 40))) {

					ServerBullet.activity = " explo ";
				}

				if (((xp > xc) && (xp < xc + 40) && (yp > yc) && (yp < yc + 40))
						&& !((xp > xc + 1) && (xp < xc + 39) && (yp > yc + 1) && (yp < yc + 39))) {
					if (partstank[4].equals("dead")) {
						ServerBullet.activity = " oblit ";
					} else {
						ServerBullet.activity = " destr ";
					}
				}

				if ((xp > xc + 1) && (xp < xc + 39) && (yp > yc + 1) && (yp < yc + 39)) {

					checkIfHit = true;

					String newprop = tanksProp.get(j).replace("aliv", "dead");

					tanksProp.set(j, newprop);

					ServerUtils.broadcast(Server.tanksProp);
					
					break outerloop;
				}
			}
		}
		return checkIfHit;
	}
}
