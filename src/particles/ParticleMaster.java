package particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import renderEngine.Loader;
import toolbox.InsertionSort;

/*
 * Global Particle Handler 
 * - Takes care of initializing the particle systems, containing them in lists, sorting them and updating them
 * - Also connects the textures with the corresponding particlesystem
 */
public class ParticleMaster {
	
	// HashMap of all the particlesystems and their texture
	private static Map<ParticleTexture, List<Particle>> particles = 
			new HashMap<ParticleTexture, List<Particle>>(); 
	
	// The ParticleRenderer
	private static ParticleRenderer renderer;
	
	// Simple init function to set the renderer based on the games main loader
	// and also getting the projectionMatrix
	public static void init(Loader loader, Matrix4f projectionMatrix) {
		renderer = new ParticleRenderer(loader, projectionMatrix);
		
	}
	
	// Update particle function which updates and removes dead particles and sorts them for efficiency 
	// Takes in the camera to calculate the distance from the camera to each particle
	public static void update(Camera camera) {
		// Standard Iterator
		Iterator<Entry<ParticleTexture, List<Particle>>> mapIterator = 
				particles.entrySet().iterator();
		// Loop through all the particlesystems
		while(mapIterator.hasNext()) {
			List<Particle> list = mapIterator.next().getValue();
			// New iterator on the the specific system
			Iterator<Particle> iterator = list.iterator();
			// Loop through each particlesystem 
			while(iterator.hasNext()) {
				Particle p = iterator.next();
				boolean stillAlive = p.update(camera); // Updating particle and checking if dead
				if(!stillAlive) {
					iterator.remove(); // Remove if dead
					if(list.isEmpty()) {
						mapIterator.remove(); // If whole system is empty the remove the particlesystem
					}
				}
			}
			// Simple sorting function to always have the lists be sorted (efficiency)
			InsertionSort.sortHighToLow(list); 
		}
		
	}
	
	// Renderfunction from mainGameLoop
	public static void renderParticles(Camera camera) {
		renderer.render(particles, camera); // Calls the renderer class render() func
	}
	
	// Cleanup func called each frame
	public static void cleanUp() {
		renderer.cleanUp();
	}
	
	// Standard function for adding particles in list based on texture key
	public static void addParticle(Particle particle) {
		List<Particle> list = particles.get(particle.getTexture());
		if(list==null) {
			list = new ArrayList<Particle>();
			particles.put(particle.getTexture(), list);
		}
		list.add(particle);
	}
}
