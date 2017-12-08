/*
 * 
 */
package toolbox;

import org.lwjgl.util.vector.Vector3f;

/**
 * The Class Colors.
 */
public class Colors {

	/**
	 * Convert to float.
	 *
	 * @param color the color
	 * @return the vector 3 f
	 */
	public static Vector3f convertToFloat(Vector3f color) {
		Vector3f c = new Vector3f();
		c.x = color.x / 255;
		c.y = color.y / 255;
		c.z = color.z / 255;

		return c;
	}
}
