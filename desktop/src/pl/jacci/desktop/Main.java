package pl.jacci.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import pl.jacci.main.MainGame;


public class Main {

	public static void main (String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Isaac";
		cfg.width = MainGame.Vw * MainGame.SCALE;
		cfg.height = MainGame.Vh * MainGame.SCALE;
		cfg.resizable = true;
		
		new LwjglApplication(new MainGame(), cfg);
	}
}
