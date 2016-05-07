package mainGame;

import engine.GameMode;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Class that runs a graphical version of the game that allows the user to play normally
 * @author Hank O'Brien
 *
 */
public class GameApp extends Application{

  public static final int GAME_HEIGHT = 20;
  public static final int GAME_WIDTH = 10;
  public static final int MIN_TIME_PER_TURN = 100;
  public static final boolean USE_GRAPHICS = true;
  
  public static void main(String args[]) throws Exception {
    launch();
  }

  @Override
  public void start(Stage arg0) throws Exception {
    Game game = new Game(GAME_HEIGHT, GAME_WIDTH, MIN_TIME_PER_TURN, GameMode.DISTRO, USE_GRAPHICS);
    game.run(arg0);
    
  }
}
