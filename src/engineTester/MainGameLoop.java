/*
 * 
 */
package engineTester;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MastrerRendrer;
import renderEngine.WaterRenderer;
import shaders.WaterShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import textures.WaterTile;
import toolbox.FrameBuffer;
import toolbox.Maths;

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

		/*
		 * If game objects are clipping, then this w component should be increased
		 */
		Vector4f clipPlane = new Vector4f(0, -1, 0, 1000);
		
		// Create a new instance of the MasterRendrer object
		MastrerRendrer renderer = new MastrerRendrer(loader);
		
		/**
		 * Particle Systems
		 */

		// Instance of particle handler
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		
		 // Init of texture for fire particlesystem
		ParticleTexture pSmokeTexture = new ParticleTexture(loader.loadTexture("particle/smoke"), 8, true); // (name of file, number of rows in atlastexture)
		ParticleSystem pSmokeSystem = new ParticleSystem(pSmokeTexture // texture of the system
				, 100 // Number of particles to spawn each frame
				,-3 // Init velocity of the particles
				, -2f // Gravity effect
				, 3 // Time the particles are alive
				, 10 //Scalemodifier
				, 1); // isSquared (1 is not a square) 

		pSmokeSystem.randomizeRotation();
		pSmokeSystem.setScaleError(0.8f);
		pSmokeSystem.setLifeError(0.5f);
		pSmokeSystem.setSpeedError(0.9f);

		 // Init of texture for fire particlesystem
		ParticleTexture pFireTexture = new ParticleTexture(loader.loadTexture("particle/fire"), 8, true); // (name of file, number of rows in atlastexture)
		ParticleSystem pFireSystem = new ParticleSystem(pFireTexture // texture of the system
				, 30 // Number of particles to spawn each frame
				,-1f // Init velocity of the particles
				,-1f // Gravity effect
				, 2f // Time the particles are alive
				, 20
				, 1); // Scalemodifier of the particles
		 // Init of texture for fire particlesystem
		
		ParticleTexture pWeatherTexture = new ParticleTexture(loader.loadTexture("particle/cosmic"), 4, false); // (name of file, number of rows in atlastexture)
		ParticleSystem weatherSystem = new ParticleSystem(pWeatherTexture // texture of the system
				, 1000 // Number of particles to spawn each frame
				,10 // Init velocity of the particles
				,0 // Gravity effect
				,5 // Time the particles are alive
				, 1
				, 1000); // Scalemodifier of the particles

		weatherSystem.randomizeRotation();
		weatherSystem.setScaleError(0.8f);
		weatherSystem.setLifeError(0.5f);
		weatherSystem.setSpeedError(0.9f);
		
		List<Vector3f> firePositions = new ArrayList<Vector3f>();
		for(int i = 0; i<50; i++) {
			float randX, randZ;
			float camX = camera.getPosition().x;
			float camZ = camera.getPosition().z;
			randX = Maths.randomIntFromInterval(camX-1000, camX+1000);
			randZ = Maths.randomIntFromInterval(camZ-1000, camZ+1000);
			firePositions.add(new Vector3f(randX, terrains.get(0).getHeight(randX, randZ) + 5, randZ));
		}
		
		
		/*
		 * End of particle init
		 */


		/**
		 * Water
		 */
		
		/**
		 * Variables to change the appearance of the water
		 * wateTiling: Changes the sizes of the waves (in x/z direction)
		 * WaveSpeed: Changes how fast the distortion travels over the water
		 * reflectionFactor: Decides the amount of reflection/specular highlights. The larger the number, the LESS amount of reflectivity
		 * distortionStrength: Changes the amount of distortion. If this is increased, then reflectionFactor should probably also be increased
		 */
		float waveTiling = 15.0f, 
				waveSpeed = 0.01f,
				reflectionFactor = 5.0f,
				distortionStrength = 0.06f;
		
		FrameBuffer reflectionFrameBuffer = new FrameBuffer(320, 180, 2);
		FrameBuffer refractionFrameBuffer = new FrameBuffer(1280, 720, 1);
		
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(),
				reflectionFrameBuffer, refractionFrameBuffer);
		

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

			// For each of the positions - check if over waterlevel
			// if true - generate fire particles and above those, smoke
			for(Vector3f positions : firePositions) {
				if(positions.y > -15f) {
					//Adding particles			
					pFireSystem.generateParticles(positions);
					pSmokeSystem.generateParticles(new Vector3f(positions.x, positions.y+10, positions.z));
				}
			}
			
			// Generate the gloomy weather system if the time is between 20:00 and 08:00 
			if(DisplayManager.getInGameHour() < 8 || DisplayManager.getInGameHour() > 20) {
				weatherSystem.generateParticles(new Vector3f(camera.getPosition().x-500, camera.getPosition().y, camera.getPosition().z-500));
			}
				
			// Updates the particles
			ParticleMaster.update(camera);

			//Enables clipping planes. For this to be utilized, the gl_ClipDistance[0] must be implemented in the vertexShader. 
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			
			//Binds the reflectionBuffer to be rendered to. 
			reflectionFrameBuffer.bindFrameBuffer();
			
			// The height difference between the camera and the waterTile
			float cameraDistance = 2 * (camera.getPosition().y - waterTile.getHeight());
			
			//Moves the camera to correct position under the plane to capture the reflection
			camera.moveToReflection(camera, cameraDistance);
			
			//Render the scene to the bound frameBuffer (here the reflectionBuffer)
			renderer.render(terrains, entities, light, camera, new Vector4f(0, 1, 0, -waterTile.getHeight() + 1f));
			
			//Moves the camera back to the correct player/camera position
			camera.moveFromReflection(camera, cameraDistance);
			
			//Binds the refractionBuffer to be rendered to. 
			refractionFrameBuffer.bindFrameBuffer();
			//Render the scene to the bound frameBuffer (here the refractionBuffer)
			renderer.render(terrains, entities, light, camera, new Vector4f(0, -1, 0, waterTile.getHeight() + 1f));
			//Unbinds the refractionbuffer to the standard frameBuffer
			refractionFrameBuffer.unbindFrameBuffer();
			
			/**
			 * Disables clipping planes for the main renderer. This command isn't always registered. For the clipPlane not to affect the main
			 * renderer, increase the w value of the Vec4 variable clipPlane to a high number.
			 */
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			
			//Renders the scene to the standard frameBuffer
			renderer.render(terrains, entities, light, camera, clipPlane);
			//Renders the water to the standard frameBuffer
			waterRenderer.render(waterTileList, camera, light, waveTiling, waveSpeed, reflectionFactor, distortionStrength);
			
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
		loader.cleanUp();
		waterShader.cleanUp();
		reflectionFrameBuffer.cleanUp();
		refractionFrameBuffer.cleanUp();

		// Close
		DisplayManager.closeDisplay();

	}
	

}