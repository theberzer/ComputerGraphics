package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

/**
 * @author berzi
 * 
 *         Entity class that will create entities that will be rendered on the
 *         display
 */
public class Entity {

	private TexturedModel model;
	private Vector3f postion;
	private float rotX, rotY, rotZ;
	private float scale;

	public Entity(TexturedModel model, Vector3f postion, float rotX, float rotY, float rotZ, float scale) {
		this.model = model;
		this.postion = postion;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	//Method used for movement
	public void increasePostion(float dx, float dy, float dz) {
		this.postion.x += dx;
		this.postion.y += dy;
		this.postion.z += dz;
	}
	
	//Method used for rotating objects
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPostion() {
		return postion;
	}

	public void setPostion(Vector3f postion) {
		this.postion = postion;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

}
