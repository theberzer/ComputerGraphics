package particles;
 
import java.util.Random;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.DisplayManager;

/*
 * Particle System
 * - Acts as a controlpoint for how the particles behave, look and spawn
 */

public class ParticleSystem {
     
	// 
    private float pps // Particles spawn per second
    , averageSpeed // Average speed
    , gravityComplient // How the particles is effected by gravity
    , averageLifeLength // Average lifelenght
    , averageScale; // Avergage scale
 
    // Error's (How much randomness it will generate) 0 = no random, 1 = completly random
    private float speedError, lifeError, scaleError = 0;
    private boolean randomRotation = false;
    
    private Vector3f direction; // Direction of the system - in which way the particle spew out
    private float directionDeviation = 0; // Randomness factor
 
    private ParticleTexture texture; // Particle texture 
    
    private Random random = new Random(); 
 
    // Standard constructor
    public ParticleSystem(ParticleTexture texture, float pps, float speed, float gravityComplient, float lifeLength, float scale) {
    	this.texture = texture;
        this.pps = pps;
        this.averageSpeed = speed;
        this.gravityComplient = gravityComplient;
        this.averageLifeLength = lifeLength;
        this.averageScale = scale;
    }
    
    // Function for generating particles in a system
    // Called from mainGameLoop
    public void generateParticles(Vector3f systemCenter){
        float delta = DisplayManager.getDelta();
        float particlesToCreate = pps * delta;
        int count = (int) Math.floor(particlesToCreate);
        float partialParticle = particlesToCreate % 1;
        for(int i=0;i<count;i++){
            emitParticle(systemCenter);
        }
        if(Math.random() < partialParticle){
            emitParticle(systemCenter);
        }
    }
    
    private void emitParticle(Vector3f center) {
        Vector3f velocity = null;
        if(direction!=null){
            velocity = generateRandomUnitVectorWithinCone(direction, directionDeviation);
        }else{
            velocity = generateRandomUnitVector();
        }
        velocity.normalise();
        velocity.scale(generateValue(averageSpeed, speedError));
        float scale = generateValue(averageScale, scaleError);
        float lifeLength = generateValue(averageLifeLength, lifeError);
        new Particle(texture, new Vector3f(center), velocity, gravityComplient, lifeLength, generateRotation(), scale);
    }
    
    public void setDirection(Vector3f direction, float deviation) {
        this.direction = new Vector3f(direction);
        this.directionDeviation = (float) (deviation * Math.PI);
    }
 
    public void randomizeRotation() {
        randomRotation = true;
    }
    
    public void setSpeedError(float error) {
        this.speedError = error * averageSpeed;
    }
    
    public void setLifeError(float error) {
        this.lifeError = error * averageLifeLength;
    }
    
    public void setScaleError(float error) {
        this.scaleError = error * averageScale;
    }
    

    /*
     * Functions for generating values based on previously input values
     * Used to calculate how to randomly assign velocty, 
     * rotation, scale and lifelength of particles in emitParticle()
     * 
     * These are all mostly written by ThinMatrix (As mentioned in the projectfile)
     * and are therefor not in the scope of this project (not commented)
     */
    
    
    private static Vector3f generateRandomUnitVectorWithinCone(Vector3f coneDirection, float angle) {
        float cosAngle = (float) Math.cos(angle);
        Random random = new Random();
        float theta = (float) (random.nextFloat() * 2f * Math.PI);
        float z = cosAngle + (random.nextFloat() * (1 - cosAngle));
        float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
        float x = (float) (rootOneMinusZSquared * Math.cos(theta));
        float y = (float) (rootOneMinusZSquared * Math.sin(theta));
 
        Vector4f direction = new Vector4f(x, y, z, 1);
        if (coneDirection.x != 0 || coneDirection.y != 0 || (coneDirection.z != 1 && coneDirection.z != -1)) {
            Vector3f rotateAxis = Vector3f.cross(coneDirection, new Vector3f(0, 0, 1), null);
            rotateAxis.normalise();
            float rotateAngle = (float) Math.acos(Vector3f.dot(coneDirection, new Vector3f(0, 0, 1)));
            Matrix4f rotationMatrix = new Matrix4f();
            rotationMatrix.rotate(-rotateAngle, rotateAxis);
            Matrix4f.transform(rotationMatrix, direction, direction);
        } else if (coneDirection.z == -1) {
            direction.z *= -1;
        }
        return new Vector3f(direction);
    }
     
    private Vector3f generateRandomUnitVector() {
        float theta = (float) (random.nextFloat() * 2f * Math.PI);
        float z = (random.nextFloat() * 2) - 1;
        float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
        float x = (float) (rootOneMinusZSquared * Math.cos(theta));
        float y = (float) (rootOneMinusZSquared * Math.sin(theta));
        return new Vector3f(x, y, z);
    }
     
    private float generateRotation() {
        if (randomRotation) {
            return random.nextFloat() * 360f;
        } else {
            return 0;
        }
    }    
    
    private float generateValue(float average, float errorMargin) {
        float offset = (random.nextFloat() - 0.5f) * 2f * errorMargin;
        return average + offset;
    }
 
}