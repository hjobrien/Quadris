package clients;

import blocks.blockGeneration.BlockGenerator;
import blocks.blockGeneration.StandardizeBlocks;
import clients.interfaces.Autoplayable;
import mainGame.Game;
import mainGame.GameMode;
import mainGame.ScoreMode;

public class NoGraphicsClient implements Autoplayable{
  public static final int GAME_HEIGHT = 20;
  public static final int GAME_WIDTH = 10;
  public static final int MIN_TIME_PER_TURN = 10000000;
  public static final boolean USE_GRAPHICS = false;
  public static final boolean DO_DEBUG = false;
//  public static final boolean PLAY_MULTIPLE = false;
  public static final ScoreMode SCORE_MODE = ScoreMode.SIMPLE;
  public static final BlockGenerator GENERATOR = new StandardizeBlocks(0);
  public static final int MAX_GAMES_TO_PLAY = 5;

  public static void main(String args[]) throws Exception {
    Game game = new Game(GAME_HEIGHT, GAME_WIDTH, MIN_TIME_PER_TURN, MAX_GAMES_TO_PLAY, GameMode.AUTOPLAY,
        USE_GRAPHICS, DO_DEBUG, GENERATOR, DEFAULT_WEIGHTS, SCORE_MODE, 1);
    game.run();
  }

}
