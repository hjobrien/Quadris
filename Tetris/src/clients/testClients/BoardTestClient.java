package clients.testClients;

import blocks.BlockType;
import blocks.Tile;
import blocks.blockGeneration.BlockGenerator;
import blocks.blockGeneration.SingleBlockGenerator;
import clients.interfaces.Viewable;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mainGame.Game;
import mainGame.GameMode;
import mainGame.ScoreMode;

public class BoardTestClient extends Application implements Viewable{

	public static final int MIN_TIME_PER_TURN = 100;
	public static final boolean DEBUG = false;
	public static final ScoreMode SCORE_MODE = ScoreMode.NINTENDO;
	public static final BlockGenerator GENERATOR = new SingleBlockGenerator(BlockType.LINE);
	public static final int MAX_GAMES_TO_PLAY = 1;
	
	public static final Color COLOR = Color.BLACK;
	  
	public static void main(String args[]) throws Exception {
	  launch();
	}

	  @Override
	  public void start(Stage arg0) throws Exception {
	    Game game = new Game(getBoard(), MIN_TIME_PER_TURN, MAX_GAMES_TO_PLAY, GameMode.DISTRO, USE_GRAPHICS,
	        DEBUG, GENERATOR, SCORE_MODE);
	    game.run(arg0);

	  }
	  
	  public static Tile[][] getBoard(){
	    return new Tile[][]{
	    	//					column 1				column 2				column 3				column 4				column 5				column 6				column 7				column 8				column 9				column 10
	      new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},               
		  new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},
		  new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},
	      
		  //*********************************************************************************************************************************************************************************************************************************************************
		  
		  new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},               
	      new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},
	      new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},
	      new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},
	      new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},

	      new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},
	      new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},
	      new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},
	      new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},
	      new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},

	      new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},
	      new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},
	      new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},
	      new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},
	      new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},

	      new Tile[]{new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            ), new Tile(            )},
	      new Tile[]{new Tile(            ), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false)},
	      new Tile[]{new Tile(            ), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false)},
	      new Tile[]{new Tile(            ), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false)},
	      new Tile[]{new Tile(            ), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false), new Tile(COLOR, false)}

	    };
	  }
}
