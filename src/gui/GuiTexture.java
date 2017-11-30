package gui;

public class GuiTexture {
	private int Texture;
	private org.lwjgl.util.vector.Vector2f position, scale;
	
	public GuiTexture(int texture, org.lwjgl.util.vector.Vector2f position, org.lwjgl.util.vector.Vector2f scale) {
		super();
		Texture = texture;
		this.position = position;
		this.scale = scale;
	}

	public int getTexture() {
		return Texture;
	}

	public org.lwjgl.util.vector.Vector2f getPosition() {
		return position;
	}

	public org.lwjgl.util.vector.Vector2f getScale() {
		return scale;
	} 
	
	
}
