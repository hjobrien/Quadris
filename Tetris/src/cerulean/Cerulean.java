package cerulean;

import java.util.ArrayList;

import blocks.Block;
import blocks.Tile;
import engine.ComputationDoneEvent;
import engine.Engine;
import javafx.event.Event;
import mainGame.Move;

/**
 * General AI class, handles game state analysis and move optimization
 * 
 * @author Hank
 *
 */
public class Cerulean {

  // Board Weight constants
  // negative means its a bad thing being weighted (overall board height)
  // positive means its a good thing (full lines);
  private static final double HEIGHT_WEIGHT = -0.1;
  private static final double VOID_WEIGHT = -0.5;
  private static final double LINE_WEIGHT = 1;



  private static Move[] solutionPath = new Move[] {Move.RIGHT}; // partially filled to prevent
                                                                // errors later on

  /**
   * called when a solution is needed for a given block
   * 
   * @return an array of moves needed to get piece to the optimal location, should be some form of
   *         left/right, rotate, drop
   */
  public static Move[] getSolution() {
    return solutionPath;
  }

  /**
   * gives AI data for what block is active and the board state it is to be placed in
   * 
   * @param nextBlockType the kind of block to be analyzed
   * @param boardState the current board state
   */
  public static void submitBlock(Block nextBlock, Tile[][] boardState) {
    // TODO:
    // ideal behavior: blocks drop normally but an array is generated each time a block is added to
    // the game
    long t1 = System.currentTimeMillis();
    solutionPath = computeBestPath(nextBlock, boardState);
    System.out.println("Weight analysis took " + (System.currentTimeMillis() - t1) + " Milli(s)");
    // using grid from engine as event target, should change to something else
    // TODO: make board extend GridPane? it'd be a node then
    Event.fireEvent(Engine.getBoard().getGrid(), new ComputationDoneEvent(solutionPath));
  }

  /**
   * Generalized move optimization method
   * 
   * @param nextBlock the block just introduced to the board
   * @param boardState the board state without the block entered, all tiles are not active
   * @return an array of moves that positions the piece in to the optimal location
   */
  private static Move[] computeBestPath(Block nextBlock, Tile[][] boardState) {
    double maxWeight = -1;
    Block nextBlockCopy = new Block(nextBlock.getType());
    Move[] bestPath = new Move[] {};
    // TODO: reduce number of loops reps
    for (int moveCount = 0; moveCount < 10; moveCount++) { // 10 possible worst-case left/right
                                                           // positions
      for (int rotCount = 0; rotCount < 4; rotCount++) { // 4 possible worst case rotations
        Tile[][] testState = positionBlock(nextBlockCopy, boardState, moveCount, rotCount);
        double testWeight = evaluateWeight(testState) - evaluateWeight(boardState);
        if (testWeight > maxWeight) {
          maxWeight = testWeight;
          bestPath = getPath(moveCount, rotCount);
        }
        nextBlockCopy.rotateRight();
      }
    }
    return bestPath;
  }

  /**
   * converts integer representations of moves into array of individual moves
   * 
   * @param moveCount starting from the far left, how many times the block must be moved to the
   *        right
   * @param rotCount starting from the default configuration, the number of rotations to the right
   *        are required
   * @return an array of moves that first shifts the block to the left, then some amount to the
   *         right, rotates, and issues a 'drop' command to terminate the sequence
   */
  private static Move[] getPath(int moveCount, int rotCount) {
    ArrayList<Move> path = new ArrayList<Move>();
    for (int i = 0; i < 6; i++) { // puts the block into a constant, known position
      path.add(Move.LEFT);
    }
    for (int i = 0; i < moveCount; i++) {
      path.add(Move.RIGHT);
    }
    for (int i = 0; i < rotCount; i++) {
      path.add(Move.ROT_RIGHT);
    }
    path.add(Move.DROP);
    return path.toArray(new Move[] {});
  }

  /**
   * positions a block in a copy of the board based on translation and rotation parameters
   * 
   * @param nextBlock the active block
   * @param boardState the state of non active tiles
   * @param moveCount the number of left/right moves in the test arrangement
   * @param rotCount the number of rotations in the test arrangement
   * @return the board with the block moved to a certain position
   */
  private static Tile[][] positionBlock(Block nextBlock, Tile[][] boardState, int moveCount,
      int rotCount) {
    // avoids reference issues
    Tile[][] boardCopy = new Tile[boardState.length][boardState[0].length];
//    for (int i = 0; i < boardState.length; i++) {
//      boardCopy[i] = boardState[i].clone();
//    }
    for (int i = 0; i < boardState.length; i++) {
      for (int j = 0; j < boardState[0].length; j++) {
        boardCopy[i][j] = new Tile(boardState[i][j].isActive(), boardState[i][j].isFilled(),
            boardState[i][j].getColor());
      }
    }

    for (int i = 0; i < nextBlock.getShape().length; i++) {
      for (int j = 0; j < nextBlock.getShape()[i].length; j++) {
        boardCopy[i + moveCount][j] = nextBlock.getShape()[i][j]; // shifts block to the far left
      }
    }
    int blankCount = 0;
    int minSpace = 40; // intentionally high
    for (int i = moveCount; i < nextBlock.getShape()[0].length; i++) { // starts where the shape was
      // placed, scans all columns it occupies
      boolean hasPassedFilledBlock = false;
      for (int j = 0; j < boardCopy.length; j++) { // boardCopy.length = height, loops through each
                                                   // row
        if (boardCopy[j][i].isActive()) {
          blankCount = 0;
        }
        if (hasPassedFilledBlock && !boardCopy[j][i].isFilled()) {
          blankCount++;
        } else {
          if (boardCopy[j][i].isFilled()) {
            hasPassedFilledBlock = true;
          }
        }
      }
      if (blankCount < minSpace) {
        minSpace = blankCount;
      }
    }
    Tile[][] shape = new Tile[nextBlock.getShape().length][]; // avoids pass by reference
    for (int i = 0; i < nextBlock.getShape().length; i++) {
      shape[i] = nextBlock.getShape()[i].clone();
    }
    for (int i = moveCount; i < shape[0].length; i++) { // goes over columns
      for (int j = 0; j < shape.length; j++) { // repeats for the height of the
                                               // block
        if (j + minSpace == 23) {
          minSpace--;
        }
        boardCopy[j + minSpace][i].setFilled(boardCopy[j][i].isFilled()); // should drop the block
                                                                          // down by minSpace blocks
      }
    }
    // board copy should now have the block dropped all the way down it can go but with no lines
    // removed
    return boardCopy;

  }

  /**
   * evaluates the relative value of the board
   * 
   * @param boardCopy the board to be analyzed
   * @return the value of the board
   */
  private static double evaluateWeight(Tile[][] boardCopy) {
    double weight = 0;
    for (int i = 0; i < boardCopy.length; i++) {
      weight += (HEIGHT_WEIGHT * getHeight(boardCopy[i]));
      weight += (VOID_WEIGHT * getNumVoids(boardCopy[i])); // make this scale quadratically?
    }
    for (int i = 0; i < boardCopy[0].length; i++) {
      weight += (LINE_WEIGHT * getNumLines(boardCopy));
    }
    return weight;
  }

  /**
   * returns the number of completed lines in the board
   * 
   * @param boardCopy the board to be analyzed. It should not have been processed to remove lines
   * @return the number of completed lines on the board (rows)
   */
  private static int getNumLines(Tile[][] boardCopy) {
    int numLines = 0;
    for (int i = 0; i < boardCopy.length; i++) {
      boolean isFull = true;
      for (int j = 0; j < boardCopy[i].length; j++) {
        if (!boardCopy[i][j].isFilled()) {
          isFull = false;
        }
      }
      if (isFull) {
        numLines++;
      }
    }
    return numLines;
  }

  /**
   * analyzes a column of tiles for the number of voids it contains. A void is defined as any blank
   * space with a filled, non active tile at some point above it
   * 
   * @param tiles the column of tiles to be analyzed
   * @return the number of voids in the column
   */
  private static int getNumVoids(Tile[] tiles) {
    boolean hasFoundBlock = false;
    int voidCount = 0;
    for (int i = 0; i < tiles.length; i++) {
      if (hasFoundBlock && !tiles[i].isFilled()) {
        voidCount++;
      }
      if (tiles[i].isFilled()) {
        hasFoundBlock = true;
      }
    }
    return voidCount;
  }

  /**
   * gets the height of a column of tiles. The height is defined as the index of the highest filled,
   * non active tile in the column
   * 
   * @param tiles the column to be analyzed
   * @return the height of the column
   */
  private static int getHeight(Tile[] tiles) {
    for (int i = 0; i < tiles.length; i++) {
      if (tiles[i].isFilled()) {
        return i;
      }
    }
    return 0;
  }
}


