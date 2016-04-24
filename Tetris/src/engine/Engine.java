package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import blocks.Block;
import blocks.LeftL;
import blocks.LeftS;
import blocks.RightL;
import blocks.RightS;
import blocks.Square;
import blocks.StraightLine;
import blocks.TBlock;
import blocks.Tile;
import cerulean.Cerulean;
import mainGame.Board;
import mainGame.Game;
import mainGame.Move;

public class Engine {
  private static Board board;
  private static Board nextPieceBoard;
  private static Block nextBlock = genRandomBlock();
  private static boolean isPaused = false;
  private static boolean autoplay = false;
  private static boolean randomizeBlocks;
  private static int blockCount = 0;
  private static int gameNum = 0;

  private static boolean logMode;
  // private static boolean debugMode;

  public static final String BLOCK_DATA = "test";

  // lists of numbers corresponding to blocks, translated in the 'genNextBlock' method
  private static int[][] blocks = new int[][] {};


  /**
   * is the general engine method coordinates dropping blocks, adding blocks, logging game state,
   * and line clearing
   */
  public static void update() {
    if (!isPaused) { // little hacky, could be improved
      if (board.getFallingBlock().isFalling()) {
        if (board.checkDown()) {
          board.blockDown();
        } else {
          // block just landed
          if (logMode) {
            Logger.log(board.getBoardState(), board.getFallingBlock(),
                nextPieceBoard.getFallingBlock());
          }
          ArrayList<Integer> linesToClear = board.getFullRows();
          if (!linesToClear.isEmpty()) {
            board.clearLines(linesToClear);
          } else {
            board.setNotFalling();
            addBlock();
          }
        }
      } else {
        addBlock();
      }
    }
  }

  /**
   * 
   * @return a 2D array of the board, each index references a possible square and if it is filled or
   *         not
   */
  public Tile[][] getBoardState() {
    return board.getBoardState();
  }

  /**
   * adds a new random block to the board. Before one is added, a BlockAddedEvent is fired the new
   * block is taken from the nextBlockBoard with is shown to the user, introduced to the board, and
   * a new random one added to the nextBlockBoard
   */
  public static void addBlock() {
    // //needed to access Node.fireEvent, the GridPane was an accessible Node
    // if (autoplay){
    // board.getGrid().fireEvent(new BlockAddedEvent(nextBlock, board.getBoardState()));
    // }
    Move[] solution = null;
    if (autoplay) {
      solution = Cerulean.submitBlock(nextBlock, board.getBoardState());
    }
    board.setFallingBlock(nextBlock);
    board.updateBoardWithNewBlock(nextBlock);
    //toggle for step by step block analysis
//    Engine.togglePause();
//    Renderer.pause();
//    Game.togglePause();
    if (autoplay) {
      for (Move m : solution) {
        Engine.getBoard().pressed(m);
      }
    }
    if (randomizeBlocks)
      nextBlock = genRandomBlock();
    else
      nextBlock = getNextBlock(blockCount);
    nextPieceBoard.clearBoard();
    Renderer.draw(nextPieceBoard);
    nextPieceBoard.setFallingBlock(nextBlock);
    nextPieceBoard.updateBoardWithNewBlock(nextBlock);
    nextPieceBoard.setNotFalling();
    Renderer.draw(nextPieceBoard);
    blockCount++;
  }

  /**
   * 
   * @return a new random instance of Block
   */
  private static Block genRandomBlock() {
    Random r = new Random();
    int i = r.nextInt(7);
    return translateToBlock(i);
  }

  private static Block getNextBlock(int blockNum) {
    Block b = translateToBlock(blocks[gameNum][blockNum]); // change the 0 to gameNum for automated
                                                     // testing TODO
//    System.out.println(gameNum + " " + blockNum + " " + b.getType());
    return b;
  }

  private static Block translateToBlock(int i) {
//     return new RightS();
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

  /**
   * gets the Board object the engine is monitoring
   * 
   * @return the Board object the engine is working with
   */
  public static Board getBoard() {
    return board;
  }

  /**
   * toggles pause
   * 
   * @return the new value of the isPaused variable
   */
  public static boolean togglePause() {
    isPaused = !isPaused;
    return isPaused;
  }

  /**
   * serves as a kinds of initial pseudo-constructor to set the boards the engine has to work with
   * 
   * @param b1 the main game board
   * @param b2 a 4x4 smaller board where the next piece is shown
   */
  public static void setBoards(Board b1, Board b2) {
    board = b1;
    nextPieceBoard = b2;
  }

  /**
   * also serves to initialize values to the engine
   * 
   * @param debugMode whether the engine should operate in debugMode
   * @param logMode whether the engine should log its actions
   */
  public static void setMode(boolean debugMode, boolean logMode, boolean autoplay) {
    // Engine.debugMode = debugMode;
    Engine.logMode = logMode;
    Engine.autoplay = autoplay;
  }

  public static void setRandomizeBlocks(boolean randomizeBlocks) {
    Engine.randomizeBlocks = randomizeBlocks;
    if (!randomizeBlocks) {
      blocks = readInBlocks();
      nextBlock = getNextBlock(Engine.blockCount);
      blockCount++;
    }
  }

  private static int[][] readInBlocks() {
    Scanner fileReader = null;
    try {
      fileReader = new Scanner(new File(BLOCK_DATA));
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
      fileReader = new Scanner(new File(BLOCK_DATA));
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

  public static int getBlockCount() {
    return blockCount;
  }


  public static void reset() {
    Engine.blockCount = 0;
    if(randomizeBlocks){
      Engine.nextBlock = genRandomBlock();
    }
    else{
      Engine.nextBlock = getNextBlock(blockCount);
      blockCount++;
    }
     Engine.gameNum++; //keep for automated testing
  }

  public static int getGameNum() {
    return Engine.gameNum;
  }


}
