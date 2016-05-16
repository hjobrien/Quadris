package mainGame;

import blocks.BlockGenerator;
import blocks.RandomizeBlocks;
import engine.GameMode;

public class NoGraphicsClient {
  public static final int GAME_HEIGHT = 20;
  public static final int GAME_WIDTH = 10;
  public static final int MIN_TIME_PER_TURN = 100000000;
  public static final boolean USE_GRAPHICS = false;
  public static final boolean DO_DEBUG = false;
  public static final boolean PLAY_MULTIPLE = false;
  public static final ScoreMode SCORE_MODE = ScoreMode.SIMPLE;
  public static final BlockGenerator GENERATOR = new RandomizeBlocks();
  public static final int MAX_GAMES_TO_PLAY = 5;

  public static void main(String args[]) throws Exception {
    Game game = new Game(GAME_HEIGHT, GAME_WIDTH, MIN_TIME_PER_TURN, MAX_GAMES_TO_PLAY, GameMode.DISTRO, USE_GRAPHICS,
        DO_DEBUG, GENERATOR, PLAY_MULTIPLE, SCORE_MODE);
    game.run();
  }

}
