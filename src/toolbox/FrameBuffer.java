/*
 * 
 */
package toolbox;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import renderEngine.DisplayManager;

/**
 * The Class WaterFrameBuffer.
 *
 * @author Kim Nilsen Brusevold 
 * 
 * 		   Framebuffer Class. This class is used to create framebuffers for
 *         the water, and generate textures from the drawn images in the buffer.
 *         If the game/level doesn't need any water reflection or refraction,
 *         then this class shouldn't be used for optimizing the use of GPU
 */

public class FrameBuffer {
	int frameBuffer;
	
	int width;
	int height;
	
	public static final int NONE = 0;
    public static final int DEPTH_TEXTURE = 1;
    public static final int DEPTH_RENDER_BUFFER = 2;
    
    private int colourTexture;
    private int depthTexture;
 
    private int depthBuffer;
    private int colourBuffer;
	
	/**
	 * Instantiates a new water frame buffer.
	 */
	/*
	 * Constructor Creates the FrameBuffers when class is created
	 */
	public FrameBuffer(int width, int height, int depthBufferType) {
		this.width = width;
        this.height = height;
        initialiseFrameBuffer(depthBufferType);
	}

	private void initialiseFrameBuffer(int bufferType) {
		createFrameBuffer();
		createTexture();
		if (bufferType == DEPTH_RENDER_BUFFER) {
			createDepthBuffer();
        } else if (bufferType == DEPTH_TEXTURE) {
        	createDepthTexture();
        }
	}

	// Changes what framebuffer that is being drawn to, from default screen frame
	/**
	 * Bind frame buffer.
	 *
	 * @param framebuffer the framebuffer
	 * @param width the width
	 * @param height the height
	 */
	public void bindFrameBuffer() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, width, height);
	}

	/**
	 * Creates the frame buffer.
	 *
	 * @return the int
	 */
	// Creates a frameBuffer, and binds it. Returns the framebuffer id.
	private void createFrameBuffer() {
		frameBuffer = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		// Uses standard the default color attachment
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
	}

	/**
	 * Creates the texture.
	 *
	 * @param width the width
	 * @param height the height
	 * @return the int
	 */
	// Creates a texture, and binds it to the currently bound frameBuffer
	private void createTexture() {
		colourTexture = GL11.glGenTextures();
		/*
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
     	*/
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colourTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height,
                0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
        		colourTexture, 0);
	}

	/**
	 * Creates the depth texture.
	 *
	 * @param width the width
	 * @param height the height
	 * @return the int
	 */
	// Creates a depth texture, and binds it to the currently bound frameBuffer
	private void createDepthTexture() {
		depthTexture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0, GL11.GL_DEPTH_COMPONENT,
				GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture, 0);
	}

	/**
	 * Creates the depth buffer.
	 *
	 * @param width the width
	 * @param height the height
	 * @return the int
	 */
	// Creates a depth texture, and binds it to the currently bound frameBuffer
	private void createDepthBuffer() {
		depthBuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER,
				depthBuffer);
	}

	/**
	 * Unbind frame buffer.
	 */
	// Sets the framebuffer back to the default screen frameBuffer
	public void unbindFrameBuffer() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, DisplayManager.getWidth(), DisplayManager.getHeight());
	}

	/**
	 * Clean up.
	 */
	// freeing up memory when exiting level/game/application
	public void cleanUp() {
		GL30.glDeleteFramebuffers(frameBuffer);
        GL11.glDeleteTextures(colourTexture);
        GL11.glDeleteTextures(depthTexture);
        GL30.glDeleteRenderbuffers(depthBuffer);
        GL30.glDeleteRenderbuffers(colourBuffer);
	}

	public int getColourTexture() {
		return colourTexture;
	}

	public int getDepthTexture() {
		return depthTexture;
	}

	public int getDepthBuffer() {
		return depthBuffer;
	}

	public int getColourBuffer() {
		return colourBuffer;
	}
	
	
}