package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import blocks.Block;
import blocks.LeftL;
import blocks.LeftS;
import blocks.RightL;
import blocks.RightS;
import blocks.Square;
import blocks.StraightLine;
import blocks.TBlock;
import blocks.Tile;
import cerulean.Cerulean;
import mainGame.Move;
import mainGame.ScoreMode;
import util.Util;

public class Engine {

	private Tile[][] gameBoard;
	private Tile[][] nextPieceBoard;
	private Block nextBlock;
	private Block activeBlock;
	private Cerulean cerulean;
	private boolean isPaused = false;
	private boolean autoplay = false;
	private boolean randomizeBlocks;
	private int blockCount = 0;
	private int gameNum = 0;
	private boolean rowsNotFalling = true;
	private int score = 0;
	private int numOfFullRows = 0;
	private ScoreMode scoreMode;

	// would indicate the game is over
	boolean full = false;

	private boolean debugMode;

	public static final String BLOCK_DATA = "Blocks to add"; // file name

	// lists of numbers corresponding to blocks, translated in the
	// 'genNextBlock' method
	private static int[][] blocks = new int[][] {};

	/**
	 * constructs an engine with a board and tells it if it should automatically
	 * place blocks and if the blocks should be randomly generated
	 * 
	 * @param mainBoard
	 *            the board the engine acts on
	 * @param autoplay
	 *            whether the engine should automatically place the blocks
	 * @param randomizeBlocks
	 *            whether blocks should be randomly generated or read from a
	 *            file
	 */
	public Engine(Tile[][] mainBoard, boolean autoplay, boolean randomizeBlocks, ScoreMode scoring) {
		this.scoreMode = scoring;
		this.autoplay = autoplay;
		this.randomizeBlocks = randomizeBlocks;
		this.gameBoard = deepCopy(mainBoard);
		this.nextPieceBoard = initBoard(new Tile[4][4]);
		if (!randomizeBlocks && blocks.length == 0) {
			blocks = readInBlocks();
			nextBlock = getNextBlock(blockCount);
		} else {
			nextBlock = getRandomBlock();
		}
		if (autoplay) {
			cerulean = new Cerulean();
		}

	}

	/**
	 * copies a board by value
	 * 
	 * @param mainBoard
	 *            the board to be copied
	 * @return a new board with the values of the original
	 */
	private Tile[][] deepCopy(Tile[][] mainBoard) {
		Tile[][] copy = new Tile[mainBoard.length][mainBoard[0].length];
		for (int i = 0; i < mainBoard.length; i++) {
			for (int j = 0; j < mainBoard[i].length; j++) {
				if (mainBoard[i][j] != null) {
					copy[i][j] = new Tile(mainBoard[i][j].isActive(), mainBoard[i][j].isFilled(),
							mainBoard[i][j].getColor());
				} else {
					copy[i][j] = new Tile();
				}
			}
		}
		return copy;
	}

	/**
	 * Initialize a board with empty tiles
	 * 
	 * @param mainBoard
	 *            the board to be initialized
	 * @return a board whose tiles are non-null, specifically all unfilled and
	 *         inactive
	 */
	private Tile[][] initBoard(Tile[][] mainBoard) {
		for (int i = 0; i < mainBoard.length; i++) {
			for (int j = 0; j < mainBoard[i].length; j++) {
				mainBoard[i][j] = new Tile();
			}
		}
		return mainBoard;
	}

	/**
	 * is the general engine method coordinates dropping blocks, adding blocks,
	 * logging game state, and line clearing
	 */
	public void update() {
		// printBoard();
		// System.out.println();
		if (!isPaused) { // little hacky, could be improved
			if (activeBlock.isFalling()) {
				if (checkDown()) {
					blockDown();
				} else {
					// block just landed
					// if (logMode) {
					// Logger.log(gameBoard, activeBlock, activeBlock);
					// }
					ArrayList<Integer> linesToClear = getFullRows();
					if (!linesToClear.isEmpty()) {
						clearLines(linesToClear);
					} else {
						setNotFalling();
						if (getScoreMode() == ScoreMode.NINTENDO){
							score += 10;
						
							//should reward a board clear
							if (getLowestEmptyRow() == gameBoard.length - 1){
								score += 2000;
							}
						} else if (getScoreMode() == ScoreMode.SIMPLE){
							score += 1;
						}
						addBlock();
					}
				}
			} else {
				if (getScoreMode() == ScoreMode.NINTENDO){
					score += 10;
				} else if (getScoreMode() == ScoreMode.SIMPLE){
					score += 1;
				}
				addBlock();
			}
		}
	}

	/**
	 * checks to see if lowering the block by one space is a valid move
	 * 
	 * @return true if the move is valid, false otherwise
	 */
	public boolean checkDown() {

		// if (debugMode) {
		// System.out
		// .println(activeBlock.getGridLocation()[0] + " " +
		// activeBlock.getGridLocation()[1]);
		// }
		// this if is a hacky fix to stop the game from freezing under certain
		// unknown conditions when
		// it should be resetting instead
		if (activeBlock.getGridLocation()[1] < -4) {
			System.err.println("Freeze detected: resetting now...");
			Util.sleep(10);
			// printBoard();
			System.exit(-10);
		}

		if (checkBlockAtBottom() || checkUnderneath()) {
			if (!rowsNotFalling) {
				int lowestEmptyRow = getLowestEmptyRow();
				if (existRowsAbove(lowestEmptyRow)) {
					setBlocksUnderToInactive(lowestEmptyRow);
					return true;
				} else {
					rowsNotFalling = true;
				}
			}
			return false;
		}

		return true;
		/*
		 * if lowestEmptyRow has blocks above it: set blocks under
		 * lowestEmptyLine to inactive keep blocks above lowestEmptyLine active
		 */
	}

	/**
	 * checks if the block is at the bottom of the screen
	 * 
	 * @return true if the block is at the bottom, false otherwise
	 */
	private boolean checkBlockAtBottom() {
		int lastIndex = gameBoard.length - 1;
		for (int i = 0; i < gameBoard[0].length; i++) {
			if (tileAt(lastIndex, i).isActive()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * checks if there is an inactive tile (block that already fell) below
	 * 
	 * @return true if there is an inactive tile, false otherwise
	 */
	private boolean checkUnderneath() {
		// boolean isUnderneath = false;
		for (int i = 0; i < gameBoard.length; i++) {
			for (int j = 0; j < gameBoard[i].length; j++) {
				Tile thisT = gameBoard[i][j];
				if (thisT.isActive()) {
					Tile nextT = gameBoard[i + 1][j];
					if (nextT.isFilled() && !nextT.isActive()) {
						// isUnderneath = true;
						return true;
					}
				}
			}
		}
		return false;
		// return isUnderneath;
	}

	/**
	 * moves all the blocks down 1 row
	 */
	public void blockDown() {
		for (int i = gameBoard.length - 1; i >= 0; i--) {
			for (int j = gameBoard[i].length - 1; j >= 0; j--) {
				if (tileAt(i, j).isActive()) {
					// creates new tile
					updateTileLocation(i + 1, j, tileAt(i, j));
					// clears old tile
					updateTileLocation(i, j, new Tile());
				}
			}
		}
		activeBlock.moveDown(); // adjusts internal coordinates
	}

	/**
	 * updates the board with the Tiles values for color, active, and filled
	 * 
	 * @param i
	 *            the x index to set
	 * @param j
	 *            the y index to set
	 * @param t
	 *            the tile to get the values from
	 */
	public void updateTileLocation(int i, int j, Tile t) {
		this.gameBoard[i][j].setActive(t.isActive());
		this.gameBoard[i][j].setFilled(t.isFilled());
		this.gameBoard[i][j].setColor(t.getColor());
	}

	/**
	 * convenience method for getting a tile at a specified x,y coordinate
	 * 
	 * @param i
	 *            the row index
	 * @param j
	 *            the column index
	 * @return the tile at the specified index
	 */
	public Tile tileAt(int i, int j) {
		return this.gameBoard[i][j];
	}

	/**
	 * gets the lowest row that is totally empty
	 * 
	 * @return the lowest row devoid of any filled tiles
	 */
	private int getLowestEmptyRow() {
		boolean foundEmptyRow = false;
		int row = gameBoard.length - 1;
		while (!foundEmptyRow && row >= 0) {
			boolean rowIsEmpty = true;
			int column = 0;
			while (rowIsEmpty && column < gameBoard[0].length) {
				if (gameBoard[row][column].isFilled()) {
					rowIsEmpty = false;
				} else {
					column++;
				}
			}
			if (rowIsEmpty) {
				foundEmptyRow = true;
			} else {
				row--;
			}
		}
		return row;
	}

	/**
	 * checks to see if any filled tile exists below the parameter
	 * 
	 * @param lowestEmptyRow
	 *            the row below which should be checked
	 * @return true if any block exists below, false otherwise
	 */
	// TODO: should the name be below?
	private boolean existRowsAbove(int lowestEmptyRow) {
		for (int i = lowestEmptyRow; i >= 0; i--) {
			for (int j = 0; j < gameBoard[0].length; j++) {
				if (gameBoard[i][j].isFilled()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * gets the indexes of all the filled rows on the board
	 * 
	 * @return an ArrayList of the filled indexes
	 */
	public ArrayList<Integer> getFullRows() {
		ArrayList<Integer> fullRows = new ArrayList<Integer>();
		for (int i = 0; i < gameBoard.length; i++) {
			boolean full = true;
			int index = 0;
			while (full && index < gameBoard[i].length) {
				if (!gameBoard[i][index].isFilled()) {
					full = false;
				}
				index++;
			}
			if (full) {
				numOfFullRows++;
				fullRows.add(i);
			}
		}
		return fullRows;
	}

	/**
	 * sets all tiles under the parameter to inactive to keep them from moving
	 * 
	 * @param lowestEmptyRow
	 *            the row at which the inactivity should begin
	 */
	private void setBlocksUnderToInactive(int lowestEmptyRow) {
		for (int i = lowestEmptyRow; i < gameBoard.length; i++) {
			for (Tile t : gameBoard[i]) {
				if (t.isFilled()) {
					t.setActive(false);
				}
			}
		}

	}

	/**
	 * removes all the tiles on the lines indicated in the parameter
	 * 
	 * @param linesToClear
	 *            the lines that need to be cleared
	 */
	public void clearLines(ArrayList<Integer> linesToClear) {
		// rewards a "quadris"

		ScoreMode sm = getScoreMode();

		if (linesToClear.size() == 4) {
			if (sm == ScoreMode.NINTENDO) {
				score += 1000;
			} else if (sm == ScoreMode.HANK_LIAM) {
				score += 900;
			} else if (sm == ScoreMode.SIMPLE) {
				score += 100;
			}
		}

		if (linesToClear.size() == 3) {
			if (sm == ScoreMode.NINTENDO) {
				score += 350;
			} else if (sm == ScoreMode.HANK_LIAM) {
				score += 300;
			} else if (sm == ScoreMode.SIMPLE) {
				score += 60;
			}
		}

		if (linesToClear.size() == 2) {
			if (sm == ScoreMode.NINTENDO) {
				score += 150;
			} else if (sm == ScoreMode.HANK_LIAM) {
				score += 200;
			} else if (sm == ScoreMode.SIMPLE) {
				score += 30;
			}
		}

		if (linesToClear.size() == 1) {
			if (sm == ScoreMode.NINTENDO) {
				score += 50;
			} else if (sm == ScoreMode.HANK_LIAM) {
				score += 100;
			} else if (sm == ScoreMode.SIMPLE) {
				score += 10;
			}
		}

		for (int i = 0; i < linesToClear.size(); i++) {
			for (int j = 0; j < gameBoard[i].length; j++) {
				updateTileLocation(linesToClear.get(i), j, new Tile());
			}
		}

		rowsNotFalling = false;
		setNotFalling();
		boolean tileAboveLine = false;
		for (int i = 0; i < linesToClear.get(linesToClear.size() - 1); i++) {
			for (Tile t : gameBoard[i]) {
				if (t.isFilled()) {
					t.setActive(true);
					tileAboveLine = true;
				}
			}
		}

		if (!tileAboveLine) {
			activeBlock.stoppedFalling();
			rowsNotFalling = true;
		}
	}

	/**
	 * 
	 * @return a 2D array of the board, each index references a possible square
	 *         and if it is filled or not
	 */
	public Tile[][] getGameBoard() {
		return gameBoard;
	}

	/**
	 * adds a new random block to the board. Before one is added, a
	 * BlockAddedEvent is fired the new block is taken from the nextBlockBoard
	 * with is shown to the user, introduced to the board, and a new random one
	 * added to the nextBlockBoard
	 */
	public void addBlock() {
		// //needed to access Node.fireEvent, the GridPane was an accessible
		// Node
		// if (autoplay){
		// board.getGrid().fireEvent(new BlockAddedEvent(nextBlock,
		// board.getBoardState()));
		// }
		Move[] solution = null;
		// long now = System.currentTimeMillis();
		if (autoplay) {
			solution = cerulean.submitBlock(nextBlock, gameBoard);
		}
		activeBlock = nextBlock;
		updateBoardWithNewBlock(nextBlock);
		// toggle for step by step block analysis
		// Engine.togglePause();
		// Renderer.pause();
		// Game.togglePause();
		if (autoplay) {
			for (Move m : solution) {
				executeMove(m);
			}
		}
		// time for evaluation and movement of block
		// System.out.println("\t\t" + (System.currentTimeMillis() -now));
		if (randomizeBlocks) {
			nextBlock = getRandomBlock();
		} else {
			nextBlock = getNextBlock(blockCount);
		}
		clearBoard(nextPieceBoard);
		addBlockToDisplay(nextPieceBoard, nextBlock);
		// activeBlock = nextBlock;
		// updateBoardWithNewBlock(nextBlock);
		// setNotFalling();
		blockCount++;
	}

	/**
	 * adds a block to the engine as an active block
	 * 
	 * @param b
	 *            the block to add
	 */
	public void addBlock(Block b) {
		activeBlock = b;
		updateBoardWithNewBlock(b);

	}

	// might need to be altered for when the stack gets very high
	/**
	 * adds block near the top of the screen
	 * 
	 * @param b
	 *            the block to add
	 */
	public void updateBoardWithNewBlock(Block b) {
		int offset = (gameBoard[0].length - b.getShape()[0].length) / 2;
		Tile[][] blockShape = b.getShape();
		for (int i = 0; i < blockShape.length; i++) {
			for (int j = 0; j < blockShape[i].length; j++) {
				if (tileAt(i, j + offset).isFilled()) {
					this.full = true;
				} else {
					updateTileLocation(i, j + offset, blockShape[i][j]);
				}
			}
		}
	}

	// might need to be altered for when the stack gets very high
	/**
	 * sets the tiles in a board to the tiles in a block, to bu used when a tile
	 * display is required
	 * 
	 * @param b
	 *            the block to add
	 */
	public void addBlockToDisplay(Tile[][] board, Block b) {
		// int offset = (gameBoard[0].length - b.getShape()[0].length) / 2;
		clearBoard(board);
		Tile[][] blockShape = b.getShape();
		for (int i = 0; i < blockShape.length; i++) {
			for (int j = 0; j < blockShape[i].length; j++) {
				if (blockShape[i][j].isFilled()) {
					board[i][j] = new Tile(blockShape[i][j].getColor());
				}
			}
		}
	}

	/**
	 * used for randomized blocks
	 * 
	 * @return a new random instance of Block
	 */
	private Block getRandomBlock() {
		Random r = new Random();
		int i = r.nextInt(7);
		return translateToBlock(i);
	}

	/**
	 * gets the next block if consistent blocks are to be used
	 * 
	 * @param blockNum
	 *            the block it should add, one more than the total number of
	 *            blocks added at that point
	 * @return the next Block
	 */
	private Block getNextBlock(int blockNum) {
		// printBoard();
		Block b = translateToBlock(blocks[gameNum][blockNum]);
		// System.out.println(gameNum + " " + blockNum + " " + b.getType());
		return b;
	}

	/**
	 * translates an integer to a Block
	 * 
	 * @param i
	 *            the integer to translate
	 * @return a Block corresponding to that integer
	 */
	private Block translateToBlock(int i) {
		// return new RightS();
		switch (i) {
		case 0:
			return new LeftL();
		case 1:
			return new RightL();
		case 2:
			return new LeftS();
		case 3:
			return new RightS();
		case 4:
			return new StraightLine();
		case 5:
			return new TBlock();
		case 6:
			return new Square();
		}
		throw new RuntimeException("bad random num");
	}

	/**
	 * updates the falling block by moving it down one row
	 */
	private void lowerFallingBlock() {
		removeFallingBlock();
		Tile[][] fallingBlockShape = activeBlock.getShape();
		int[] fallingBlockLocation = activeBlock.getGridLocation();

		for (int i = fallingBlockShape.length - 1; i >= 0; i--) {
			for (int j = fallingBlockShape[i].length - 1; j >= 0; j--) {
				if (fallingBlockShape[i][j].isFilled()) {
					// makes sure we aren't running off the board
					int column = fallingBlockLocation[1] - (fallingBlockShape[0].length - 1 - j);
					int row = fallingBlockLocation[0] - (fallingBlockShape.length - 1 - i);
					if (column >= 0 && row >= 0) {
						updateTileLocation(row, column, fallingBlockShape[i][j]);
					}
				}
			}
		}
	}

	/**
	 * remove all active tiles on the board, should only be the active block
	 */
	private void removeFallingBlock() {
		for (int i = 0; i < gameBoard.length; i++) {
			for (int j = 0; j < gameBoard[0].length; j++) {
				if (gameBoard[i][j].isActive()) {
					gameBoard[i][j] = new Tile();
				}
			}
		}

	}

	/**
	 * generalize way of getting user input into the engine
	 * 
	 * @param m
	 *            the move that the user wants to execute
	 */
	public void executeMove(Move m) {
		if (m == Move.RIGHT) {
			if (checkRight()) {
				moveRight();
			}
		} else if (m == Move.LEFT) {
			if (checkLeft()) {
				moveLeft();
			}
		} else if (m == Move.ROT_RIGHT) {
			if (checkRotate(Move.ROT_RIGHT)) {
				activeBlock.rotateRight();
				lowerFallingBlock();
			}
		} else if (m == Move.ROT_LEFT) {
			if (checkRotate(Move.ROT_LEFT)) {
				activeBlock.rotateLeft();
				lowerFallingBlock();
			}
		} else if (m == Move.DOWN) {
			if (checkDown()) {
				if (getScoreMode() == ScoreMode.HANK_LIAM) {
					score += 2;
				}
				blockDown();
			}
		} else if (m == Move.DROP) {
			while (checkDown()) {
				if (getScoreMode() == ScoreMode.HANK_LIAM) {
					score += 3;
				}
				// printBoard();
				blockDown();
			}
		} else if (m == Move.UP) {
			if (debugMode){
				blockUp();
			}
			//doesn't work for some reason
			//most tetris games have the up key rotate the block in a specified direction
			/*
			 * else { executeMove(Move.ROT_LEFT); }
			 */
		}

	}

	/**
	 * prints the board for debugging
	 */
	public void printBoard() {
		for (Tile[] row : gameBoard) {
			for (Tile t : row) {
				System.out.print(t);
			}
			System.out.println();
		}
	}

	/**
	 * checks to see if moving the block to the right is a valid move
	 * 
	 * @return true if it is valid, false otherwise
	 */
	private boolean checkRight() {
		for (Tile[] a : gameBoard) {

			// checks the far right column
			if (a[a.length - 1].isActive()) {
				return false;
			}

			// checks for an inactive but filled tile on the right of every
			// active tile
			for (int i = a.length - 2; i >= 0; i--) {
				if (a[i].isActive()) {
					Tile tileOnRight = a[i + 1];
					if (tileOnRight.isFilled() && !tileOnRight.isActive()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * moves the active block to the right
	 */
	private void moveRight() {
		for (int i = 0; i < gameBoard.length; i++) {
			for (int j = gameBoard[i].length - 2; j >= 0; j--) {
				Tile currentTile = gameBoard[i][j];
				if (currentTile.isActive()) {
					updateTileLocation(i, j + 1, currentTile);
					updateTileLocation(i, j, new Tile());
				}
			}
		}
		activeBlock.moveRight();
	}

	/**
	 * checks to see if moving the block to the left is a valid move
	 * 
	 * @return true if it is valid, false otherwise
	 */
	private boolean checkLeft() {
		for (Tile[] a : gameBoard) {

			// checks the far left column
			if (a[0].isActive()) {
				return false;
			}

			// checks for an inactive but filled tile on the left of every
			// active tile
			for (int i = 1; i < a.length; i++) {
				if (a[i].isActive()) {
					Tile tileOnLeft = a[i - 1];
					if (tileOnLeft.isFilled() && !tileOnLeft.isActive()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * moves the active block to the left
	 */
	private void moveLeft() {
		for (int i = 0; i < gameBoard.length; i++) {
			for (int j = 1; j < gameBoard[i].length; j++) {
				Tile currentTile = gameBoard[i][j];
				if (currentTile.isActive()) {
					updateTileLocation(i, j - 1, currentTile);
					updateTileLocation(i, j, new Tile());
				}
			}
		}
		activeBlock.moveLeft();
	}

	/**
	 * checks to see if a possible rotation is valid
	 * 
	 * @param m
	 *            the test rotation, should only be passes ROT_RIGHT, ROT_LEFT
	 * @return true if the rotation is valid, false otherwise
	 */
	private boolean checkRotate(Move m) {
		Block tempB = new Block(activeBlock.getType(), activeBlock.getGridLocation(), activeBlock.getRotationIndex());
		if (m == Move.ROT_LEFT) {
			tempB.rotateLeft();
		} else if (m == Move.ROT_RIGHT) {
			tempB.rotateRight();
		}
		Tile[][] tempBShape = tempB.getShape();
		for (int i = tempBShape.length - 1; i >= 0; i--) {
			for (int j = tempBShape[i].length - 1; j >= 0; j--) {
				if (tempBShape[i][j].isActive()) {
					int[] tempBLocation = tempB.getGridLocation();

					// makes sure we arent running off the board
					int row = tempBLocation[0] - (tempBShape.length - 1 - i);
					int column = tempBLocation[1] - (tempBShape[i].length - 1 - j);

					if (row >= 0 && column >= 0) {
						Tile t = tileAt(row, column);

						// checks to make sure flip is legal, ignoring itself in
						// the process
						if (t.isFilled() && !t.isActive()) {
							if (debugMode) {
								System.out.println("cant turn " + m);
							}
							return false;
						}
					} else {
						if (debugMode) {
							System.out.println("cant turn " + m);
						}
						return false;
					}
				}
			}
		}
		if (debugMode) {
			System.out.println("can turn " + m);
		}
		return true;
	}

	/**
	 * moves the active block up by one space, only used in debugging
	 */
	public void blockUp() {
		for (int i = 0; i < gameBoard.length; i++) {
			for (int j = 0; j < gameBoard[i].length; j++) {
				if (tileAt(i, j).isActive()) {
					updateTileLocation(i - 1, j, tileAt(i, j));
					updateTileLocation(i, j, new Tile());
				}
			}
		}
		activeBlock.moveUp();
	}

	/**
	 * toggles pause
	 * 
	 * @return the new value of the isPaused variable
	 */
	public boolean togglePause() {
		isPaused = !isPaused;
		return isPaused;
	}

	/**
	 * sets if the Engine should be adding random blocks or consistent blocks if
	 * they are to be non-random, this loads in the required data
	 * 
	 * @param randomizeBlocks
	 *            true if random blocks are to be used, false otherwise
	 */
	public void setRandomizeBlocks(boolean randomizeBlocks) {
		this.randomizeBlocks = randomizeBlocks;
		if (!randomizeBlocks) {
			blocks = readInBlocks();
			nextBlock = getNextBlock(this.blockCount);
			blockCount++;
		}
	}

	/**
	 * reads in the blocks to be used in the consistent training mode the blocks
	 * are stored as int's in the Blocks to Add file
	 * 
	 * @return an array of integers which can be mapped to a block type the rows
	 *         are unique games, each int in the row is one block to be added
	 *         that game
	 */
	private int[][] readInBlocks() {
		Scanner fileReader = null;
		try {
			fileReader = new Scanner(new File(BLOCK_DATA));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int numGames = 0;
		// scan through once for numbers of lines
		while (fileReader.hasNextLine()) {
			numGames++;
			fileReader.nextLine();
		}
		// Reinitialized
		try {
			fileReader = new Scanner(new File(BLOCK_DATA));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int[][] blocks = new int[numGames][];
		for (int i = 0; i < numGames; i++) {
			String[] textNums = fileReader.nextLine().split(" ");
			int[] nums = new int[textNums.length];
			for (int j = 0; j < textNums.length; j++) {
				nums[j] = Integer.parseInt(textNums[j]);
			}
			blocks[i] = nums;
		}
		return blocks;
	}

	/**
	 * gets the number of blocks that have been added
	 * 
	 * @return the number of blocks that have been added
	 */
	public int getBlockCount() {
		return blockCount;
	}

	/**
	 * reset the state of the engine (used between every game)
	 */
	public void reset() {
		this.blockCount = 0;
		this.score = 0;
		this.numOfFullRows = 0;
		if (randomizeBlocks) {
			this.nextBlock = getRandomBlock();
		} else {
			this.nextBlock = getNextBlock(blockCount);
			blockCount++;
		}
		this.full = false;
		this.gameNum++; // keep for automated testing
	}

	/**
	 * goes over the whole grid and sets each tile to not falling
	 */
	public void setNotFalling() {
		for (Tile[] list : gameBoard) {
			for (Tile t : list) {
				t.setActive(false);
			}
		}
	}

	/**
	 * sets all tiles in the passed board to inactive, unfilled tiles
	 * 
	 * @param board
	 *            the board to reset
	 */
	public void clearBoard(Tile[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = new Tile();
			}
		}
	}

	/**
	 * resets the Engine's game (used for when the weights are being changed)
	 */
	public void resetGameNum() {
		this.gameNum = 0;
	}

	/**
	 * gets the number of concurrent games this engine has played
	 * 
	 * @return the number of games the engine has played
	 */
	public int getGameNum() {
		return this.gameNum;
	}

	/**
	 * gets if the engine's board is full
	 * 
	 * @return true if the board is full, false otherwise
	 */
	public boolean hasFullBoard() {
		return full;
	}

	/**
	 * gets the score of the current game
	 * 
	 * @return the score of the game
	 */
	public int getScore() {
		return score;
	}

	/**
	 * gets the number of full rows that the engine has cleared
	 * 
	 * @return the number of full rows
	 */
	public int getNumFullRows() {
		return numOfFullRows;
	}

	/**
	 * gets if rows are currently falling on the board
	 * 
	 * @return true if rows are falling, false otherwise
	 */
	public boolean rowsAreNotFalling() {
		return rowsNotFalling;
	}

	/**
	 * gets the gameBoard that displays the next piece to be added, used for
	 * rendering
	 * 
	 * @return the gameBoard holding the next piece
	 */
	public Tile[][] getNextPieceBoard() {
		return nextPieceBoard;
	}

	/**
	 * sets the game board the engine should use, used in AI
	 * 
	 * @param newGameBoard
	 *            the game board the engine should use
	 */
	public void setGameBoard(Tile[][] newGameBoard) {
		this.gameBoard = newGameBoard;

	}
	
	public ScoreMode getScoreMode(){
		return this.scoreMode;
	}

}
