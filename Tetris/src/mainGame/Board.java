package mainGame;

import tetrominoes.Block;

public class Board {

	private boolean[][] board;
	private Block fallingBlock;
	public boolean blockAdded = false;
	
	//for debugging
	private boolean debug = true;
	
	public boolean[][] getBoardState(){
		return this.board;
	}
	
	public Board(int height, int width){
		this.board = new boolean[height][width];
	}
	
	public void updateBoardWithNewBlock(Block b){
		boolean[][] blockShape = b.getShape();
		for (int i = 0; i < blockShape.length; i++){
			for (int j = 0; j < blockShape[i].length; j++){
				if (valueOf(i, j+3) != true){
					update(i, j+3, blockShape[i][j]);
				}
			}
		}
	}
	
	public void update(int i, int j, boolean b){
		this.board[i][j] = b;
	}
	
	public boolean valueOf(int i, int j){
		return this.board[i][j];
	}
	
	public void display(){
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board[i].length; j++){
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}
	
//	public void add(Block b){
//		blockAdded = true;
//		setFallingBlock(b);
//	}
	
	//could have some checker that makes sure the falling block is still falling
	//and if it isn't, a new one can be generated
	public Block getFallingBlock(){
		return fallingBlock;
	}

	public void setFallingBlock(Block fallingBlock) {
		this.fallingBlock = fallingBlock;
	}

	public boolean checkBlockSpace() {
		boolean[][] shape = fallingBlock.getShape();
		for (int i = 0; i < shape.length; i++){
			boolean blockInColumn = false;
			int row = shape.length - 1;
			while(!blockInColumn && row >= 0){
				if (shape[i][row] == true){
					blockInColumn = true;
					if (valueOf(i, row+1) == true){
						if (debug){
							System.out.println("block has space beneath = false");
						}
						return false;
					}
				}
				row--;
			}
		}
		if (debug){
			System.out.println("block has space beneath = true");
		}
		return true;
	}

	//still in development
	public void blockDown() {
		boolean[][] shape = fallingBlock.getShape();
		for (int i = shape.length - 1; i >= 0; i--){
			for (int j = shape[i].length; j >= 0; j--){
//				if (shape[i][j] == true){
//					shape[i][j] = false;
//					
//				}
				//implement
			}
		}
	}
}
