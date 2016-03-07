package tetrominoes;

public class Block {
	private Tile[][][] configurations = new Tile[][][]{};
	private int rotationIndex = 0;
	
	public boolean debug = false;
	
//	private boolean[][] shape = new boolean[4][4];
	private boolean falling = true;
	
	//still in development
	private int[] locationInGrid = new int[2];
	
	public Block(Tile[][][] configurations){
		this.configurations = configurations;
		
		//row, column of bottom right corner
		int startingRowIndex = configurations[rotationIndex].length - 1;
		int startingColumnIndex = (10 - configurations[rotationIndex][0].length) / 2 + configurations[rotationIndex][0].length - 1;
		locationInGrid = new int[]{startingRowIndex, startingColumnIndex};
	}
	
	public Block(Block b){
		this.configurations = b.configurations;
		this.rotationIndex = b.rotationIndex;
		this.locationInGrid = b.locationInGrid;
	}
	
	public Tile[][] getShape(){
		return configurations[rotationIndex];
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
		for(Tile[] row : getShape()){
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
	public void rotateRight() {
		rotationIndex--;
		if (rotationIndex < 0){
			rotationIndex = configurations.length - 1;
		}
	}

	public void rotateLeft() {
		rotationIndex++;
		if (rotationIndex > configurations.length - 1){
			rotationIndex = 0;
		}
	}

}
