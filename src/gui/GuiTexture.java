/*
 * 
 */
package gui;

/**
 * The Class GuiTexture.
 */
public class GuiTexture {
	
	/** The Texture. */
	private int Texture;
	
	/** The scale. */
	private org.lwjgl.util.vector.Vector2f position, scale;

	/**
	 * Instantiates a new gui texture.
	 *
	 * @param texture the texture
	 * @param position the position
	 * @param scale the scale
	 */
	public GuiTexture(int texture, org.lwjgl.util.vector.Vector2f position, org.lwjgl.util.vector.Vector2f scale) {
		super();
		Texture = texture;
		this.position = position;
		this.scale = scale;
	}

	/**
	 * Gets the texture.
	 *
	 * @return the texture
	 */
	public int getTexture() {
		return Texture;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public org.lwjgl.util.vector.Vector2f getPosition() {
		return position;
	}

	/**
	 * Gets the scale.
	 *
	 * @return the scale
	 */
	public org.lwjgl.util.vector.Vector2f getScale() {
		return scale;
	}

}
