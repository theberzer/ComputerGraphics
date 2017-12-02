package terrains;

import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

public class Terrain {

	private static final float SIZE = 4000;
	private static final int VERTEX_COUNT = 256;
	
	
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	private HeightGenerator generator;
	private float x;
	private float z;
	private int seed;
	private float[][] heights;
	
	
	public Terrain(int gridX, int gridZ, int seed, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap,
			String heightMap) {
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.seed = seed;
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		
		generator = new HeightGenerator(gridX, gridZ, VERTEX_COUNT, seed);
		this.model = generateTerrain(loader, heightMap);
	}

	private RawModel generateTerrain(Loader loader, String heightMap) {

		HeightGenerator generateor = new HeightGenerator();

		int VERTEX_COUNT = 128;
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];

		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		int pointer = 0;

		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				float height = getHeight(i, j, generateor);
				heights[j][i] = height;
				vertices[vertexPointer * 3] = j / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = i / ((float) VERTEX_COUNT - 1) * SIZE;
				
				Vector3f normal = calculateNormal(j, i, generateor);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = i / ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}

		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	private Vector3f calculateNormal(int x, int z, HeightGenerator generateor) {
		float heightL = getHeight(x - 1, z, generateor);
		float heightR = getHeight(x + 1, z, generateor);
		float heightD = getHeight(x, z - 1, generateor);
		float heightU = getHeight(x, z + 1, generateor);

		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	private float getHeight(int x, int z, HeightGenerator generateor) {
		return generateor.generateHeight(x, z);
	}
	
	private float getHeight(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		
		if(gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0 ) {
			return 0;
		}
		
		float xCoordinate = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoordinate = (terrainZ % gridSquareSize) / gridSquareSize;
		float height;
		
		if (xCoordinate <= (1 - zCoordinate)) {
			height = Maths
					.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoordinate, zCoordinate));
		} else {
			height = Maths
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoordinate, zCoordinate));
		}
		
		return height;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public RawModel getModel() {
		return model;
	}

	public void setModel(RawModel model) {
		this.model = model;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}

}
