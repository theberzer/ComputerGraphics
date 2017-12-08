/*
 * 
 */
package terrains;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

/**
 * The Class Terrain.
 */
public class Terrain {

	/** The Constant VERTEX_COUNT. */
	private static final int VERTEX_COUNT = 256;

	/** The size. */
	private static float SIZE = 10000;
	
	/** The terrains. */
	private static ArrayList<Terrain> terrains = new ArrayList<>();
	
	/** The hg. */
	private static ArrayList<HeightGenerator> hg = new ArrayList<>();

	/** The model. */
	private RawModel model;
	
	/** The texture pack. */
	private TerrainTexturePack texturePack;
	
	/** The blend map. */
	private TerrainTexture blendMap;
	
	/** The generator. */
	private HeightGenerator generator;
	
	/** The x. */
	private float x;
	
	/** The z. */
	private float z;
	
	/** The heights. */
	private float[][] heights;
	
	/** The seed. */
	private int seed;

	/**
	 * Instantiates a new terrain.
	 *
	 * @param gridX the grid X
	 * @param gridZ the grid Z
	 * @param seed the seed
	 * @param loader the loader
	 * @param texturePack the texture pack
	 * @param blendMap the blend map
	 */
	public Terrain(int gridX, int gridZ, int seed, Loader loader, TerrainTexturePack texturePack,
			TerrainTexture blendMap) {
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.seed = seed;
		this.texturePack = texturePack;
		this.blendMap = blendMap;

		try {
			generator = hg.get(0);
		} catch (IndexOutOfBoundsException e) {
			generator = new HeightGenerator(gridX, gridZ, VERTEX_COUNT, seed);
			hg.add(generator);
		}

		this.model = generateTerrain(loader, generator);
		terrains.add(this);
	}

	/**
	 * Generate terrain.
	 *
	 * @param loader the loader
	 * @param generator the generator
	 * @return the raw model
	 */
	private RawModel generateTerrain(Loader loader, HeightGenerator generator) {

		int VERTEX_COUNT = 256;
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
				float height = setHeight(i, j, generator);
				heights[j][i] = height;
				vertices[vertexPointer * 3] = j / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = i / ((float) VERTEX_COUNT - 1) * SIZE;

				Vector3f normal = calculateNormal(j, i, generator);
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

	/**
	 * Calculate normal.
	 *
	 * @param x the x
	 * @param z the z
	 * @param generateor the generateor
	 * @return the vector 3 f
	 */
	private Vector3f calculateNormal(int x, int z, HeightGenerator generateor) {
		float heightL = setHeight(x - 1, z, generateor);
		float heightR = setHeight(x + 1, z, generateor);
		float heightD = setHeight(x, z - 1, generateor);
		float heightU = setHeight(x, z + 1, generateor);

		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	/**
	 * Sets the height.
	 *
	 * @param x the x
	 * @param z the z
	 * @param generateor the generateor
	 * @return the float
	 */
	private float setHeight(int x, int z, HeightGenerator generateor) {
		return generateor.generateHeight(x, z);
	}

	/**
	 * Gets the height.
	 *
	 * @param worldX the world X
	 * @param worldZ the world Z
	 * @return the height
	 */
	public float getHeight(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

		if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}

		float xCoordinate = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoordinate = (terrainZ % gridSquareSize) / gridSquareSize;
		float height;

		if (xCoordinate <= (1 - zCoordinate)) {
			height = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoordinate, zCoordinate));
		} else {
			height = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoordinate, zCoordinate));
		}

		return height;
	}

	/**
	 * Gets the terrain.
	 *
	 * @return the terrain
	 */
	public static Terrain getTerrain() {
		Terrain terrain;
		for (Terrain t : terrains) {
			terrain = t;
			return terrain;
		}
		return null;
	}

	/**
	 * Increase map size.
	 *
	 * @param increment the increment
	 */
	public static void increaseMapSize(float increment) {
		SIZE += increment;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Gets the z.
	 *
	 * @return the z
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Sets the z.
	 *
	 * @param z the new z
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public RawModel getModel() {
		return model;
	}

	/**
	 * Sets the model.
	 *
	 * @param model the new model
	 */
	public void setModel(RawModel model) {
		this.model = model;
	}

	/**
	 * Gets the texture pack.
	 *
	 * @return the texture pack
	 */
	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	/**
	 * Gets the blend map.
	 *
	 * @return the blend map
	 */
	public TerrainTexture getBlendMap() {
		return blendMap;
	}

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public static float getSIZE() {
		return SIZE;
	}

	/**
	 * Sets the size.
	 *
	 * @param sIZE the new size
	 */
	public static void setSIZE(float sIZE) {
		SIZE = sIZE;
	}

	/**
	 * Gets the terrains.
	 *
	 * @return the terrains
	 */
	public static ArrayList<Terrain> getTerrains() {
		return terrains;
	}

}
