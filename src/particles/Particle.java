package particles;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import renderEngine.DisplayManager;

/*
 *  Particle class - contains every element needed to draw and animate a particle
 */

public class Particle {

	
	private Vector3f position; // Position (x, y, z)
	private Vector3f velocity; // Velocity as vector (x, y, z)
	private float gravityEffect; // How much the particle is effected by gravity
	private float lifeLength; // How long the particle is alive (before it's killed)
	private float rotation; // Rotation around all axis equally
	private float scale; // Scale value
	private float distance; // Distance from camera - helps to render close particles before further ones. 
	
	private float elapsedTime = 0; // How long it's been alive
	
	private ParticleTexture texture; // The texture of the particle
	
	// Texture offset when using texture atlas'
	private Vector2f texOffset1 = new Vector2f(); // (x, y) of where on the atlas
	private Vector2f texOffset2 = new Vector2f(); // Where it's going (on the atlas texture)
	private float blend; // A BlendFactor to blend the texture according to the offsets
	
	private Vector3f tempVec = new Vector3f(); // Tmp variable to make the code more efficient
	
	// Basic Constructor
	public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation,
			float scale) {
		this.texture = texture;
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		
		ParticleMaster.addParticle(this); // Adding the particle to the global handler 
		
	}	
	
	// Update method checking if the particle is alive
	// Run from ParticleMaster.update() function
	protected boolean update(Camera camera) {
		
		// Calculating velocity with a time-function from the displaymanager - only effected by gravity in the y-coord
		 velocity.y += -9.81 * gravityEffect * DisplayManager.getDelta(); 
		 tempVec.set(velocity); // Set the new value in tmp
		 tempVec.scale(DisplayManager.getDelta()); // Scaling the new vector to the time per frame
		 Vector3f.add(tempVec, position, position); // Updating the position vector
		 // Finding the distance from the camera - lengthSquared() because of efficiency 
		 distance = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
		 updateTextureCoordsInfo(); // Updating the offsets and blend
		 elapsedTime += DisplayManager.getDelta(); // Extending the elapsed time
		 return elapsedTime < lifeLength; // Return bool (true if alive, false if not alive)
	}
	
	// Function for finding the offsets in the texture and the blendfactor
	private void updateTextureCoordsInfo() {
		float lifeFactor = elapsedTime / lifeLength; // How far into it's lifecycle the particle is
		int stageCount = texture.getNumRows() * texture.getNumRows(); // Total number of "stages"
		float atlasProg = lifeFactor * stageCount; // How far along the particle is in those stages
		int index1 = (int) Math.floor(atlasProg); // Finding the floored value (index) of the first position
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1; // Getting the next index if it's not the last one
		this.blend = atlasProg % 1; // Blendfactor to blend the stages of the two indexes (in the fragmentshader)
		setTextureOffset(texOffset1, index1); // Finding the texture offset1 (x, y) of index 1
		setTextureOffset(texOffset2, index2); // Finding the texture offset2 (x, y)  of index 2
	}

	// Function for finding the offsets based on the index and number of rows (Only in squared texture atlas')
	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % texture.getNumRows(); // Column of the atlas
		int row = index / texture.getNumRows(); // Row of the atlas
		offset.x = (float) column / texture.getNumRows(); // Setting the value x-coord
		offset.y = (float) row / texture.getNumRows(); // Setting the value y-coord
		
	}
	
	/*
	 * Genereic getter's
	 */
	
	public float getDistance() {
		return distance;
	}
	
	public Vector2f getTexOffset1() {
		return texOffset1;
	}

	public Vector2f getTexOffset2() {
		return texOffset2;
	}

	public float getBlend() {
		return blend;
	}

	public ParticleTexture getTexture() {
		return texture;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

}
