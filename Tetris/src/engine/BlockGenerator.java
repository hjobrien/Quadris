package engine;

import blocks.Block;

public interface BlockGenerator {

  public Block generateBlock();
  
  public void reset();
}
