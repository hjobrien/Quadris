package clients;

import blocks.blockGeneration.BlockGenerator;
import blocks.blockGeneration.StandardizeBlocks;
import blocks.blockGeneration.RandomizeBlocks;
import clients.interfaces.Autoplayable;
import clients.interfaces.Viewable;
import javafx.application.Application;
import javafx.stage.Stage;
import mainGame.Game;
import mainGame.GameMode;
import mainGame.ScoreMode;

/**
 * basic client that runs a game in autoplay mode
 * 
 * @author Hank O'Brien
 *
 */
@SuppressWarnings("unused") // for toggling different BlockGenerators, this makes both be imported
                            // even when one isn't being used
public class AutoplayClient extends Application implements Viewable, Autoplayable {

  public static final int GAME_HEIGHT = 20;
  public static final int GAME_WIDTH = 10;
  public static final int MIN_TIME_PER_TURN = 10000000;
  public static final boolean DEBUG = false;
  // public static final boolean PLAY_MULTIPLE = true;
  public static final ScoreMode SCORE_MODE = ScoreMode.SIMPLE;
  
  public static final BlockGenerator GENERATOR = new StandardizeBlocks(2);
//  public static final BlockGenerator GENERATOR = new RandomizeBlocks();
  public  static final int MAX_GAMES_TO_PLAY = 50;
  public static final int NUM_BLOCKS_TO_ANALYZE = 2;

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage arg0) throws Exception {
    Game game = new Game(GAME_HEIGHT, GAME_WIDTH, MIN_TIME_PER_TURN, MAX_GAMES_TO_PLAY,
        GameMode.AUTOPLAY, USE_GRAPHICS, DEBUG, GENERATOR, DEFAULT_WEIGHTS, SCORE_MODE, NUM_BLOCKS_TO_ANALYZE);
    game.run(arg0);

  }
}
