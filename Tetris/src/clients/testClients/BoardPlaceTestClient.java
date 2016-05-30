package clients.testClients;

import java.util.Arrays;

import blocks.Block;
import blocks.LeftS;
import blocks.RightS;
import blocks.Square;
import blocks.Tile;
import cerulean.Cerulean;
import clients.interfaces.Autoplayable;
import engine.BoardFullException;
import engine.Engine;
import javafx.scene.paint.Color;
import mainGame.Move;

/**
 * places and positions a particular block into a particular board state to check if the AI is working correctly
 * @author Hank O'Brien
 *
 */
public class BoardPlaceTestClient {
  
  public static final Color COLOR = Color.BLACK;

  public static void main(String[] args) throws BoardFullException {
    Block blockToPlace = new LeftS();
    Block nextBlock = new RightS();
    Tile[][] testBoard = getBoard();
    testBoard = getBestPosition(blockToPlace, nextBlock, testBoard);
    cleanBoard(testBoard);
    getBestPosition(blockToPlace, new Square(), testBoard);

  }
  
  private static void cleanBoard(Tile[][] testBoard) {
    for(Tile[] row : testBoard){
      for(Tile t : row){
        t.setActive(false);
      }
    }
    
  }

  /**
   * gets the best board state given a block and a board state
   * @param blockToPlace the block to be added
   * @param testBoard the board where the block is to be added
   * @return the shape of the board after the block has been placed
   * @throws BoardFullException if the block causes the board to fill
   */
  private static Tile[][] getBestPosition(Block blockToPlace, Block nextBlock, Tile[][] testBoard) throws BoardFullException {
    Cerulean c = new Cerulean();
    c.setWeights(Autoplayable.DEFAULT_WEIGHTS);
    Engine e = new Engine(testBoard);
    System.out.println(blockToPlace);
    e.addBlock(blockToPlace);
    e.updateBoardWithNewBlock(blockToPlace);
    Move[] solution = c.submitBlock(blockToPlace, nextBlock, testBoard);
    System.out.println(Arrays.toString(solution));
    for(Move m : solution){
      Engine.printBoard(e.getGameBoard());
      System.out.println();
      e.executeMove(m);
    }
    Engine.printBoard(e.getGameBoard());
    return e.getGameBoard();
  }

  /**
   * @return the board in to be used under testing
   */
  public static Tile[][] getBoard(){
    return new Tile[][]{
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},               
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},

      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},

      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},

      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()}

    };
  }

}
