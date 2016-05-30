package blocks.blockGeneration;

import blocks.Block;
import blocks.BlockType;

/**
 * Generator used primarily for the test clients, it returns new instances of the same kind of block 
 * every time it is asked for a new block. The kind of block it generates is set in the constructor
 * and currently cannot be changed
 * @author Hank O'Brien
 *
 */
public class SingleBlockGenerator implements BlockGenerator {

  private BlockType blockToMake;
  
  /**
   * constructs a new instance of this object and tells it what kind of blocks it should be generating
   * @param blockToMake
   */
  public SingleBlockGenerator(BlockType blockToMake){
    this.blockToMake = blockToMake;
  }
  
  @Override
  /**
   * generates a new instance of the block specified in the constructor
   */
  public Block generateBlock() {
    return new Block(blockToMake, new int[]{4, 4}, 0);
  }

  @Override
  /**
   * resets the object irrelevant here
   */
  public void reset() {}

  @Override
  /**
   * sets the game number, also irrelevant
   */
  public void setGameNumber(int j) {}

}
