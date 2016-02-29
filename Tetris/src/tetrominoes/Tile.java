package tetrominoes;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

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
	public Paint getColor() {
		return color;
	}

}
