package cerulean;

import java.util.ArrayList;

import engine.ComputationDoneEvent;
import engine.Engine;
import javafx.event.Event;
import mainGame.Move;
import tetrominoes.Block;
import tetrominoes.Tile;

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
    solutionPath = computeBestPath(nextBlock, boardState);

    // using grid from engine as event target, should change to something else TODO
    Event.fireEvent(Engine.getBoard().getGrid(), new ComputationDoneEvent(solutionPath)); 
  }

  private static Move[] computeBestPath(Block nextBlock, Tile[][] boardState) {
    double maxWeight = -1;
    Block nextBlockCopy = new Block(nextBlock);
    Move[] bestPath = new Move[] {};
    // TODO: reduce number of loops reps
    for (int moveCount = 0; moveCount < 10; moveCount++) { // 10 possible worst-case left/right
                                                           // positions
      for (int rotCount = 0; rotCount < 4; rotCount++) { // 4 possible worst case rotations
        double testWeight = computeWeight(nextBlockCopy, boardState, moveCount, rotCount);
        if (testWeight > maxWeight) {
          maxWeight = testWeight;
          bestPath = getPath(moveCount, rotCount);
        }
        nextBlockCopy.rotateRight();
      }
    }
    return bestPath;
  }

  private static Move[] getPath(int moveCount, int rotCount) {
    ArrayList<Move> path = new ArrayList<Move>();
    for (int i = 0; i < 6; i++) {   //puts the block into a constant, known position
      path.add(Move.LEFT);
    }
    for (int i = 0; i < moveCount; i++) {
      path.add(Move.RIGHT);
    }
    for (int i = 0; i < rotCount; i++) {
      path.add(Move.ROT_RIGHT);
    }
    path.add(Move.FULL_DOWN);
    return path.toArray(new Move[] {});
  }

  private static double computeWeight(Block nextBlock, Tile[][] boardState, int moveCount,
      int rotCount) {
    Tile[][] boardCopy = boardState.clone(); // makes a copy of the array, shouldn't have reference
                                             // issues
    for (int i = 0; i < nextBlock.getShape().length; i++) {
      for (int j = 0; j < nextBlock.getShape()[i].length; j++) {
        boardCopy[i + moveCount][j] = nextBlock.getShape()[i][j]; // shifts block to the far left
      }
    }
    int blankCount = 0;
    int minSpace = 20; // intentionally high
    for (int i = moveCount; i < nextBlock.getShape()[0].length; i++) { // starts where the shape was
                                                                    // placed, scans all columns it
                                                                    // occupies
      boolean hasPassedFilledBlock = false;
      blankCount = 0;
      for (int j = 0; j < boardCopy.length; j++) {  //boardCopy.length = height, loops through each row
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
    Tile[][] shape = nextBlock.getShape();
    for (int i = moveCount; i < shape[0].length; i++) { //goes over columns
      for (int j = 0; j < shape.length; j++) { // repeats for the height of the
                                                                 // block
        boardCopy[j + minSpace][i].setFilled(boardCopy[j][i].isFilled()); // should drop the block
                                                                          // down by minSpace blocks
        boardCopy[i][j].setFilled(false);
      }
    }
    // board copy should now have the block dropped all the way down it can go but with no lines
    // removed
    return evaluateWeight(boardCopy) - evaluateWeight(boardState);

  }

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

  private static int getHeight(Tile[] tiles) {
    for (int i = 0; i < tiles.length; i++) {
      if (tiles[i].isFilled()) {
        return i;
      }
    }
    return 0;
  }
}


