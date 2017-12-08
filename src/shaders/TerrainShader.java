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
 * The Class TerrainShader.
 */
public class TerrainShader extends ShaderProgram {

	/** The Constant VERTEX_FILE. */
	private static final String VERTEX_FILE = "src/shaders/terrainVertexShader.txt";
	
	/** The Constant FRAGMENT_FILE. */
	private static final String FRAGMENT_FILE = "src/shaders/terrainFragmentShader.txt";

	/** The location transformation matrix. */
	private int location_transformationMatrix;
	
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
	
	/** The location background texture. */
	private int location_backgroundTexture;
	
	/** The location r texture. */
	private int location_rTexture;
	
	/** The location g texture. */
	private int location_gTexture;
	
	/** The location b texture. */
	private int location_bTexture;
	
	/** The location blend map. */
	private int location_blendMap;
	
	/** The location sky colour. */
	private int location_skyColour;
	
	/** The location plane. */
	private int location_plane;

	/**
	 * Instantiates a new terrain shader.
	 */
	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	/* (non-Javadoc)
	 * @see shaders.ShaderProgram#bindAttributes()
	 */
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
	}

	/* (non-Javadoc)
	 * @see shaders.ShaderProgram#getAllUniformLocations()
	 */
	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");
		location_skyColour = super.getUniformLocation("skyColour");
		location_plane = super.getUniformLocation("plane");

	}

	/**
	 * Connect texture units.
	 */
	public void connectTextureUnits() {
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);

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
	 * Load shine variables.
	 *
	 * @param damper the damper
	 * @param reflectivity the reflectivity
	 */
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}

	/**
	 * Load transformation matrix.
	 *
	 * @param matrix the matrix
	 */
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	/**
	 * Load light.
	 *
	 * @param light the light
	 */
	public void loadLight(Light light) {
		super.loadVector(location_lightPosition, light.getPostion());
		super.loadVector(location_lightColour, Light.getIntensity());
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
	 * Load projection matrix.
	 *
	 * @param projection the projection
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}

	/**
	 * Load plane.
	 *
	 * @param plane the plane
	 */
	public void loadPlane(Vector4f plane) {
		super.loadVector(location_plane, plane);
	}

}
