package tetrominoes;

import java.util.ArrayList;

public class Block {
	private ArrayList<ArrayList<Tile>> shape = new ArrayList<ArrayList<Tile>>();
	
	
//	private boolean[][] shape = new boolean[4][4];
	private boolean falling = true;
	
	//still in development
	private int[][] locationInGrid;
	
	public Block(ArrayList<ArrayList<Tile>> size){
		this.shape = size;
	}
	
	public ArrayList<ArrayList<Tile>> getShape(){
		return shape;
	}
	
	public void rotRight(){
		ArrayList<ArrayList<Tile>> tempMap = shape;
		reflectY(tempMap);
		reflectXY(tempMap);
	}
	
	public void rotLeft(){
		ArrayList<ArrayList<Tile>> tempMap = shape;
		reflectXY(tempMap);
		reflectY(tempMap);
	}

	private void reflectXY(ArrayList<ArrayList<Tile>> tempMap) {
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				shape.get(i).set(j, tempMap.get(j).get(i));
			}
		}
		
	}

	private void reflectY(ArrayList<ArrayList<Tile>> tempMap) {
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				shape.get(i).set(4 - j, tempMap.get(i).get(j));
			}
		}
	}
	
	public boolean isFalling(){
		return falling;
	}
	
	public void stoppedFalling(){
		falling = false;
		//set each tile of the block to active = false
	}

}
