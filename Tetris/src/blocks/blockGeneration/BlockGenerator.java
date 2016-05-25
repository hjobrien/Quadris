package blocks.blockGeneration;

import blocks.Block;

public interface BlockGenerator {

  public Block generateBlock();
  
  public void reset();

  public void setGameNumber(int j);
    
}
