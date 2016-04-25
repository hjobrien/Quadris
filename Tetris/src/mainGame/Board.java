package mainGame;

import java.util.ArrayList;

import blocks.Block;
import blocks.Tile;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Board {
  private Tile[][] boardState;
  private Block fallingBlock;
  private GridPane grid;
  public boolean blockAdded = false;
  private Rectangle[][] boardRects;
  private int numOfFullRows = 0;

  private boolean rowsNotFalling = true;

  // would indicate the game is over
  boolean full = false;

  // for debugging
  private static boolean debugMode = false;
  // private static boolean debugMode = true;

  int boardScore = 0;

  // three extra blocks so that the straight all blocks
  // (particularly the straight line) can rotate
  // when they are initialized
  /**
   * makes a new board with a GridPane, a height and a width
   * 
   * @param height
   * @param width
   * @param grid
   */
  public Board(int height, int width, GridPane grid) {
    Tile[][] tempBoard = new Tile[height + 3][width];
    for (int i = 0; i < height + 3; i++) {
      for (int j = 0; j < width; j++) {
        tempBoard[i][j] = new Tile(); // not active, not filled
      }
    }
    this.boardState = tempBoard;
    this.grid = grid;

    Rectangle[][] tempBoardRects = new Rectangle[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Rectangle r = new Rectangle(29, 29);
        r.setFill(Color.WHITE);
        tempBoardRects[i][j] = r;
        grid.add(r, j, i);
      }
    }
    this.boardRects = tempBoardRects;
  }

  public Board(Tile[][] t, Block nextBlock) {
    this.boardState = t;
    this.grid = new GridPane();

    // int height = t.length;
    // int width = t[0].length;
    // Rectangle[][] tempBoardRects = new Rectangle[height][width];
    //
    // for (int i = 0; i < height; i++) {
    // for (int j = 0; j < width; j++) {
    // Rectangle r = new Rectangle(29, 29);
    // r.setFill(Color.WHITE);
    // tempBoardRects[i][j] = r;
    // grid.add(r, j, i);
    // }
    // }

    setFallingBlock(nextBlock);
    updateBoardWithNewBlock(nextBlock);

    // this.boardRects = tempBoardRects;
  }

  public Rectangle[][] getBoardRects() {
    return this.boardRects;
  }

  public boolean rowsAreNotFalling() {
    return rowsNotFalling;
  }

  public int getBoardScore() {
    return boardScore;
  }

  public int getNumOfFullRows() {
    return this.numOfFullRows;
  }

  // might need to be altered for when the stack gets very high
  /**
   * adds block near the top of the screen
   * 
   * @param b the block to add
   */
  public void updateBoardWithNewBlock(Block b) {
    int offset = (this.boardState[0].length - b.getShape()[0].length) / 2;
    Tile[][] blockShape = b.getShape();
    for (int i = 0; i < blockShape.length; i++) {
      for (int j = 0; j < blockShape[i].length; j++) {
        if (tileAt(i + 3, j + offset).isFilled()) {
          this.full = true;
        } else {
          update(i + 3, j + offset, blockShape[i][j]);
        }
      }
    }
  }

  /**
   * updates the board with the Tiles values for color, active, and filled
   * 
   * @param i the x index to set
   * @param j the y index to set
   * @param t the tile to get the values from
   */
  public void update(int i, int j, Tile t) {
    this.boardState[i][j].setActive(t.isActive());
    this.boardState[i][j].setFilled(t.isFilled());
    this.boardState[i][j].setColor(t.getColor());
  }

  public Tile tileAt(int i, int j) {
    return this.boardState[i][j];
  }

  public Tile[][] getBoardState() {
    return this.boardState;
  }

  public Block getFallingBlock() {
    return fallingBlock;
  }

  public void setFallingBlock(Block fallingBlock) {
    this.fallingBlock = fallingBlock;
    this.fallingBlock.isFalling();
  }

  // TODO move related things

  /**
   * generalize way of getting user input into the engine
   * 
   * @param m the move that the user wants to execute
   */
  public void pressed(Move m) {
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
        fallingBlock.rotateRight();
        updateFallingBlock();
      }
    } else if (m == Move.ROT_LEFT) {
      if (checkRotate(Move.ROT_LEFT)) {
        fallingBlock.rotateLeft();
        updateFallingBlock();
      }
    } else if (m == Move.DOWN) {
      if (checkDown()) {
        if (!Game.NINTENDO_SCORING) {
          boardScore += 2;
        }
        blockDown();
      }
    } else if (m == Move.DROP) {
      while (checkDown()) {
        if (!Game.NINTENDO_SCORING) {
          boardScore += 3;
        }
        // System.out.println(fallingBlock.getGridLocation()[1]);
        if (fallingBlock.getGridLocation()[1] == -10) {
          System.out.println();
        }
        blockDown();
      }
    } else if (m == Move.UP) {
      blockUp();
    }

  }

  /**
   * checks to see if lowering the block by one space is a valid move
   * 
   * @return true if the move is valid, false otherwise
   */
  public boolean checkDown() {

    if (debugMode) {
      System.out
          .println(fallingBlock.getGridLocation()[0] + " " + fallingBlock.getGridLocation()[1]);
    }
    // this if is a hacky fix to stop the game from freezing under certain unknown conditions when
    // it should be resetting instead
    if (fallingBlock.getGridLocation()[1] < -4) {
      Game.resetGame();
      System.err.println("Freeze condition: resetting now...");
      return false;
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
      if (Game.NINTENDO_SCORING) {
        boardScore += 1;
      }
      return false;
    }

    return true;
    /*
     * if lowestEmptyRow has blocks above it: set blocks under lowestEmptyLine to inactive keep
     * blocks above lowestEmptyLine active
     */



  }

  /**
   * sets all tiles under the parameter to inactive to keep them from moving
   * 
   * @param lowestEmptyRow the row at which the inactivity should begin
   */
  private void setBlocksUnderToInactive(int lowestEmptyRow) {
    for (int i = lowestEmptyRow; i < boardState.length; i++) {
      for (Tile t : boardState[i]) {
        if (t.isFilled()) {
          t.setActive(false);
        }
      }
    }

  }

  /**
   * checks to see if any filled tile exists below the parameter
   * 
   * @param lowestEmptyRow the row below which should be checked
   * @return true if any block exists below, false otherwise
   */
  // TODO: should the name be below?
  private boolean existRowsAbove(int lowestEmptyRow) {
    for (int i = lowestEmptyRow; i >= 0; i--) {
      for (int j = 0; j < boardState[0].length; j++) {
        if (boardState[i][j].isFilled()) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * gets the lowest row that is totally empty
   * 
   * @return the lowest row devoid of any filled tiles
   */
  private int getLowestEmptyRow() {
    boolean foundEmptyRow = false;
    int row = boardState.length - 1;
    while (!foundEmptyRow && row >= 0) {
      boolean rowIsEmpty = true;
      int column = 0;
      while (rowIsEmpty && column < boardState[0].length) {
        if (boardState[row][column].isFilled()) {
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
   * checks if there is an inactive tile (block that already fell) below
   * 
   * @return true if there is an inactive tile, false otherwise
   */
  private boolean checkUnderneath() {
    boolean isUnderneath = false;
    for (int i = 0; i < boardState.length; i++) {
      for (int j = 0; j < boardState[i].length; j++) {
        Tile thisT = boardState[i][j];
        if (thisT.isActive()) {
          Tile nextT = boardState[i + 1][j];
          if (nextT.isFilled() && !nextT.isActive()) {
            isUnderneath = true;
            // return true;
          }
        }
      }
    }
    return isUnderneath;
  }

  /**
   * checks if the block is at the bottom of the screen
   * 
   * @return true if the block is at the bottom, false otherwise
   */
  private boolean checkBlockAtBottom() {
    int lastIndex = boardState.length - 1;
    for (int i = 0; i < boardState[0].length; i++) {
      if (tileAt(lastIndex, i).isActive()) {
        return true;
      }
    }
    return false;
  }

  /**
   * moves all the blocks down 1 row
   */
  public void blockDown() {
    for (int i = boardState.length - 1; i >= 0; i--) {
      for (int j = boardState[i].length - 1; j >= 0; j--) {
        if (tileAt(i, j).isActive()) {
          // creates new tile
          update(i + 1, j, tileAt(i, j));
          // clears old tile
          update(i, j, new Tile());
        }
      }
    }
    fallingBlock.moveDown();
  }

  /**
   * moves the active block up by one space, only used in debugging
   */
  public void blockUp() {
    for (int i = 0; i < boardState.length; i++) {
      for (int j = 0; j < boardState[i].length; j++) {
        if (tileAt(i, j).isActive()) {
          update(i - 1, j, tileAt(i, j));
          update(i, j, new Tile());
        }
      }
    }
    fallingBlock.moveUp();
  }

  /**
   * checks to see if moving the block to the right is a valid move
   * 
   * @return true if it is valid, false otherwise
   */
  private boolean checkRight() {
    for (Tile[] a : boardState) {

      // checks the far right column
      if (a[a.length - 1].isActive()) {
        return false;
      }


      // checks for an inactive but filled tile on the right of every active tile
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
    for (int i = 0; i < boardState.length; i++) {
      for (int j = boardState[i].length - 2; j >= 0; j--) {
        Tile currentTile = boardState[i][j];
        if (currentTile.isActive()) {
          update(i, j + 1, currentTile);
          update(i, j, new Tile());
        }
      }
    }
    fallingBlock.moveRight();
  }

  /**
   * checks to see if moving the block to the left is a valid move
   * 
   * @return true if it is valid, false otherwise
   */
  private boolean checkLeft() {
    for (Tile[] a : boardState) {

      // checks the far left column
      if (a[0].isActive()) {
        return false;
      }

      // checks for an inactive but filled tile on the left of every active tile
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
    for (int i = 0; i < boardState.length; i++) {
      for (int j = 1; j < boardState[i].length; j++) {
        Tile currentTile = boardState[i][j];
        if (currentTile.isActive()) {
          update(i, j - 1, currentTile);
          update(i, j, new Tile());
        }
      }
    }
    fallingBlock.moveLeft();
  }

  /**
   * checks to see if a possible rotation is valid
   * 
   * @param m the test rotation, should only be passes ROT_RIGHT, ROT_LEFT
   * @return true if the rotation is valid, false otherwise
   */
  private boolean checkRotate(Move m) {
    Block tempB = new Block(fallingBlock.getType(), fallingBlock.getGridLocation());
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

            // checks to make sure flip is legal, ignoring itself in the process
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
   * updates the falling block by moving it down one row
   */
  private void updateFallingBlock() {
    removeFallingBlock();
    Tile[][] fallingBlockShape = fallingBlock.getShape();
    int[] fallingBlockLocation = fallingBlock.getGridLocation();

    for (int i = fallingBlockShape.length - 1; i >= 0; i--) {
      for (int j = fallingBlockShape[i].length - 1; j >= 0; j--) {
        if (fallingBlockShape[i][j].isFilled()) {
          // makes sure we aren't running off the board
          int column = fallingBlockLocation[1] - (fallingBlockShape[0].length - 1 - j);
          int row = fallingBlockLocation[0] - (fallingBlockShape.length - 1 - i);
          if (column >= 0 && row >= 0) {
            update(row, column, fallingBlockShape[i][j]);
          }
        }
      }
    }
  }

  /**
   * remove all active tiles on the board, should only be the active block
   */
  private void removeFallingBlock() {
    for (int i = 0; i < boardState.length; i++) {
      for (int j = 0; j < boardState[0].length; j++) {
        if (boardState[i][j].isActive()) {
          boardState[i][j] = new Tile();
        }
      }
    }

  }


  // TODO clearing lines

  // if full, the game is over
  public boolean isFull() {
    return this.full;
  }

  /**
   * goes over the whole grid and sets each tile to not falling
   */
  public void setNotFalling() {
    for (Tile[] list : boardState) {
      for (Tile t : list) {
        t.setActive(false);
      }
    }
  }

  /**
   * removes all tiles from the board
   */
  public void clearBoard() {
    for (int i = 0; i < boardState.length; i++) {
      for (int j = 0; j < boardState[i].length; j++) {
        update(i, j, new Tile());
      }
    }
    this.boardScore = 0;
    this.numOfFullRows = 0;
    this.full = false;
    this.rowsNotFalling = true;
  }

  public GridPane getGrid() {
    return grid;
  }

  /**
   * gets the indexes of all the filled rows on the board
   * 
   * @return an ArrayList of the filled indexes
   */
  public ArrayList<Integer> getFullRows() {
    ArrayList<Integer> fullRows = new ArrayList<Integer>();
    for (int i = 0; i < boardState.length; i++) {
      boolean full = true;
      int index = 0;
      while (full && index < boardState[i].length) {
        if (!boardState[i][index].isFilled()) {
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
   * removes all the tiles on the lines indicated in the parameter
   * 
   * @param linesToClear the lines that need to be cleared
   */
  public void clearLines(ArrayList<Integer> linesToClear) {
    // rewards a "quadris"
    if (linesToClear.size() == 4) {
      if (Game.NINTENDO_SCORING) {
        boardScore += 1200;
      } else {
        boardScore += 900;
      }
    }

    if (linesToClear.size() == 3) {
      if (Game.NINTENDO_SCORING) {
        boardScore += 300;
      } else {
        boardScore += 300;
      }
    }

    if (linesToClear.size() == 2) {
      if (Game.NINTENDO_SCORING) {
        boardScore += 120;
      } else {
        boardScore += 200;
      }
    }

    if (linesToClear.size() == 1) {
      if (Game.NINTENDO_SCORING) {
        boardScore += 40;
      } else {
        boardScore += 100;
      }
    }

    for (int i = 0; i < linesToClear.size(); i++) {
      for (int j = 0; j < boardState[i].length; j++) {
        update(linesToClear.get(i), j, new Tile());
      }
    }

    rowsNotFalling = false;
    setNotFalling();
    boolean tileAboveLine = false;
    for (int i = 0; i < linesToClear.get(linesToClear.size() - 1); i++) {
      for (Tile t : boardState[i]) {
        if (t.isFilled()) {
          t.setActive(true);
          tileAboveLine = true;
        }
      }
    }

    if (!tileAboveLine) {
      fallingBlock.stoppedFalling();
      rowsNotFalling = true;
    }
  }

  public String toString() {
    String s = "";
    for (int i = 0; i < boardState.length; i++) {
      for (int j = 0; j < boardState[i].length; j++) {
        s += boardState[i][j].isFilled() ? "x" : "o";
      }
      s += "\n";
    }
    return s;
  }

  /**
   * sets the mode of the object
   * 
   * @param debugMode whether the object should print out its debug information
   */
  public static void setMode(boolean debugMode/* , boolean logMode */) {
    Board.debugMode = debugMode;
    // this.logMode = logMode;
  }

}
