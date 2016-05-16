package mainGame;

import blocks.BlockGenerator;
import blocks.StandardizeBlocks;
import engine.GameMode;
import engine.StandardizeBlocks;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * basic client that runs a game in autoplay mode
 * @author Hank O'Brien
 *
 */
public class AutoplayClient extends Application {

  public static final int GAME_HEIGHT = 20;
  public static final int GAME_WIDTH = 10;
  public static final int MIN_TIME_PER_TURN = 100000;
  public static final boolean USE_GRAPHICS = true;
  public static final boolean DEBUG = false;
  public static final boolean PLAY_MULTIPLE = true;
  public static final double[] WEIGHTS = new double[] {-70, -97.85, 306.77, 5};
  public static final ScoreMode SCORE_MODE = ScoreMode.SIMPLE;
  public static final BlockGenerator GENERATOR = new StandardizeBlocks(19);
  public static final BlockGenerator GENERATOR = new StandardizeBlocks(19);
  public  static final int MAX_GAMES_TO_PLAY = 5;

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage arg0) throws Exception {
    Game game = new Game(GAME_HEIGHT, GAME_WIDTH, MIN_TIME_PER_TURN, GameMode.AUTOPLAY,
        USE_GRAPHICS, DEBUG, GENERATOR, PLAY_MULTIPLE, WEIGHTS, SCORE_MODE);
    game.run(arg0);

  }
}
