/*
 * 
 */
package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import models.RawModel;
import shaders.SkyboxShader;
import terrains.Terrain;
import toolbox.Colors;

/**
 * The Class SkyBoxRenderer.
 */
public class SkyBoxRenderer {

	/** The Constant SIZE. */
	private static final float SIZE = 4000f;
	
	/** The Constant VERTICES. */
	private static final float[] VERTICES = { -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE,
			-SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE,
			-SIZE, SIZE,

			SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE,
			-SIZE,

			-SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE,
			SIZE,

			-SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE,
			-SIZE,

			-SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE,
			-SIZE, SIZE };
	
	/** The texture day files. */
	private static String[] textureDayFiles = { "stormydays_rt", "stormydays_lf", "stormydays_up", "stormydays_dn",
			"stormydays_bk", "stormydays_ft" };
	
	/** The texture night files. */
	private static String[] textureNightFiles = { "purplenebula_rt", "purplenebula_lf", "purplenebula_up",
			"purplenebula_dn", "purplenebula_bk", "purplenebula_ft" };

	/** The cube. */
	private RawModel cube;
	
	/** The shader. */
	private SkyboxShader shader;
	
	/** The texture ID. */
	private int textureID;
	
	/** The night texture ID. */
	private int nightTextureID;
	
	/** The time. */
	private float time;
	
	/** The x. */
	private float x = 0;
	
	/** The y. */
	private float y = -100;
	
	/** The z. */
	private float z = 1000;
	
	/** The intensity. */
	private float intensity = 0.9f;
	
	/** The day color. */
	private Vector3f dayColor = Colors.convertToFloat(new Vector3f(93, 94, 96));
	
	/** The night color. */
	private Vector3f nightColor = Colors.convertToFloat(new Vector3f(149, 152, 155));

	/**
	 * Instantiates a new sky box renderer.
	 *
	 * @param loader the loader
	 * @param projectionMatrix the projection matrix
	 */
	public SkyBoxRenderer(Loader loader, Matrix4f projectionMatrix) {
		cube = loader.loadToVAO(VERTICES, 3);
		textureID = loader.loadCubeMap(textureDayFiles);
		nightTextureID = loader.loadCubeMap(textureNightFiles);
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	/**
	 * Render.
	 *
	 * @param camera the camera
	 */
	public void render(Camera camera) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFogColor(dayColor.x, dayColor.y, dayColor.z);

		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		dayNight();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);

		shader.stop();
	}

	/**
	 * Day night.
	 */
	private void dayNight() {
		float timeHour = DisplayManager.getInGameHour();

		
		int cube;
		int cube1;
		float blend = 0;

		// Time variables to easier manipulate speed of the cycle
		float start = 0;
		float morning = 5;
		float noon = 10;
		float night = 19;
		float end = 24;

		;

		if (timeHour >= start && timeHour < morning) {
			cube = nightTextureID;
			cube1 = nightTextureID;
			blend = (timeHour - start) / (morning - start);
			intensity += 0.2f;
		} else if (timeHour >= morning && timeHour < noon) {
			cube = nightTextureID;
			cube1 = textureID;
			blend = (timeHour - morning) / (noon - morning);

		} else if (timeHour >= noon && timeHour < night) {
			cube = textureID;
			cube1 = textureID;
			blend = (timeHour - noon) / (night - noon);
		} else {
			cube = textureID;
			cube1 = nightTextureID;
			blend = (timeHour - night) / (end - night);
		}

		if (intensity > 1) {
			intensity = 1;
		}

		if (intensity < 0.1f) {
			intensity = 0.1f;
		}

		Light.setIntensity(new Vector3f(intensity, intensity, intensity));
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, cube);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, cube1);
		shader.loadBlend(blend);
	}
}
