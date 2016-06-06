package clients;

import blocks.blockGeneration.BlockGenerator;
import blocks.blockGeneration.StandardizeBlocks;
import clients.interfaces.Autoplayable;
import clients.interfaces.Viewable;
import javafx.application.Application;
import javafx.stage.Stage;
import mainGame.Game;
import mainGame.GameMode;
import mainGame.ScoreMode;
import renderer.DualPlayRenderer;

public class DualPlayClient extends Application implements Viewable, Autoplayable {

	private static final int GAME_HEIGHT = 20;
	private static final int GAME_WIDTH = 10;
	public static final int MIN_TIME_PER_USER_TURN = 50000000;
	private static final int MIN_TIME_PER_CERULEAN_TURN = 1000000;
	private static final boolean DEBUG = false;
	public static final ScoreMode SCORE_MODE = ScoreMode.SIMPLE;
	public static final int MAX_GAMES_TO_PLAY = 5;
	//probably shouldn't randomize because then the computer will get different blocks than the user
	// public static final BlockGenerator GENERATOR = new RandomizeBlocks();
	public static final BlockGenerator GENERATOR = new StandardizeBlocks(2);
	public static final int NUM_BLOCKS_TO_ANALYZE = 2;
	
	public static void main(String[] args) {
		launch();
	}

	/*
	 * Every time a new block is added, the game timer on the AI pauses (no
	 * updates occur). When the next block is added to the user's game, the
	 * timer unpauses, the block is played, and a new block is spawned, which
	 * again pauses the AI. This should be so quick that it will seem
	 * instantaneous to the user, allowing them feel like they are playing
	 * against the AI without being told where the AI will move before they move
	 * themselves
	 * 
	 * Another possiblity could be starting the human game one block earlier so
	 * that the computer can addBlock whenever the user addBlocks without giving
	 * away the "best position"
	 */

	@Override
	public void start(Stage stage) throws Exception {
		Game ceruleanGame = new Game(GAME_HEIGHT, GAME_WIDTH, MIN_TIME_PER_CERULEAN_TURN, MAX_GAMES_TO_PLAY,
		        GameMode.AUTOPLAY, USE_GRAPHICS, DEBUG, GENERATOR, DEFAULT_WEIGHTS, SCORE_MODE, NUM_BLOCKS_TO_ANALYZE);
		    
		Game userGame = new Game(GAME_HEIGHT, GAME_WIDTH, MIN_TIME_PER_USER_TURN, MAX_GAMES_TO_PLAY, GameMode.DISTRO, USE_GRAPHICS,
			        DEBUG, GENERATOR, SCORE_MODE);

		stage.setScene(new DualPlayRenderer(false).makeGame());
		stage.show();

	}
}
