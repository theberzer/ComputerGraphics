/*
 * 
 */
package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import renderEngine.DisplayManager;
import toolbox.Maths;

/**
 * The Class SkyboxShader.
 */
public class SkyboxShader extends ShaderProgram {

	/** The Constant VERTEX_FILE. */
	private static final String VERTEX_FILE = "src/shaders/skyboxVertexShader.txt";
	
	/** The Constant FRAGMENT_FILE. */
	private static final String FRAGMENT_FILE = "src/shaders/skyboxFragmentShader.txt";
	
	/** The Constant ROTATE. */
	private static final float ROTATE = 0.05f;

	/** The location projection matrix. */
	private int location_projectionMatrix;
	
	/** The location view matrix. */
	private int location_viewMatrix;
	
	/** The location fog color. */
	private int location_fogColor;
	
	/** The location cube map. */
	private int location_cubeMap;
	
	/** The location cube map 1. */
	private int location_cubeMap1;
	
	/** The location blend. */
	private int location_blend;

	/** The rotation. */
	private float rotation = 0;

	/**
	 * Instantiates a new skybox shader.
	 */
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	/**
	 * Load projection matrix.
	 *
	 * @param matrix the matrix
	 */
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	/**
	 * Load fog color.
	 *
	 * @param dayRed the day red
	 * @param dayGreen the day green
	 * @param dayBlue the day blue
	 */
	public void loadFogColor(float dayRed, float dayGreen, float dayBlue) {
		super.loadVector(location_fogColor, new Vector3f(dayRed, dayGreen, dayBlue));

	}

	/**
	 * Load view matrix.
	 *
	 * @param camera the camera
	 */
	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		rotation += ROTATE * DisplayManager.getDelta();
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);
		super.loadMatrix(location_viewMatrix, matrix);
	}

	/**
	 * Load blend.
	 *
	 * @param blend the blend
	 */
	public void loadBlend(float blend) {
		super.loadFloat(location_blend, blend);
	}

	/**
	 * Connect texture units.
	 */
	public void connectTextureUnits() {
		super.loadInt(location_cubeMap, 0);
		super.loadInt(location_cubeMap1, 1);
	}

	/* (non-Javadoc)
	 * @see shaders.ShaderProgram#getAllUniformLocations()
	 */
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_fogColor = super.getUniformLocation("fogColor");
		location_cubeMap = super.getUniformLocation("cubeMap");
		location_cubeMap1 = super.getUniformLocation("cubeMap1");
		location_blend = super.getUniformLocation("blend");
	}

	/* (non-Javadoc)
	 * @see shaders.ShaderProgram#bindAttributes()
	 */
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
}