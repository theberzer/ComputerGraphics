/*
 * 
 */
package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * The Class ShaderProgram.
 *
 * @author berzi
 * 
 *         The shader Program is a abstract class that is used by the
 *         StaticShader (and in the future, the TerrainShader)
 */
public abstract class ShaderProgram {
	
	/** The program ID. */
	private int programID;
	
	/** The vertex shader ID. */
	private int vertexShaderID;
	
	/** The fragment shader ID. */
	private int fragmentShaderID;

	/** The matrix buffer. */
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	/**
	 * Instantiates a new shader program.
	 *
	 * @param vertexFile the vertex file
	 * @param fragmentFile the fragment file
	 */
	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}

	/**
	 * Gets the all uniform locations.
	 *
	 * @return the all uniform locations
	 */
	protected abstract void getAllUniformLocations();

	/**
	 * Gets the uniform location.
	 *
	 * @param uniformName the uniform name
	 * @return the uniform location
	 */
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}

	/**
	 * Start.
	 */
	public void start() {
		GL20.glUseProgram(programID);
	}

	/**
	 * Stop.
	 */
	public void stop() {
		GL20.glUseProgram(0);
	}

	/**
	 * Clean UP.
	 */
	public void cleanUP() {
		stop();
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}

	/**
	 * Bind attributes.
	 */
	protected abstract void bindAttributes();

	/**
	 * Bind attribute.
	 *
	 * @param attribute the attribute
	 * @param variableName the variable name
	 */
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}

	/**
	 * Load int.
	 *
	 * @param location the location
	 * @param value the value
	 */
	protected void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}

	/**
	 * Load float.
	 *
	 * @param location the location
	 * @param value the value
	 */
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}

	/**
	 * Load vector.
	 *
	 * @param location the location
	 * @param vector the vector
	 */
	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}

	/**
	 * Load vector.
	 *
	 * @param location the location
	 * @param vector the vector
	 */
	protected void loadVector(int location, Vector4f vector) {
		GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
	}

	/**
	 * Load boolean.
	 *
	 * @param location the location
	 * @param value the value
	 */
	protected void loadBoolean(int location, boolean value) {
		float toLoad = 0;
		if (value) {
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}

	/**
	 * Load matrix.
	 *
	 * @param location the location
	 * @param matrix the matrix
	 */
	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}

	/**
	 * Load shader.
	 *
	 * @param file the file
	 * @param type the type
	 * @return the int
	 */
	private static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader! Check your GLSL");
			System.exit(-1);
		}
		return shaderID;
	}
}
