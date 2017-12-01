package renderEngine;

import java.util.List;

import models.RawModel;
import shaders.WaterShader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import toolbox.Maths;
import water.WaterFrameBuffer;
import water.WaterTile;
import entities.Camera;
/**
 * @author Kim Nilsen Brusevold
 * 
 * 
 *
 */
public class WaterRenderer {

	private RawModel quad;
	private WaterShader shader;
	private WaterFrameBuffer wfb;

	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffer wfb) {
		this.shader = shader;
		this.wfb = wfb;
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	public void render(List<WaterTile> watertiles, Camera camera) {
		prepareRender(camera);	
		for (WaterTile tile : watertiles) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(
					new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
					WaterTile.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}
	
	private void prepareRender(Camera camera){
		shader.start();
		shader.loadViewMatrix(camera);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, wfb.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, wfb.getRefractionTexture());
	}
	
	private void unbind(){
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
