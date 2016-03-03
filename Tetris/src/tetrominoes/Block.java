package tetrominoes;

public class Block {
	Tile[][] shape = new Tile[][]{};
	
	public boolean debug = false;
	
	
//	private boolean[][] shape = new boolean[4][4];
	private boolean falling = true;
	
	//still in development
//	private int[][] locationInGrid;
	
	public Block(Tile[][] size){
		this.shape = size;
	}
	
//	public Block(Block b){
//		Tile[][] bShape = b.getShape();
//		Tile[][] size = new Tile[bShape.length][bShape[0].length];
//		for (int i = 0; i < bShape.length; i++){
//			for (int j = 0; j < bShape[i].length; j++){
//				size 
//			}
//		}
//		this.shape = b.getShape();
//	}
	
	public Tile[][] getShape(){
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

}
