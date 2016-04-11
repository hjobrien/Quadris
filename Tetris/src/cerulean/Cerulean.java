package cerulean;

import engine.ComputationDoneEvent;
import engine.Engine;
import javafx.event.Event;
import mainGame.Move;
import tetrominoes.BlockType;
import tetrominoes.Tile;

public class Cerulean {
  
  private static Move[] solutionPath = new Move[]{Move.RIGHT};  //partially filled to prevent errors later on
  
  /**
   * called when a solution is needed for a given block
   * @return an array of moves needed to get piece to the optimal location, should be some form of left/right, rotate, drop
   */
  public static Move[] getSolution() {
    return solutionPath;
  }
  
  /**
   * gives AI data for what block is active and the board state it is to be placed in
   * @param nextBlockType   the kind of block to be analyzed
   * @param boardState  the current board state
   */
  public static void submitBlock(BlockType nextBlockType, Tile[][] boardState) {
    //do things
    solutionPath = computeBestPath(nextBlockType, boardState);
    
    
    Event.fireEvent(Engine.getBoard().getGrid(), new ComputationDoneEvent());   //using grid from engine as end event target, should change to something else TODO
  }

  private static Move[] computeBestPath(BlockType nextBlockType, Tile[][] boardState) {
    double maxWeight = -1;
    Move[] bestPath = new Move[]{};
    //TODO: reduce number of loops reps
    for(int moveCount = 0; moveCount < 14; moveCount++){    //14 possible worst-case left/right positions
      for(int rotCount = 0; rotCount < 4; rotCount++){      //4 possible worst case rotations
        double testWeight = computeWeight(nextBlockType, boardState, moveCount, rotCount);
        if(testWeight > maxWeight){
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

  private static double computeWeight(BlockType nextBlockType, Tile[][] boardState, int moveCount,
      int rotCount) {
    return -1; //TODO
    
  }
}
