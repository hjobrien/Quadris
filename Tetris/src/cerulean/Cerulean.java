package cerulean;

import mainGame.Move;
import tetrominoes.BlockType;
import tetrominoes.Tile;

public class Cerulean {
  
  
  /**
   * called when a solution is needed for a given block
   * @return an array of moves needed to get piece to the optimal location, should be some form of left/right, rotate, drop
   */
  public static Move[] getSolution() {
    return new Move[]{Move.RIGHT};
  }
  
  /**
   * gives AI data for what block is active and the board state it is to be placed in
   * @param nextBlockType   the kind of block to be analyzed
   * @param boardState  the current board state
   */
  public static void submitBlock(BlockType nextBlockType, Tile[][] boardState) {
    
    
  }
}
