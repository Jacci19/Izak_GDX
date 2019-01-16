package pl.jacci.entities;

import static pl.jacci.resources.Vars.R;

import pl.jacci.main.MainScreen;
import pl.jacci.resources.Assets;
import pl.jacci.resources.Vars;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;



public class StaticBody {
	
	private float w = Vars.w/2;
	private float h = Vars.h;
	private static float X;
	private static float Y;
	
	public Body body;
	
	private static BodyDef bdef = new BodyDef();
	private static FixtureDef fdef = new FixtureDef();
	
	public StaticBody () {
		bdef = new BodyDef();
		fdef = new FixtureDef();
	}
	
	public static void setCoords (float x, float y) {
		StaticBody.X = x;
		StaticBody.Y = y;
	}
	
	public void createWalls () {
		
		PolygonShape rect = new PolygonShape();
		bdef.type = BodyType.StaticBody;
		fdef.shape = rect;
		fdef.filter.categoryBits = Vars.bWALL;
		fdef.filter.maskBits = Vars.bPLAYER | Vars.bENTITY | Vars.bTEAR;
		fdef.isSensor = false;
		
		float[] xCoords = {w/9/R, (w-w/9/2)/R*2, w/2/R, (w-w/2/2)/R*2};
		float[] yCoords = {h/2/2/R, (h-h/2/2)/R, h/6/2/R, (h-h/6/2)/R};
		int[] xc = {0, 1, 0, 1, 2, 2, 3, 3}; // index of x coordinates
		int[] yc = {0, 0, 1, 1, 2, 3, 2, 3}; // index of y coordinates
		int side = 0;
		
		for (int i = 0; i < 8; ++i) {
			switch (i) {
			case 0:
				rect.setAsBox(w/9/R, (h/2/2-Vars.x*0.73f)/R);
				break;
			case 2:
				side = 1;
				break;
			case 4:
				rect.setAsBox((w/2/2-Vars.x*0.73f)/R*2.5f, h/6/2/R);
				side = 2;
				break;
			case 6:
				side = 3;
				break;
			}
			bdef.position.set(X+xCoords[yc[i]], Y+yCoords[xc[i]]);
			body = MainScreen.world.createBody(bdef);
			body.createFixture(fdef).setUserData("W_" + side);
		}
	}
	
	public void createGround () {
		PolygonShape floor = new PolygonShape();
		bdef.type = BodyType.StaticBody;
		fdef.shape = floor;
		fdef.filter.categoryBits = Vars.bGROUND;
		fdef.isSensor = true;
		
		bdef.position.set(X+w/R, Y+h/2/R);
		body = MainScreen.world.createBody(bdef);
		floor.setAsBox(w/9*7/R, h/6*4/2/R);
		body.createFixture(fdef).setUserData("G");
	}
	
	
	public void createRocks () {
		PolygonShape rock = new PolygonShape();
		bdef.type = BodyType.StaticBody;
		fdef.shape = rock;
		fdef.filter.categoryBits = Vars.bROCK;
		fdef.filter.maskBits = Vars.bPLAYER | Vars.bENTITY | Vars.bTEAR;
		fdef.isSensor = false;
		
		for (int i = 0; i < 7; ++i) {
			for (int j = 0; j < 13; ++j) {
				if (Assets.rockMaps[RoomManager.current][i][j] == -1) {
					continue;
				}
				bdef.position.set(X+(Vars.x(j)+Vars.x/2)/R, Y+(Vars.y(i)+Vars.y/2)/R);
				body = MainScreen.world.createBody(bdef);
				rock.setAsBox((Vars.x/2-Vars.x/10)/R, (Vars.y/2-Vars.y/10)/R);
				body.createFixture(fdef).setUserData(String.format("R_%d_%d", i, j)); /////
			}
		}
	}
	
	public void createDoors (boolean u, boolean d, boolean l, boolean r) {
		PolygonShape door = new PolygonShape();
		bdef.type = BodyType.StaticBody;
		fdef.shape = door;
		door.setAsBox(Vars.x/1.5f/R, Vars.y/1.5f/R);
		fdef.filter.categoryBits = Vars.bDOOR;
		fdef.isSensor = false;
		
		short bitPA = Vars.bENTITY | Vars.bTEAR;
		short bitPB = Vars.bPLAYER | Vars.bENTITY | Vars.bTEAR;
		short[] bits = {
				(u) ? bitPA : bitPB,
				(d) ? bitPA : bitPB,
				(l) ? bitPA : bitPB,
				(r) ? bitPA : bitPB
		};
		float[] xCoords = {w/R, (w-Vars.x*7.2f)/R, (w+Vars.x*7.2f)/R};
		float[] yCoords = {h/10/R, (h-h/10)/R, h/2/R};
		int[] xc = {0, 0, 1, 2};
		int[] yc = {0, 1, 2, 2};
		int[] UD = {2, 3, 0, 1};
		
		for (int i = 0; i < 4; ++i) {
			fdef.filter.maskBits = bits[i];
			bdef.position.set(X+xCoords[xc[i]], Y+yCoords[yc[i]]);
			body = MainScreen.world.createBody(bdef);
			body.createFixture(fdef).setUserData("D_" + UD[i]);
		}
	}
}
