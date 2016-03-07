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
		
		//x, y of bottom right corner
		locationInGrid = new int[]{configurations[0].length / 2 + 5, configurations.length};
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
		// TODO Auto-generated method stub
		
	}

	public void rotateLeft() {
		// TODO Auto-generated method stub
		
	}

}
