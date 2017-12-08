/*
 * 
 */
package engineTester;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import gui.GuiRenderer;
import gui.GuiTexture;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MastrerRendrer;
import renderEngine.WaterRenderer;
import shaders.WaterShader;
import terrains.Terrain;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import textures.WaterTile;
import toolbox.WaterFrameBuffer;

/**
 * The Class MainGameLoop.
 *
 * @author berzi
 * 
 *         main class that will run the game
 */
public class MainGameLoop {

	/**
	 * The Constant SEED.
	 *
	 */
	private static final int SEED = 5000000;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// Start
		DisplayManager.createDisplay();
		Loader loader = new Loader();

		List<Terrain> terrains = new ArrayList<Terrain>();
		List<Entity> entities = new ArrayList<Entity>();

		// Terrains
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		Terrain terrain = new Terrain(0, 0, SEED, loader, texturePack, blendMap);
		terrains.add(terrain);

		Light light = new Light();
		Camera camera = new Camera();
		camera.setPosition(new Vector3f(5000, terrain.getHeight(5000, 5000) + 50, 5000));

		// Create a new instance of the MasterRendrer object
		MastrerRendrer renderer = new MastrerRendrer(loader);
		

		// Instance of particle handler
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		
		
		/**
		 * Particle Systems
		 */

		 // Init of texture for fire particlesystem
		ParticleTexture pSmokeTexture = new ParticleTexture(loader.loadTexture("particle/smoke"), 8, false); // (name of file, number of rows in atlastexture)
		ParticleSystem pSmokeSystem = new ParticleSystem(pSmokeTexture // texture of the system
				, 500 // Number of particles to spawn each frame
				,1 // Init velocity of the particles
				, -0.1f // Gravity effect
				, 10 // Time the particles are alive
				, 5); //Scalemodifier

		pSmokeSystem.randomizeRotation();
		pSmokeSystem.setScaleError(0.8f);
		pSmokeSystem.setLifeError(0.5f);
		pSmokeSystem.setSpeedError(0.9f);
		
		 // Init of texture for fire particlesystem
		ParticleTexture pFireTexture = new ParticleTexture(loader.loadTexture("particle/fire"), 8, true); // (name of file, number of rows in atlastexture)
		ParticleSystem pFireSystem = new ParticleSystem(pFireTexture // texture of the system
				, 300 // Number of particles to spawn each frame
				,3 // Init velocity of the particles
				, -0.5f // Gravity effect
				, 1.5f // Time the particles are alive
				, 10); // Scalemodifier of the particles

		pFireSystem.setLifeError(0.5f);
		
		/*
		 * End of particle init
		 */

		/**
		 * GUI
		 */

		//GuiRenderer guiRenderer = new GuiRenderer(loader);
		//List<GuiTexture> guis = new ArrayList<GuiTexture>();

		/**
		 * Water
		 */
		WaterFrameBuffer waterFrameBuffer = new WaterFrameBuffer();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(),
				waterFrameBuffer);
		Vector4f clipPlane = new Vector4f(0, -1, 0, 100);

		// Creates a plane at a position (the x and y position is in the centre of the
		// side)
		WaterTile waterTile = new WaterTile(5000, -20, 5000);

		List<WaterTile> waterTileList = new ArrayList<>();
		waterTileList.add(waterTile);

		// Main loop that will run until you press close
		while (!Display.isCloseRequested()) {
			// updates the camera position once per frame
			camera.move();
			for (Terrain t : terrains) {
				if (t != null) {
					renderer.processTerrain(t);
				}
			}

			for (Entity e : entities) {
				if (e != null) {
					renderer.processEntity(e);
				}
			}
			
			//Adding particles			
			pFireSystem.generateParticles(new Vector3f(5000,100,5000));
			pSmokeSystem.generateParticles(new Vector3f(5020,terrain.getHeight(5020, 5020)+10, 5020));
			// Updates the particles
			ParticleMaster.update(camera);

			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

			waterFrameBuffer.bindReflectionFrameBuffer();
			// Move the camera to properly capture the "reflected" image
			float cameraDistance = 2 * (camera.getPosition().y - waterTile.getHeight());
			camera.setPosition(new Vector3f(camera.getPosition().x, camera.getPosition().y - cameraDistance,
					camera.getPosition().z));
			camera.invertPitch();

			renderer.render(terrains, entities, light, camera, new Vector4f(0, 1, 0, -waterTile.getHeight() + 1f));

			camera.setPosition(new Vector3f(camera.getPosition().x, camera.getPosition().y + cameraDistance,
					camera.getPosition().z));
			// Moves the camera back to the original position
			// camera.getPosition().y += cameraDistance;
			camera.invertPitch();

			waterFrameBuffer.bindRefractionFrameBuffer();
			renderer.render(terrains, entities, light, camera, new Vector4f(0, -1, 0, waterTile.getHeight() + 1f));
			waterFrameBuffer.unbindFrameBuffer();
			// //////////////////////////////////////////////

			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);

			renderer.render(terrains, entities, light, camera, clipPlane);

			waterRenderer.render(waterTileList, camera, light);
			// guiRenderer.render(guis);


			// Render all particles
			ParticleMaster.renderParticles(camera);
			
			// Updates the display once per frame
			DisplayManager.updateDisplay();

			renderer.cleanLists();

		}

		// Clean up - deletes instances of the object so that it does not clog
		// up your memory
		ParticleMaster.cleanUp();
		renderer.cleanUp();
		loader.cleanUP();
		//guiRenderer.cleanUp();
		waterShader.cleanUP();

		// Close
		DisplayManager.closeDisplay();

	}

}