/*
 * 
 */
package gui;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import models.RawModel;
import renderEngine.Loader;
import toolbox.Maths;


/**
 * The Class GuiRenderer.
 */
public class GuiRenderer {

	/** The quad. */
	private RawModel quad;
	
	/** The shader. */
	private GuiShader shader;

	/**
	 * Instantiates a new gui renderer.
	 *
	 * @param loader the loader
	 */
	public GuiRenderer(Loader loader) {
		float[] vertexPosition = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = loader.loadToVAO(vertexPosition);
		shader = new GuiShader();
	}

	/**
	 * Render.
	 *
	 * @param guis the guis
	 */
	public void render(List<GuiTexture> guis) {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);

		for (GuiTexture texture : guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTexture());
			Matrix4f matrix = Maths.createTransformationMatrix(texture.getPosition(), texture.getScale());
			shader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	/**
	 * Clean up.
	 */
	public void cleanUp() {
		shader.cleanUp();
	}
}
