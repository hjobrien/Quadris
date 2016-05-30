package clients.testClients;

import blocks.Block;
import blocks.StraightLine;
import blocks.Tile;
import cerulean.Cerulean;
import clients.interfaces.Autoplayable;
import engine.BoardFullException;
import engine.Engine;
import javafx.scene.paint.Color;
import mainGame.Move;

public class BoardPlaceTestClient {
  
  public static final Color COLOR = Color.BLACK;

  public static void main(String[] args) throws BoardFullException {
    Block blockToPlace = new StraightLine();
    Tile[][] testBoard = getBoard();
    Engine.printBoard(getBestPosition(blockToPlace, testBoard));

  }
  
  private static Tile[][] getBestPosition(Block blockToPlace, Tile[][] testBoard) throws BoardFullException {
    Cerulean c = new Cerulean();
    c.setWeights(Autoplayable.DEFAULT_WEIGHTS);
    Engine e = new Engine(testBoard);
    e.addBlock(blockToPlace);
    e.updateBoardWithNewBlock(blockToPlace);
    for(Move m : c.submitBlock(blockToPlace, testBoard)){
      e.executeMove(m);
    }
    return testBoard;
    
  }

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
      new Tile[]{new Tile(), new Tile(COLOR), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(COLOR), new Tile(COLOR), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(COLOR), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()}

    };
  }

}
