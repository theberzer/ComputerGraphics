/*
 * 
 */
package textures;

import java.nio.ByteBuffer;

/**
 * The Class TextureData.
 */
public class TextureData {

	/** The width. */
	private int width;
	
	/** The height. */
	private int height;
	
	/** The buffer. */
	private ByteBuffer buffer;

	/**
	 * Instantiates a new texture data.
	 *
	 * @param width the width
	 * @param height the height
	 * @param buffer the buffer
	 */
	public TextureData(int width, int height, ByteBuffer buffer) {
		super();
		this.width = width;
		this.height = height;
		this.buffer = buffer;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the buffer.
	 *
	 * @return the buffer
	 */
	public ByteBuffer getBuffer() {
		return buffer;
	}

}
