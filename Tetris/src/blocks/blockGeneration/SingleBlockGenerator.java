package blocks.blockGeneration;

import blocks.Block;
import blocks.BlockType;

public class SingleBlockGenerator implements BlockGenerator {

  private BlockType blockToMake;
  
  public SingleBlockGenerator(BlockType blockToMake){
    this.blockToMake = blockToMake;
  }
  
  @Override
  public Block generateBlock() {
    return new Block(blockToMake, new int[]{4, 4}, 0);
  }

  @Override
  public void reset() {
    //not relevant here
  }

  @Override
  public void setGameNumber(int j) {
    //not relenant here
  }

}
