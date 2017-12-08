/*
 * 
 */
package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Light;
import renderEngine.MastrerRendrer;
import toolbox.Maths;

/**
 * The Class WaterShader.
 */
public class WaterShader extends ShaderProgram {

	/** The Constant VERTEX_FILE. */
	private final static String VERTEX_FILE = "src/shaders/waterVertexShader.txt";
	
	/** The Constant FRAGMENT_FILE. */
	private final static String FRAGMENT_FILE = "src/shaders/waterFragmentShader.txt";

	/** The location model matrix. */
	private int location_modelMatrix;
	
	/** The location view matrix. */
	private int location_viewMatrix;
	
	/** The location projection matrix. */
	private int location_projectionMatrix;
	
	/** The location reflection texture. */
	private int location_reflectionTexture;
	
	/** The location refraction texture. */
	private int location_refractionTexture;
	
	/** The location du dv texture. */
	private int location_DuDvTexture;
	
	/** The location move factor. */
	private int location_moveFactor;
	
	/** The location camera position. */
	private int location_cameraPosition;
	
	/** The location noral map. */
	private int location_noralMap;
	
	/** The location light position. */
	private int location_lightPosition;
	
	/** The location light color. */
	private int location_lightColor;
	
	/** The location depth texture. */
	private int location_depthTexture;
	
	/** The location plane near. */
	private int location_planeNear;
	
	/** The location plane far. */
	private int location_planeFar;
	
	/** The location tiling. */
	private int location_tiling;
	
	/** The location reflectionFactor */
	private int location_reflectionFactor;
	
	/** The location distortionStrength */
	private int location_distortionStrength;
	
	
	/**
	 * Instantiates a new water shader.
	 */
	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	/* (non-Javadoc)
	 * @see shaders.ShaderProgram#bindAttributes()
	 */
	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	/* (non-Javadoc)
	 * @see shaders.ShaderProgram#getAllUniformLocations()
	 */
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
		location_reflectionTexture = getUniformLocation("reflectionTexture");
		location_refractionTexture = getUniformLocation("refractionTexture");
		location_DuDvTexture = getUniformLocation("DuDvTexture");
		location_moveFactor = getUniformLocation("moveFactor");
		location_cameraPosition = getUniformLocation("cameraPosition");
		location_noralMap = getUniformLocation("noralMap");
		location_lightPosition = getUniformLocation("lightPosition");
		location_lightColor = getUniformLocation("lightColor");
		location_depthTexture = getUniformLocation("depthTexture");
		location_planeNear = getUniformLocation("planeNear");
		location_planeFar = getUniformLocation("planeFar");
		location_tiling = getUniformLocation("tiling");
		location_reflectionFactor = getUniformLocation("reflectionFactor");
		location_distortionStrength = getUniformLocation("distortionStrength");
	}

	/**
	 * Load move factor.
	 *
	 * @param factor the factor
	 */
	public void loadMoveFactor(float factor) {
		super.loadFloat(location_moveFactor, factor);
	}

	/**
	 * Load light.
	 *
	 * @param light the light
	 */
	public void loadLight(Light light) {
		super.loadVector(location_lightColor, Light.getIntensity());
		super.loadVector(location_lightPosition, light.getPostion());
	}

	/**
	 * Connect texture units.
	 */
	public void connectTextureUnits() {
		super.loadInt(location_reflectionTexture, 0);
		super.loadInt(location_refractionTexture, 1);
		super.loadInt(location_DuDvTexture, 2);
		super.loadInt(location_noralMap, 3);
		super.loadInt(location_depthTexture, 4);
	}

	/**
	 * Load projection matrix.
	 *
	 * @param projection the projection
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}

	/**
	 * Load view matrix.
	 *
	 * @param camera the camera
	 */
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
		super.loadVector(location_cameraPosition, camera.getPosition());
	}

	/**
	 * Load model matrix.
	 *
	 * @param modelMatrix the model matrix
	 */
	public void loadModelMatrix(Matrix4f modelMatrix) {
		loadMatrix(location_modelMatrix, modelMatrix);
	}

	/**
	 * Load projection planes.
	 */
	public void loadProjectionPlanes() {
		super.loadFloat(location_planeNear, MastrerRendrer.getNearPlane());
		super.loadFloat(location_planeFar, MastrerRendrer.getFarPlane());
	}
	
	public void loadTiling(float tiling) {
		super.loadFloat(location_tiling, tiling);
	}
	
	public void loadReflectionFactor(float factor) {
		super.loadFloat(location_reflectionFactor, factor);
	}
	
	public void loadDistortionStrength(float distortionStrength) {
		super.loadFloat(location_distortionStrength, distortionStrength);
	}
}
