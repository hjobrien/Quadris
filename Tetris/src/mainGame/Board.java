package mainGame;

import java.util.ArrayList;

import tetrominoes.Block;
import tetrominoes.Tile;

public class Board {
	private ArrayList<ArrayList<Tile>> boardState;
	private Block fallingBlock;
	public boolean blockAdded = false;
	
	//would indicate the game is over
	boolean full = false;
	
	//for debugging
	private boolean debug = true;
	
	public Board(int height, int width){
		ArrayList<ArrayList<Tile>> tempBoard = new ArrayList<ArrayList<Tile>>();
		for(int i = 0; i < height; i++){
			ArrayList<Tile> temp = new ArrayList<Tile>();
			for(int j = 0; j < width; j++){
				temp.add(new Tile(false, false));			//not active, not filled
			}
			tempBoard.add(temp);
		}
		this.boardState = tempBoard;
	}
	
	
	//adds a block near the top of the screen
	//might need to be altered for when the stack gets very high
	public void updateBoardWithNewBlock(Block b){
		ArrayList<ArrayList<Tile>> blockShape = b.getShape();
		for (int i = 0; i < blockShape.size(); i++){
			for (int j = 0; j < blockShape.get(i).size(); j++){
				if (!tileAt(i, j+3).isFilled()){
					update(i, j+3, blockShape.get(i).get(j));
				}
			}
		}
	}
	
	//changes the fields of the tiles in Board based on the tile passed in
	public void update(int i, int j, Tile t){
		this.boardState.get(i).get(j).setActive(t.isActive());
		this.boardState.get(i).get(j).setFilled(t.isFilled());
	}
	
	public Tile tileAt(int i, int j){
		return this.boardState.get(i).get(j);
	}
	
	public ArrayList<ArrayList<Tile>> getBoardState(){
		return this.boardState;
	}
	
//	public void display(){
//		for (int i = 0; i < this.boardState.size(); i++){
//			for (int j = 0; j < this.boardState.get(i).size(); j++){
//				System.out.print(this.boardState.get(i).get(j).isFilled());
//			}
//			System.out.println();
//		}
//	}
	
	public Block getFallingBlock(){
		return fallingBlock;
	}

	public void setFallingBlock(Block fallingBlock) {
		this.fallingBlock = fallingBlock;
	}

	public boolean checkBlockSpace() {
		//checks if the board has filled up
		for (int i = 0; i < boardState.get(0).size(); i++){
			Tile firstTile = tileAt(0, i);
			if (firstTile.isFilled() && !firstTile.isActive()){
				full = true;
			}
		}
		
		//checks if the block is at the bottom of the screen
		int lastIndex = boardState.size() - 1;
		for (int i = 0; i < boardState.get(lastIndex).size(); i++){
			if (tileAt(lastIndex, i).isActive()){
				if (debug){
					System.out.println("block has space beneath = false");
				}
				return false;
			}
		}
		
		//checks if there is an inactive tile (block that already fell) below
		for (int i = 0; i < boardState.size(); i++){
			for (int j = 0; j < boardState.get(i).size(); j++){
				Tile thisT = boardState.get(i).get(j);
				if (thisT.isActive()){
					Tile nextT = boardState.get(i + 1).get(j);
					if (nextT.isFilled() && !nextT.isActive()){
						if (debug){
							System.out.println("block has space beneath = false");
						}
						return false;
					}
				}
			}
		}
		if (debug){
			System.out.println("block has space beneath = true");
		}
		return true;
	}

	//moves all the blocks down 1 row
	public void blockDown() {
		for (int i = boardState.size() - 1; i >= 0; i--){
			for (int j = boardState.get(i).size() - 1; j >= 0; j--){
				if (tileAt(i, j).isActive()){
					//updates new tile
					update(i + 1, j, tileAt(i ,j));
					//clears old tile
					update(i, j, new Tile(false, false));
				}
			}
		}
	}

	//goes over the whole grid and sets each tile to not falling
	public void setNotFalling() {
		for (ArrayList<Tile> list : boardState){
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
		} else if (string.equals("up")){
			tryToRotate("right");
//			fallingBlock.rotRight();
		} else if (string.equals("down")){
			tryToRotate("left");
//			fallingBlock.rotLeft();
		}
		
	}

	//TODO
	/* currently condenses the board to a block
	 * I would like to have some method that tries to make that block a square
	 * if it can't, nothing will happen because that means blocks are in the way.
	 * if it can, it will resize to a square, then perform the translation, then tell 
	 * the board it's new shape
	 */
	
	private void tryToRotate(String string) {
		ArrayList<ArrayList<Tile>> block = makeBoard();
		ArrayList<Integer> columnsErased = new ArrayList<Integer>();
		ArrayList<Integer> rowsErased = new ArrayList<Integer>();
		
		//gets rid of any rows that don't have active tiles
		for (int row = block.size() - 1; row >= 0; row--){
			boolean rowCleared = true;
			int column = 0;
			while (rowCleared && column < block.get(row).size()){
				if(block.get(row).get(column).isActive()){
					rowCleared = false;
				}
				column++;
			}
			if (rowCleared){
				block.remove(row);
				rowsErased.add(row);
			}
		}
		
		//gets rid of any columns that don't have active tiles
		for (int column = block.get(0).size() - 1; column >= 0; column --){
			boolean columnCleared = true;
			int row = 0;
			while (columnCleared && row < block.size()){
				if (block.get(row).get(column).isActive()){
					columnCleared = false;
				}
				row++;
			}
			if (columnCleared){
				for (int i = 0; i < block.size(); i++){
					block.get(i).remove(column);
				}
				columnsErased.add(column);
			}
		}
		
		//returns blockSquare or empty double arrayList if it can't make a square
		Block b = new Block(block);
		if (block.size() != block.get(0).size()){
			b = makeBlockSquare(block, rowsErased, columnsErased);
		}
		
		//prints old and new shape or that it can't rotate
		if (!b.getShape().isEmpty()){
			if (debug){
				for (int i = 0; i < b.getShape().size(); i++){
					for (int j = 0; j < b.getShape().get(i).size(); j++){
						System.out.print(b.getShape().get(i).get(j).isFilled());
					}
					System.out.println();
				}
			}
			
			if (string.equals("right")){
				b = rotRight(b);
			} else if (string.equals("left")){
				b = rotLeft(b);
			}
			
			if (debug){
				System.out.println();
				for (int i = 0; i < b.getShape().size(); i++){
					for (int j = 0; j < b.getShape().get(i).size(); j++){
						System.out.print(b.getShape().get(i).get(j).isFilled());
					}
					System.out.println();
				}
			}
			
		} else {
			System.out.println("Can't rotate");
		}
	}
	
	private Block makeBlockSquare(ArrayList<ArrayList<Tile>> block, ArrayList<Integer> rowsErased,
			ArrayList<Integer> columnsErased) {
		while (block.get(0).size() < block.size()){
			if (checkSpaceOnRight(block, rowsErased, columnsErased)){
				for (int i = 0; i < block.size(); i++){
					block.get(i).add(new Tile(false, false));
				}
			} else {
				if (checkSpaceOnLeft(block, rowsErased, columnsErased)){
					for (int i = 0; i < block.size(); i++){
						block.get(i).add(0, new Tile(false, false));
					}
				} else {
					return new Block(new ArrayList<ArrayList<Tile>>());
				}
			}
		}
		while (block.get(0).size() > block.size()){
			if (checkSpaceUnder(block, rowsErased, columnsErased)){
				ArrayList<Tile> row = new ArrayList<Tile>();
				for (int i = 0; i < block.get(0).size(); i++){
					row.add(new Tile(false, false));
				}
				block.add(row);
			} else {
				if (checkSpaceAbove(block, rowsErased, columnsErased)){
					ArrayList<Tile> row = new ArrayList<Tile>();
					for (int i = 0; i < block.get(0).size(); i++){
						row.add(new Tile(false, false));
					}
					block.add(0, row);
				} else {
					return new Block(new ArrayList<ArrayList<Tile>>());
				}
			}
		}
		return new Block(block);
	}
	
	private boolean checkSpaceAbove(ArrayList<ArrayList<Tile>> block, ArrayList<Integer> rowsErased,
			ArrayList<Integer> columnsErased) {
		if (!rowsErased.contains(0)){
			return false;
		}
		boolean spaceAbove = true;
		int rowLength = 0;
		while (spaceAbove && rowLength < block.get(0).size()){
			int column = blockColumn(columnsErased, rowLength);
			int row = aboveRow(rowsErased);
			if (column == -1 || row == -1 || boardState.get(row).get(column).isFilled()){
				return false;
			}
		}
		return spaceAbove;
	}

	private boolean checkSpaceUnder(ArrayList<ArrayList<Tile>> block, ArrayList<Integer> rowsErased,
			ArrayList<Integer> columnsErased) {
		if (!rowsErased.contains(boardState.size() - 1)){
			return false;
		}
		boolean spaceUnder = true;
		int rowLength = 0;
		while (spaceUnder && rowLength < block.get(0).size()){
			int column = blockColumn(columnsErased, rowLength);
			int row = underRow(rowsErased);
			if (column == -1 || row == -1 || boardState.get(row).get(column).isFilled()){
				return false;
			}
			rowLength++;
		}
		return spaceUnder;
	}

	private boolean checkSpaceOnRight(ArrayList<ArrayList<Tile>> block, ArrayList<Integer> rowsErased,
			ArrayList<Integer> columnsErased){
		if (!columnsErased.contains(boardState.get(0).size() - 1)){
			return false;
		}
		boolean spaceOnRight = true;
		int columnHeight = 0;
		while (spaceOnRight && columnHeight < block.size()){
			int row = blockRow(rowsErased, columnHeight);
			int column = rightColumn(columnsErased);
			if (column == -1 || row == -1 || boardState.get(row).get(column).isFilled()){
				return false;
			}
			columnHeight++;
		}
		return spaceOnRight;
	}
	
	private boolean checkSpaceOnLeft(ArrayList<ArrayList<Tile>> block, ArrayList<Integer> rowsErased,
			ArrayList<Integer> columnsErased){
		if (!columnsErased.contains(0)){
			return false;
		}
		boolean spaceOnLeft = true;
		int columnHeight = 0;
		while (spaceOnLeft && columnHeight < block.size()){
			int row = blockRow(rowsErased, columnHeight);
			int column = leftColumn(columnsErased);
			if (column == -1 || row == -1 || boardState.get(row).get(column).isFilled()){
				return false;
			}
			columnHeight++;
		}
		return spaceOnLeft;
	}

	private int blockColumn(ArrayList<Integer> columnsErased, int rowLength) {
		int index = 0;
		for (int i = boardState.get(0).size() - 1; i >= 0; i--){
			if (index > columnsErased.size() - 1|| columnsErased.get(index) != i){
				return i - rowLength;
			}
			index++;
		}
		return -1;
	}

	private int blockRow(ArrayList<Integer> rowsErased, int columnHeight) {
		int index = 0;
		for (int i = boardState.size() - 1; i >= 0; i--){
			if (rowsErased.get(index) != i){
				return i - columnHeight;
			}
			index++;
		}
		return -1;
	}
	
	private int aboveRow(ArrayList<Integer> rowsErased) {
		int index = rowsErased.size() - 1;
		for (int i = 0; i < boardState.size(); i++){
			if (index < 0){
				index = 0;
			}
			if (rowsErased.get(index) != i){
				return i - 1;
			}
			index--;
		}
		return -1;
	}
	
	private int underRow(ArrayList<Integer> rowsErased) {
		int index = 0;
		for (int i = boardState.size() - 1; i >= 0; i--){
			if (index > rowsErased.size() - 1 || rowsErased.get(index) != i){
				return i + 1;
			}
		}
		return -1;
	}

	private int rightColumn(ArrayList<Integer> columnsErased) {
		int index = 0;
		for (int i = boardState.get(0).size() - 1; i >= 0; i--){
			if (index > columnsErased.size() - 1 || columnsErased.get(index) != i){
				return i + 1;
			}
			index++;
		}
		return -1;
	}
	
	private int leftColumn(ArrayList<Integer> columnsErased){
		int index = columnsErased.size() - 1;
		for (int i = 0; i < boardState.size(); i++){
			if (index < 0){
				index = 0;
			}
			if (columnsErased.get(index) != i){
				return i - 1;
			}
			index--;
		}
		return -1;
	}


	public Block rotRight(Block b){
		ArrayList<ArrayList<Tile>> tempMap = b.getShape();
		b = reflectY(b, tempMap);
		b = reflectXY(b, tempMap);
		return b;
	}
	
	public Block rotLeft(Block b){
		ArrayList<ArrayList<Tile>> tempMap = b.getShape();
		b = reflectXY(b, tempMap);
		b = reflectY(b, tempMap);
		return b;
	}

	private Block reflectXY(Block b, ArrayList<ArrayList<Tile>> tempMap) {
		for(int i = 0; i < tempMap.size(); i++){
			for(int j = 0; j < tempMap.get(i).size(); j++){
				b.getShape().get(i).set(j, tempMap.get(j).get(i));
			}
		}
		return b;
	}

	private Block reflectY(Block b, ArrayList<ArrayList<Tile>> tempMap) {
		for(int i = 0; i < tempMap.size(); i++){
			for(int j = 0; j < tempMap.get(i).size(); j++){
				b.getShape().get(i).set(tempMap.get(i).size() - 1 - j, tempMap.get(i).get(j));
			}
		}
		return b;
	}
	
	private ArrayList<ArrayList<Tile>> makeBoard(){
		ArrayList<ArrayList<Tile>> newBoard = new ArrayList<ArrayList<Tile>>();
		for (ArrayList<Tile> currentRow : boardState){
			ArrayList<Tile> newRow = new ArrayList<Tile>();
			for (Tile t : currentRow){
				newRow.add(t);
			}
			newBoard.add(newRow);

		}
		return newBoard;
		
	}

	private boolean checkRight() {
		for (ArrayList<Tile> a : boardState){
			
			//checks the far right column
			if (a.get(a.size() - 1).isActive()){
				return false;
			}
			
			//checks for an inactive but filled tile on the right of every active tile
			for (int i = a.size() - 2; i >= 0; i--){
				if (a.get(i).isActive()){
					Tile tileOnRight = a.get(i + 1);
					if (tileOnRight.isFilled() && !tileOnRight.isActive()){
						return false;
					}
				}
			}
		}
		return true;
	}

	private void moveRight() {
		for (int i = 0; i < boardState.size(); i++){
			for (int j = boardState.get(i).size() - 2; j >= 0; j--){
				Tile currentTile = boardState.get(i).get(j);
				if (currentTile.isActive()){
					update(i, j + 1, currentTile);
					update(i, j, new Tile(false, false));
				}
			}
		}
	}
	
	private boolean checkLeft() {
		for (ArrayList<Tile> a : boardState){
			
			//checks the far left column
			if (a.get(0).isActive()){
				return false;
			}
			
			//checks for an inactive but filled tile on the left of every active tile
			for (int i = 1; i < a.size(); i++){
				if (a.get(i).isActive()){
					Tile tileOnLeft = a.get(i - 1);
					if (tileOnLeft.isFilled() && !tileOnLeft.isActive()){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private void moveLeft() {
		for (int i = 0; i < boardState.size(); i++){
			for (int j = 1; j < boardState.get(i).size(); j++){
				Tile currentTile = boardState.get(i).get(j);
				if (currentTile.isActive()){
					update(i, j - 1, currentTile);
					update(i, j, new Tile(false, false));
				}
			}
		}
	}
}
