package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * @author berzi
 *
 *
 *         And God said let there be light
 */
public class Light {
	private static Vector3f position = new Vector3f(1000, 1000, 1000);
	private static Vector3f intensity = new Vector3f(0.1f, 0.1f, 0.1f);

	public Light() {
		super();
	}

	public Vector3f getPostion() {
		return position;
	}
	

	
	public static Vector3f getIntensity() {
		return intensity;
	}

	public static void setIntensity(Vector3f intensity) {
		Light.intensity = intensity;
	}

	public static void setxPosition(float postion) {
		position.x = postion;
	}
	
	public static void setyPosition(float postion) {
		position.y = postion;
	}
	
	public static void setzPosition(float postion) {
		position.z = postion;
	}

	public static void setPosition(Vector3f position) {
		Light.position = position;
	}
	
	
	
	
}
