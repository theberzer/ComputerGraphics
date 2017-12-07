package renderEngine;

import java.util.List;

import models.RawModel;
import shaders.WaterShader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import toolbox.Maths;
import water.WaterFrameBuffer;
import water.WaterTile;
import entities.Camera;
import entities.Light;
/**
 * @author Kim Nilsen Brusevold
 * 
 * 
 *
 */
public class WaterRenderer {
	
	private static final String DUDV_MAP = "DUDV_water";
	private static final String NORMAL_MAP = "normalmap_water";
	private static final float  WAVE_SPEED = 0.03f;
	private RawModel quad;
	private WaterShader shader;
	private WaterFrameBuffer wfb;
	
	private int DuDvTexture; 
	private float moveFactor = 0; 
	
	private int normalMap;

	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffer wfb) {
		this.shader = shader;
		this.wfb = wfb;
		DuDvTexture = loader.loadTexture(DUDV_MAP);
		normalMap = loader.loadTexture(NORMAL_MAP);
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	public void render(List<WaterTile> watertiles, Camera camera, Light light) {
		prepareRender(camera, light);	
		for (WaterTile tile : watertiles) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(
                    new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
                    WaterTile.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
			System.out.println("Water rendered");
		}
		unbind();
	}
	
	private void prepareRender(Camera camera, Light light){
		shader.start();
		shader.loadViewMatrix(camera);
		//This value 60 should be the fps the game is running at. 
		moveFactor += WAVE_SPEED * 0.01;
		moveFactor %= 1;
		shader.loadMoveFactor(moveFactor);
		shader.loadLight(light);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, wfb.getReflectionTexture());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, wfb.getRefractionTexture());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, DuDvTexture);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMap);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, wfb.getRefractionDepthBuffer());
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void unbind(){
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void setUpVAO(Loader loader) {
		//A 2 dimentional plane.
		float[] vertices = { -1, -1, 
							 -1,  1, 
							  1, -1, 
							  1, -1, 
							 -1,  1, 
							  1,  1 };
		quad = loader.loadToVAO(vertices, 2);
	}

}
