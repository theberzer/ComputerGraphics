package water;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import renderEngine.DisplayManager;
/**
 * 
 * @author Kim
 * Framebuffer Class.
 * This class is used to create framebuffers for the water, and generate textures from the drawn images in the buffer. 
 * If the game/level doesn't need anny water reflection or refraction, then this class shouldn't be used for optimizing the use of GPU
 */

public class WaterFrameBuffer {
	
	//Larger sizes result in better quality, but worse performance
    protected static final int REFLECTION_WIDTH = 320;
    private static final int REFLECTION_HEIGHT = 180;
    
    //Larger sizes result in better quality, but worse performance
    protected static final int REFRACTION_WIDTH = 1280;
    private static final int REFRACTION_HEIGHT = 720;
 
	
	private int reflectionFrameBuffer,
	reflectionTexture,
	reflectionDepthBuffer;
	
	private int refractionFrameBuffer,
	refractionTexture,
	refractionDepthBuffer;
	
	/*
	 * Constructor
	 * Creates the FrameBuffers when class is created
	 */
	public WaterFrameBuffer() {
		initialiseReflectionFrameBuffer();
        initialiseRefractionFrameBuffer();
	}
	
	//Creates a frameBuffer to create reflections
	private void initialiseReflectionFrameBuffer() {
        reflectionFrameBuffer = createFrameBuffer();
        reflectionTexture = createTexture(REFLECTION_WIDTH,REFLECTION_HEIGHT);
        reflectionDepthBuffer = createDepthBuffer(REFLECTION_WIDTH,REFLECTION_HEIGHT);
        unbindFrameBuffer();
    }
	
	//Creates a frameBuffer to create refraction
    private void initialiseRefractionFrameBuffer() {
        refractionFrameBuffer = createFrameBuffer();
        refractionTexture = createTexture(REFRACTION_WIDTH,REFRACTION_HEIGHT);
        refractionDepthBuffer = createDepthTexture(REFRACTION_WIDTH,REFRACTION_HEIGHT);
        unbindFrameBuffer();
    }
	
	//Binds the reflection frameBuffer to be rendered to
	public void bindReflectionFrameBuffer() {
        bindFrameBuffer(reflectionFrameBuffer,REFLECTION_WIDTH,REFLECTION_HEIGHT);
    }
    
	//Binds the refraction frameBuffer to be rendered to
    public void bindRefractionFrameBuffer() {
        bindFrameBuffer(refractionFrameBuffer,REFRACTION_WIDTH,REFRACTION_HEIGHT);
    }
	
    //Changes what framebuffer that is being drawn to, from default screen frame buffer, to the created framebuffer
  	private void bindFrameBuffer(int framebuffer, int width, int height) {
  		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
  		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer);
  		GL11.glViewport(0,0,width, height);
  	}
  	
	//Creates a frameBuffer, and binds it. Returns the framebuffer id. 
	private int createFrameBuffer() {
		int frameBuffer = GL30.glGenFramebuffers();
		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		//Uses standard the default color attachment
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		return frameBuffer;
	}
	
	//Creates a texture, and binds it to the currently bound frameBuffer
	private int createTexture(int width, int height){
		int texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texture, 0);
		return texture;
	}
	
	//Creates a depth texture, and binds it to the currently bound frameBuffer
	private int createDepthTexture(int width, int height){
		int depthTexture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT,
				(ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, depthTexture, 0);
		return depthTexture;
	}
		
	//Creates a depth texture, and binds it to the currently bound frameBuffer
	private int createDepthBuffer(int width, int height){
		int depthBuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
		return depthBuffer;
	}
	
	//Sets the framebuffer back to the default screen frameBuffer
	public void unbindFrameBuffer() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, DisplayManager.getWidth() , DisplayManager.getHeight());
	}
	
	
	public int getReflectionTexture() {
		return reflectionTexture;
	}

	public int getRefractionTexture() {
		return refractionTexture;
	}
	
	public int getRefractionDepthBuffer() {
		return refractionDepthBuffer;
	}
	
	//freeing up memory when exiting level/game/application
	public void cleanUp() {
		GL30.glDeleteFramebuffers(reflectionFrameBuffer);
		GL30.glDeleteRenderbuffers(reflectionDepthBuffer);
		GL11.glDeleteTextures(reflectionTexture);
		
		GL30.glDeleteFramebuffers(refractionFrameBuffer);
		GL30.glDeleteRenderbuffers(refractionDepthBuffer);
		GL11.glDeleteTextures(refractionTexture);
	}
}