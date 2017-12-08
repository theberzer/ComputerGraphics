/*
 * 
 */
package textures;

/**
 * The Class TerrainTexturePack.
 */
public class TerrainTexturePack {

	/** The background texture. */
	private TerrainTexture backgroundTexture;
	
	/** The r texture. */
	private TerrainTexture rTexture;
	
	/** The g texture. */
	private TerrainTexture gTexture;
	
	/** The b texture. */
	private TerrainTexture bTexture;

	/**
	 * Instantiates a new terrain texture pack.
	 *
	 * @param backgroundTexture the background texture
	 * @param rTexture the r texture
	 * @param gTexture the g texture
	 * @param bTexture the b texture
	 */
	public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture,
			TerrainTexture bTexture) {
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}

	/**
	 * Gets the background texture.
	 *
	 * @return the background texture
	 */
	public TerrainTexture getBackgroundTexture() {
		return backgroundTexture;
	}

	/**
	 * Gets the r texture.
	 *
	 * @return the r texture
	 */
	public TerrainTexture getrTexture() {
		return rTexture;
	}

	/**
	 * Gets the g texture.
	 *
	 * @return the g texture
	 */
	public TerrainTexture getgTexture() {
		return gTexture;
	}

	/**
	 * Gets the b texture.
	 *
	 * @return the b texture
	 */
	public TerrainTexture getbTexture() {
		return bTexture;
	}

}
