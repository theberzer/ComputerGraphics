package renderEngine;

import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;
import toolbox.Colors;

import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;

/**
 * @author berzi
 * 
 *         MasterRenderer class that i will utilise to create all objects that
 *         need rendering
 *         
 *
 */
public class MastrerRendrer {
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.01f;
	private static final float FAR_PLANE = 6000;

	private static Vector3f color = Colors.convertToFloat(new Vector3f(149, 152, 155));

	private Matrix4f projectionMatrix;

	
	private StaticShader shader = new StaticShader();
	private TerrainShader terrainShader = new TerrainShader();

	private EntityRenderer renderer;
	private TerrainRenderer terrainRendrer;
	private SkyBoxRenderer skyboxRendrer;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();

	public MastrerRendrer(Loader loader) {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRendrer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRendrer = new SkyBoxRenderer(loader, projectionMatrix);
	}

	
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
	
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

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

	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(color.x, color.y, color.z, 1);
	}
 
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

	public void cleanUp() {
		shader.cleanUP();
		terrainShader.cleanUP();
	}

	public void cleanLists() {
		entities.clear();
		terrains.clear();
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	public static float getNearPlane() {
		return NEAR_PLANE;
	}
	public static float getFarPlane() {
		return FAR_PLANE;
	}
}
