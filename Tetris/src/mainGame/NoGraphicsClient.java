package mainGame;

import engine.GameMode;

public class NoGraphicsClient {
  public static final int GAME_HEIGHT = 20;
  public static final int GAME_WIDTH = 10;
  public static final int MIN_TIME_PER_TURN = 100;
  public static final boolean USE_GRAPHICS = false;
  public static final boolean DO_DEBUG = false;
  public static final boolean RANDOMIZE = true;
  public static final boolean PLAY_MULTIPLE = false;

  public static void main(String args[]) throws Exception {
    Game game = new Game(GAME_HEIGHT, GAME_WIDTH, MIN_TIME_PER_TURN, GameMode.DISTRO, USE_GRAPHICS,
        DO_DEBUG, RANDOMIZE, PLAY_MULTIPLE);
    game.run();
  }

}
