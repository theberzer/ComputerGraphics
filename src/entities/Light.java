/*
 * 
 */
package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * The Class Light.
 *
 * @author berzi
 * 
 * 
 *         And God said let there be light
 */
public class Light {
	
	/** The position. */
	private static Vector3f position = new Vector3f(1000, 1000, 1000);
	
	/** The intensity. */
	private static Vector3f intensity = new Vector3f(0.1f, 0.1f, 0.1f);

	/**
	 * Instantiates a new light.
	 */
	public Light() {
		super();
	}

	/**
	 * Gets the postion.
	 *
	 * @return the postion
	 */
	public Vector3f getPostion() {
		return position;
	}

	/**
	 * Gets the intensity.
	 *
	 * @return the intensity
	 */
	public static Vector3f getIntensity() {
		return intensity;
	}

	/**
	 * Sets the intensity.
	 *
	 * @param intensity the new intensity
	 */
	public static void setIntensity(Vector3f intensity) {
		Light.intensity = intensity;
	}

	/**
	 * Sets the x position.
	 *
	 * @param postion the new x position
	 */
	public static void setxPosition(float postion) {
		position.x = postion;
	}

	/**
	 * Sets the y position.
	 *
	 * @param postion the new y position
	 */
	public static void setyPosition(float postion) {
		position.y = postion;
	}

	/**
	 * Sets the z position.
	 *
	 * @param postion the new z position
	 */
	public static void setzPosition(float postion) {
		position.z = postion;
	}

	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	public static void setPosition(Vector3f position) {
		Light.position = position;
	}

	/**
	 * Sets the position.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 */
	public static void setPosition(float x, float y, float z) {
		Light.position.x = x;
		Light.position.y = y;
		Light.position.z = z;

	}

}
