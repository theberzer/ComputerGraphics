package particles;

public class ParticleTexture {
	
	private int textureId;
	private int numRows;
	
	private boolean additive;
	
	public ParticleTexture(int textureId, int numRows, boolean additive) {
		this.textureId = textureId;
		this.numRows = numRows;
		this.additive = additive;
	}
	
	public boolean getAdditive() {
		return additive;
	}
	
	public int getTextureId() {
		return textureId;
	}
	
	public int getNumRows() {
		return numRows;
	}
	
}
