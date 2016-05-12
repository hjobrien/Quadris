package mainGame;

import javafx.application.Application;
import javafx.stage.Stage;
import renderer.DualPlayRenderer;

public class DualPlayClient extends Application {



  private static final int GAME_HEIGHT = 20;
  private static final int GAME_WIDTH = 10;
  private static final int MIN_TIME_PER_TURN = 100000000;
  private static final boolean USE_GRAPHICS = true;
  private static final boolean DO_DEBUG = false;
  private static final boolean RANDOMIZE = false;
  private static final boolean PLAY_MULTIPLE = false;
  private static final double[] WEIGHTS = new double[] {-70, -97.85, 306.77, 5};

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage stage) throws Exception {
//    Game aiGame = new Game(GAME_HEIGHT, GAME_WIDTH, MIN_TIME_PER_TURN, GameMode.AUTOPLAY,
//        USE_GRAPHICS, DO_DEBUG, RANDOMIZE, PLAY_MULTIPLE, WEIGHTS);
//    Game userGame = new Game(GAME_HEIGHT, GAME_WIDTH, MIN_TIME_PER_TURN, GameMode.DISTRO,
//        USE_GRAPHICS, DO_DEBUG, RANDOMIZE, PLAY_MULTIPLE);
    
    stage.setScene(new DualPlayRenderer(false).makeGame());
    stage.show();

  }
}
