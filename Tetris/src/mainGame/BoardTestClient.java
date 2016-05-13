package mainGame;

import blocks.Tile;
import engine.BlockGenerator;
import engine.GameMode;
import engine.RandomizeBlocks;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BoardTestClient extends Application {

  // public static final int GAME_HEIGHT = 20;
  // public static final int GAME_WIDTH = 10;
  public static final int MIN_TIME_PER_TURN = 100;
  public static final boolean USE_GRAPHICS = true;
  public static final boolean DEBUG = false;
  public static final boolean PLAY_MULTIPLE = false;
  public static final ScoreMode SCORE_MODE = ScoreMode.NINTENDO;
  public static final BlockGenerator GENERATOR = new RandomizeBlocks();


  public static final Color COLOR = Color.BLACK;

  public static void main(String args[]) throws Exception {
    launch();
  }

  @Override
  public void start(Stage arg0) throws Exception {
    Game game = new Game(getBoard(), MIN_TIME_PER_TURN, GameMode.DISTRO, USE_GRAPHICS, DEBUG,
        GENERATOR, PLAY_MULTIPLE, SCORE_MODE);
    game.run(arg0);

  }

  // public static void main(String[] args){
  // Tile[][] testBoard = getBoard();
  // System.out.println(getWeights(testBoard));
  //
  // }

  // public static String getWeights(Tile[][] testBoard){
  // Cerulean cerulean = new Cerulean();
  // cerulean.setWeights(new double[]{-200, -50, 100, 1.68});
  // double[] weights = cerulean.evaluateWeight(testBoard);
  // return "net: " + DoubleStream.of(weights).sum() + " voids: " + weights[0] +
  // " heights: " + weights[1] + " lines: " + weights[2] + " edges: " + weights[3];
  // }

  public static Tile[][] getBoard() {
    return new Tile[][] {
        // column 1 column 2 column 3 column 4 column 5 column 6 column 7 column 8 column 9 column
        // 10
        new Tile[] {new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(),
            new Tile(), new Tile(), new Tile(), new Tile()},
        new Tile[] {new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(),
            new Tile(), new Tile(), new Tile(), new Tile()},
        new Tile[] {new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(),
            new Tile(), new Tile(), new Tile(), new Tile()},
        new Tile[] {new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(),
            new Tile(), new Tile(), new Tile(), new Tile()},
        new Tile[] {new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(),
            new Tile(), new Tile(), new Tile(), new Tile()},

        new Tile[] {new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(),
            new Tile(), new Tile(), new Tile(), new Tile()},
        new Tile[] {new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(),
            new Tile(), new Tile(), new Tile(), new Tile()},
        new Tile[] {new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(),
            new Tile(), new Tile(), new Tile(), new Tile()},
        new Tile[] {new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(),
            new Tile(), new Tile(), new Tile(), new Tile()},
        new Tile[] {new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(),
            new Tile(), new Tile(), new Tile(), new Tile()},

        new Tile[] {new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(),
            new Tile(), new Tile(), new Tile(), new Tile()},
        new Tile[] {new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(),
            new Tile(), new Tile(), new Tile(), new Tile()},
        new Tile[] {new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(),
            new Tile(), new Tile(), new Tile(), new Tile()},
        new Tile[] {new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(),
            new Tile(), new Tile(), new Tile(), new Tile()},
        new Tile[] {new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(),
            new Tile(), new Tile(), new Tile(), new Tile()},

        new Tile[] {new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(),
            new Tile(), new Tile(), new Tile(), new Tile()},
        new Tile[] {new Tile(), new Tile(COLOR, false), new Tile(COLOR, false),
            new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false),
            new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false),
            new Tile(COLOR, false)},
        new Tile[] {new Tile(), new Tile(COLOR, false), new Tile(COLOR, false),
            new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false),
            new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false),
            new Tile(COLOR, false)},
        new Tile[] {new Tile(), new Tile(COLOR, false), new Tile(COLOR, false),
            new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false),
            new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false),
            new Tile(COLOR, false)},
        new Tile[] {new Tile(), new Tile(COLOR, false), new Tile(COLOR, false),
            new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false),
            new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false),
            new Tile(COLOR, false)}

    };
  }
}
