package engineTester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MastrerRendrer;
import renderEngine.OBJLoader;
import renderEngine.WaterRenderer;
import shaders.WaterShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import water.WaterFrameBuffer;
import water.WaterTile;
import entities.Camera;
import entities.Entity;
import entities.Light;
import gui.GuiRenderer;
import gui.GuiTexture;

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
	private static final int SEED = 5000000;
	
	public static void main(String[] args) {
		// Start
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		List<Terrain> terrains = new ArrayList<Terrain>();
		List<Entity> entities = new ArrayList<Entity>();
		
		
		//Terrains
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		Terrain terrain = new Terrain(0, 0, SEED, loader , texturePack, blendMap);
		terrains.add(terrain);
		
		Light light = new Light();
		Camera camera = new Camera();
		camera.setPosition(new Vector3f(2000,50, 2000));

		// Create a new instance of the MasterRendrer object
		MastrerRendrer renderer = new MastrerRendrer(loader);
		
		/**
		 * GUI
		 */
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		
		/**
		 * Water
		 */
		WaterFrameBuffer waterFrameBuffer = new WaterFrameBuffer();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), waterFrameBuffer);
		
		
		//Creates a plane at a position (the x and y position is in the centre of the side)
		WaterTile waterTile = new WaterTile(570,300,-50 );

		
		
		List<WaterTile> waterTileList = new ArrayList<>();
		waterTileList.add(waterTile);
		
		// Main loop that will run until you press close
		while (!Display.isCloseRequested()) {
			// updates the camera position once per frame
			camera.move();
			for (Terrain t : terrains) {
				if(t != null){
					renderer.processTerrain(t);
				}
			}
			
			for (Entity e: entities) {
				if(e != null) {
					renderer.processEntity(e);
				}
			}
			
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			
			waterFrameBuffer.bindReflectionFrameBuffer();
			//Move the camera to properly capture the "reflected" image
			float cameraDistance = 2 * (camera.getPosition().y - waterTile.getHeight() - 1f);
			camera.setPosition(new Vector3f(camera.getPosition().x, camera.getPosition().y - cameraDistance, camera.getPosition().z));
			camera.invertPitch();
		
			renderer.render(terrains, entities, light, camera, new Vector4f(0, 1, 0, -waterTile.getHeight()));
			
			camera.setPosition(new Vector3f(camera.getPosition().x, camera.getPosition().y + cameraDistance, camera.getPosition().z));
			//Moves the camera back to the original position
			//camera.getPosition().y += cameraDistance;
			camera.invertPitch();
			
			
			
			waterFrameBuffer.bindRefractionFrameBuffer();
			renderer.render(terrains, entities, light, camera, new Vector4f(0, -1, 0, waterTile.getHeight()));
			waterFrameBuffer.unbindFrameBuffer();
			// //////////////////////////////////////////////
			
			
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			
			renderer.render(terrains, entities, light, camera, new Vector4f(0, -1, 0, 100));
				
			waterRenderer.render(waterTileList, camera, light);
			//guiRenderer.render(guis);
			
			// Updates the display once per frame
			DisplayManager.updateDisplay();
			
			renderer.cleanLists();
			
		}

		// Clean up - deletes instances of the object so that it does not clog
		// up your memory
		renderer.cleanUp();
		loader.cleanUP();
		guiRenderer.cleanUp();
		waterShader.cleanUP();
		
		// Close
		DisplayManager.closeDisplay();

	}

}