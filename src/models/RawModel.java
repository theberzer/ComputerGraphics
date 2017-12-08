/*
 * 
 */
package models;

/**
 * The Class RawModel.
 *
 * @author berzi
 * 
 * 
 *         RawModel class where we will save vaoID and vertexCount
 */
public class RawModel {
	
	/** The vao ID. */
	private int vaoID;
	
	/** The vertex count. */
	private int vertexCount;

	/**
	 * Instantiates a new raw model.
	 *
	 * @param vaoID the vao ID
	 * @param vertexCount the vertex count
	 */
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	/**
	 * Gets the vao ID.
	 *
	 * @return the vao ID
	 */
	public int getVaoID() {
		return vaoID;
	}

	/**
	 * Gets the vertex count.
	 *
	 * @return the vertex count
	 */
	public int getVertexCount() {
		return vertexCount;
	}

}
