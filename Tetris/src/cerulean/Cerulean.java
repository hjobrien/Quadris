package cerulean;

import engine.ComputationDoneEvent;
import engine.Engine;
import javafx.event.Event;
import mainGame.Move;
import tetrominoes.Block;
import tetrominoes.BlockType;
import tetrominoes.Tile;

public class Cerulean {

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
    // do things
    solutionPath = computeBestPath(nextBlock, boardState);


    Event.fireEvent(Engine.getBoard().getGrid(), new ComputationDoneEvent()); // using grid from
                                                                              // engine as event
                                                                              // target, should
                                                                              // change to something
                                                                              // else TODO
  }

  private static Move[] computeBestPath(Block nextBlock, Tile[][] boardState) {
    double maxWeight = -1;
    Move[] bestPath = new Move[] {};
    // TODO: reduce number of loops reps
    for (int moveCount = 0; moveCount < 10; moveCount++) { // 10 possible worst-case left/right
                                                           // positions
      for (int rotCount = 0; rotCount < 4; rotCount++) { // 4 possible worst case rotations
        double testWeight = computeWeight(nextBlock, boardState, moveCount, rotCount);
        if (testWeight > maxWeight) {
          maxWeight = testWeight;
          bestPath = getPath(moveCount, rotCount);
        }
      }
    }
    return bestPath;
  }

  private static Move[] getPath(int moveCount, int rotCount) {
    // TODO Auto-generated method stub
    return null;
  }

  // TODO: may have pass-by-reference issues
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
    int minSpace = 20; //intentionally high
    for (int i = moveCount; i < nextBlock.getShape().length; i++) { // starts where the shape was
                                                                    // placed, scans all columns it
                                                                    // occupies
      boolean hasPassedFilledBlock = false;
      blankCount = 0;
      for (int j = 0; j < boardCopy[i].length; j++) {
          if(hasPassedFilledBlock && !boardCopy[i][j].isFilled()){
            blankCount++;
          }
          else{
            if(boardCopy[i][j].isFilled()){
              hasPassedFilledBlock = true;
            }
          }
      }
      if(blankCount < minSpace){
        minSpace = blankCount;
      }
    }
    for(int i = moveCount; i < nextBlock.getShape().length; i++){
      for(int j = 0; j < nextBlock.getShape()[i].length; j++){  //repeats for the height of the block
        boardCopy[i][j + minSpace].setActive(boardCopy[i][j].isActive());   //should drop the block down by minSpace blocks
        boardCopy[i][j].setActive(false);
      }
    }
    //board copy should now have the block dropped all the way down it can go but with no lines removed
    return evaluateWeight(boardCopy) - evaluateWeight(boardState);
    
    // edit board state with hypothetical placement
    // get block shape
    // add to a 10x4 board
    // shift to left
    // Concatenate the boards
    // pass to weight analyzer
    // return weight of new board - old board
    // moves block 7 times to the left, then moveCount times to the right

  }

  private static int evaluateWeight(Tile[][] boardCopy) {
    // TODO Auto-generated method stub
    return 0;
  }
}


