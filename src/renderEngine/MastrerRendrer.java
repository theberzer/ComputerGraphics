package renderEngine;

import shaders.StaticShader;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
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
 *         MasterRenderer class that is utilized to create all objects that
 *         need rendering
 *
 */
public class MastrerRendrer {
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.01f;
	private static final float FAR_PLANE = 1000;

	private Matrix4f projectionMatrix;

	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;

	;

	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();

	public MastrerRendrer() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
	}

	public void render(Light sun, Camera camera, Vector4f clipPlane) {
		prepare();
		shader.start();
		
		shader.loadClipPlane(clipPlane);
		shader.loadLightPosition(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		
		shader.stop();
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
		GL11.glClearColor(0.2f, 0.1f, 0f, 1);
	}

	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}

	public void cleanUp() {
		shader.cleanUp();
	}
	
	/**
	 * Because the way water reflection/refraction is implemented, the scene (different parts of it) has to be rendered multiple times. 
	 * The renderer now only clears the hashMap after the display is updated, NOT after each render
	 */
	public void cleanUpEntities() {
		entities.clear();
	}
	
    public Matrix4f getProjectionMatrix() {
    	return projectionMatrix;
    }
}
