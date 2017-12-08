package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import models.RawModel;
import shaders.SkyboxShader;
import toolbox.Colors;

public class SkyBoxRenderer {

	private static final float SIZE = 4000f;
	private static final float[] VERTICES = { -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE,
			-SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE,

			-SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE,
			-SIZE, SIZE,

			SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE,
			-SIZE,

			-SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE,
			SIZE,

			-SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE,
			-SIZE,

			-SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE,
			-SIZE, SIZE };
	private static String[] textureDayFiles = { "stormydays_rt", "stormydays_lf", "stormydays_up", "stormydays_dn",
			"stormydays_bk", "stormydays_ft" };
	private static String[] textureNightFiles = { "purplenebula_rt", "purplenebula_lf", "purplenebula_up",
			"purplenebula_dn", "purplenebula_bk", "purplenebula_ft" };

	private RawModel cube;
	private SkyboxShader shader;
	private int textureID;
	private int nightTextureID;
	private float time;
	private float x = 0; 
	private float y = -100;
	private float z = 1000;
	private float intensity = 0.1f;
	private Vector3f dayColor = Colors.convertToFloat(new Vector3f(93, 94, 96));
	private Vector3f nightColor = Colors.convertToFloat(new Vector3f(149, 152, 155));

	
	public SkyBoxRenderer(Loader loader, Matrix4f projectionMatrix) {
		cube = loader.loadToVAO(VERTICES, 3);
		textureID = loader.loadCubeMap(textureDayFiles);
		nightTextureID = loader.loadCubeMap(textureNightFiles);
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Camera camera) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFogColor(dayColor.x, dayColor.y, dayColor.z);

		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		dayNight();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);

		shader.stop();
	}

	private void dayNight() {
		time += DisplayManager.getFrameTimeSeconds() * 1000;

		int cube;
		int cube1;
		float blend = 0;
		
		//Time variables to easier manipulate speed of the cycle
		float start = 0;
		float morning = 5000;
		float noon = 10000;
		float night = 19000;
		float end = 24000;
		Vector3f lightIntennsity = new Vector3f(blend, blend, blend);
		
		
		time %= end;
		
		if (time >= start && time < morning) {
			cube = nightTextureID;
			cube1 = nightTextureID;
			blend = (time - start) / (morning - start);
			x += 0.2;
			y += 0.2; 
			z -= 0.2;
			intensity += 0.0002f;
		} else if (time >= morning && time < noon) {
			cube = nightTextureID;
			cube1 = textureID;
			blend = (time - morning) / (noon - morning);
			x -= 0.2;
			y += 0.2; 
			z -= 0.2;
			intensity += 0.0016f;
		} else if (time >= noon && time < night) {
			cube = textureID;
			cube1 = textureID;
			blend = (time - noon) / (night - noon);
			x -= 0.111;
			y -= 0.111; 
			z += 0.111; 
			intensity -= 0.000555556f;
		} else {
			cube = textureID;
			cube1 = nightTextureID;
			blend = (time - night) / (end - night);
			x += 0.2;
			y -= 0.2; 
			z += 0.2;
			intensity -= 0.00008f;
		}
			
		if (intensity > 1) {
			intensity = 1;
		}
		
		if (intensity < 0.1f) {
			intensity = 0.1f;
		}
		
		lightIntennsity.set(intensity, intensity, intensity);
		Light.setIntensity(lightIntennsity);
		Light.setPosition(x, y, z);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, cube);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, cube1);
		shader.loadBlend(blend);
	}
}
