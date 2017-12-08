/*
 * 
 */
package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;
import toolbox.Colors;

/**
 * The Class MastrerRendrer.
 *
 * @author berzi
 * 
 *         MasterRenderer class that i will utilise to create all objects that
 *         need rendering
 */
public class MastrerRendrer {
	
	/** The Constant Field of View. */
	private static final float FOV = 70;
	
	/** The Constant Minimum Render Distance. */
	private static final float NEAR_PLANE = 0.1f;
	
	/** The Constant Max Render Distance. */
	private static final float FAR_PLANE = 8000;

	/** The color */
	private static Vector3f color = Colors.convertToFloat(new Vector3f(149, 152, 155));

	/** The projection matrix. */
	private Matrix4f projectionMatrix;

	/** The shader. */
	private StaticShader shader = new StaticShader();
	
	/** The terrain shader. */
	private TerrainShader terrainShader = new TerrainShader();

	/** The renderer. */
	private EntityRenderer renderer;
	
	/** The terrain rendrer. */
	private TerrainRenderer terrainRendrer;
	
	/** The skybox rendrer. */
	private SkyBoxRenderer skyboxRendrer;

	/** The entities list to help keep order. */
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	
	/** The terrains. */
	private List<Terrain> terrains = new ArrayList<Terrain>();

	/**
	 * Instantiates a new mastrer rendrer.
	 *
	 * @param loader the loader
	 */
	public MastrerRendrer(Loader loader) {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRendrer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRendrer = new SkyBoxRenderer(loader, projectionMatrix);
	}

	/**
	 * Render.
	 *
	 * @param terrains the terrains
	 * @param entities the entities
	 * @param sun the sun
	 * @param camera the camera
	 * @param clipPlane the clip plane
	 */
	public void render(List<Terrain> terrains, List<Entity> entities, Light sun, Camera camera, Vector4f clipPlane) {

		prepare();
		skyboxRendrer.render(camera);
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColour(color.x, color.y, color.z);
		shader.loadLightPosition(sun);
		shader.loadViewMatrix(camera);
		renderer.render(this.entities);
		shader.stop();

		terrainShader.start();
		terrainShader.loadSkyColour(color.x, color.y, color.z);
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		terrainShader.loadPlane(clipPlane);
		terrainRendrer.render(this.terrains);
		terrainShader.stop();
	}

	/**
	 * Processes terrain.
	 *
	 * @param terrain the terrain
	 */
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	/**
	 * Processes entity.
	 *
	 * @param entity the entity
	 */
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}

	/**
	 * Prepares the scene for rendering
	 */
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(color.x, color.y, color.z, 1);
	}

	/**
	 * Creates the projection matrix.
	 */
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / length);
		projectionMatrix.m33 = 0;
	}

	/**
	 * Cleans up the shaders.
	 */
	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}

	/**
	 * Cleans the lists.
	 */
	public void cleanLists() {
		entities.clear();
		terrains.clear();
	}

	/**
	 * Gets the projection matrix.
	 *
	 * @return the projection matrix
	 */
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	/**
	 * Gets the near plane.
	 *
	 * @return the near plane
	 */
	public static float getNearPlane() {
		return NEAR_PLANE;
	}

	/**
	 * Gets the far plane.
	 *
	 * @return the far plane
	 */
	public static float getFarPlane() {
		return FAR_PLANE;
	}
}
