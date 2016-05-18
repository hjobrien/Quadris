package blocks;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class StandardizeBlocks implements BlockGenerator {

  public static final String BLOCK_FILE_PATH = "Blocks to Add";
  
  private int gameNumber;
  private int numBlocksAdded;
  private int[][] blocksToAdd;
  
  public StandardizeBlocks(){
    this(0);
  }
  
  public StandardizeBlocks(int gameNumber){
    this.gameNumber = gameNumber;
    numBlocksAdded = 0;
    if(blocksToAdd == null){
      blocksToAdd = readInBlocks();
    }
  }
  
  @Override
  public Block generateBlock() {
    int i = blocksToAdd[gameNumber][numBlocksAdded];
    numBlocksAdded++;
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
      throw new RuntimeException("bad file num");
  }
  
  private int[][] readInBlocks() {
    Scanner fileReader = null;
    try {
        fileReader = new Scanner(new File(BLOCK_FILE_PATH));
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    int numGames = 0;
    // scan through once for numbers of lines
    while (fileReader.hasNextLine()) {
        numGames++;
        fileReader.nextLine();
    }
    // Reinitialized
    try {
        fileReader = new Scanner(new File(BLOCK_FILE_PATH));
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    int[][] blocks = new int[numGames][];
    for (int i = 0; i < numGames; i++) {
        String[] textNums = fileReader.nextLine().split(" ");
        int[] nums = new int[textNums.length];
        for (int j = 0; j < textNums.length; j++) {
            nums[j] = Integer.parseInt(textNums[j]);
        }
        blocks[i] = nums;
    }
    return blocks;
    
  }

  @Override
  public void reset() {
    this.gameNumber++;
    this.numBlocksAdded = 0;
    
  }

  public void setGameNumber(int j) {
    this.gameNumber = j;
    
  }


}
