package entities;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import terrains.Terrain;

/**
 * @author berzi
 * 
 *         Camera classs that helps with moving the entire world in relation to
 *         your view
 *
 */
public class Camera {

	private static Vector3f cameraPosition = new Vector3f(-2000, 70, -2000);
	
	private Vector3f position;
	private float pitch, yaw, roll;
	


	public Camera() {
		ArrayList<Terrain> terrains = Terrain.getTerrains();
		for (Terrain t : terrains) {
			cameraPosition.x = cameraPosition.x;
			cameraPosition.y = t.getHeight(cameraPosition.x, cameraPosition.z) + 20;
			cameraPosition.z = cameraPosition.z;
			position = cameraPosition;
		}
	}

	public void move() {
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
			position.x -= toX;
			position.z -= toZ;

		}
		// Backwards
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			float toZ = ((float) Math.sin(Math.toRadians(yaw + 90)));
			float toX = ((float) Math.cos(Math.toRadians(yaw + 90)));
			position.x += toX;
			position.z += toZ;
		}
		// Right
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			float toZ = ((float) Math.sin(Math.toRadians(yaw)));
			float toX = ((float) Math.cos(Math.toRadians(yaw)));
			position.x += toX;
			position.z += toZ;
		}
		// Left
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			float toZ = ((float) Math.sin(Math.toRadians(yaw)));
			float toX = ((float) Math.cos(Math.toRadians(yaw)));
			position.x -= toX;
			position.z -= toZ;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			position.y += 0.2f;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			position.y -= 0.2f;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			System.exit(0);
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

	public static Vector3f getCameraPosition() {
		return cameraPosition;
	}

	public static void setCameraPosition(Vector3f cameraPosition) {
		Camera.cameraPosition = cameraPosition;
	}
	
	public void setPosition(Vector3f cameraPosition) {
		position = cameraPosition;
	}
	
	public void invertPitch() {
		pitch = -pitch;
	}
	

}
