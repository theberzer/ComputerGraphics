package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

/**
 * @author berzi
 *
 *	Shader class that talks with the vertexShader program as to help with communication with the GPU
 *
 */
public class StaticShader extends ShaderProgram {
	
	private static final String VERTEX = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT = "src/shaders/fragmentShader.txt";
	
	private int location_tranformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_shineDamper;
	private int location_reflectivity;

	public StaticShader() {
		super(VERTEX, FRAGMENT);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_tranformationMatrix = super.getUniformLocation("tranformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix"); 
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
	}
	
	public void loadSgineVariables(float damper, float refletivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, refletivity);
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_tranformationMatrix, matrix);

	}
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadLightPosition(Light light){
		super.loadVector(location_lightPosition, light.getPostion());
		super.loadVector(location_lightColour, light.getColour());
	}

}
