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
	private boolean debug = true;
	
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

	public boolean checkDown() {
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
					//updates new tile
					update(i + 1, j, tileAt(i ,j));
					//clears old tile
					update(i, j, new Tile(false, false));
				}
			}
		}
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
	}

	//goes over the whole grid and sets each tile to not falling
	public void setNotFalling() {
		for (Tile[] list : boardState){
			for (Tile t : list){
				t.setActive(false);
			}
		}
	}
	
	//if full, the game is over
	public boolean isFull(){
		return this.full;
	}

	public void pressed(String string) {
		if (string.equals("right")){
			if (checkRight()){
				moveRight();
			}
		} else if (string.equals("left")){
			if (checkLeft()){
				moveLeft();
			}
//		} else if (string.equals("x")){
//			tryToRotate("right");
//		} else if (string.equals("z")){
//			tryToRotate("left");
 		} else if (string.equals("down")){
 			boardScore += 2;
 			if (checkDown()){
 				blockDown();
 			}
 		} else if (string.equals("up")){
 			blockUp();
 		}
		
	}

	//TODO rotations
	/* currently condenses the board to a block
	 * I would like to have some method that tries to make that block a square
	 * if it can't, nothing will happen because that means blocks are in the way.
	 * if it can, it will resize to a square, then perform the translation, then tell 
	 * the board it's new shape
	 */
	
//	private void tryToRotate(String string) {
//		Tile[][] block = makeBoard();
//		ArrayList<Integer> columnsErased = new ArrayList<Integer>();
//		ArrayList<Integer> rowsErased = new ArrayList<Integer>();
//		
//		//gets rid of any rows that don't have active tiles
//		for (int row = block.length - 1; row >= 0; row--){
//			boolean rowCleared = true;
//			int column = 0;
//			while (rowCleared && column < block[row].length){
//				if(block[row][column].isActive()){
//					rowCleared = false;
//				}
//				column++;
//			}
//			if (rowCleared){
//				block.remove(row);
//				rowsErased.add(row);
//			}
//		}
//		
//		//gets rid of any columns that don't have active tiles
//		for (int column = block[0].length - 1; column >= 0; column --){
//			boolean columnCleared = true;
//			int row = 0;
//			while (columnCleared && row < block.length){
//				if (block[row][column].isActive()){
//					columnCleared = false;
//				}
//				row++;
//			}
//			if (columnCleared){
//				for (int i = 0; i < block.length; i++){
//					block[i].remove(column);
//				}
//				columnsErased.add(column);
//			}
//		}
//		
//		//returns blockSquare or empty double arrayList if it can't make a square
//		Block b = new Block(block);
//		if (block.length != block[0].length){
//			b = makeBlockSquare(block, rowsErased, columnsErased);
//		}
//		
//		//prints old and new shape or that it can't rotate
//		if (!b.getShape().equals(new Tile[b.getShape().length][b.getShape()[0].length])){
//			if (debug){
//				for (int i = 0; i < b.getShape().length; i++){
//					for (int j = 0; j < b.getShape()[i].length; j++){
//						System.out.print(b.getShape()[i][j].isFilled());
//					}
//					System.out.println();
//				}
//			}
//			
//			if (string.equals("right")){
//				b = rotRight(b);
//			} else if (string.equals("left")){
//				b = rotLeft(b);
//			}
//			
//			if (debug){
//				System.out.println();
//				for (int i = 0; i < b.getShape().length; i++){
//					for (int j = 0; j < b.getShape()[i].length; j++){
//						System.out.print(b.getShape()[i][j].isFilled());
//					}
//					System.out.println();
//				}
//			}
//			removeFallingBlock();
//			//sketchy numbers
//			updateBoardWithOldBlock(b, blockRow(rowsErased, 0) - 1, blockColumn(columnsErased, 0) - 2);
//			
//		} else {
//			System.out.println("Can't rotate");
//		}
//	}
	
	private void removeFallingBlock() {
		for(int i = 0; i < boardState.length; i++){
			for(int j = 0; j < boardState[0].length; j++){
				if(boardState[i][j].isActive()){
					boardState[i][j] = new Tile(false, false);
				}
			}
		}
		
	}
	
	//TODO making the block a square

//	private Block makeBlockSquare(Tile[][] block, ArrayList<Integer> rowsErased,
//			ArrayList<Integer> columnsErased) {
//		while (block[0].length < block.length){
//			if (checkSpaceOnRight(block, rowsErased, columnsErased)){
//				for (int i = 0; i < block.length; i++){
//					block[i].add(new Tile(false, false));
//				}
//			} else {
//				if (checkSpaceOnLeft(block, rowsErased, columnsErased)){
//					for (int i = 0; i < block.length; i++){
//						block[i].add(0, new Tile(false, false));
//					}
//				} else {
//					return new Block(new Tile[][]);
//				}
//			}
//		}
//		while (block[0].length > block.length){
//			if (checkSpaceUnder(block, rowsErased, columnsErased)){
//				Tile[] row = new Tile[];
//				for (int i = 0; i < block[0].length; i++){
//					row.add(new Tile(false, false));
//				}
//				block.add(row);
//			} else {
//				if (checkSpaceAbove(block, rowsErased, columnsErased)){
//					Tile[] row = new Tile[]();
//					for (int i = 0; i < block[0].length; i++){
//						row.add(new Tile(false, false));
//					}
//					block.add(0, row);
//				} else {
//					return new Block(new Tile[][]);
//				}
//			}
//		}
//		return new Block(block);
//	}
//	
//	private boolean checkSpaceAbove(Tile[][] block, ArrayList<Integer> rowsErased,
//			ArrayList<Integer> columnsErased) {
//		if (!rowsErased.contains(0)){
//			return false;
//		}
//		boolean spaceAbove = true;
//		int rowLength = 0;
//		while (spaceAbove && rowLength < block[0].length){
//			int column = blockColumn(columnsErased, rowLength);
//			int row = aboveRow(rowsErased);
//			if (column == -1 || row == -1 || boardState[row][column].isFilled()){
//				return false;
//			}
//		}
//		return spaceAbove;
//	}
//
//	private boolean checkSpaceUnder(Tile[][] block, ArrayList<Integer> rowsErased,
//			ArrayList<Integer> columnsErased) {
//		if (!rowsErased.contains(boardState.length - 1)){
//			return false;
//		}
//		boolean spaceUnder = true;
//		int rowLength = 0;
//		while (spaceUnder && rowLength < block[0].length){
//			int column = blockColumn(columnsErased, rowLength);
//			int row = underRow(rowsErased);
//			if (column == -1 || row == -1 || boardState[row][column].isFilled()){
//				return false;
//			}
//			rowLength++;
//		}
//		return spaceUnder;
//	}
//
//	private boolean checkSpaceOnRight(Tile[][] block, ArrayList<Integer> rowsErased,
//			ArrayList<Integer> columnsErased){
//		if (!columnsErased.contains(boardState[0].length - 1)){
//			return false;
//		}
//		boolean spaceOnRight = true;
//		int columnHeight = 0;
//		while (spaceOnRight && columnHeight < block.length){
//			int row = blockRow(rowsErased, columnHeight);
//			int column = rightColumn(columnsErased);
//			if (column == -1 || row == -1 || boardState[row][column].isFilled()){
//				return false;
//			}
//			columnHeight++;
//		}
//		return spaceOnRight;
//	}
//	
//	private boolean checkSpaceOnLeft(Tile[][] block, ArrayList<Integer> rowsErased,
//			ArrayList<Integer> columnsErased){
//		if (!columnsErased.contains(0)){
//			return false;
//		}
//		boolean spaceOnLeft = true;
//		int columnHeight = 0;
//		while (spaceOnLeft && columnHeight < block.length){
//			int row = blockRow(rowsErased, columnHeight);
//			int column = leftColumn(columnsErased);
//			if (column == -1 || row == -1 || boardState[row][column].isFilled()){
//				return false;
//			}
//			columnHeight++;
//		}
//		return spaceOnLeft;
//	}
//
//	private int blockColumn(ArrayList<Integer> columnsErased, int rowLength) {
//		int index = 0;
//		for (int i = boardState[0].length - 1; i >= 0; i--){
//			if (index > columnsErased.size() - 1|| columnsErased[index] != i){
//				return i - rowLength;
//			}
//			index++;
//		}
//		return -1;
//	}
//
//	private int blockRow(ArrayList<Integer> rowsErased, int columnHeight) {
//		int index = 0;
//		for (int i = boardState.length - 1; i >= 0; i--){
//			if (rowsErased[index] != i){
//				return i - columnHeight;
//			}
//			index++;
//		}
//		return -1;
//	}
//	
//	private int aboveRow(ArrayList<Integer> rowsErased) {
//		int index = rowsErased.size() - 1;
//		for (int i = 0; i < boardState.length; i++){
//			if (index < 0){
//				index = 0;
//			}
//			if (rowsErased[index] != i){
//				return i - 1;
//			}
//			index--;
//		}
//		return -1;
//	}
//	
//	private int underRow(ArrayList<Integer> rowsErased) {
//		int index = 0;
//		for (int i = boardState.length - 1; i >= 0; i--){
//			if (index > rowsErased.size() - 1 || rowsErased[index] != i){
//				return i + 1;
//			}
//		}
//		return -1;
//	}
//
//	private int rightColumn(ArrayList<Integer> columnsErased) {
//		int index = 0;
//		for (int i = boardState[0].length - 1; i >= 0; i--){
//			if (index > columnsErased.size() - 1 || columnsErased[index] != i){
//				return i + 1;
//			}
//			index++;
//		}
//		return -1;
//	}
//	
//	private int leftColumn(ArrayList<Integer> columnsErased){
//		int index = columnsErased.size() - 1;
//		for (int i = 0; i < boardState.length; i++){
//			if (index < 0){
//				index = 0;
//			}
//			if (columnsErased[index] != i){
//				return i - 1;
//			}
//			index--;
//		}
//		return -1;
//	} 
//

	//TODO rotations and reflections
	
//	public Block rotRight(Block b){
//		Block temp = new Block(b);	
//		b = new Block(reflectY(b, temp));
//		b = new Block(reflectXY(b, temp));
//		return b;
//	}
//	
//	public Block rotLeft(Block b){
//		Block temp = new Block(b);
//		b = new Block(reflectXY(b, temp));
//		b = new Block(reflectY(b, temp));
//		return b;
//	}
//
//	private Block reflectXY(Block b, Block temp) {
//		Tile[][] tempMap = temp.getShape();
//		Tile[][] emptyShape = new Tile[][]{};
//
//		for(int i = 0; i < tempMap.length; i++){
//			Tile[] e = new Tile[]();
//			for(int j = 0; j < tempMap[0].length; j++){
//				e.add(tempMap[j][i]);
//			}
//			emptyShape.add(e);
//		}
//		b.setShape(emptyShape);
//		return b;
//	}
//
//	private Block reflectY(Block b, Block temp) {
//		Tile[][] tempMap = temp.getShape();
//		for(int i = 0; i < tempMap.length; i++){
//			Collections.reverse(b.getShape()[i]);
//		}
//		return b;
//	}
//	
//	//TODO moving right and left
//
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
	}
//	
//	//TODO makes board replica and clears the board
//	
//	private Tile[][] makeBoard(){
//		Tile[][] newBoard = new Tile[][]{};
//		for (Tile[] currentRow : boardState){
//			Tile[] newRow = new Tile[]();
//			for (Tile t : currentRow){
//				newRow.add(t);
//			}
//			newBoard.add(newRow);
//		}
//		return newBoard;
//		
//	}
	
	public void clearBoard(){
		for(int i = 0; i < boardState.length; i++){
			for(int j = 0; j < boardState[i].length; j++){
				update(i,j, new Tile(false,false));
			}
		}
		
	}
	
	public GridPane getGrid(){
		return grid;
	}
	
	//TODO clearing lines
	
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
