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
import textures.ModelTexture;
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

		// Create a new instance of the MasterRendrer object
		MastrerRendrer renderer = new MastrerRendrer();

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