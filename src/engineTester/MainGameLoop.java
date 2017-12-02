package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MastrerRendrer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import entities.Camera;
import entities.Entity;
import entities.Light;

/**
 * @author berzi
 *
 *	main class that will run the game
 *
 */
public class MainGameLoop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Start
		DisplayManager.createDisplay();
		Loader loader = new Loader();

		
		//Terrains
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		Terrain terrain = new Terrain(0, 01, loader , texturePack, blendMap, "heightmap");
		Terrain terrain1 = new Terrain(1, 1, loader , texturePack, blendMap, "heightmap");
				
		
		
		


		// Light and Camera entities
		Light light = new Light(new Vector3f(1000, 1000, 1000), new Vector3f(1, 1, 1));
		Camera camera = new Camera();

		// Create a new instance of the MasterRendrer object
		MastrerRendrer renderer = new MastrerRendrer();

		// Main loop that will run until you press close
		while (!Display.isCloseRequested()) {
			// updates the camera position once per frame
			camera.move();
			
			//Render Terrain
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain1);


			renderer.render(light, camera);

			// Updates the display once per frame
			DisplayManager.updateDisplay();
		}

		// Clean up - deletes instances of the object so that it does not clog
		// up your memory
		renderer.cleanUp();
		loader.cleanUP();

		// Close
		DisplayManager.closeDisplay();

	}

}