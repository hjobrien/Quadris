package clients;

import blocks.blockGeneration.BlockGenerator;
import blocks.blockGeneration.StandardizeBlocks;
import clients.interfaces.Autoplayable;
import clients.interfaces.Viewable;
import engine.GameMode;
import javafx.application.Application;
import javafx.stage.Stage;
import mainGame.Game;
import mainGame.ScoreMode;

/**
 * basic client that runs a game in autoplay mode
 * @author Hank O'Brien
 *
 */
public class AutoplayClient extends Application implements Viewable, Autoplayable{

  public static final int GAME_HEIGHT = 20;
  public static final int GAME_WIDTH = 10;
  public static final int MIN_TIME_PER_TURN = 100000000;
  public static final boolean DEBUG = false;
//  public static final boolean PLAY_MULTIPLE = true;
  public static final ScoreMode SCORE_MODE = ScoreMode.SIMPLE;
  public static final BlockGenerator GENERATOR = new StandardizeBlocks(0);
//  public static final BlockGenerator GENERATOR = new RandomizeBlocks();
  public  static final int MAX_GAMES_TO_PLAY = 1;

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage arg0) throws Exception {
    Game game = new Game(GAME_HEIGHT, GAME_WIDTH, MIN_TIME_PER_TURN, MAX_GAMES_TO_PLAY, GameMode.AUTOPLAY,
        USE_GRAPHICS, DEBUG, GENERATOR, DEFAULT_WEIGHTS, SCORE_MODE);
    game.run(arg0);

  }
}
