package mainGame;

import java.awt.Robot;


import cerulean.Cerulean;
import javafx.application.Application;
import javafx.stage.Stage;

public class ComputerClient extends Application {

  public static void main(String[] args) {
    Game.launch(args);

  }

  @Override

  //
  public void start(Stage arg0) throws Exception {
    Game game = new Game();
    game.start(arg0);
    Robot robot = new Robot();
    // currently crashes my computer
//    long now = System.currentTimeMillis();
//    Thread.sleep(21);
//    long now2 = System.currentTimeMillis();
    while (game.isActive()) {
      
//      System.out.println("flag");
//      now2 = System.currentTimeMillis();
//      if (now2 - now > 1) {
//        Move[] moves = Cerulean.win(); // needs to be able to get an int for a move
//        for (Move move : moves) {
//          robot.keyPress(move.getValue()); // takes an int
//          robot.keyRelease(move.getValue());
//        }
//        now = now2;
//      }
    }
  }
}
