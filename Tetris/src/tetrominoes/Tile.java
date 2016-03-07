package tetrominoes;

import javafx.scene.paint.Color;

public class Tile {
	private boolean active;
	private boolean filled;
	private Color color;
	
	//all tiles are either initialized inactive, unfilled, white, or active, filled, color
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
	public void setColor(Color c) {
		this.color = c;
	}

}
