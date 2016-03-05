package tetrominoes;

public class Block {
	Tile[][] shape = new Tile[][]{};
	
	public boolean debug = false;
	
	
//	private boolean[][] shape = new boolean[4][4];
	private boolean falling = true;
	
	//still in development
	private int[] locationInGrid = new int[2];
	
	public Block(Tile[][] size){
		this.shape = size;
		
		//x, y of bottom right corner
		locationInGrid = new int[]{size[0].length / 2 + 5, size.length};
	}
	
	public Tile[][] getShape(){
		return shape;
	}
	
	public boolean isFalling(){
		return falling;
	}
	
	public void setFalling(){
		falling = true;
	}
	
	public void stoppedFalling(){
		falling = false;
	}
	
	public String toString(){
		String s = "";
		for(Tile[] row : shape){
			for(Tile t : row){
				if(t.isFilled())
					s += "x";
				else
					s += "o";
			}
			s += "\n";
		}
		return s;
	}

	public void setShape(Tile[][] emptyShape) {
		this.shape = emptyShape;
	}
	
	public void setGridLocation(int[] point){
		this.locationInGrid = point;
	}
	
	public void setGridLocation(int x, int y){
		this.locationInGrid = new int[]{x, y};
	}
	
	public int[] getGridLocation(){
		return this.locationInGrid;
	}
	
	public void moveRight(){
		this.locationInGrid = new int[]{ locationInGrid[0], locationInGrid[1] + 1};
	}

	public void moveLeft(){
		this.locationInGrid = new int[]{ locationInGrid[0], locationInGrid[1] - 1};
	}
	
	public void moveUp(){
		this.locationInGrid = new int[]{ locationInGrid[0] - 1, locationInGrid[1]};
	}
	
	public void moveDown(){
		this.locationInGrid = new int[]{ locationInGrid[0] + 1, locationInGrid[1]};
	}
}
