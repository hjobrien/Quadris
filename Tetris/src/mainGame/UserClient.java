package mainGame;

import javafx.application.Application;
import javafx.stage.Stage;

public class UserClient extends Application {

  public static void main(String[] args) {
  	Game.launch(args);
  
  }

  @Override
  public void start(Stage arg0) throws Exception {
    Game game = new Game();
    game.start(arg0);
  }

}
