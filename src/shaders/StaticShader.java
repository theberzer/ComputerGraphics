/*
 * 
 */
package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

/**
 * The Class StaticShader.
 *
 * @author berzi
 * 
 *         Shader class that talks with the vertexShader program as to help with
 *         communication with the GPU
 */
public class StaticShader extends ShaderProgram {

	/** The Constant VERTEX. */
	private static final String VERTEX = "src/shaders/vertexShader.txt";
	
	/** The Constant FRAGMENT. */
	private static final String FRAGMENT = "src/shaders/fragmentShader.txt";

	/** The location tranformation matrix. */
	private int location_tranformationMatrix;
	
	/** The location projection matrix. */
	private int location_projectionMatrix;
	
	/** The location view matrix. */
	private int location_viewMatrix;
	
	/** The location light position. */
	private int location_lightPosition;
	
	/** The location light colour. */
	private int location_lightColour;
	
	/** The location shine damper. */
	private int location_shineDamper;
	
	/** The location reflectivity. */
	private int location_reflectivity;
	
	/** The location sky colour. */
	private int location_skyColour;
	
	/** The location plane. */
	private int location_plane;

	/**
	 * Instantiates a new static shader.
	 */
	public StaticShader() {
		super(VERTEX, FRAGMENT);
	}

	/* (non-Javadoc)
	 * @see shaders.ShaderProgram#bindAttributes()
	 */
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	/* (non-Javadoc)
	 * @see shaders.ShaderProgram#getAllUniformLocations()
	 */
	@Override
	protected void getAllUniformLocations() {
		location_tranformationMatrix = super.getUniformLocation("tranformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColour = super.getUniformLocation("skyColour");
		location_plane = super.getUniformLocation("plane");
	}

	/**
	 * Load sky colour.
	 *
	 * @param r the r
	 * @param g the g
	 * @param b the b
	 */
	public void loadSkyColour(float r, float g, float b) {
		super.loadVector(location_skyColour, new Vector3f(r, g, b));
	}

	/**
	 * Load sgine variables.
	 *
	 * @param damper the damper
	 * @param refletivity the refletivity
	 */
	public void loadSgineVariables(float damper, float refletivity) {
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, refletivity);
	}

	/**
	 * Load transformation matrix.
	 *
	 * @param matrix the matrix
	 */
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_tranformationMatrix, matrix);

	}

	/**
	 * Load projection matrix.
	 *
	 * @param projection the projection
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}

	/**
	 * Load view matrix.
	 *
	 * @param camera the camera
	 */
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

	/**
	 * Load light position.
	 *
	 * @param light the light
	 */
	public void loadLightPosition(Light light) {
		super.loadVector(location_lightPosition, light.getPostion());
		super.loadVector(location_lightColour, Light.getIntensity());
	}

	/**
	 * Load clip plane.
	 *
	 * @param plane the plane
	 */
	public void loadClipPlane(Vector4f plane) {
		super.loadVector(location_plane, plane);
	}

}
