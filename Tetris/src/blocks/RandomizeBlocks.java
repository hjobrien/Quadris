package blocks;

import java.util.Random;

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

  @Override
  public void setGameNumber(int j) {
    //don't need to do anything, game number doesn't matter to randomized blocks  

  }

}
