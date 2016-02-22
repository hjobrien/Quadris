package tetrominoes;

public class Block {
	private int[][] shape = new int[4][4];
	
	
	
	public Block(int[][] size){
		this.shape = size;
	}
	
	public void mapNeg90(){
		int[][] tempMap = shape;
		reflectY(tempMap);
		reflectXY(tempMap);
	}
	
	public void mapPos90(){
		int[][] tempMap = shape;
		reflectXY(tempMap);
		reflectY(tempMap);
	}

	private void reflectXY(int[][] tempMap) {
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				shape[i][j] = tempMap[j][i];
			}
		}
		
	}

	private void reflectY(int[][] tempMap) {
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				shape[i][4-j] = tempMap[i][j];
			}
		}
	}
	
	

}
