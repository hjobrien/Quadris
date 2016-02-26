package tetrominoes;

public class Block {
	private boolean[][] shape = new boolean[4][4];
	private boolean falling = true;
	
	//still in development
	private int[][] locationInGrid;
	
	public Block(boolean[][] size){
		this.shape = size;
	}
	
	public boolean[][] getShape(){
		return shape;
	}
	
	public void rotRight(){
		boolean[][] tempMap = shape;
		reflectY(tempMap);
		reflectXY(tempMap);
	}
	
	public void rotLeft(){
		boolean[][] tempMap = shape;
		reflectXY(tempMap);
		reflectY(tempMap);
	}

	private void reflectXY(boolean[][] tempMap) {
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				shape[i][j] = tempMap[j][i];
			}
		}
		
	}

	private void reflectY(boolean[][] tempMap) {
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				shape[i][4-j] = tempMap[i][j];
			}
		}
	}
	
	public boolean isFalling(){
		return falling;
	}
	
	public void stoppedFalling(){
		falling = false;
	}

}
