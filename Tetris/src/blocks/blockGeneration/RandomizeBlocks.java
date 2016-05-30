package blocks.blockGeneration;

import java.util.Random;

import blocks.Block;
import blocks.LeftL;
import blocks.LeftS;
import blocks.RightL;
import blocks.RightS;
import blocks.Square;
import blocks.StraightLine;
import blocks.TBlock;

/**
 * This class is a specification of BlockGenerator that generates new blocks based on a random number
 * This produces the effect of random blocks being generated
 * @author Hank O'Brien
 * 
 */
public class RandomizeBlocks implements BlockGenerator {
  public static final Random RANDOM = new Random();

  @Override
  /**
   * generates a new block using a random number
   */
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
  /**
   * resets the game, but doesn't need to do anything in this implementation
   */
  public void reset() {}

  @Override
  /**
   * sets the game number of the generator, but this is also irrelevant to this particular object
   */
  public void setGameNumber(int j) {
    //don't need to do anything, game number doesn't matter to randomized blocks  

  }
  
}
