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

	private static Vector3f cameraPosition = new Vector3f(-4000, 70, -4000);

	private Vector3f position;
	private float pitch, yaw, roll;
	private Terrain t;
	private int counter = 0;

	public Camera() {
		t = Terrain.getTerrain();
		cameraPosition.x = cameraPosition.x;
		cameraPosition.y = t.getHeight(cameraPosition.x, cameraPosition.z) + 50;
		cameraPosition.z = cameraPosition.z;
		position = cameraPosition;

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
			if (counter == 0) {
				position.y = t.getHeight(position.x, position.z) + 50;
			}
		}
		// Backwards
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			float toZ = ((float) Math.sin(Math.toRadians(yaw + 90)));
			float toX = ((float) Math.cos(Math.toRadians(yaw + 90)));
			position.x += toX;
			position.z += toZ;
			if (counter == 0) {
				position.y = t.getHeight(position.x, position.z) + 50;
			}
		}
		// Right
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			float toZ = ((float) Math.sin(Math.toRadians(yaw)));
			float toX = ((float) Math.cos(Math.toRadians(yaw)));
			position.x += toX;
			position.z += toZ;
			if (counter == 0) {
				position.y = t.getHeight(position.x, position.z) + 50;
			}
		}
		// Left
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			float toZ = ((float) Math.sin(Math.toRadians(yaw)));
			float toX = ((float) Math.cos(Math.toRadians(yaw)));
			position.x -= toX;
			position.z -= toZ;
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
