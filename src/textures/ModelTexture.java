/*
 * 
 */
package textures;

/**
 * The Class ModelTexture.
 *
 * @author berzi
 * 
 *         A Texture Model, used to set the texture, shine and reflectivity
 */
public class ModelTexture {

	/** The texture ID. */
	private int textureID;
	
	/** The shine damper. */
	private float shineDamper = 1;
	
	/** The reflectivity. */
	private float reflectivity = 0;

	/**
	 * Instantiates a new model texture.
	 *
	 * @param id the id
	 */
	public ModelTexture(int id) {
		this.textureID = id;
	}

	/**
	 * Gets the texture ID.
	 *
	 * @return the texture ID
	 */
	public int getTextureID() {
		return textureID;
	}

	/**
	 * Gets the shine damper.
	 *
	 * @return the shine damper
	 */
	public float getShineDamper() {
		return shineDamper;
	}

	/**
	 * Sets the shine damper.
	 *
	 * @param shineDamper the new shine damper
	 */
	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	/**
	 * Gets the reflectivity.
	 *
	 * @return the reflectivity
	 */
	public float getReflectivity() {
		return reflectivity;
	}

	/**
	 * Sets the reflectivity.
	 *
	 * @param reflectivity the new reflectivity
	 */
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

}
