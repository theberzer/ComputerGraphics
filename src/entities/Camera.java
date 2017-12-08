/*
 * 
 */
package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import terrains.Terrain;
import textures.WaterTile;

/**
 * The Class Camera.
 *
 * @author berzi
 * 
 *         Camera classs that helps with moving the entire world in relation to
 *         your view
 */
public class Camera {

	/** The camera position. */
	private static Vector3f cameraPosition = new Vector3f(-2000, 70, -2000);

	/** The position. */
	private Vector3f position;
	
	/** The roll. */
	private float pitch, yaw, roll;
	
	/** The t. */
	private Terrain t;
	
	/** The counter. */
	private int counter = 0;
		
	private float movement;
	
	private float movementSpeed = 100;


	/**
	 * Instantiates a new camera.
	 */
	public Camera() {
		t = Terrain.getTerrain();
		cameraPosition.x = cameraPosition.x;
		cameraPosition.y = t.getHeight(cameraPosition.x, cameraPosition.z) + 50;
		cameraPosition.z = cameraPosition.z;
		position = cameraPosition;

	}

	/**
	 * Move.
	 */
	public void move() {
		movement = movementSpeed * DisplayManager.getDelta();
		// This code was found in a comment during a binge on youtube and helps
		// with the camera movement
		float arg_yaw = Mouse.getDX();
		yaw += arg_yaw / 10;
		float arg_roll = Mouse.getDY();
		pitch += -(arg_roll / 10);
		Mouse.setGrabbed(true);
		// Forward
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			float toZ = ((float) Math.sin(Math.toRadians(yaw + 90)));
			float toX = ((float) Math.cos(Math.toRadians(yaw + 90)));
			position.x -= toX * movement;
			position.z -= toZ * movement;
			if (counter == 0) {
				position.y = t.getHeight(position.x, position.z) + 50;
			}

		}
		// Backwards
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			float toZ = ((float) Math.sin(Math.toRadians(yaw + 90)));
			float toX = ((float) Math.cos(Math.toRadians(yaw + 90)));
			position.x += toX * movement;
			position.z += toZ * movement;
			if (counter == 0) {
				position.y = t.getHeight(position.x, position.z) + 50;
			}
		}
		// Right
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			float toZ = ((float) Math.sin(Math.toRadians(yaw)));
			float toX = ((float) Math.cos(Math.toRadians(yaw)));
			position.x += toX * movement;
			position.z += toZ * movement;
			if (counter == 0) {
				position.y = t.getHeight(position.x, position.z) + 50;
			}
		}
		// Left
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			float toZ = ((float) Math.sin(Math.toRadians(yaw)));
			float toX = ((float) Math.cos(Math.toRadians(yaw)));
			position.x -= toX * movement;
			position.z -= toZ * movement;
			if (counter == 0) {
				position.y = t.getHeight(position.x, position.z) + 50;
			} 
			
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_CAPITAL)) {
			if (counter == 0)
				counter++;
			else
				counter--;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += 2f;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			position.y -= 2f;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			System.exit(0);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
			System.out.println(position);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_U)) {
			Light.setPosition(9000, 10000, 9000);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
			Light.setPosition(3000, 1000, 5000);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
			Light.setPosition(5000, 1000, 7000);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
			Light.setPosition(5000, 1000, 3000);
		}

		
		
		cameraPosition = position;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Gets the pitch.
	 *
	 * @return the pitch
	 */
	public float getPitch() {
		return pitch;
	}

	/**
	 * Gets the yaw.
	 *
	 * @return the yaw
	 */
	public float getYaw() {
		return yaw;
	}

	/**
	 * Gets the roll.
	 *
	 * @return the roll
	 */
	public float getRoll() {
		return roll;
	}

	/**
	 * Gets the camera position.
	 *
	 * @return the camera position
	 */
	public static Vector3f getCameraPosition() {
		return cameraPosition;
	}

	/**
	 * Sets the camera position.
	 *
	 * @param cameraPosition the new camera position
	 */
	public static void setCameraPosition(Vector3f cameraPosition) {
		Camera.cameraPosition = cameraPosition;
	}

	/**
	 * Sets the position.
	 *
	 * @param cameraPosition the new position
	 */
	public void setPosition(Vector3f cameraPosition) {
		position = cameraPosition;
	}

	/**
	 * Invert pitch.
	 */
	public void invertPitch() {
		pitch = -pitch;
	}
	
	public void moveToReflection(Camera camera, float cameraDistance) {
		camera.setPosition(new Vector3f(camera.getPosition().x, camera.getPosition().y - cameraDistance,
				camera.getPosition().z));
		camera.invertPitch();
	}
	
	public void moveFromReflection(Camera camera, float cameraDistance) {
		camera.setPosition(new Vector3f(camera.getPosition().x, camera.getPosition().y + cameraDistance,
				camera.getPosition().z));
		camera.invertPitch();
	}
}
