package mainGame;

import java.util.ArrayList;

import javafx.scene.layout.GridPane;
import tetrominoes.Block;
import tetrominoes.Tile;

public class Board {
	private Tile[][] boardState;
	private Block fallingBlock;
	private GridPane grid;
	public boolean blockAdded = false;
	
	//would indicate the game is over
	boolean full = false;
	
	//for debugging
	private boolean debug = false;
//	private boolean debug = true;
	
	int boardScore = 0;
	
	public Board(int height, int width, GridPane grid){
		Tile[][] tempBoard = new Tile[height][width];
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				tempBoard[i][j] = new Tile(false, false);			//not active, not filled
			}
		}
		this.boardState = tempBoard;
		this.grid = grid;
	}
	
	public int getBoardScore(){
		return boardScore;
	}
	
	//adds a block near the top of the screen
	//might need to be altered for when the stack gets very high
	public void updateBoardWithNewBlock(Block b){
		int offset = (this.boardState[0].length - b.getShape()[0].length) / 2;
		Tile[][] blockShape = b.getShape();
		for (int i = 0; i < blockShape.length; i++){
			for (int j = 0; j < blockShape[i].length; j++){
				if (!tileAt(i, j+offset).isFilled()){
					update(i, j+offset, blockShape[i][j]);
				}
			}
		}
	}
	
	public void updateBoardWithOldBlock(Block b, int row, int column){
		Tile[][] blockShape = b.getShape();
		for (int i = 0; i < blockShape.length; i++){
			for (int j = 0; j < blockShape[i].length; j++){
				if (!tileAt(i + row, j + column).isFilled()){
					update(i + row, j + column, blockShape[i][j]);
				}
			}
		}
	}
	
	//changes the fields of the tiles in Board based on the tile passed in
	public void update(int i, int j, Tile t){
		this.boardState[i][j].setActive(t.isActive());
		this.boardState[i][j].setFilled(t.isFilled());
		this.boardState[i][j].setColor(t.getColor());
	}
	
	public Tile tileAt(int i, int j){
		return this.boardState[i][j];
	}
	
	public boolean isEmpty(){
		for (Tile[] row : boardState){
			for (Tile t : row){
				if (t.isFilled()){
					return false;
				}
			}
		}
		return true;
	}
	
	public Tile[][] getBoardState(){
		return this.boardState;
	}
	
	public Block getFallingBlock(){
		return fallingBlock;
	}

	public void setFallingBlock(Block fallingBlock) {
		this.fallingBlock = fallingBlock;
	}
	
	//TODO move related things
	
	public void pressed(Move m) {
		if (m == Move.RIGHT){
			if (checkRight()){
				moveRight();
			}
		} else if (m == Move.LEFT){
			if (checkLeft()){
				moveLeft();
			}
		} else if (m == Move.ROT_RIGHT){
			if (checkRotate(Move.ROT_RIGHT)){
				fallingBlock.rotateRight();
				updateFallingBlock();
			}
		} else if (m == Move.ROT_LEFT){
			if (checkRotate(Move.ROT_LEFT)){
				fallingBlock.rotateLeft();
				updateFallingBlock();
			}
 		} else if (m == Move.DOWN){
 			if (checkDown()){
 	 			boardScore += 2;
 				blockDown();
 			}
 		} else if (m == Move.UP){
 			blockUp();
 		}
		
	}

	public boolean checkDown() {
		if (debug){
			System.out.println(fallingBlock.getGridLocation()[0] + " " + fallingBlock.getGridLocation()[1]);
		}
		
		//checks if the board has filled up
		for (int i = 0; i < boardState[0].length; i++){
			Tile firstTile = tileAt(0, i);
			if (firstTile.isFilled() && !firstTile.isActive()){
				full = true;
			}
		}
		
		//checks if the block is at the bottom of the screen
		int lastIndex = boardState.length - 1;
		for (int i = 0; i < boardState[0].length; i++){
			if (tileAt(lastIndex, i).isActive()){
//				if (debug){
//					System.out.println("block has space beneath = false");
//				}
				return false;
			}
		}
		
		//checks if there is an inactive tile (block that already fell) below
		for (int i = 0; i < boardState.length; i++){
			for (int j = 0; j < boardState[i].length; j++){
				Tile thisT = boardState[i][j];
				if (thisT.isActive()){
					Tile nextT = boardState[i + 1][j];
					if (nextT.isFilled() && !nextT.isActive()){
//						if (debug){
//							System.out.println("block has space beneath = false");
//						}
						return false;
					}
				}
			}
		}
		if (debug){
//			System.out.println("block has space beneath = true");
		}
		return true;
	}

	//moves all the blocks down 1 row
	public void blockDown() {
		for (int i = boardState.length - 1; i >= 0; i--){
			for (int j = boardState[i].length - 1; j >= 0; j--){
				if (tileAt(i, j).isActive()){
					//s new tile
					update(i + 1, j, tileAt(i ,j));
					//clears old tile
					update(i, j, new Tile(false, false));
				}
			}
		}
		fallingBlock.moveDown();
	}
	
	//only used in debugging
	public void blockUp(){ 
		for (int i = 0; i < boardState.length; i++){
			for (int j = 0; j < boardState[i].length; j++){
				if (tileAt(i, j).isActive()){
					update(i - 1, j, tileAt(i, j));
					update(i, j, new Tile(false, false));
				}
			}
		}
		fallingBlock.moveUp();
	}
	
	private boolean checkRight() {
		for (Tile[] a : boardState){
			
			//checks the far right column
			if (a[a.length - 1].isActive()){
				return false;
			}
			
			//checks for an inactive but filled tile on the right of every active tile
			for (int i = a.length - 2; i >= 0; i--){
				if (a[i].isActive()){
					Tile tileOnRight = a[i + 1];
					if (tileOnRight.isFilled() && !tileOnRight.isActive()){
						return false;
					}
				}
			}
		}
		return true;
	}

	private void moveRight() {
		for (int i = 0; i < boardState.length; i++){
			for (int j = boardState[i].length - 2; j >= 0; j--){
				Tile currentTile = boardState[i][j];
				if (currentTile.isActive()){
					update(i, j + 1, currentTile);
					update(i, j, new Tile(false, false));
				}
			}
		}
		fallingBlock.moveRight();
	}
	
	private boolean checkLeft() {
		for (Tile[] a : boardState){
			
			//checks the far left column
			if (a[0].isActive()){
				return false;
			}
			
			//checks for an inactive but filled tile on the left of every active tile
			for (int i = 1; i < a.length; i++){
				if (a[i].isActive()){
					Tile tileOnLeft = a[i -1];
					if (tileOnLeft.isFilled() && !tileOnLeft.isActive()){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private void moveLeft() {
		for (int i = 0; i < boardState.length; i++){
			for (int j = 1; j < boardState[i].length; j++){
				Tile currentTile = boardState[i][j];
				if (currentTile.isActive()){
					update(i, j - 1, currentTile);
					update(i, j, new Tile(false, false));
				}
			}
		}
		fallingBlock.moveLeft();
	}
	
	private boolean checkRotate(Move m) {
		Block tempB = new Block(fallingBlock);
		if (m == Move.ROT_LEFT){
			tempB.rotateLeft();
		} else if (m == Move.ROT_RIGHT){
			tempB.rotateRight();
		}
		Tile[][] tempBShape = tempB.getShape();
		for (int i = tempBShape.length - 1; i >= 0; i--){
			for (int j = tempBShape[i].length - 1; j >= 0; j--){
				if (tempBShape[i][j].isActive()){
					int[] tempBLocation = tempB.getGridLocation();
					
					//makes sure we arent running off the board
					int row = tempBLocation[0] - (tempBShape.length - 1 - i);
					int column = tempBLocation[1] - (tempBShape[i].length - 1 - j);
					if (row >= 0 &&  column >= 0){
						Tile t = tileAt(row, column);
						
						//checks to make sure flip is legal, ignoring itself in the process
						if (t.isFilled() && !t.isActive()){
							if (debug){
								System.out.println("cant turn " + m);
							}
							return false;
						}
					} else {
						if (debug){
							System.out.println("cant turn " + m);
						}
						return false;
					}
				}
			}
		}
		if (debug){
			System.out.println("can turn " + m);
		}
		return true;
	}

	private void updateFallingBlock() {
		removeFallingBlock();
		Tile[][] fallingBlockShape = fallingBlock.getShape();
		int[] fallingBlockLocation = fallingBlock.getGridLocation();
		
		for (int i = fallingBlockShape.length - 1; i >= 0; i--){
			for (int j = fallingBlockShape[i].length - 1; j >= 0; j--){
				//makes sure we arent running off the board
				int row = fallingBlockLocation[0] - (fallingBlockShape.length - 1 - i);
				int column = fallingBlockLocation[1] - (fallingBlockShape[0].length - 1 - j);
				update(row, column, fallingBlockShape[i][j]);
			}
		}
	}
	
	private void removeFallingBlock() {
		for(int i = 0; i < boardState.length; i++){
			for(int j = 0; j < boardState[0].length; j++){
				if(boardState[i][j].isActive()){
					boardState[i][j] = new Tile(false, false);
				}
			}
		}
		
	}
	
	
	//TODO clearing lines
	
	//if full, the game is over
	public boolean isFull(){
		return this.full;
	}

	//goes over the whole grid and sets each tile to not falling
	public void setNotFalling() {
		for (Tile[] list : boardState){
			for (Tile t : list){
				t.setActive(false);
			}
		}
	}
	
	public void clearBoard(){
		for(int i = 0; i < boardState.length; i++){
			for(int j = 0; j < boardState[i].length; j++){
				update(i,j, new Tile(false,false));
			}
		}
		this.boardScore = 0;
	}
	
	public GridPane getGrid(){
		return grid;
	}
	
	public ArrayList<Integer> getFullRows() {
		ArrayList<Integer> fullRows = new ArrayList<Integer>();
		for (int i = 0; i < boardState.length; i++){
			boolean full = true;
			int index = 0;
			while (full && index < boardState[i].length){
				if (!boardState[i][index].isFilled()){
					full = false;
				}
				index++;
			}
			if (full){
				fullRows.add(i);
			}
		}
		return fullRows;
	}


	public void clearLines(ArrayList<Integer> linesToClear) {
		setNotFalling();
		linesToClear.sort(null);
		for (int i = 0; i < linesToClear.get(0); i++){
			for (Tile t : boardState[i]){
				if (t.isFilled()){
					t.setActive(true);
				}
			}
		}
		for (int i = 0; i < linesToClear.size(); i++){
			for (int j = 0; j < boardState[i].length; j++){
				update(linesToClear.get(0), j, new Tile(false, false));
			}
			boardScore += 100;
		}
		if (isEmpty()){
			fallingBlock.stoppedFalling();
		}
	}

}
