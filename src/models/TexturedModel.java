/*
 * 
 */
package models;

import textures.ModelTexture;

/**
 * The Class TexturedModel.
 *
 * @author berzi
 * 
 *         sets the texture of the rawModel
 */
public class TexturedModel {
	
	/** The raw model. */
	private RawModel rawModel;
	
	/** The texture. */
	private ModelTexture texture;

	/**
	 * Instantiates a new textured model.
	 *
	 * @param rawModel the raw model
	 * @param texture the texture
	 */
	public TexturedModel(RawModel rawModel, ModelTexture texture) {
		super();
		this.rawModel = rawModel;
		this.texture = texture;
	}

	/**
	 * Gets the raw model.
	 *
	 * @return the raw model
	 */
	public RawModel getRawModel() {
		return rawModel;
	}

	/**
	 * Gets the texture.
	 *
	 * @return the texture
	 */
	public ModelTexture getTexture() {
		return texture;
	}
}
