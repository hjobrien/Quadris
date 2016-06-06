package clients;

import java.io.IOException;

import clients.interfaces.Autoplayable;
import engine.ParellelizedCore;
import mainGame.ScoreMode;

public class ParellelizedClient implements Autoplayable {
  public static final int GAME_HEIGHT = 20;
  public static final int GAME_WIDTH = 10;
  public static final int MIN_TIME_PER_TURN = 100000000;
  public static final boolean USE_GRAPHICS = false;
  public static final boolean DO_DEBUG = false;
  // should be 1, if a game finishes, the thread should be allowed to take a new game from the pool
  public static final int MAX_GAMES_PER_THREAD = 1;
  public static final ScoreMode SCORE_MODE = ScoreMode.SIMPLE;

  private static final int NUM_GAMES_TO_PLAY = 10;

  public static void main(String args[]) throws IOException {
    ParellelizedCore runner = new ParellelizedCore(GAME_HEIGHT, GAME_WIDTH, MIN_TIME_PER_TURN,
        MAX_GAMES_PER_THREAD, NUM_GAMES_TO_PLAY, USE_GRAPHICS, DO_DEBUG, SCORE_MODE);
    System.out.println(
        "Average Score over " + NUM_GAMES_TO_PLAY + " Games using " + SCORE_MODE + " scoring is " + runner.run(DEFAULT_WEIGHTS));
    System.exit(0);
  }


}
