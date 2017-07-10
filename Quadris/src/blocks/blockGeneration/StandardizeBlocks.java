package blocks.blockGeneration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import blocks.Block;
import blocks.LeftL;
import blocks.LeftS;
import blocks.RightL;
import blocks.RightS;
import blocks.Square;
import blocks.StraightLine;
import blocks.TBlock;
import engine.UsedAllBlocksException;

/**
 * Object dedicated to producing repeatable streams of blocks using pre-written lines of numbers in
 * a text file that the object converts to Blocks
 * 
 * @author Hank O'Brien
 *
 */
public class StandardizeBlocks implements BlockGenerator {

  public static final String BLOCK_FILE_PATH = "./Quadris/Blocks-to-Add.txt";

  private int gameNumber;
  private int numBlocksAdded;
  private int[][] blocksToAdd;

  /**
   * convenience constructor that creates a new instance of the object with gameNumber 0
   */
  public StandardizeBlocks() {
    this(0);
  }

  /**
   * constructor that lets the caller pick which game they want to start at (up to 50 supported)
   * 
   * @param gameNumber
   */
  public StandardizeBlocks(int gameNumber) {
    if (gameNumber > 50) {
      throw new IllegalArgumentException("Game Number is unsupported");
    }
    this.gameNumber = gameNumber;
    numBlocksAdded = 0;
    if (blocksToAdd == null) {
      blocksToAdd = readInBlocks();
    }
  }

  @Override
  /**
   * generates a new Block by converting the number from the file to a Block Object
   */
  public Block generateBlock() throws UsedAllBlocksException {
    int i = -1;
    try{
      i = blocksToAdd[gameNumber][numBlocksAdded];
    }catch(ArrayIndexOutOfBoundsException e){
      throw new UsedAllBlocksException("No more blocks to add");
    }
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
    throw new RuntimeException("bad number in file");
  }

  /**
   * reads in the lists of numbers from the file when an instance of this object is first
   * constructed As this method is only called once, it keeps slow file I/O to a minimum and before
   * the game starts to play
   * 
   * @return an array whose rows are unique games composed of lists of number that will if necessary
   *         be converted to Blocks
   */
  private int[][] readInBlocks() {
    File temp = new File("./");
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
  /**
   * updates the gameNumber and blockNumbers so blocks will be pulled from the right part of the
   * int[][] if more blocks are requested
   */
  public void reset() {
    this.gameNumber++;
    this.numBlocksAdded = 0;

  }

  /**
   * sets the game number of the object
   */
  @Override
public void setGameNumber(int j) {
    this.gameNumber = j;

  }

}
