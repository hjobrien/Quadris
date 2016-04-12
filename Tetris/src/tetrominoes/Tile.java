package tetrominoes;

import javafx.scene.paint.Color;

public class Tile {
	private boolean isActive;
	private boolean isFilled;
	private Color color;
	
	/**
	 * all tiles are either initialized inactive, unfilled, white, or active, filled, color
	 */
	public Tile(){
		this.isActive = false;
		this.isFilled = false;
		this.color = Color.WHITE;
	}
	
	/**
	 * initialized to a filled tile with a color
	 * @param c    the tile color
	 */
	public Tile(Color c){
		this.isActive = true;
		this.isFilled = true;
		this.color = c;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		this.isActive = active;
	}

	public boolean isFilled() {
		return isFilled;
	}

	public void setFilled(boolean filled) {
		this.isFilled = filled;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color c) {
		this.color = c;
	}
	
	public String toString(){
	  return "Filled: " + isFilled + " Active: " + isActive;
	}

}
