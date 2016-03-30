package mainGame;

import com.sun.glass.ui.Robot;

import cerulean.Cerulean;
import javafx.application.Application;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class ComputerClient extends Application {

	public static void main(String[] args) {
		Game.launch(args);

	}

	@SuppressWarnings("restriction")
	@Override
	
	//
	public void start(Stage arg0) throws Exception {
		Game game = new Game();
		game.start(arg0);
		Robot robot = com.sun.glass.ui.Application.GetApplication().createRobot();
		//currently crashes my computer
//      long now = System.currentTimeMillis();
//		while (game.isActive()) {
//			long now2 = System.currentTimeMillis();
//			if (now2 - now > 1){
//				Move[] moves = Cerulean.win(); // needs to be able to get an int for a move
//				for (Move move : moves) {
//					robot.keyPress(move.getValue()); // takes an int
//					robot.keyRelease(move.getValue());
//				}
//				now = now2;
//			}
//		}
	}
}
