package engine;

import java.util.Random;

import blocks.Block;
import blocks.LeftL;
import blocks.LeftS;
import blocks.RightL;
import blocks.RightS;
import blocks.Square;
import blocks.StraightLine;
import blocks.TBlock;

public class RandomizeBlocks implements BlockGenerator {
  public static final Random RANDOM = new Random();

  @Override
  public Block generateBlock() {
    int i = RANDOM.nextInt(7);
    switch (i) {
      case 0:
        return new LeftL();
      case 1:
        return new RightL();
      case 2:
        return new LeftS();
      case 3:
        return new RightS();
      case 4:
        return new StraightLine();
      case 5:
        return new TBlock();
      case 6:
        return new Square();
    }
    throw new RuntimeException("bad random num");
  }

  @Override
  public void reset() {
    //don't need to do anything, resetting doesn't mean anything here
    
  }

}
