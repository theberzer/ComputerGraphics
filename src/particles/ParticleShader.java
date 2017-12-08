package particles;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import shaders.ShaderProgram;

/*
 * Standard Shader program for the particlesystem 
 * 
 */

public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/particles/particleVShader.txt";
	private static final String FRAGMENT_FILE = "src/particles/particleFShader.txt";

	private int location_numRows;
	private int location_projectionMatrix;

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_numRows = super.getUniformLocation("numRows");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "modelViewMatrix");
		super.bindAttribute(5, "texOffsets");
		super.bindAttribute(6, "blendFactor");
	}

	protected void loadNumRows(float numRows) {
		super.loadFloat(location_numRows, numRows);
	}

	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}
