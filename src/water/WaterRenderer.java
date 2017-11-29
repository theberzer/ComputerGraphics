package water;

import java.util.List;

import models.RawModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.Loader;
import toolbox.Maths;
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

	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
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
