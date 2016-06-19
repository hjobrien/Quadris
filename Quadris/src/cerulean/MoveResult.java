package cerulean;

import blocks.Tile;

/**
 * wrapper class for a move array that serves to map a path to a board state, this allows for more
 * intuitive data structures in other parts of the code
 * 
 * @author Hank O'Brien
 *
 */
public class MoveResult {

  private int[] path;

  private Tile[][] boardResult;

  public MoveResult(int[] path, Tile[][] boardResult) {
    this.path = path;
    this.boardResult = boardResult;
  }

  /**
   * @return the path
   */
  public int[] getPath() {
    return this.path;
  }

  /**
   * @return the boardResult
   */
  public Tile[][] getBoardResult() {
    return this.boardResult;
  }


}
