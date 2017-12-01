package engineTester;

import java.util.ArrayList;
import java.util.List;
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
import textures.ModelTexture;
import water.WaterFrameBuffer;
import water.WaterRenderer;
import water.WaterShader;
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
	public static void main(String[] args) {
		// Start
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		// Create a new instance of the MasterRendrer object
		MastrerRendrer renderer = new MastrerRendrer();

		// Dragon
		RawModel modelDragon = OBJLoader.loadObjModel("dragon", loader);
		TexturedModel staticModelDragon = new TexturedModel(modelDragon, new ModelTexture(loader.loadTexture("white")));
		staticModelDragon.getTexture().setShineDamper(15);
		staticModelDragon.getTexture().setReflectivity(5);
		Entity dragon = new Entity(staticModelDragon, new Vector3f(100, 1, -1), 0, 0, 0, 1f);

		// Rocket
		RawModel modelRocket = OBJLoader.loadObjModel("aim9", loader);
		TexturedModel staticModelRocket = new TexturedModel(modelRocket, new ModelTexture(loader.loadTexture("aim9")));
		staticModelRocket.getTexture().setShineDamper(15);
		staticModelRocket.getTexture().setReflectivity(5);
		List<Entity> rocketModels = new ArrayList<Entity>();

		// Creates 10 rockets with x+= 50 for each
		float xR = 0;
		for (int i = 0; i < 10; i++) {
			rocketModels.add(new Entity(staticModelRocket, new Vector3f(xR, -200, -300), 0, 0, 0, 0.5f));
			xR += 50;
		}

		// Car
		RawModel modelCar = OBJLoader.loadObjModel("car", loader);
		TexturedModel staticModelCar = new TexturedModel(modelCar, new ModelTexture(loader.loadTexture("balloon")));
		staticModelCar.getTexture().setShineDamper(15);
		staticModelCar.getTexture().setReflectivity(5);
		Entity car = new Entity(staticModelCar, new Vector3f(50, 1, -1), 0, 0, 0, 1f);

		// Bottle
		RawModel modelBottle = OBJLoader.loadObjModel("bottleSprite", loader);
		TexturedModel staticModelBottle = new TexturedModel(modelBottle,
				new ModelTexture(loader.loadTexture("spriteTexture")));
		staticModelBottle.getTexture().setShineDamper(15);
		staticModelBottle.getTexture().setReflectivity(10);
		Entity bottle = new Entity(staticModelBottle, new Vector3f(75, 1, -1), 0, 0, 0, 1f);

		// Balloons
		RawModel modelBalloon = OBJLoader.loadObjModel("balloon", loader);
		TexturedModel staticModelBalloon = new TexturedModel(modelBalloon,
				new ModelTexture(loader.loadTexture("balloon")));
		staticModelBalloon.getTexture().setShineDamper(10);
		staticModelBalloon.getTexture().setReflectivity(7);
		List<Entity> balloonModels = new ArrayList<Entity>();

		// Creates 10 random balloons with random xyz coordinates
		for (int i = 0; i < 10; i++) {
			balloonModels.add(new Entity(staticModelBalloon, new Vector3f(new Random().nextFloat() * (-100 + 500),
					new Random().nextFloat() * 100, new Random().nextFloat() * 100), 0, 0, 0, new Random().nextFloat()*2));
		}
		
		//Lego 
		RawModel modelLego = OBJLoader.loadObjModel("lego", loader);
		TexturedModel staticModelLego = new TexturedModel(modelLego,
				new ModelTexture(loader.loadTexture("black")));
		staticModelLego.getTexture().setShineDamper(15);
		staticModelLego.getTexture().setReflectivity(5);
		Entity lego = new Entity(staticModelLego, new Vector3f(50, 1, -80), 0, 0, 0, 1f);
		
		TexturedModel staticModelLego2 = new TexturedModel(modelLego,
				new ModelTexture(loader.loadTexture("white")));
		staticModelLego2.getTexture().setShineDamper(15);
		staticModelLego2.getTexture().setReflectivity(5);
		Entity lego2 = new Entity(staticModelLego2, new Vector3f(100, 1, -80), 0, 0, 0, 1f);

		//Pokeball
		RawModel modelPokeball = OBJLoader.loadObjModel("Pokeball", loader);
		TexturedModel staticModelPokeball = new TexturedModel(modelPokeball,
				new ModelTexture(loader.loadTexture("pokeball")));
		staticModelPokeball.getTexture().setShineDamper(15);
		staticModelPokeball.getTexture().setReflectivity(5);
		Entity pokeball = new Entity(staticModelPokeball, new Vector3f(50, 1, -80), 0, 0, 0, 1f);
		

		// Cubes
		RawModel modelCube = OBJLoader.loadObjModel("cubeRainbow", loader);
		TexturedModel staticModelCube = new TexturedModel(modelCube, new ModelTexture(loader.loadTexture("texture")));
		staticModelCube.getTexture().setShineDamper(15);
		staticModelCube.getTexture().setReflectivity(5);
		List<Entity> models = new ArrayList<Entity>();

		// Creates a pyramid with the cubes
		float x = 7;
		float y = 9;
		for (int i = 0; i < 5; i++) {
			y -= 2;
			int j = 0;
			for (; j <= i; j++) {
				x += 3;
				models.add(new Entity(staticModelCube, new Vector3f(x, y, -1), 1, 0, 0, 1));
			}
			x += j * (-3) - 1.5f;
		}
		
		
		

		// Light and Camera entities
		Light light = new Light(new Vector3f(1000, 1000, 1000), new Vector3f(1, 1, 1));
		Camera camera = new Camera();
		
		
		//GUI TEST
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		 
		
		
		//water
		
		WaterFrameBuffer waterFrameBuffer = new WaterFrameBuffer();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), waterFrameBuffer);
		
		
		//Creates a plane at a position (the x and y position is in the centre of the side)
		WaterTile waterTile = new WaterTile(50 ,0, -1);
		
		List<WaterTile> waterTileList = new ArrayList<>();
		waterTileList.add(waterTile);
		
		
		GuiTexture reflectionGui = new GuiTexture(waterFrameBuffer.getReflectionTexture(),new Vector2f(0.25f, 0.25f), new Vector2f(0.25f, 0.25f));
		guis.add(reflectionGui);
		
		
		// Main loop that will run until you press close
		while (!Display.isCloseRequested()) {
			// updates the camera position once per frame
			camera.move();
			dragon.increaseRotation(0, 0.5f, 0);
			
			
		

			// Render entities
			for (Entity e : models) {
				e.increaseRotation(new Random().nextFloat() * (1 - 15), new Random().nextFloat() * (1 - 5), 0);
				renderer.processEntity(e);
			}

			for (Entity e : rocketModels) {
				e.increasePostion(0, 1, 0);
				renderer.processEntity(e);
			}

			int i = 0;
			for (Entity e : balloonModels) {
				if (i % 2 == 0) {
					e.increasePostion(new Random().nextFloat() * (0.2f - 0.1f),
							new Random().nextFloat() * (0.2f - 0.1f), new Random().nextFloat() * (0.2f - 0.1f));
				} else {
					e.increasePostion(new Random().nextFloat() * -(0.2f - 0.1f),
							new Random().nextFloat() * (0.2f - 0.1f), new Random().nextFloat() * -(0.2f - 0.1f));
				}
				renderer.processEntity(e);
				i++;
			}
			
			
			
			
			renderer.processEntity(dragon);
			renderer.processEntity(car);
			renderer.processEntity(bottle);
			renderer.processEntity(lego);
			renderer.processEntity(lego2);
			renderer.processEntity(pokeball);
			
			
			
			//This doesn't have anny visual effects yet. Don't remove
			//If you are struggling for gpu power, comment these lines out:
			//////////////////////////////////////////////////
			
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			waterFrameBuffer.bindReflectionFrameBuffer();
			
			//Needs to move the camera to properly capture the "reflected" image
			float cameraDistance = 2 * (camera.getPosition().y - waterTile.getHeight());
			camera.getPosition().y -= cameraDistance;
			camera.invertPitch();
			
			//Moves the camera back to the original position
			camera.getPosition().y += cameraDistance;
			camera.invertPitch();
			
			renderer.render(light, camera, new Vector4f(0, 1, 0, -waterTile.getHeight()));
			waterFrameBuffer.unbindFrameBuffer();
			
			waterFrameBuffer.bindRefractionFrameBuffer();
			renderer.render(light, camera, new Vector4f(0, -1, 0, waterTile.getHeight()));
			waterFrameBuffer.unbindFrameBuffer();
			// //////////////////////////////////////////////
			
			
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			
			renderer.render(light, camera, new Vector4f(0, 1, 0, 10000));
			
			waterRenderer.render(waterTileList, camera);
			guiRenderer.render(guis);
			// Updates the display once per frame
			DisplayManager.updateDisplay();
			
			renderer.cleanUpEntities();
		}

		// Clean up - deletes instances of the object so that it does not clog
		// up your memory
		renderer.cleanUp();
		loader.cleanUp();
		waterShader.cleanUp();
		waterFrameBuffer.cleanUp();
		guiRenderer.cleanUp();
		
	
		// Close
		DisplayManager.closeDisplay();

	}

}