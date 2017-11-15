package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * @author berzi
 *
 *
 *         And God said let there be light
 */
public class Light {
	private Vector3f postion;
	private Vector3f colour;

	public Light(Vector3f postion, Vector3f colour) {
		super();
		this.postion = postion;
		this.colour = colour;
	}

	public Vector3f getPostion() {
		return postion;
	}

	public Vector3f getColour() {
		return colour;
	}
}
