/*
 * 
 */
package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
 * The Class DisplayManager.
 *
 * @author berzi
 * 
 *         Main Display class, The Display to render my objects
 */
public class DisplayManager {

	/** The Constant WIDTH. */
	private static final int WIDTH = 1280;
	
	/** The Constant HEIGHT. */
	private static final int HEIGHT = 720;
	
	/** The Constant FPS_CAP. */
	private static final int FPS_CAP = 120;

	/** The delta. */
	private static float delta;
	
	/** The last frame time. */
	private static long lastFrameTime;

	/**
	 * Creates the display.
	 */
	public static void createDisplay() {
		ContextAttribs attribs = new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true);

		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Computer Graphics Assaignment 1 v1");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
	}

	/**
	 * Update display.
	 */
	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
		Display.update();
	}

	/**
	 * Close display.
	 */
	public static void closeDisplay() {
		Display.destroy();
	}

	/**
	 * Gets the frame time seconds.
	 *
	 * @return the frame time seconds
	 */
	public static float getFrameTimeSeconds() {
		return delta;
	}

	/**
	 * Gets the current time.
	 *
	 * @return the current time
	 */
	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public static int getWidth() {

		return WIDTH;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public static int getHeight() {
		return HEIGHT;
	}

}
