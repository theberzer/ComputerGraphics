package terrains;

import java.util.ArrayList;
import java.util.Random;

public class HeightGenerator {

	private static final float AMPLITUDE = 200f;
	private static final int NOISE = 4;
	private static final float ROUGHNESS = 0.9f;
	
	private static ArrayList<HeightGenerator> hgs = new ArrayList<>();
	private static ArrayList<Float> seeds = new ArrayList<>();
	private Random random = new Random();
	private int seed;
	private int xOffset = 0;
    private int zOffset = 0;
    
    public HeightGenerator() {			
    	this.seed = random.nextInt(1000000000);
    }
    		    

	public HeightGenerator(int gridX, int gridZ, int vertexCount, int seed) {
        this.seed = seed;
        xOffset = gridX * (vertexCount - 1);
        zOffset = gridZ * (vertexCount - 1);
        hgs.add(this);
    }
	
	
	public float generateHeight(int x, int z) {
		float d = (float) Math.pow(2, NOISE - 1);
		float total =  interpolateNoise(x / 15f, z / 400f) * AMPLITUDE;
		
		for(int i = 0; i < NOISE; i++) {
			float frequency = (float) (Math.pow(2, 1) / d);
			float amplitude = (float) Math.pow(ROUGHNESS, i) * AMPLITUDE;
			total += interpolateNoise((x + xOffset / 400f) * frequency, (z + zOffset) * frequency) * amplitude; 
		}
		
		return total;
	}

	private float interpolateNoise (float x, float z) {
		int intX = (int) x;
		int intZ = (int) z;
		float fracX = x - intX;
		float fracZ = z - intZ;
		
		float v1 = smoothNoise(intX, intZ);
		float v2 = smoothNoise(intX + 1 , intZ);
		float v3 = smoothNoise(intX, intZ + 1);
		float v4 = smoothNoise(intX + 1, intZ + 1);
		float i1 = interpolate(v1, v2, fracX);
		float i2 = interpolate(v3, v4, fracX);
		
		return interpolate(i1, i2, fracZ);
	}
	
	private float interpolate(float a, float b, float blend) {
		 double theta = blend * Math.PI;
	     float f = (float)(1f - Math.cos(theta)) * 0.5f;
	 
	     return a * (1f - f) + b * f;
	}
	
	private float smoothNoise(int x, int z) {
		  float total = 0;
		  float corners = (noise(x - 1, z - 1) + noise(x + 1, z - 1) 
		   + noise(x - 1, z + 1) + noise(x + 1, z + 1)) / 16f;

		   float sides = (noise(x - 1, z) + noise(x + 1, z) 
		   + noise(x, z - 1) + noise(x, z + 1)) / 8f;
		   
		   float center = noise(x, z) / 4f;
		   
		   total = corners + sides + center;
		   
		   return total;
	}
	
	private float noise(int x, int z) {
		random.setSeed(x * 79648 + z * 61957 + seed);
		final float r = random.nextFloat() * 2f - 1f;
		return r;
		
	}


	public static ArrayList<HeightGenerator> getHgs() {
		return hgs;
	}
	
	
	
}
