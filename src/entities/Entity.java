/*
 * 
 */
package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

/**
 * The Class Entity.
 *
 * @author berzi
 * 
 *         Entity class that will create entities that will be rendered on the
 *         display
 */
public class Entity {

	/** The model. */
	private TexturedModel model;
	
	/** The postion. */
	private Vector3f postion;
	
	/** The rot Z. */
	private float rotX, rotY, rotZ;
	
	/** The scale. */
	private float scale;

	/**
	 * Instantiates a new entity.
	 *
	 * @param model the model
	 * @param postion the postion
	 * @param rotX the rot X
	 * @param rotY the rot Y
	 * @param rotZ the rot Z
	 * @param scale the scale
	 */
	public Entity(TexturedModel model, Vector3f postion, float rotX, float rotY, float rotZ, float scale) {
		this.model = model;
		this.postion = postion;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	/**
	 * Increase postion.
	 *
	 * @param dx the dx
	 * @param dy the dy
	 * @param dz the dz
	 */
	// Method used for movement
	public void increasePostion(float dx, float dy, float dz) {
		this.postion.x += dx;
		this.postion.y += dy;
		this.postion.z += dz;
	}

	/**
	 * Increase rotation.
	 *
	 * @param dx the dx
	 * @param dy the dy
	 * @param dz the dz
	 */
	// Method used for rotating objects
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public TexturedModel getModel() {
		return model;
	}

	/**
	 * Sets the model.
	 *
	 * @param model the new model
	 */
	public void setModel(TexturedModel model) {
		this.model = model;
	}

	/**
	 * Gets the postion.
	 *
	 * @return the postion
	 */
	public Vector3f getPostion() {
		return postion;
	}

	/**
	 * Sets the postion.
	 *
	 * @param postion the new postion
	 */
	public void setPostion(Vector3f postion) {
		this.postion = postion;
	}

	/**
	 * Gets the rot X.
	 *
	 * @return the rot X
	 */
	public float getRotX() {
		return rotX;
	}

	/**
	 * Sets the rot X.
	 *
	 * @param rotX the new rot X
	 */
	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	/**
	 * Gets the rot Y.
	 *
	 * @return the rot Y
	 */
	public float getRotY() {
		return rotY;
	}

	/**
	 * Sets the rot Y.
	 *
	 * @param rotY the new rot Y
	 */
	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	/**
	 * Gets the rot Z.
	 *
	 * @return the rot Z
	 */
	public float getRotZ() {
		return rotZ;
	}

	/**
	 * Sets the rot Z.
	 *
	 * @param rotZ the new rot Z
	 */
	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	/**
	 * Gets the scale.
	 *
	 * @return the scale
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * Sets the scale.
	 *
	 * @param scale the new scale
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}

}
