package blocks.blockGeneration;

import blocks.Block;
import engine.UsedAllBlocksException;

public interface BlockGenerator {

  public Block generateBlock() throws UsedAllBlocksException;
  
  public void reset();

  public void setGameNumber(int j);
    
}
