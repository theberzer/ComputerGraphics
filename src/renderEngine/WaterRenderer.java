/*
 * 
 */
package renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import models.RawModel;
import shaders.WaterShader;
import textures.WaterTile;
import toolbox.Maths;
import toolbox.FrameBuffer;

/**
 * The Class WaterRenderer.
 *
 * @author Kim Nilsen Brusevold
 */
public class WaterRenderer {

	/** The Constant DUDV_MAP. */
	private static final String DUDV_MAP = "DUDV_water";
	
	/** The Constant NORMAL_MAP. */
	private static final String NORMAL_MAP = "normalmap_water";
	
	/** The Constant WAVE_SPEED. */
	private static final float WAVE_SPEED = 0.03f;
	
	/** The quad. */
	private RawModel quad;
	
	/** The shader. */
	private WaterShader shader;
	
	/** The wfb. */
	private FrameBuffer reflectionBuffer;
	
	private FrameBuffer refractionBuffer;

	/** The Du dv texture. */
	private int DuDvTexture;
	
	/** The move factor. */
	private float moveFactor = 0;

	/** The normal map. */
	private int normalMap;

	/**
	 * Instantiates a new water renderer.
	 *
	 * @param loader the loader
	 * @param shader the shader
	 * @param projectionMatrix the projection matrix
	 * @param wfb the wfb
	 */
	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, FrameBuffer reflectionBuffer, 
			FrameBuffer refractionBuffer) {
		this.shader = shader;
		this.reflectionBuffer = reflectionBuffer;
		this.refractionBuffer = refractionBuffer;
		DuDvTexture = loader.loadTexture(DUDV_MAP);
		normalMap = loader.loadTexture(NORMAL_MAP);
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	/**
	 * Render.
	 *
	 * @param watertiles the watertiles
	 * @param camera the camera
	 * @param light the light
	 */
	public void render(List<WaterTile> watertiles, Camera camera, Light light, float tiling, float waveSpeed, float reflectionFactor,
			float distortionStrength) {
		prepareRender(camera, light, tiling, waveSpeed, reflectionFactor, distortionStrength);
		for (WaterTile tile : watertiles) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(
					new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0, WaterTile.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}

	/**
	 * Prepare render.
	 *
	 * @param camera the camera
	 * @param light the light
	 */
	private void prepareRender(Camera camera, Light light, float tiling, float waveSpeed, float reflectionFactor, float distortionStrength) {
		shader.start();
		shader.loadViewMatrix(camera);
		// This value 60 should be the fps the game is running at.
		moveFactor += waveSpeed * DisplayManager.getFrameTimeSeconds();
		moveFactor %= 1;
		shader.loadMoveFactor(moveFactor);
		shader.loadLight(light);
		shader.loadProjectionPlanes();
		shader.loadTiling(tiling);
		shader.loadReflectionFactor(reflectionFactor);
		shader.loadDistortionStrength(distortionStrength);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, reflectionBuffer.getColourTexture());

		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, refractionBuffer.getColourTexture());

		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, DuDvTexture);

		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMap);

		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, refractionBuffer.getDepthTexture());

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	/**
	 * Unbind.
	 */
	private void unbind() {
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	/**
	 * Sets the up VAO.
	 *
	 * @param loader the new up VAO
	 */
	private void setUpVAO(Loader loader) {
		// A 2 dimentional plane.
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVAO(vertices, 2);
	}

}
