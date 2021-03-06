package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author berzi
 * 
 *         Camera classs that helps with moving the entire world in relation to
 *         your view
 *
 */
public class Camera {

	private Vector3f position = new Vector3f(10, 10, 50);
	private float pitch, yaw, roll;
	private boolean focusWindow;

	public Camera() {
		focusWindow = true;
		Mouse.setGrabbed(focusWindow);
	}

	public void move() {
		
		// This code was found in a comment during a binge on youtube and helps
		// with the camera movement
		
		//code for grabbing and freeing mouse is not from youtube @KimB
		
		
		//free mouse from window if the escape key is pressed, or grab mouse if user want to focus window again
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			focusWindow = false;
			Mouse.setGrabbed(focusWindow);
		}		
		
		//regrabbs 
		if(focusWindow == false && Mouse.isButtonDown(0)) {
			focusWindow = true;
			Mouse.setGrabbed(focusWindow);
		}
		
		//Turns off camera movement if the cursor is freed from the window
		if(focusWindow == true) {
			float arg_yaw = Mouse.getDX();
			System.out.println(arg_yaw);
			yaw += arg_yaw / 10;
			float arg_roll = Mouse.getDY();
			pitch += -(arg_roll / 10);
		}
		
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

	public void invertPitch() {
		pitch = -pitch;
		
	}

}
