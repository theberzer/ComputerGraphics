/*
 * 
 */
package textures;

/**
 * The Class WaterTile.
 */
public class WaterTile {

	/** The Constant TILE_SIZE. */
	public static final float TILE_SIZE = 6000;

	/** The height. */
	private float height;
	
	/** The z. */
	private float x, z;

	/**
	 * Instantiates a new water tile.
	 *
	 * @param centerX the center X
	 * @param height the height
	 * @param centerZ the center Z
	 */
	public WaterTile(float centerX, float height, float centerZ) {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * Gets the z.
	 *
	 * @return the z
	 */
	public float getZ() {
		return z;
	}

}
