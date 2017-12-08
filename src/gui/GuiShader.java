/*
 * 
 */
package gui;

import org.lwjgl.util.vector.Matrix4f;

import shaders.ShaderProgram;

/**
 * The Class GuiShader.
 */
public class GuiShader extends ShaderProgram {

	/** The Constant VERTEX_FILE. */
	private static final String VERTEX_FILE = "src/gui/guiVertexShader.txt";
	
	/** The Constant FRAGMENT_FILE. */
	private static final String FRAGMENT_FILE = "src/gui/guiFragmentShader.txt";

	/** The location transformation matrix. */
	private int location_transformationMatrix;

	/**
	 * Instantiates a new gui shader.
	 */
	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	/**
	 * Load transformation.
	 *
	 * @param matrix the matrix
	 */
	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	/* (non-Javadoc)
	 * @see shaders.ShaderProgram#getAllUniformLocations()
	 */
	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	/* (non-Javadoc)
	 * @see shaders.ShaderProgram#bindAttributes()
	 */
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
