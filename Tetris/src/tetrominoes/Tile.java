package tetrominoes;

import javafx.scene.paint.Color;

public class Tile {
	private boolean active;
	private boolean filled;
	private Color color;
	
	public Tile(boolean active, boolean filled){
		this.active = active;
		this.filled = filled;
		this.color = Color.WHITE;
	}
	
	public Tile(boolean active, boolean filled, Color c){
		this.active = active;
		this.filled = filled;
		this.color = c;
	}
	
	public Tile(){
		this.active = false;
		this.filled = false;
		this.color = Color.WHITE;
	}
	
	public Tile(Color c){
		this.active = true;
		this.filled = true;
		this.color = c;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isFilled() {
		return filled;
	}

	public void setFilled(boolean filled) {
		this.filled = filled;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color2) {
		this.color = color2;
		
	}

}
