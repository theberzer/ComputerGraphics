package renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;


import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import models.RawModel;
import textures.TextureData;

/**
 * @author berzi
 *
 *	The loader class bring it together, contains the vaos, vbos and textures
 *
 */
public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indicies){
		int vaoID = createVAO();
		bindIndiciesBuffer(indicies);
		storeDatainAttributeList(0, 3, positions);
		storeDatainAttributeList(1, 2, textureCoords);
		storeDatainAttributeList(2, 3, normals);
		unbindVAO();
		
		return new RawModel(vaoID, indicies.length);
	}
	
	public RawModel loadToVAO(float[] positions, int dimension) {
		int vaoID = createVAO();
		this.storeDatainAttributeList(0, dimension, positions);
		unbindVAO();
		
		return new RawModel(vaoID, positions.length / dimension);
	}
	
	public RawModel loadToVAO(float[] position) {
		int vaoID = createVAO();
		this.storeDatainAttributeList(0, 2, position);
		unbindVAO();
		return new RawModel(vaoID, position.length/2);
	}
	
	public int loadTexture(String fileName){
		Texture texture = null;
		
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + fileName + ".png"));
			
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.5f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int textureID = texture.getTextureID();
		textures.add(textureID);
		
		return textureID;
	}
	
	
	public void cleanUP() {
		for(int vao:vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo:vbos){
			GL15.glDeleteBuffers(vbo);
		}
		for (int texture:textures){
			GL11.glDeleteTextures(texture);
		}
	}
	
	private int createVAO(){
		int vaoID = GL30.glGenVertexArrays();
		
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		
		return vaoID;
	}
	
	private void storeDatainAttributeList(int attributeNumber,int coordinateSize, float[] data){
		int vboID = GL15.glGenBuffers();
		
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void unbindVAO(){
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndiciesBuffer(int[] indicies){
		int vboID = GL15.glGenBuffers();
		
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indicies);	
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		}
	
	private IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		
		buffer.put(data);
		buffer.flip();
		
		return buffer;
		
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	  public int loadCubeMap(String[] cubeMapTextures) { 
		    int textureID = GL11.glGenTextures(); 
		     
		    GL13.glActiveTexture(GL13.GL_TEXTURE0); 
		    GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID); 
		     
		    for (int i = 0; i < cubeMapTextures.length; i++) { 
		      TextureData data = decodeTexture("res/skybox/" + cubeMapTextures[i] + ".png"); 
		       
		      GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 
		          0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer()); 
		    } 
		     
		    GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		    GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		    GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
		    GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		    GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR); 
		    
		    textures.add(textureID); 
		    return textureID; 
		  } 
		   
		  private TextureData decodeTexture(String fileName) { 
		    int width = 0; 
		    int height = 0; 
		    ByteBuffer buffer = null; 
		     
		    try { 
		      FileInputStream in = new FileInputStream(fileName); 
		      PNGDecoder decoder = new PNGDecoder(in); 
		       
		      width = decoder.getWidth(); 
		      height = decoder.getHeight(); 
		      buffer = ByteBuffer.allocateDirect(4 * width * height); 
		      decoder.decode(buffer, width * 4, Format.RGBA); 
		       
		      buffer.flip(); 
		      in.close(); 
		    } catch (Exception e) { 
		      e.printStackTrace(); 
		      System.err.println("Failed loading:  " + fileName); 
		      System.exit(-1); 
		    } 
		     
		    return new TextureData(height, width, buffer); 
		  } 
}
