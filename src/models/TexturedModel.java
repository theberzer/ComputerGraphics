package models;

import textures.ModelTexture;

/**
 * @author berzi
 *
 *         sets the texture of the rawModel
 */
public class TexturedModel {
	private RawModel rawModel;
	private ModelTexture texture;

	public TexturedModel(RawModel rawModel, ModelTexture texture) {
		super();
		this.rawModel = rawModel;
		this.texture = texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}
}
