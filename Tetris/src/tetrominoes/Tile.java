package tetrominoes;

public class Tile {
	private boolean active;
	private boolean filled;
	
	public Tile(boolean active, boolean filled){
		this.active = active;
		this.filled = filled;
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

}
