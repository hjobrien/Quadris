package clients.testClients;

import java.util.stream.DoubleStream;

import blocks.Tile;
import cerulean.Cerulean;
import javafx.scene.paint.Color;

/**
 * a testing program to see how the fitness function evaluates particular board states
 * @author Hank
 *
 */
public class BoardEvaluationClient {
  
  public static final Color COLOR = Color.BLACK;
  
  public static void main(String[] args){
    Tile[][] testBoard = getBoard();
    System.out.println(getWeights(testBoard));
    
  }

  public static String getWeights(Tile[][] testBoard){
    Cerulean cerulean = new Cerulean();
    cerulean.setWeights(new double[]{-200, -50, 100, 1.68});
    double[] weights = cerulean.evaluateEachWeight(testBoard);
    return "net: " + DoubleStream.of(weights).sum() + "\n voids: " + weights[0] +
        " heights: " + weights[1] + " edges: " + weights[2] + " lines: " + weights[3];
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
      new Tile[]{new Tile(COLOR), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(COLOR), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},

      new Tile[]{new Tile(COLOR), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},

      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(COLOR), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(COLOR), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()},
      new Tile[]{new Tile(COLOR), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()}

    };
  }
}
