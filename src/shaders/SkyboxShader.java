package shaders; 
 
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import renderEngine.DisplayManager;
import shaders.ShaderProgram; 
import toolbox.Maths; 
 
public class SkyboxShader extends ShaderProgram{ 
     
      private static final String VERTEX_FILE = "src/shaders/skyboxVertexShader.txt"; 
      private static final String FRAGMENT_FILE = "src/shaders/skyboxFragmentShader.txt"; 
      private static final float ROTATE = 1f;
        
      private int location_projectionMatrix; 
      private int location_viewMatrix; 
      private int location_fogColor;
      private int location_cubeMap;
      private int location_cubeMap1;
      private int location_blend;

      
      private float rotation = 0;
        
      public SkyboxShader() { 
          super(VERTEX_FILE, FRAGMENT_FILE); 
      } 
        
      public void loadProjectionMatrix(Matrix4f matrix){ 
          super.loadMatrix(location_projectionMatrix, matrix); 
      } 
      
      public void loadFogColor(float red, float green, float blue){ 
          super.loadVector(location_fogColor, new Vector3f(red, green, blue));
      } 
    
      public void loadViewMatrix(Camera camera){ 
          Matrix4f matrix = Maths.createViewMatrix(camera); 
          matrix.m30 = 0;
          matrix.m31 = 0;
          matrix.m32 = 0;
          rotation += ROTATE * DisplayManager.getFrameTimeSeconds();
          Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);
          super.loadMatrix(location_viewMatrix, matrix); 
      } 
        
      public void loadBlend(float blend) {
    	  super.loadFloat(location_blend, blend);
      }
      
      public void connectTextureUnits() {
    	  super.loadInt(location_cubeMap, 0);
    	  super.loadInt(location_cubeMap1, 1);

      }
      @Override 
      protected void getAllUniformLocations() { 
          location_projectionMatrix = super.getUniformLocation("projectionMatrix"); 
          location_viewMatrix = super.getUniformLocation("viewMatrix"); 
          location_fogColor = super.getUniformLocation("fogColor"); 
          location_cubeMap = super.getUniformLocation("cubeMap"); 
          location_cubeMap1 = super.getUniformLocation("cubeMap1"); 
          location_blend = super.getUniformLocation("blend"); 

      } 
    
      
      @Override 
      protected void bindAttributes() { 
          super.bindAttribute(0, "position"); 
      } 
} 