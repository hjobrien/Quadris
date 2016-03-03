package tetrominoes;

import java.util.ArrayList;

public class Block {
	private ArrayList<ArrayList<Tile>> shape = new ArrayList<ArrayList<Tile>>();
	
	public boolean debug = true;
	
	
//	private boolean[][] shape = new boolean[4][4];
	private boolean falling = true;
	
	//still in development
	private int[][] locationInGrid;
	
	public Block(ArrayList<ArrayList<Tile>> size){
		this.shape = size;
	}
	
	public Block(Block b){
		this.shape = new ArrayList<ArrayList<Tile>>(b.getShape());
	}
	
	public ArrayList<ArrayList<Tile>> getShape(){
		return shape;
	}
	
//	public void rotRight(){
//		ArrayList<ArrayList<Tile>> tempMap = shape;
//		reflectY(tempMap);
//		reflectXY(tempMap);
//	}
//	
//	public void rotLeft(){
//		ArrayList<ArrayList<Tile>> tempMap = shape;
//		reflectXY(tempMap);
//		reflectY(tempMap);
//	}
//
//	private void reflectXY(ArrayList<ArrayList<Tile>> tempMap) {
//		for(int i = 0; i < tempMap.size(); i++){
//			for(int j = 0; j < tempMap.get(0).size(); j++){
//				shape.get(i).set(j, tempMap.get(j).get(i));
//			}
//		}
//		
//	}
//
//	private void reflectY(ArrayList<ArrayList<Tile>> tempMap) {
//		for(int i = 0; i < tempMap.size(); i++){
//			for(int j = 0; j < tempMap.get(0).size(); j++){
//				shape.get(i).set(j, tempMap.get(j).get(i));
//			}
//		}
//	}
	
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
		for(ArrayList<Tile> row : shape){
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

	public void setShape(ArrayList<ArrayList<Tile>> emptyShape) {
		this.shape = emptyShape;
		
	}

}
