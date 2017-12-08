/*
 * 
 */
package renderEngine;

import java.text.DecimalFormat;

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
		
	private static long lastFrameTime;
		
	private static float delta;
	
	private static float time;
	
	private static float lastFPS;
	
	private static int fps;
	
	private static int day = 0;

	/**
	 * Creates the display.
	 */
	public static void createDisplay() {
		ContextAttribs attribs = new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true);

		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getTime();
		lastFPS = getTime();
	}

	/**
	 * Update display.
	 */
	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getTime();
	    delta = (currentFrameTime - lastFrameTime) / 1000f;
	    lastFrameTime = currentFrameTime;
	    time += getDelta() * 1000;
	    updateFPS();
	}

	/**
	 * Close display.
	 */
	public static void closeDisplay() {
		Display.destroy();
	}
	
	public static float getDelta() {
	    return delta;
	}


	/**
	 * Gets the current time.
	 *
	 * @return the current time
	 */
	private static long getTime() {
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
	
	public  static float getInGameHour() {
		float timeMin = time / 60f;
		float timeHour = timeMin / 60f;
		day = (int) Math.floor(timeHour / 24);
		
		timeHour %= 24;
			
		return timeHour;
	}
	
	
    public static void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("FPS: " + fps + " Hour: " + getInGameHour() +" Day: " + day);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }
    
    public static boolean isBright() {
    	boolean answer = true;
    	    	
    	if (getInGameHour() >= 0  && getInGameHour() < 5) {
			answer = false;
		} else if (getInGameHour() >= 5 && getInGameHour() < 10) {
			answer = true;
		} else if (getInGameHour() >= 10 && getInGameHour() < 19) {
			answer = true;
		} else {
			answer = false;
		}
    	
    	return answer;
    }
    
    public static int getDay() {
    	return day;
    }
	

}
