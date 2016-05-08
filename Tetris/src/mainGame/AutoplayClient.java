package mainGame;

import engine.GameMode;
import javafx.application.Application;
import javafx.stage.Stage;

public class AutoplayClient extends Application {

  public static final int GAME_HEIGHT = 20;
  public static final int GAME_WIDTH = 10;
  public static final int MIN_TIME_PER_TURN = 100;
  public static final boolean USE_GRAPHICS = true;
  public static final boolean DO_DEBUG = false;
  public static final boolean RANDOMIZE = true;
  public static final boolean PLAY_MULTIPLE = true;
  public static final double[] WEIGHTS = new double[] {-70, -97.85, 306.77, 5};

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage arg0) throws Exception {
    Game game = new Game(GAME_HEIGHT, GAME_WIDTH, MIN_TIME_PER_TURN, GameMode.AUTOPLAY, WEIGHTS,
        USE_GRAPHICS, DO_DEBUG, RANDOMIZE, PLAY_MULTIPLE);
    game.run(arg0);

  }
}
