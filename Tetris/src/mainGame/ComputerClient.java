package mainGame;

import com.sun.glass.ui.Robot;

import cerulean.Cerulean;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class ComputerClient extends Application{

  public static void main(String[] args) {

  }

  @SuppressWarnings("restriction")
  @Override
  public void start(Stage arg0) throws Exception {
    Game game = new Game();
    game.start(arg0);
    Robot robot = com.sun.glass.ui.Application.GetApplication().createRobot();
    while(game.getGameActive()){
      Move[] moves = Cerulean.win();        //needs to be able to get an int for a move
      for(KeyCode move : moves){
        robot.keyPress(move);               //takes an int
      }
    }

    
  }

}
