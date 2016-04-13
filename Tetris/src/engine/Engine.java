package engine;

import java.util.ArrayList;
import java.util.Random;

import blocks.Block;
import blocks.LeftL;
import blocks.LeftS;
import blocks.RightL;
import blocks.RightS;
import blocks.Square;
import blocks.StraightLine;
import blocks.TBlock;
import blocks.Tile;
import mainGame.Board;

public class Engine {
  private static Board board;
  private static Board nextPieceBoard;
  private static Block nextBlock = generateRandomBlock();
  private static boolean isPaused = false;

  private static boolean logMode;
  // private static boolean debugMode;

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
    //needed to access Node.fireEvent, the GridPane was an accessible Node
    board.getGrid().fireEvent(new BlockAddedEvent(nextBlock, board.getBoardState())); 
    board.setFallingBlock(nextBlock);
    board.updateBoardWithNewBlock(nextBlock);
    nextBlock = generateRandomBlock();
    nextPieceBoard.clearBoard();
    Renderer.draw(nextPieceBoard);
    nextPieceBoard.setFallingBlock(nextBlock);
    nextPieceBoard.updateBoardWithNewBlock(nextBlock);
    nextPieceBoard.setNotFalling();
    Renderer.draw(nextPieceBoard);
  }

  /**
   * 
   * @return a new random instance of Block
   */
  private static Block generateRandomBlock() {
    return new StraightLine();
//    Random r = new Random();
//    int i = r.nextInt(7);
//    switch (i) {
//      case 0:
//        return new LeftL();
//      case 1:
//        return new RightL();
//      case 2:
//        return new LeftS();
//      case 3:
//        return new RightS();
//      case 4:
//        return new StraightLine();
//      case 5:
//        return new TBlock();
//      case 6:
//        return new Square();
//    }
//    throw new RuntimeException("bad random num");
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
  public static void setValues(Board b1, Board b2) {
    board = b1;
    nextPieceBoard = b2;

  }

  /**
   * also serves to initialize values to the engine
   * 
   * @param debugMode whether the engine should operate in debugMode
   * @param logMode whether the engine should log its actions
   */
  public static void setMode(boolean debugMode, boolean logMode) {
    // Engine.debugMode = debugMode;
    Engine.logMode = logMode;
  }

}
